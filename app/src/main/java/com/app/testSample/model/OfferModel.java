package com.app.testSample.model;

public class OfferModel extends BaseModel {
    private String imageUrl;
    private String offerTitle;
    private String description;
    private String expiryDate;

    /**
     *
     * @return imageUrl
     */
    public String getImageUrl() {
        return imageUrl;
    }

    /**
     * Set the imageUrl to set
     * @param imageUrl
     */
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    /**
     *
     * @return offerTitle
     */
    public String getOfferTitle() {
        return offerTitle;
    }

    /**
     * Set the offerTitle to set
     * @param offerTitle
     */
    public void setOfferTitle(String offerTitle) {
        this.offerTitle = offerTitle;
    }

    /**
     *
     * @return description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Set the description to set
     * @param description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     *
     * @return expiryDate
     */
    public String getExpiryDate() {
        return expiryDate;
    }

    /**
     * Set the expiryDate to set
     * @param expiryDate
     */
    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }
}
