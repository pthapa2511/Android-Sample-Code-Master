package com.app.testSample.parser;

import android.util.Log;

import com.app.testSample.model.BaseModel;
import com.app.testSample.model.BestOfferModel;
import com.app.testSample.model.CateGoryModel;
import com.app.testSample.model.CouponsByCategoriesView;
import com.app.testSample.model.GetCouponByCategoryModel;
import com.app.testSample.model.GetCouponsByStoresResultModel;
import com.app.testSample.model.GetCouponsDetails;
import com.app.testSample.model.GetStoreDetails;
import com.app.testSample.model.ImportantLinkModel;
import com.app.testSample.model.SliderUrl;
import com.app.testSample.model.StoreModel;
import com.app.testSample.utility.StringUtils;
import com.app.testSample.utility.network.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Class will parse all the application related Json data
 * received from multiple API's.
 */
public class JsonParser {
    private static final String LOG_TAG = "JsonParser";

    /**
     * Method will parse all the categories data
     * @param response
     * @return
     */
    public static List<CateGoryModel> getCategoryList(Response response){
        List<CateGoryModel> cateGoryModelList = new ArrayList<>();
        try{
            String responseString = new String(response.getResponseData());
            Log.i(LOG_TAG, responseString);

            JSONArray jsonArray = new JSONArray(responseString);
            JSONObject jsonObject = jsonArray.getJSONObject(0);
            JSONArray childArray = jsonObject.getJSONArray("GetAllCategories");
            int arraySize = childArray.length();
            for(int i = 0; i< arraySize; i++){
                JSONObject childJson = childArray.getJSONObject(i);
                CateGoryModel cateGoryModel = new CateGoryModel();
                cateGoryModel.setCategoryName(childJson.optString("CategoryName"));
                cateGoryModel.setCouponCount(childJson.optString("CouponCount"));
                cateGoryModel.setCouponDescription(childJson.optString("CouponDescription"));
                cateGoryModel.setTermTaxonomyId(childJson.optString("TermTaxonomyID"));
                cateGoryModel.setTermId(childJson.optString("TermID"));
                cateGoryModel.setResponseCode(jsonObject.optString("Response_Code"));
                cateGoryModel.setMessage(jsonObject.optString("Response_Message"));
                cateGoryModelList.add(cateGoryModel);
            }


        } catch (JSONException je){
            Log.e(LOG_TAG, "getCategoryList" + je);
        }

        return cateGoryModelList;
    }



    /**
     * Method will parse all the stores data
     * @param response
     * @return
     */
    public static List<StoreModel> getStoreList(Response response){
        List<StoreModel> storeModelList = new ArrayList<>();
        try{
            String responseString = new String(response.getResponseData());
            Log.i(LOG_TAG, responseString);

            JSONArray jsonArray = new JSONArray(responseString);
            JSONObject jsonObject = jsonArray.getJSONObject(0);
            JSONArray childArray = jsonObject.getJSONArray("GetAllStores");
            int arraySize = childArray.length();
            for(int i = 0; i< arraySize; i++){
                JSONObject childJson = childArray.getJSONObject(i);
                StoreModel storeModel = new StoreModel();
                storeModel.setStorename(childJson.optString("Storename"));
                storeModel.setCouponCount(childJson.optString("CouponCount"));
                storeModel.setIMAGE_URL(childJson.optString("IMAGE_URL"));
                storeModel.setTermID(childJson.optString("TermID"));
                storeModel.setTermTaxonomyID(childJson.optString("TermTaxonomyID"));
                storeModel.setResponseCode(jsonObject.optString("Response_Code"));
                storeModel.setMessage(jsonObject.optString("Response_Message"));
                storeModelList.add(storeModel);
            }


        } catch (JSONException je){
            Log.e(LOG_TAG, "getStoreList" + je);
        }

        return storeModelList;
    }

    /**
     * Method will parse all the stores data
     * @param response
     * @return
     */
    public static List<CouponsByCategoriesView> getBestOffers(Response response){
        List<CouponsByCategoriesView> bestOfferModelList = new ArrayList<>();
        try{
            String responseString = new String(response.getResponseData());
            Log.i(LOG_TAG, responseString);

            JSONArray jsonArray = new JSONArray(responseString);
            JSONObject jsonObject = jsonArray.getJSONObject(0);
            JSONArray childArray = jsonObject.getJSONArray("CouponsByCategoriesView");
            int arraySize = childArray.length();
            for(int i = 0; i< arraySize; i++){
                JSONObject childJson = childArray.getJSONObject(i);
                CouponsByCategoriesView model = new CouponsByCategoriesView();
                String description = childJson.optString("Description");
                if(description.contains("<\\/div>")){
                    description = description.replace("<\\/div>", "");
                }
                if(description.contains("[")){
                    description = description.substring(0, description.indexOf("["));
                }
                model.setDescription(description);
                model.setExpiryDate(childJson.optString("ExpiryDate"));
                model.setImage_URL(childJson.optString("Image_URL"));
                model.setObjectID(childJson.optString("ObjectID"));
                model.setPostStatus(childJson.optString("PostStatus"));
                model.setPublishedDate(childJson.optString("PublishedDate"));
                model.setTitle(childJson.optString("Title"));
                model.setSocialURL(childJson.optString("SocialURL"));
                model.setResponseCode(jsonObject.optString("Response_Code"));
                model.setMessage(jsonObject.optString("Response_Message"));
                bestOfferModelList.add(model);
            }


        } catch (JSONException je){
            Log.e(LOG_TAG, "getBestOffers" + je);
        }

        return bestOfferModelList;
    }

    /**
     * Method to parse Get coupons by stores result
     * @param response
     * @return
     */
    public static List<GetCouponsByStoresResultModel> getCouponsByStoreResult(Response response){
        List<GetCouponsByStoresResultModel> getCouponsByStoresResultModelList = new ArrayList<>();

        try{
            String result = new String(response.getResponseData());
            JSONObject jsonObject = new JSONObject(result);
            JSONArray jsonArray = jsonObject.getJSONArray("GetCouponsBy_StoresResult");
            JSONObject childJsonObject = jsonArray.getJSONObject(0);
            JSONArray couponsByCategoryView = childJsonObject.getJSONArray("CouponsByCategoriesView");
            List<CouponsByCategoriesView> couponByCategoryModelList = new ArrayList<>();
            for(int i =0; i< couponsByCategoryView.length(); i++){
                JSONObject couponsByCategoryJson = couponsByCategoryView.getJSONObject(i);
                CouponsByCategoriesView couponsByCategoriesView = new CouponsByCategoriesView();
                couponByCategoryModelList.add(couponsByCategoriesView);
                couponsByCategoriesView.setTitle(couponsByCategoryJson.optString("Title"));
                couponsByCategoriesView.setPublishedDate(couponsByCategoryJson.optString("PublishedDate"));
                couponsByCategoriesView.setPostStatus(couponsByCategoryJson.optString("PostStatus"));
                couponsByCategoriesView.setObjectID(couponsByCategoryJson.optString("ObjectID"));
                couponsByCategoriesView.setImage_URL(couponsByCategoryJson.optString("Image_URL"));
                couponsByCategoriesView.setExpiryDate(couponsByCategoryJson.optString("ExpiryDate"));
                couponsByCategoriesView.setDescription(couponsByCategoryJson.optString("Description"));
                couponsByCategoriesView.setSocialURL(couponsByCategoryJson.optString("SocialURL"));
            }

            JSONArray getStoreDetails = childJsonObject.getJSONArray("GetStoreDetails");
            List<GetStoreDetails> getStoreDetailsList = new ArrayList<>();
            for(int i =0; i< getStoreDetails.length(); i++){
                GetStoreDetails storeDetailsModel = new GetStoreDetails();
                getStoreDetailsList.add(storeDetailsModel);
                JSONObject storeDetailsJson = getStoreDetails.getJSONObject(i);
                storeDetailsModel.setCouponCount(storeDetailsJson.optString("CouponCount"));
                storeDetailsModel.setDescription(storeDetailsJson.optString("Description"));
                storeDetailsModel.setImage_URL(storeDetailsJson.optString("Image_URL"));
                storeDetailsModel.setName(storeDetailsJson.optString("Name"));
            }

            GetCouponsByStoresResultModel model = new GetCouponsByStoresResultModel();
            model.setCouponsByCategoriesViewList(couponByCategoryModelList);
            model.setGetStoreDetailsList(getStoreDetailsList);
            model.setResponseCode(childJsonObject.optString("Response_Code"));
            model.setMessage(childJsonObject.optString("Response_Message"));
            getCouponsByStoresResultModelList.add(model);
        } catch (JSONException je){
            Log.e(LOG_TAG, "getCouponsByStoreResult()" + je);
        }

        return getCouponsByStoresResultModelList;
    }

    /**
     * Method to return the coupons by category result
     * @param response
     * @return
     */
    public static List<GetCouponByCategoryModel> getCouponByCategoryResult(Response response){
        List<GetCouponByCategoryModel> couponByCategoryModelList = new ArrayList<>();

        try{
            String result = new String(response.getResponseData());
            JSONObject jsonObject = new JSONObject(result);
            JSONArray jsonArray = jsonObject.getJSONArray("GetCouponsBy_CategoriesResult");
            JSONObject childJsonObject = jsonArray.getJSONObject(0);
            JSONArray couponsByCategoryView = childJsonObject.getJSONArray("CouponsByCategoriesView");
            List<CouponsByCategoriesView> couponsByCategoriesViewList = new ArrayList<>();
            for(int i =0; i< couponsByCategoryView.length(); i++){
                JSONObject couponsByCategoryJson = couponsByCategoryView.getJSONObject(i);
                CouponsByCategoriesView couponsByCategoriesView = new CouponsByCategoriesView();
                couponsByCategoriesViewList.add(couponsByCategoriesView);
                couponsByCategoriesView.setTitle(couponsByCategoryJson.optString("Title"));
                couponsByCategoriesView.setPublishedDate(couponsByCategoryJson.optString("PublishedDate"));
                couponsByCategoriesView.setPostStatus(couponsByCategoryJson.optString("PostStatus"));
                couponsByCategoriesView.setObjectID(couponsByCategoryJson.optString("ObjectID"));
                couponsByCategoriesView.setImage_URL(couponsByCategoryJson.optString("Image_URL"));
                couponsByCategoriesView.setExpiryDate(couponsByCategoryJson.optString("ExpiryDate"));
                couponsByCategoriesView.setDescription(couponsByCategoryJson.optString("Description"));
                couponsByCategoriesView.setSocialURL(couponsByCategoryJson.optString("SocialURL"));
            }

           /* JSONArray getStoreDetails = childJsonObject.getJSONArray("GetStoreDetails");
            List<GetStoreDetails> getStoreDetailsList = new ArrayList<>();
            for(int i =0; i< getStoreDetails.length(); i++){
                GetStoreDetails storeDetailsModel = new GetStoreDetails();
                getStoreDetailsList.add(storeDetailsModel);
                JSONObject storeDetailsJson = getStoreDetails.getJSONObject(i);
                storeDetailsModel.setMetaKey(storeDetailsJson.optString("MetaKey"));
                storeDetailsModel.setMetavalue(storeDetailsJson.optString("Metavalue"));
            }*/

            GetCouponByCategoryModel model = new GetCouponByCategoryModel();
            model.setCouponsByCategoriesViewList(couponsByCategoriesViewList);
            model.setCategoriesDescription(childJsonObject.optString("CategoriesDescription"));
            model.setCategoriesName(childJsonObject.optString("CategoriesName"));
            model.setCouponsCount(childJsonObject.optString("CouponsCount"));
            model.setResponseCode(childJsonObject.optString("Response_Code"));
            model.setMessage(childJsonObject.optString("Response_Message"));
            couponByCategoryModelList.add(model);
        } catch (JSONException je){
            Log.e(LOG_TAG, "getCouponByCategoryResult()" + je);
        }

        return couponByCategoryModelList;
    }

    /**
     * Method to parse coupons details list
     * @param response
     * @return
     */
    public static List<GetCouponsDetails> getCouponsDetailsList(Response response){
        List<GetCouponsDetails> couponsDetailsList = new ArrayList<>();
        try {
            String couponsDetails = new String(response.getResponseData());
            Log.i(LOG_TAG, couponsDetails);
            JSONObject jsonObject = new JSONObject(couponsDetails);
            JSONArray jsonArray = jsonObject.getJSONArray("GetCoupons_DetailsResult");
            for(int i =0; i<jsonArray.length(); i++){
                GetCouponsDetails model = new GetCouponsDetails();
                JSONObject childJson = jsonArray.getJSONObject(i);
                model.setResponseCode(childJson.optString("Response_Code"));
                model.setMessage(childJson.optString("Response_Message"));
                model.setCouponAffiliateUrl(childJson.optString("clpr_coupon_aff_url"));
                model.setCouponCode(childJson.optString("clpr_coupon_code"));
                model.setCouponExpiryDate(childJson.optString("clpr_expire_date"));
                model.setCouponType(childJson.optString("coupon_type"));
                couponsDetailsList.add(model);
            }
        } catch (JSONException je){
            Log.e(LOG_TAG, "getCouponsDetailsList()" + je);
        }
        return couponsDetailsList;
    }

    /**
     * Method to parse important links like facebook, twitter, linked-in
     * @param response
     * @return
     */
    public static List<ImportantLinkModel> getImportantLinks(Response response){
        List<ImportantLinkModel> importantLinkModelList = new ArrayList<>();
        try {
            String importantLinks = new String(response.getResponseData());
            Log.i(LOG_TAG, importantLinks);
            JSONObject jsonObject = new JSONObject(importantLinks);
            JSONArray jsonArray = jsonObject.getJSONArray("Important_linksResult");
            for(int i =0; i<jsonArray.length(); i++){
                ImportantLinkModel model = new ImportantLinkModel();
                JSONObject childJson = jsonArray.getJSONObject(i);
                model.setResponseCode(childJson.optString("Response_Code"));
                model.setMessage(childJson.optString("Response_Message"));
                model.setFacebook_URL(childJson.optString("Facebook_URL"));
                model.setFeedback_Email(childJson.optString("Feedback_Email"));
                model.setLinkdin_URL(childJson.optString("Linkdin_URL"));
                model.setTwitter_URL(childJson.optString("Twitter_URL"));
                importantLinkModelList.add(model);
            }
        } catch (JSONException je){
            Log.e(LOG_TAG, "getImportantLinks()" + je);
        }
        return importantLinkModelList;
    }

    /**
     * Method to parse search coupons result
     * We have used CouponsByCategoriesView model as the data is same
     * @param response
     * @return
     */
    public static List<CouponsByCategoriesView> getSearchCouponsResult(Response response){
        List<CouponsByCategoriesView> couponsByCategoriesViewList = new ArrayList<>();
        try {
            String searchResult = new String(response.getResponseData());
            Log.i(LOG_TAG, searchResult);
            JSONObject jsonObject = new JSONObject(searchResult);
            JSONArray jsonArray = jsonObject.getJSONArray("GetCouponsBy_SearchResult");
            JSONObject childJson = jsonArray.getJSONObject(0);
            JSONArray couponsArray = childJson.getJSONArray("CouponsByCategoriesView");

            for (int i =0; i< couponsArray.length(); i++){
                CouponsByCategoriesView model = new CouponsByCategoriesView();
                JSONObject coupons = couponsArray.getJSONObject(i);
                model.setDescription(coupons.optString("Description"));
                model.setExpiryDate(coupons.optString("ExpiryDate"));
                model.setImage_URL(coupons.optString("Image_URL"));
                model.setObjectID(coupons.optString("ObjectID"));
                model.setPostStatus(coupons.optString("PostStatus"));
                model.setPublishedDate(coupons.optString("PublishedDate"));
                model.setTitle(coupons.optString("Title"));
                model.setResponseCode(childJson.optString("Response_Code"));
                model.setMessage(childJson.optString("Response_Message"));
                model.setSocialURL(childJson.optString("SocialURL"));
                couponsByCategoriesViewList.add(model);
            }
        } catch (JSONException je){
            Log.e(LOG_TAG, "getSearchCouponsResult()" + je);
        }
        return couponsByCategoriesViewList;
    }

    /**
     * Method to parse NewsLetterResponse
     * @param response
     * @return
     */
    public static BaseModel getNewsLetterResponse(Response response){
        BaseModel baseModel = new BaseModel();
        try{
            String newsLetterResponse = new String(response.getResponseData());
            JSONObject jsonObject = new JSONObject(newsLetterResponse);
            JSONArray jsonArray = jsonObject.getJSONArray("NewsletterSubscribeResult");
            JSONObject childJson = jsonArray.getJSONObject(0);
            baseModel.setResponseCode(childJson.optString("Response_Code"));
            baseModel.setMessage(childJson.optString("Response_Message"));
        }catch (JSONException je){
            Log.e(LOG_TAG, "getNewsLetterResponse()" + je);
        }
        return baseModel;
    }

    /**
     * Method to parse NewsLetterResponse
     * @param response
     * @return
     */
    public static BaseModel getHelpAndSupport(Response response){
        BaseModel baseModel = new BaseModel();
        try{
            String helpSupportResult = new String(response.getResponseData());
            JSONObject jsonObject = new JSONObject(helpSupportResult);
            JSONArray jsonArray = jsonObject.getJSONArray("Help_SupportResult");
            JSONObject childJson = jsonArray.getJSONObject(0);
            baseModel.setResponseCode(childJson.optString("Response_Code"));
            baseModel.setMessage(childJson.optString("Response_Message"));
        }catch (JSONException je){
            Log.e(LOG_TAG, "getNewsLetterResponse()" + je);
        }
        return baseModel;
    }

    /**
     * Method to parse NewsLetterResponse
     * @param response
     * @return
     */
    public static List<SliderUrl> getSliderUrl(Response response){
        List<SliderUrl> sliderUrlList = new ArrayList<>();
        try{
            String sliderUrlResult = new String(response.getResponseData());
            JSONObject jsonObject = new JSONObject(sliderUrlResult);
            JSONArray jsonArray = jsonObject.getJSONArray("Slider_URLResult");
            for(int i = 0; i< jsonArray.length(); i++){
                JSONObject childJson = jsonArray.getJSONObject(i);
                SliderUrl sliderUrl = new SliderUrl();
                sliderUrl.setImageUrl(childJson.optString("ImageURL"));
                sliderUrl.setLinkUrl(childJson.optString("LinkURL"));
                sliderUrlList.add(sliderUrl);
            }

        }catch (JSONException je){
            Log.e(LOG_TAG, "getSliderUrl()" + je);
        }
        return sliderUrlList;
    }
}
