package com.app.testSample.controller;

import android.app.Activity;
import android.content.Context;

import com.app.testSample.R;
import com.app.testSample.constants.Constants;
import com.app.testSample.model.BaseModel;
import com.app.testSample.model.BestOfferModel;
import com.app.testSample.model.CateGoryModel;
import com.app.testSample.model.CouponsByCategoriesView;
import com.app.testSample.model.GetCouponByCategoryModel;
import com.app.testSample.model.GetCouponsByStoresResultModel;
import com.app.testSample.model.GetCouponsDetails;
import com.app.testSample.model.ImportantLinkModel;
import com.app.testSample.model.SliderUrl;
import com.app.testSample.model.StoreModel;
import com.app.testSample.parser.JsonParser;
import com.app.testSample.utility.controller.BaseController;
import com.app.testSample.utility.network.AsyncConnection;
import com.app.testSample.utility.network.Response;
import com.app.testSample.utility.network.ServiceRequest;
import com.app.testSample.utility.ui.IScreen;

import java.util.List;

/**
 * Controller to handle ApiRequest
 */

public class ApiRequestController extends BaseRequestController {

    private Context mContext;

    /**
     * @param activity
     * @param screen
     */
    public ApiRequestController(Activity activity, IScreen screen) {
        super(activity, screen);
        this.mContext = activity;
    }

    @Override
    public ServiceRequest getData(int requestType, Object requestData) {
        ServiceRequest serviceRequest = new ServiceRequest();
        serviceRequest.setRequestData(requestData);
        serviceRequest.setResponseController(this);
        serviceRequest.setDataType(requestType);
        serviceRequest.setPriority(AsyncConnection.PRIORITY.LOW);
        serviceRequest.setResponseController(this);
        serviceRequest.setHttpMethod(AsyncConnection.HTTP_METHOD.GET);
        String url = "";
        switch (requestType){
            case Constants.REQUEST_GET_TOP_20_CATEGORY:{
                url = mContext.getString(R.string.base_url) + mContext.getString(R.string.api_get_top_20_category);
                break;
            }

            case Constants.REQUEST_GET_ALL_CATEGORY:{
                url = mContext.getString(R.string.base_url) + mContext.getString(R.string.api_get_all_stores_category);
                break;
            }
            case Constants.REQUEST_GET_TOP_20_STORE:{
                url = mContext.getString(R.string.base_url) + mContext.getString(R.string.api_get_top_20_stores);
                break;
            }
            case Constants.REQUEST_GET_ALL_STORE:{
                url = mContext.getString(R.string.base_url) + mContext.getString(R.string.api_get_all_stores);
                break;
            }
            case Constants.REQUEST_GET_BEST_OFFER:{
                url = mContext.getString(R.string.base_url) + mContext.getString(R.string.api_get_best_offers);
                break;
            }
            case Constants.REQUEST_GET_COUPONS_BY_CATEGORIES:{
                String [] params = ((String[])requestData);
                String urlParams = "TermID=" + params[0] + "&TaxanomyID=" + params[1];
//                String params = "/TermID" + (String[]) requestData[0]
                url = mContext.getString(R.string.base_url) + mContext.getString(R.string.api_get_coupon_by_category) + "?" + urlParams;
                break;
            }

            case Constants.REQUEST_GET_COUPONS_BY_STORES:{
                String [] params = ((String[])requestData);
                String urlParams = "TermID=" + params[0] + "&TaxanomyID=" + params[1];
//                String params = "/TermID" + (String[]) requestData[0]
                url = mContext.getString(R.string.base_url) + mContext.getString(R.string.api_get_coupon_by_stores) + "?" + urlParams;
                break;
            }

            case Constants.REQUEST_GET_COUPONS_DETAILS:{
                String param = (String)requestData;
                url = mContext.getString(R.string.base_url) + mContext.getString(R.string.api_get_coupon_details) + "?" + "ObjectID=" + param;
                break;
            }

            case Constants.REQUEST_GET_IMPORTANT_LINKS:{
                url = mContext.getString(R.string.base_url) + mContext.getString(R.string.api_get_important_links);
                break;
            }

            case Constants.REQUEST_GET_SEARCH_COUPONS:{
                url = mContext.getString(R.string.base_url) + mContext.getString(R.string.api_get_coupons_by_search) + "?SearchKeyword=" + (String)requestData;
                break;
            }

            case Constants.REQUEST_GET_BANK_OFFER:{
                url = mContext.getString(R.string.base_url) + mContext.getString(R.string.api_bank_offers);
                break;
            }

            case Constants.REQUEST_NEWSLETTER_SUBSCRIPTION:{
                url = mContext.getString(R.string.base_url) + mContext.getString(R.string.api_news_letter) + "?EmailID=" + (String)requestData;
                break;
            }

            case Constants.REQUEST_HELP_SUPPORT:{
                url = mContext.getString(R.string.base_url) + mContext.getString(R.string.api_help_and_support);
                break;
            }

            case Constants.REQUEST_SLIDER_URL:{
                url = mContext.getString(R.string.base_url) + mContext.getString(R.string.api_slider_url);
                break;
            }
        }
        serviceRequest.setUrl(url);
        AsyncConnection asyncConnection = new AsyncConnection();
        asyncConnection.execute(serviceRequest);
        return serviceRequest;
    }


    @Override
    public void parseResponse(Response response) {
        switch (response.getDataType()){
            case Constants.REQUEST_GET_TOP_20_CATEGORY:{
                parseTop20CateGory(response);
                break;
            }
            case Constants.REQUEST_GET_BANK_OFFER:
            case Constants.REQUEST_GET_ALL_CATEGORY:{
                parseAllCategory(response);
                break;
            }
            case Constants.REQUEST_GET_ALL_STORE:
            case Constants.REQUEST_GET_TOP_20_STORE:{
                parseAllStores(response);
                break;
            }

            case Constants.REQUEST_GET_BEST_OFFER:{
                parseBestOffers(response);
                break;
            }

            case Constants.REQUEST_GET_COUPONS_BY_CATEGORIES:{
                parseCouponsByCategory(response);
                break;
            }

            case Constants.REQUEST_GET_COUPONS_BY_STORES:{
                parseGetCouponsByStores(response);
                break;
            }
            case Constants.REQUEST_GET_COUPONS_DETAILS:{
                parseGetCouponDetailsResult(response);
                break;
            }

            case Constants.REQUEST_GET_IMPORTANT_LINKS:{
                parseImportantLinksResult(response);
                break;
            }

            case Constants.REQUEST_GET_SEARCH_COUPONS:{
                parseCouponsBySearch(response);
                break;
            }

            case Constants.REQUEST_NEWSLETTER_SUBSCRIPTION:{
                parseNewsLetterResult(response);
                break;
            }

            case Constants.REQUEST_HELP_SUPPORT:{
                parseHelpSupportResponse(response);
                break;
            }

            case Constants.REQUEST_SLIDER_URL:{
                parseSliderResponse(response);
                break;
            }
        }
    }

    /**
     * Method to parse home screen slider response
     * @param response
     */
    private void parseSliderResponse(Response response) {
        boolean isSuccess = false;
        if(response.getErrorMessage() != null){
            isSuccess = false;
        } else {
            isSuccess = true;
            List<SliderUrl> sliderUrlList = JsonParser.getSliderUrl(response);
            response.setResponseObject(sliderUrlList);
        }
        response.setSuccess(isSuccess);
    }

    /**
     * Method to parse help and support result
     * @param response
     */
    private void parseHelpSupportResponse(Response response) {
        boolean isSuccess = false;
        if(response.getErrorMessage() != null){
            isSuccess = false;
        } else {
            isSuccess = true;
            BaseModel baseModel = JsonParser.getHelpAndSupport(response);
            response.setResponseObject(baseModel);
        }
        response.setSuccess(isSuccess);
    }

    /**
     * Method to parse news letter
     * @param response
     */
    private void parseNewsLetterResult(Response response) {
        boolean isSuccess = false;
        if(response.getErrorMessage() != null){
            isSuccess = false;
        } else {
            isSuccess = true;
            BaseModel baseModel = JsonParser.getNewsLetterResponse(response);
            response.setResponseObject(baseModel);
        }
        response.setSuccess(isSuccess);
    }

    /**
     * Method to parse search coupons result
     * @param response
     */
    private void parseCouponsBySearch(Response response) {
        boolean isSuccess = false;
        if(response.getErrorMessage() != null){
            isSuccess = false;
        } else {
            isSuccess = true;
            List<CouponsByCategoriesView> importantLinkModelList = JsonParser.getSearchCouponsResult(response);
            response.setResponseObject(importantLinkModelList);
        }
        response.setSuccess(isSuccess);
    }

    /**
     * Method to parse important links for app
     * @param response
     */
    private void parseImportantLinksResult(Response response) {
        boolean isSuccess = false;
        if(response.getErrorMessage() != null){
            isSuccess = false;
        } else {
            isSuccess = true;
            List<ImportantLinkModel> importantLinkModelList = JsonParser.getImportantLinks(response);
            response.setResponseObject(importantLinkModelList);
        }
        response.setSuccess(isSuccess);
    }

    /**
     * Method to parse coupon details
     * @param response
     */
    private void parseGetCouponDetailsResult(Response response) {
        boolean isSuccess = false;
        if(response.getErrorMessage() != null){
            isSuccess = false;
        } else {
            isSuccess = true;
            List<GetCouponsDetails> couponsDetailList = JsonParser.getCouponsDetailsList(response);
            response.setResponseObject(couponsDetailList);
        }
        response.setSuccess(isSuccess);
    }

    /**
     * Method to parse coupons by category list
     * @param response
     */
    private void parseCouponsByCategory(Response response) {
        boolean isSuccess = false;
        if(response.getErrorMessage() != null){
            isSuccess = false;
        } else {
            isSuccess = true;
            List<GetCouponByCategoryModel> couponsByCategoryList = JsonParser.getCouponByCategoryResult(response);
            response.setResponseObject(couponsByCategoryList);
        }
        response.setSuccess(isSuccess);
    }

    /**
     * Method to parse coupons by stores
     * @param response
     */
    private void parseGetCouponsByStores(Response response) {
        boolean isSuccess = false;
        if(response.getErrorMessage() != null){
            isSuccess = false;
        } else {
            isSuccess = true;
            List<GetCouponsByStoresResultModel> couponsByStoresResultModelList = JsonParser.getCouponsByStoreResult(response);
            response.setResponseObject(couponsByStoresResultModelList);
        }
        response.setSuccess(isSuccess);
    }

    /**
     * Method to parse best offer list
     * @param response
     */
    private void parseBestOffers(Response response) {
        boolean isSuccess = false;
        if(response.getErrorMessage() != null){
            isSuccess = false;
        } else {
            isSuccess = true;
            List<CouponsByCategoriesView> bestOfferModelList = JsonParser.getBestOffers(response);
            response.setResponseObject(bestOfferModelList);
        }
        response.setSuccess(isSuccess);
    }

    /**
     * Method to parse store response
     * @param response
     */
    private void parseAllStores(Response response) {
        boolean isSuccess = false;
        if(response.getErrorMessage() != null){
            isSuccess = false;
        } else {
            isSuccess = true;
            List<StoreModel> storeModelList = JsonParser.getStoreList(response);
            response.setResponseObject(storeModelList);
        }
        response.setSuccess(isSuccess);
    }

    /**
     *
     * @param response
     */
    private void parseAllCategory(Response response) {
        boolean isSuccess = false;
        if(response.getErrorMessage() != null){
            isSuccess = false;
        } else {
            isSuccess = true;
            List<CateGoryModel> cateGoryModelList = JsonParser.getCategoryList(response);
            response.setResponseObject(cateGoryModelList);
        }
        response.setSuccess(isSuccess);
    }

    /**
     *
     * @param response
     */
    private void parseTop20CateGory(Response response) {
        boolean isSuccess = false;
        if(response.getErrorMessage() != null){
            isSuccess = false;
        } else {
            isSuccess = true;
            List<CateGoryModel> cateGoryModelList = JsonParser.getCategoryList(response);
            response.setResponseObject(cateGoryModelList);
        }
        response.setSuccess(isSuccess);
    }
}
