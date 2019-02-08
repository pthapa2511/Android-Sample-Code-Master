package com.app.testSample.model;

import java.util.List;


public class GetCouponByCategoryModel extends BaseModel {
    private List<CouponsByCategoriesView> couponsByCategoriesViewList;
    private String CategoriesDescription;
    private String CategoriesName;
    private String CouponsCount;


    public List<CouponsByCategoriesView> getCouponsByCategoriesViewList() {
        return couponsByCategoriesViewList;
    }

    public void setCouponsByCategoriesViewList(List<CouponsByCategoriesView> couponsByCategoriesViewList) {
        this.couponsByCategoriesViewList = couponsByCategoriesViewList;
    }

    public String getCategoriesDescription() {
        return CategoriesDescription;
    }

    public void setCategoriesDescription(String categoriesDescription) {
        CategoriesDescription = categoriesDescription;
    }

    public String getCategoriesName() {
        return CategoriesName;
    }

    public void setCategoriesName(String categoriesName) {
        CategoriesName = categoriesName;
    }

    public String getCouponsCount() {
        return CouponsCount;
    }

    public void setCouponsCount(String couponsCount) {
        CouponsCount = couponsCount;
    }
}
