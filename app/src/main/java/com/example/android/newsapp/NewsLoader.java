package com.example.android.newsapp;


import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import java.util.List;

public class NewsLoader extends AsyncTaskLoader<List<News>>{

    private static final String LOG_TAG = NewsLoader.class.getSimpleName();

    private String mUrl;

    public NewsLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();

    }

    @Override
    public List<News> loadInBackground() {
        if (mUrl == null) {
            return null;
        }

        List<News> newses = QueryUtils.fetchNewsData(mUrl);

        Log.i(LOG_TAG, "NewsLoader: loadInBackground is initiated");

        return newses;
    }
}
