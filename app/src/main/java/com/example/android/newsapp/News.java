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

    public String getSectionName() {
        return mSectionName;
    }

    public String getWebTitle() {
        return mWebTitle;
    }

    public String getType() {
        return mType;
    }

    public String getWebUrl() {
        return mWebUrl;
    }

    public String getWebPublicationDate() {
        return mWebPublicationDate;
    }
}



