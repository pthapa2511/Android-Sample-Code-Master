package com.app.testSample.utility.controller;

import android.app.Activity;

import com.app.testSample.utility.network.Response;
import com.app.testSample.utility.network.ServiceRequest;
import com.app.testSample.utility.ui.IScreen;


/**
 * This interface will be used as a base interface for all controllers
 */
public interface IController {

	IScreen getScreen();

	Activity getActivity();

	ServiceRequest getData(int dataType, Object requestData);

	ServiceRequest getData(Object requestData);

	void handleResponse(Response response);

	void parseResponse(Response response);
	
	void sendResponseToScreen(Response response);
}
