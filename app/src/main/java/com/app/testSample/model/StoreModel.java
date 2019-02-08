package com.app.testSample.model;

import java.util.Comparator;

public class StoreModel extends BaseModel {
    private String CouponCount;
    private String IMAGE_URL;
    private String Storename;
    private String TermID;
    private String TermTaxonomyID;

    public String getCouponCount() {
        return CouponCount;
    }

    public void setCouponCount(String couponCount) {
        CouponCount = couponCount;
    }

    public String getIMAGE_URL() {
        return IMAGE_URL;
    }

    public void setIMAGE_URL(String IMAGE_URL) {
        this.IMAGE_URL = IMAGE_URL;
    }

    public String getStorename() {
        return Storename;
    }

    public void setStorename(String storename) {
        Storename = storename;
    }

    public String getTermID() {
        return TermID;
    }

    public void setTermID(String termID) {
        TermID = termID;
    }

    public String getTermTaxonomyID() {
        return TermTaxonomyID;
    }

    public void setTermTaxonomyID(String termTaxonomyID) {
        TermTaxonomyID = termTaxonomyID;
    }

    public static Comparator<StoreModel> COMPARE_BY_STORE_NAME = new Comparator<StoreModel>() {
        public int compare(StoreModel one, StoreModel other) {
            return one.Storename.compareTo(other.Storename);
        }
    };
}
