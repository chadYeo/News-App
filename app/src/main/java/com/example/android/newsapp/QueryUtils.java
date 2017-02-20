package com.example.android.newsapp;


import android.text.TextUtils;
import android.util.Log;

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

public class QueryUtils {

    static final String LOG_TAG = QueryUtils.class.getSimpleName();

    public QueryUtils() {
    }

    //Returns new URL object from the given string URL.
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error with creating URL ", e);
        }
        Log.i(LOG_TAG, "createUrl is initiated");
        return url;
    }

    private static String makeHttpRequest(URL url) throws IOException{
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
                Log.i(LOG_TAG, "HttpURLConnction is Good: 200");
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

    private static String readFromStream(InputStream inputStream) throws IOException {
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

    private static ArrayList<News> extractNewsFromJson(String newsJSON) {
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
        Log.i(LOG_TAG, "extractNewsFromJson is initiated");
        return newsArrayList;
    }

    // This helper method ties all the steps together in the above
    public static List<News> fetchNewsData(String requestUrl) {

        URL url = createUrl(requestUrl);

        String jsonResponse = "";
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "IOException", e);
        }

        ArrayList<News> newsArrayList = extractNewsFromJson(jsonResponse);

        Log.i(LOG_TAG, "fetching data: " + url);

        return newsArrayList;
    }
}
