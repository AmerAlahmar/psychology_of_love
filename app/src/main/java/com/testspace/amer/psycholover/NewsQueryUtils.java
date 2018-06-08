package com.testspace.amer.psycholover;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

public final class NewsQueryUtils {
    private static final String TAG = NewsQueryUtils.class.getName();

    private NewsQueryUtils() {
    }

    public static ArrayList<News> getNews(String requestUrl) {
        if (requestUrl.isEmpty())
            return new ArrayList<>();
        URL url = createURL(requestUrl);
        if (url == null)
            return new ArrayList<>();
        InputStream inputStream = makeHttpConnection(url);
        if (inputStream == null)
            return new ArrayList<>();
        String jsonResponse = readFromStream(inputStream);
        if (jsonResponse == null || jsonResponse.isEmpty())
            return new ArrayList<>();
        return parseJsonResponse(jsonResponse);
    }

    private static URL createURL(String requestUrl) {
        URL url;
        try {
            url = new URL(requestUrl);
        } catch (MalformedURLException e) {
            Log.e(TAG, "createURL: Failed to create URL object", e);
            return null;
        }
        return url;
    }

    private static InputStream makeHttpConnection(URL url) {
        InputStream inputStream;
        HttpURLConnection httpURLConnection = null;
        try {
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setReadTimeout(10000);
            httpURLConnection.setConnectTimeout(700000);
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.connect();
            if (httpURLConnection.getResponseCode() != 200) {
                Log.e(TAG, "makeHttpConnection: Response code not OK, Response code: " + httpURLConnection.getResponseCode());
                httpURLConnection.disconnect();
                return null;
            }
            inputStream = httpURLConnection.getInputStream();
        } catch (IOException e) {
            Log.e(TAG, "makeHttpConnection: Error With httpConnection", e);
            if (httpURLConnection != null)
                httpURLConnection.disconnect();
            return null;
        } finally {
            if (httpURLConnection != null)
                httpURLConnection.disconnect();
        }
        return inputStream;
    }

    private static String readFromStream(InputStream inputStream) {
        StringBuilder stringBuilder = new StringBuilder();
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        try {
            String line = bufferedReader.readLine();
            while (line != null) {
                stringBuilder.append(line);
                line = bufferedReader.readLine();
            }
        } catch (IOException e) {
            Log.e(TAG, "readFromStream: Error While reading line from BufferReader", e);
            try {
                inputStream.close();
            } catch (IOException e1) {
                Log.e(TAG, "readFromStream: Error While closing inputStream object", e1);
            }
            return null;
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                Log.e(TAG, "readFromStream: Error While closing inputStream object", e);
            }
        }
        return stringBuilder.toString();
    }

    private static ArrayList<News> parseJsonResponse(String jsonResponse) {
        ArrayList<News> news = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(jsonResponse);
            JSONArray jsonArray = jsonObject.getJSONObject("response").getJSONArray("results");
            if (jsonArray.length() < 1)
                return news;
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonCurrentObject = jsonArray.getJSONObject(i);
                Bitmap imgBitmap = News.NO_IMGBITMAP;
                String title;
                String desc = News.NO_DESCRIPTION;
                String sectionName = News.NO_SECTION;
                String author = News.NO_AUTHOR;
                String date = News.NO_DATE;
                String url = News.NO_URL;
                if (jsonCurrentObject.has("webTitle"))
                    title = jsonCurrentObject.getString("webTitle");
                else
                    break;
                if (jsonCurrentObject.getJSONObject("fields").has("thumbnail"))
                    imgBitmap = getImgBitmap(jsonCurrentObject.getJSONObject("fields").getString("thumbnail"));
                if (jsonCurrentObject.getJSONObject("fields").has("trailText"))
                    desc = jsonCurrentObject.getJSONObject("fields").getString("trailText");
                if (jsonCurrentObject.has("sectionName"))
                    sectionName = jsonCurrentObject.getString("sectionName");
                if (jsonCurrentObject.getJSONArray("references").length() > 0)
                    author = getDataFromJsonArray(jsonCurrentObject.getJSONArray("references"));
                if (jsonCurrentObject.has("webPublicationDate"))
                    date = jsonCurrentObject.getString("webPublicationDate");
                if (jsonCurrentObject.has("webUrl"))
                    url = jsonCurrentObject.getString("webUrl");
                news.add(new News(imgBitmap, title, desc, sectionName, author, date, url));
            }
        } catch (JSONException e) {
            Log.e(TAG, "parseJsonResponse: Error parsing JSONObject", e);
            return null;
        }
        return news;
    }

    private static String getDataFromJsonArray(JSONArray references) throws JSONException {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < references.length(); i++) {
            try {
                if (references.getJSONObject(i).has("type") && references.getJSONObject(i).get("type").equals("author")) {
                    stringBuilder.append(references.getJSONObject(i).getString("id")).append(News.AUTHOR_SEPARATOR);
                }
            } catch (JSONException e) {
                Log.e(TAG, "getDataFromJsonArray: Error while parsing JSONArray object");
                throw e;
            }
        }
        return stringBuilder.toString();
    }

    private static Bitmap getImgBitmap(String img) {
        Bitmap bitmap = null;
        InputStream inputStream = null;
        try {
            inputStream = new java.net.URL(img).openStream();
            bitmap = BitmapFactory.decodeStream(inputStream);
        } catch (Exception e) {
            Log.e(TAG, "getImgBitmap: Error Downloading Img", e);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    Log.e(TAG, "getImgBitmap: Error While closing inputStream object", e);
                }
            }
        }
        return bitmap;
    }
}