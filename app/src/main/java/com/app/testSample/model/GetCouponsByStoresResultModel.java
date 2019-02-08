package com.app.testSample.model;

import java.util.List;


public class GetCouponsByStoresResultModel extends BaseModel {
    private List<CouponsByCategoriesView> couponsByCategoriesViewList;
    private List<GetStoreDetails> getStoreDetailsList;

    public List<CouponsByCategoriesView> getCouponsByCategoriesViewList() {
        return couponsByCategoriesViewList;
    }

    public void setCouponsByCategoriesViewList(List<CouponsByCategoriesView> couponsByCategoriesViewList) {
        this.couponsByCategoriesViewList = couponsByCategoriesViewList;
    }

    public List<GetStoreDetails> getGetStoreDetailsList() {
        return getStoreDetailsList;
    }

    public void setGetStoreDetailsList(List<GetStoreDetails> getStoreDetailsList) {
        this.getStoreDetailsList = getStoreDetailsList;
    }
}
