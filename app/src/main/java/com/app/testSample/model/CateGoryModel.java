package com.app.testSample.model;

import java.util.Comparator;

/**
 * Model for Categories
 */

public class CateGoryModel extends BaseModel {

    private String categoryName;
    private String couponCount;
    private String couponDescription;
    private String termTaxonomyId;
    private String termId;

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCouponCount() {
        return couponCount;
    }

    public void setCouponCount(String couponCount) {
        this.couponCount = couponCount;
    }

    public String getCouponDescription() {
        return couponDescription;
    }

    public void setCouponDescription(String couponDescription) {
        this.couponDescription = couponDescription;
    }

    public String getTermTaxonomyId() {
        return termTaxonomyId;
    }

    public String getTermId() {
        return termId;
    }

    public void setTermId(String termId) {
        this.termId = termId;
    }

    public void setTermTaxonomyId(String termTaxonomyId) {
        this.termTaxonomyId = termTaxonomyId;
    }


    public static Comparator<CateGoryModel> COMPARE_BY_CATEGORY_NAME = new Comparator<CateGoryModel>() {
        public int compare(CateGoryModel one, CateGoryModel other) {
            return one.categoryName.compareTo(other.categoryName);
        }
    };

}
