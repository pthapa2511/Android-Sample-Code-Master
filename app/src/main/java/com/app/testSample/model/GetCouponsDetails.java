package com.app.testSample.model;

public class GetCouponsDetails extends BaseModel {
    private String couponType;
    private String couponCode;
    private String couponAffiliateUrl;
    private String couponExpiryDate;


    public String getCouponType() {
        return couponType;
    }

    public void setCouponType(String couponType) {
        this.couponType = couponType;
    }

    public String getCouponCode() {
        return couponCode;
    }

    public void setCouponCode(String couponCode) {
        this.couponCode = couponCode;
    }

    public String getCouponAffiliateUrl() {
        return couponAffiliateUrl;
    }

    public void setCouponAffiliateUrl(String couponAffiliateUrl) {
        this.couponAffiliateUrl = couponAffiliateUrl;
    }

    public String getCouponExpiryDate() {
        return couponExpiryDate;
    }

    public void setCouponExpiryDate(String couponExpiryDate) {
        this.couponExpiryDate = couponExpiryDate;
    }
}
