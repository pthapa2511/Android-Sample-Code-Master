package com.app.testSample.controller;

import android.app.Activity;
import android.util.Log;

import com.app.testSample.utility.controller.BaseController;
import com.app.testSample.utility.network.HttpClientConnection;
import com.app.testSample.utility.network.Response;
import com.app.testSample.utility.ui.IScreen;

/**
 * BaseController
 */

public abstract class BaseRequestController extends BaseController {


    private final String LOG_TAG = "BaseRequestController";

    /**
     * @param activity
     * @param screen
     */
    public BaseRequestController(Activity activity, IScreen screen) {
        super(activity, screen);
    }

    /**
     * Default statusCodeChecker is to be modified for optimise specific
     * web-services
     */
    static {
        HttpClientConnection.StatusCodeChecker statusCodeChecker = new HttpClientConnection.StatusCodeChecker() {
            @Override
            public boolean isSuccess(int statusCode) {
                return statusCode / 100 == 2;
            }
        };
        HttpClientConnection.getInstance().setDefaultStatusCodeChecker(statusCodeChecker);
    }

    /**
     * This method is common for all optimise web-services
     */
    public final void handleResponse(Response response) {
        if (response.getResponseData() instanceof byte[]) {
            try {
                parseResponse(response);
            } catch (Exception e) {
                Log.i(LOG_TAG, "parseResponse()");
                response.setSuccess(false);
            }
        }
        sendResponseToScreen(response);
    }
}
