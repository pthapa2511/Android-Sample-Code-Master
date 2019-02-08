package com.app.testSample.utility.network;

import android.app.Activity;
import android.util.Log;

import com.app.testSample.utility.ConnectivityUtils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Comparator;
import java.util.Vector;
import java.util.zip.GZIPInputStream;

public final class HttpClientConnection extends Thread {
    private final String LOG_TAG = "HttpClientConnection";

    private static HttpClientConnection instance;

    private HttpClientConnection() {
        defaultStatusCodeChecker = new StatusCodeChecker() {
            @Override
            public boolean isSuccess(int statusCode) {
                return statusCode == 200;
            }
        };
    }

    public static HttpClientConnection getInstance() {
        if (instance == null) {
            instance = new HttpClientConnection();
            instance.execute();
        }
        return instance;
    }

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

    public void execute() {
        highPriorityQueue = new Vector<ServiceRequest>();
        lowPriorityQueue = new Vector<ServiceRequest>();
        isRunning = true;
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
                    Thread.sleep(10*60*10);// 10 min sleep
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
//            notifyError(activity.getString(R.string.toast_error_network), null, 0);
            return;
        }
        HttpURLConnection connection = null;
        InputStream responseContentStream = null;
        int status = 0;
            try {
                Log.i(LOG_TAG, "Request URL: " + currentRequest.getUrl());
                URL url = new URL(currentRequest.getUrl());
                connection = (HttpURLConnection) url.openConnection();
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
                connection.setDoInput(true);
                if(requestType == 0) {
//                optional request header
                    connection.setRequestProperty("Content-Type", "application/json");

//                optional request header
                    connection.setRequestProperty("Accept", "application/json");
                    connection.setRequestProperty("Accept-Encoding", "");
                }
                Thread.interrupted();

                connection.setUseCaches(true);
                connection.connect();

                status = connection.getResponseCode();
                Log.i(LOG_TAG, "Status Code " + status);
                Response response = new Response();
                response.setHttpResponseCode(status);
                response.setDataType(currentRequest.getDataType());
                response.setRequestData(currentRequest.getRequestData());

//            InputStream responseContentStream = connection.getContent() == null ? null : connection.getInputStream();
                if(requestType == 0) {
                    responseContentStream = new BufferedInputStream(connection.getInputStream());
                    if (responseContentStream != null) {
                        if (currentRequest.getIsCompressed()) {
                            responseContentStream = new GZIPInputStream(responseContentStream);
                        }
                        if (currentRequest.isCancelled()) {
                            return;
                        }
                        //Code updated by anoop singh 18-12-2015 as this previous one was throwing error on nexus devices
                        BufferedReader br = new BufferedReader(new InputStreamReader(responseContentStream));
                        StringBuffer sb = new StringBuffer();
                        String inputLine = "";
                        while ((inputLine = br.readLine()) != null && inputLine.length() > 0) {
                            sb.append(inputLine);
                        }
                        response.setResponseData(String.valueOf(sb).getBytes());

                        if (br != null) {
                            br.close();
                        }
                        if (responseContentStream != null) {
                            responseContentStream.close();
                        }
                    }

                    if (currentRequest.isCancelled()) {
                        return;
                    }
                }else{
                    responseContentStream = connection.getInputStream();
                    response.setInputStream(responseContentStream);
                }
            currentRequest.getResponseController().handleResponse(response);
            } catch (MalformedURLException me) {
                Log.e(LOG_TAG, "MalformedURLException", me);
//                notifyError(activity.getResources().getString(R.string.network_problem), me, status);
            } catch (IOException io) {
                Log.e(LOG_TAG, "IOException", io);
//                notifyError(activity.getResources().getString(R.string.network_problem), io, status);

            } catch (Exception e) {
                Log.e(LOG_TAG, "Exception", e);
//                notifyError(activity.getResources().getString(R.string.network_problem), e, status);
            }
            //change by rahul 27th August
            finally {
                if (connection != null)
                    connection.disconnect();
            }
        }


        /**
         * @param exception
         */

    private void notifyError(String errorMessage, Exception exception, int statusCode) {
        if (exception == null) {
            Log.e(LOG_TAG, "Error Response: " + errorMessage);
        } else {
            Log.e(LOG_TAG, "Error Response: " + errorMessage, exception);
        }
        Response response = new Response();
        response.setRequestData(currentRequest.getRequestData());
        response.setDataType(currentRequest.getDataType());
        response.setHttpResponseCode(statusCode);
//        response.setErrorMessage(errorMessage);
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

    private int requestType;
    public void setRequestType(int requestType){
        this.requestType = requestType;
    }
}
