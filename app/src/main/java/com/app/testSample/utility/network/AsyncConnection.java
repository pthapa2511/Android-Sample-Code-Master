package com.app.testSample.utility.network;

import android.app.Activity;
import android.util.Log;


import com.app.testSample.utility.ConnectivityUtils;
import com.app.testSample.utility.DataUtils;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Comparator;
import java.util.Vector;
import java.util.zip.GZIPInputStream;

public class AsyncConnection extends Thread {
    /**
     * @author anoop.singh
     */
    private final String LOG_TAG = "AsyncConnection";

    private static AsyncConnection instance;

    public AsyncConnection() {
        defaultStatusCodeChecker = new StatusCodeChecker() {
            @Override
            public boolean isSuccess(int statusCode) {
                return statusCode == 200;
            }
        };
    }

   /* public static HttpClientConnection getInstance() {
        if (instance == null) {
            instance = new HttpClientConnection();
            instance.execute();
        }
        return instance;
    }*/

    private StatusCodeChecker defaultStatusCodeChecker;
    private int defaultRequestTimeOut;

    /**
     * @param defaultStatusCodeChecker the defaultStatusCodeChecker to set
     */
    public void setDefaultStatusCodeChecker(StatusCodeChecker defaultStatusCodeChecker) {
        this.defaultStatusCodeChecker = defaultStatusCodeChecker;
    }

    /**
     * @param defaultRequestTimeOut the defaultRequestTimeOut in miliseconds to set
     */
    public void setDefaultRequestTimeOut(int defaultRequestTimeOut) {
        this.defaultRequestTimeOut = defaultRequestTimeOut;
    }

    private boolean isRunning;
    private Vector<ServiceRequest> highPriorityQueue;
    private Vector<ServiceRequest> lowPriorityQueue;

    public void execute(ServiceRequest request) {
        highPriorityQueue = new Vector<ServiceRequest>();
        lowPriorityQueue = new Vector<ServiceRequest>();
        isRunning = true;
        addRequest(request);
        start();
    }

    private ServiceRequest currentRequest;

    /**
     * {@link ServiceRequest} with {@link PRIORITY#HIGH} are executed before {@link ServiceRequest} with {@link PRIORITY#LOW}
     */
    public static interface PRIORITY {
        /**
         * When-ever a new {@link ServiceRequest} with {@link PRIORITY#LOW} is added,
         * it gets lower priority than previous requests with same priority.
         */
        public static byte LOW = 0;
        /**
         * When-ever a new {@link ServiceRequest} with {@link PRIORITY#HIGH} is added,
         * it gets higher priority than previous requests with same priority.
         */
        public static byte HIGH = 1;
    }

    public static interface HTTP_METHOD {
        public static byte GET = 0;
        public static byte POST = 1;
        public static byte PUT = 2;
        public static byte DELETE = 3;
    }

    /**
     * Specific instance of StatusCodeChecker can be set in ServiceRequest
     */
    public static interface StatusCodeChecker {
        boolean isSuccess(int statusCode);
    }

    @Override
    public void run() {
        while (isRunning) {
            if (nextRequest()) {
                executeRequest();
            } else {
                try {
                    Thread.sleep(10 * 60);// 10 min sleep
                } catch (InterruptedException e) {
                    Log.i(LOG_TAG, "" + e);
                }
            }
        }
    }

    private boolean nextRequest() {
        if (highPriorityQueue.size() > 0) {
            currentRequest = (ServiceRequest) highPriorityQueue.remove(0);
        } else if (lowPriorityQueue.size() > 0) {
            currentRequest = (ServiceRequest) lowPriorityQueue.remove(0);
        } else {
            currentRequest = null;
        }

        return currentRequest != null;
    }

    public void executeRequest() {
        if (currentRequest.isCancelled()) {
            return;
        }
        /**
         * Check if device is connected.
         */
        Activity activity = currentRequest.getResponseController().getActivity();
        if (activity != null && !ConnectivityUtils.isNetworkEnabled(activity)) {
            notifyError("Device is out of network coverage.", null);
            return;
        }

        HttpURLConnection connection = null;
        try {
            Log.i(LOG_TAG, "Request URL: " + currentRequest.getUrl());
            URL url = new URL(currentRequest.getUrl());
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("Content-Type", "application/json");
            String requestMethod = "";
            switch (currentRequest.getHttpMethod()) {
                case HTTP_METHOD.GET: {
                    requestMethod = "GET";
                    break;
                }
                case HTTP_METHOD.POST: {
                    requestMethod = "POST";
                    break;
                }
                default:
                    requestMethod = "GET";
                    break;
            }
            connection.setRequestMethod(requestMethod);

            /*if(currentRequest.getRequestData() != null) {
                //Send request
                OutputStream os = connection.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(currentRequest.getRequestData().toString());

                writer.flush();
                writer.close();
                os.close();
            }*/

            InputStream responseContentStream = connection.getContent() == null ? null : (InputStream) connection.getContent();
            int status = connection.getResponseCode();
            Log.i(LOG_TAG, "Status Code " + status);
            Response response = new Response();
            response.setDataType(currentRequest.getDataType());
            response.setRequestData(currentRequest.getRequestData());
            if (responseContentStream != null) {
                if (currentRequest.getIsCompressed()) {
                    responseContentStream = new GZIPInputStream(responseContentStream);
                }
                if (currentRequest.isCancelled()) {
                    return;
                }
                response.setResponseData(DataUtils.convertStreamToBytes(responseContentStream));
            }

            if (currentRequest.isCancelled()) {
                return;
            }
            currentRequest.getResponseController().handleResponse(response);
        } catch (MalformedURLException me) {
            Log.e(LOG_TAG, "MalformedURLException", me);
            notifyError("Error in in connection", me);
        } catch (IOException io) {
            Log.e(LOG_TAG, "IOException", io);
            notifyError("Error in in connection", io);
        } catch (Exception e) {
            Log.e(LOG_TAG, "Exception", e);
            notifyError("Error in in connection", e);
        } finally {
            if (connection != null)
                connection.disconnect();
        }
    }

    /**
     * @param errorMessage
     * @param exception
     */
    private void notifyError(String errorMessage, Exception exception) {
        if (exception == null) {
            Log.e(LOG_TAG, "Error Response: " + errorMessage);
        } else {
            Log.e(LOG_TAG, "Error Response: " + errorMessage, exception);
        }
        Response response = new Response();
        response.setRequestData(currentRequest.getRequestData());
        response.setDataType(currentRequest.getDataType());
        response.setErrorMessage(errorMessage);
        response.setSuccess(false);
        response.setException(exception);
        if (currentRequest.isCancelled()) {
            return;
        }
        currentRequest.getResponseController().handleResponse(response);
    }

    public void addRequest(ServiceRequest request) {
        try {
            if (request.getPriority() == PRIORITY.HIGH) {
                highPriorityQueue.add(0, request);
            } else {
                lowPriorityQueue.addElement(request);
            }
            interrupt();
        } catch (Exception ex) {
            Log.e(LOG_TAG, "addRequest()", ex);
        }
    }

    /**
     * @return the currentRequest
     */
    public ServiceRequest getCurrentRequest() {
        return currentRequest;
    }

    /**
     * @return the nextRequest
     */
    public ServiceRequest getNextRequest() {
        if (highPriorityQueue.size() > 0) {
            return highPriorityQueue.get(0);
        } else if (lowPriorityQueue.size() > 0) {
            return lowPriorityQueue.get(0);
        } else {
            return null;
        }
    }

    /**
     * @return true if pRequest is found and removed from high/low queue.
     */
    public boolean removeRequest(ServiceRequest pRequest, Comparator<ServiceRequest> pComparator) {
        ServiceRequest tempRq = null;
        Vector<ServiceRequest> targetQueue = lowPriorityQueue;
        if (pRequest.getPriority() == PRIORITY.HIGH) {
            targetQueue = highPriorityQueue;
        }
        for (int i = 0; i < targetQueue.size(); i++) {
            try {
                tempRq = targetQueue.get(i);
            } catch (Exception e) {
                return false;
            }
            if (tempRq != null && pComparator.compare(tempRq, pRequest) == 0) {
                return targetQueue.removeElement(tempRq);
            }
        }
        return false;
    }
}
