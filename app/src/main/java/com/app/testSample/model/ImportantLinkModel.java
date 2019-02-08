package com.app.testSample.model;


public class ImportantLinkModel extends BaseModel {
    private String Facebook_URL;
    private String Feedback_Email;
    private String Linkdin_URL;
    private String Twitter_URL;

    public String getFacebook_URL() {
        return Facebook_URL;
    }

    public void setFacebook_URL(String facebook_URL) {
        Facebook_URL = facebook_URL;
    }

    public String getFeedback_Email() {
        return Feedback_Email;
    }

    public void setFeedback_Email(String feedback_Email) {
        Feedback_Email = feedback_Email;
    }

    public String getLinkdin_URL() {
        return Linkdin_URL;
    }

    public void setLinkdin_URL(String linkdin_URL) {
        Linkdin_URL = linkdin_URL;
    }

    public String getTwitter_URL() {
        return Twitter_URL;
    }

    public void setTwitter_URL(String twitter_URL) {
        Twitter_URL = twitter_URL;
    }
}
