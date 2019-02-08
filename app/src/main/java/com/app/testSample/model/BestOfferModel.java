package com.app.testSample.model;

/**
 * Model for Best offers
 */

public class BestOfferModel extends BaseModel {

    private String Description;
    private String ExpiryDate;
    private String Image_URL;
    private String ObjectID;
    private String PostStatus;
    private String PublishedDate;
    private String Title;

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
}
