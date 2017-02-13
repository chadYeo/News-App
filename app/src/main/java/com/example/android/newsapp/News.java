package com.example.android.newsapp;

public class News {

    private String mSectionName;
    private String mWebTitle;
    private String mType;
    private String mWebUrl;
    private String mWebPublicationDate;

    public News(String sectionName, String webTitle, String type, String webUrl, String webPublicationDate) {
        mSectionName = sectionName;
        mWebTitle = webTitle;
        mType = type;
        mWebUrl = webUrl;
        mWebPublicationDate = webPublicationDate;
    }

    public String getmSectionName() {
        return mSectionName;
    }

    public String getmWebTitle() {
        return mWebTitle;
    }

    public String getmType() {
        return mType;
    }

    public String getmWebUrl() {
        return mWebUrl;
    }

    public String getmWebPublicationDate() {
        return mWebPublicationDate;
    }
}
