package com.app.testSample.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.v7.widget.SearchView;

/**
 * Model for Coupons by category
 */

public class CouponsByCategoriesView implements Parcelable{

    private String Description;
    private String ExpiryDate;
    private String Image_URL;
    private String ObjectID;
    private String PostStatus;
    private String PublishedDate;
    private String Title;
    private String responseCode;
    private String message;
    private String SocialURL;

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getExpiryDate() {
        return ExpiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        ExpiryDate = expiryDate;
    }

    public String getImage_URL() {
        return Image_URL;
    }

    public void setImage_URL(String image_URL) {
        Image_URL = image_URL;
    }

    public String getObjectID() {
        return ObjectID;
    }

    public void setObjectID(String objectID) {
        ObjectID = objectID;
    }

    public String getPostStatus() {
        return PostStatus;
    }

    public void setPostStatus(String postStatus) {
        PostStatus = postStatus;
    }

    public String getPublishedDate() {
        return PublishedDate;
    }

    public void setPublishedDate(String publishedDate) {
        PublishedDate = publishedDate;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSocialURL() {
        return SocialURL;
    }

    public void setSocialURL(String socialURL) {
        SocialURL = socialURL;
    }

    // no args constructor
    public CouponsByCategoriesView() {

    }

    public CouponsByCategoriesView(Parcel source) {
        Description = source.readString();
        ExpiryDate = source.readString();
        Image_URL = source.readString();
        ObjectID = source.readString();
        PostStatus = source.readString();
        PublishedDate = source.readString();
        Title = source.readString();
        SocialURL = source.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(Description);
        dest.writeString(ExpiryDate);
        dest.writeString(Image_URL);
        dest.writeString(ObjectID);
        dest.writeString(PostStatus);
        dest.writeString(PublishedDate);
        dest.writeString(Title);
        dest.writeString(SocialURL);
    }


    /**
     * @return the cREATOR
     */
    public static Creator<CouponsByCategoriesView> getCREATOR() {
        return CREATOR;
    }

    /**
     * @param cREATOR the cREATOR to set
     */
    public static void setCREATOR(
            Creator<CouponsByCategoriesView> cREATOR) {
        CREATOR = cREATOR;
    }



    public static Creator<CouponsByCategoriesView> CREATOR = new Creator<CouponsByCategoriesView>() {
        public CouponsByCategoriesView createFromParcel(Parcel source) {
            return new CouponsByCategoriesView(source);
        }

        public CouponsByCategoriesView[] newArray(int size) {
            return new CouponsByCategoriesView[size];
        }
    };

}
