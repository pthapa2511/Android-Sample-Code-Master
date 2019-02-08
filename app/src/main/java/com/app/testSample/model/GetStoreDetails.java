package com.app.testSample.model;

public class GetStoreDetails{
    private String CouponCount;
    private String Description;
    private String Image_URL;
    private String Name;

    public String getCouponCount() {
        return CouponCount;
    }

    public void setCouponCount(String couponCount) {
        CouponCount = couponCount;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getImage_URL() {
        return Image_URL;
    }

    public void setImage_URL(String image_URL) {
        Image_URL = image_URL;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }
}