package com.example.android.newsapp;

import android.app.SearchManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

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

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    public static final String LOG_TAG = MainActivity.class.getSimpleName();

    private static final String NEWS_REQUEST_URL = "http://content.guardianapis.com/search?q=debate&tag=politics/politics&from-date=2014-01-01&api-key=test";

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

        NewsAyncTask task = new NewsAyncTask();
        task.execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);

        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        return true;
    }

    private class NewsAyncTask extends AsyncTask<URL, Void, ArrayList<News>> {

        @Override
        protected ArrayList<News> doInBackground(URL... urls) {
            //Create URL
            URL url = createUrl(NEWS_REQUEST_URL);

            // Perform HTTP Request to the URL and receive a JSON response back
            String jsonResponse = "";
            try {
                jsonResponse = makeHttpRequest(url);
            } catch (IOException e) {
                Log.e(LOG_TAG, "IOException ", e);
            }

            // Extract relevant field from the JSON and create an ArrayList of News
            ArrayList<News> newsArrayList = extractNewsFromJson(jsonResponse);

            Log.i(LOG_TAG, "doInBackground is initiated");

            // Return the object as the result for the AsyncTask
            return newsArrayList;
        }

        @Override
        protected void onPostExecute(ArrayList<News> newses) {
            Log.i(LOG_TAG, "onPostExecute is initiated");

            if (newses != null && !newses.isEmpty()) {
                mRecyclerView.setVisibility(View.VISIBLE);
                mEmptyTextView.setVisibility(View.GONE);

                mAdapter = new NewsAdapter(newses);
                mRecyclerView.setAdapter(mAdapter);
            } else {
                mEmptyTextView.setVisibility(View.VISIBLE);
            }
        }

        //Returns new URL object from the given string URL.
        private URL createUrl(String stringUrl) {
            URL url = null;
            try {
                url = new URL(stringUrl);
            } catch (MalformedURLException e) {
                Log.e(LOG_TAG, "Error with creating URL ", e);
            }
            Log.i(LOG_TAG, "createUrl is initiated");
            return url;
        }

        private String makeHttpRequest(URL url) throws IOException{
            String jsonResponse = "";
            HttpURLConnection urlConnection = null;
            InputStream inputStream = null;

            try{
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setConnectTimeout(100000);
                urlConnection.setReadTimeout(100000);
                urlConnection.connect();

                if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    inputStream = urlConnection.getInputStream();
                    jsonResponse = readFromStream(inputStream);
                } else {
                    Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
                }
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error with making HTTP Request", e);
            }finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (inputStream != null) {
                    inputStream.close();
                }
            }
            Log.i(LOG_TAG, "makeHttpRequest is initiated");
            return jsonResponse;
        }

        private String readFromStream(InputStream inputStream) throws IOException {
            StringBuilder output = new StringBuilder();
            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
                BufferedReader reader = new BufferedReader(inputStreamReader);
                String line = reader.readLine();
                while (line != null) {
                    output.append(line);
                    line = reader.readLine();
                }
            }
            Log.i(LOG_TAG, "readFromStream is initiated");

            return output.toString();
        }

        private ArrayList<News> extractNewsFromJson(String newsJSON) {
            if (TextUtils.isEmpty(newsJSON)) {
                return null;
            }

            ArrayList<News> newsArrayList = new ArrayList<>();

            Log.i(LOG_TAG, "extractNewsFromJson is initiated");

            try {
                JSONObject baseJsonResponse = new JSONObject(newsJSON);
                JSONObject response = baseJsonResponse.getJSONObject("response");
                JSONArray results = response.getJSONArray("results");

                // Extract out the sectionName, webTitle, type, webUrl, webPublicationDate.
                for (int i=0; i<results.length(); i++) {
                    JSONObject currentItem = results.getJSONObject(i);

                    String sectionName = "";
                    if (currentItem.optString("sectionName") != null) {
                        sectionName = currentItem.optString("sectionName");
                    }

                    String webTitle = "";
                    if (currentItem.optString("webTitle") != null) {
                        webTitle = currentItem.optString("webTitle");
                    }

                    String type = "";
                    if(currentItem.optString("type") != null) {
                        type = currentItem.optString("type");
                    }

                    String webUrl = "";
                    if (currentItem.optString("webUrl") != null) {
                        webUrl = currentItem.optString("webUrl");
                    }

                    String webPublicationDate = "";
                    if (currentItem.optString("webPublicationDate") != null) {
                        webPublicationDate = currentItem.optString("webPublicationDate");
                    }

                    newsArrayList.add(new News(sectionName, webTitle, type, webUrl, webPublicationDate));
                }

            } catch (JSONException e) {
                Log.e(LOG_TAG, "Problem parsing the book JSON results", e);
            }
            return newsArrayList;
        }
    }
}
