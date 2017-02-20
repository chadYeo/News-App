package com.example.android.newsapp;

import android.app.LoaderManager;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SearchViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<List<News>>, SearchView.OnQueryTextListener {

    public static final String LOG_TAG = MainActivity.class.getSimpleName();

    private static final int NEWS_LOADER_ID = 1;

    private static final String NEWS_REQUEST_URL =
            "http://content.guardianapis.com/search?q=";

    private static final String NEWS_REQUST_API = "&api-key=test";

    private String mSearchInput = "";

    @BindView(R.id.empty_textView) TextView mEmptyTextView;
    @BindView(R.id.recycler_view) RecyclerView mRecyclerView;
    private NewsAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new NewsAdapter(new ArrayList<News>());

        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);

        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
        searchView.setOnQueryTextListener(this);
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String input) {
        if (isNetworkConnected()) {
            input = input.replace(" ", "+");
            input.trim();
            mSearchInput = NEWS_REQUEST_URL + input + NEWS_REQUST_API;

            LoaderManager loaderManager = getLoaderManager();
            loaderManager.restartLoader(NEWS_LOADER_ID, null, this);


        } else {
            Toast.makeText(getApplicationContext(), "There's no internet connection", Toast.LENGTH_SHORT).show();
        }
        return true;

    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return true;
    }

    @Override
    public Loader<List<News>> onCreateLoader(int id, Bundle args) {
        return new NewsLoader(this, mSearchInput);
    }

    @Override
    public void onLoadFinished(Loader<List<News>> loader, List<News> data) {
        if (data != null && !data.isEmpty()) {
            mRecyclerView.setVisibility(View.VISIBLE);
            mEmptyTextView.setVisibility(View.GONE);

            mAdapter = new NewsAdapter(data);
            mRecyclerView.setAdapter(mAdapter);
        } else {
            mEmptyTextView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<News>> loader) {
        mAdapter.clear();
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnectedOrConnecting();
    }
}
