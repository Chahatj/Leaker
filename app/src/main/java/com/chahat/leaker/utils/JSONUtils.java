package com.chahat.leaker.utils;

import android.util.Log;

import com.chahat.leaker.object.NewsObject;
import com.chahat.leaker.object.NewsSourceObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chahat on 9/8/17.
 */

public class JSONUtils {

    public static List<NewsSourceObject> getNewsSourceList(String jsonResponse){

        try {

            List<NewsSourceObject> sourceList = new ArrayList<>();
            JSONObject jsonObject = new JSONObject(jsonResponse);
            JSONArray jsonArray = jsonObject.getJSONArray("sources");

            if (jsonArray.length()==0) return null;

            for (int i=0;i<jsonArray.length();i++){
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                NewsSourceObject newsSourceObject = new NewsSourceObject();
                newsSourceObject.setId(jsonObject1.getString("id"));
                newsSourceObject.setName(jsonObject1.getString("name"));
                newsSourceObject.setDescription(jsonObject1.getString("description"));
                newsSourceObject.setUrl(jsonObject1.getString("url"));
                Log.d("JSONUtils",newsSourceObject.getName());
                sourceList.add(newsSourceObject);
            }

            return sourceList;

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static List<NewsObject> getNewsArticle(String jsonResponse){
        try {

            List<NewsObject> sourceList = new ArrayList<>();
            JSONObject jsonObject = new JSONObject(jsonResponse);
            JSONArray jsonArray = jsonObject.getJSONArray("articles");

            for (int i=0;i<jsonArray.length();i++){
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                NewsObject newsObject = new NewsObject();
                newsObject.setAuthor(jsonObject1.getString("author"));
                newsObject.setTitle(jsonObject1.getString("title"));
                newsObject.setDescription(jsonObject1.getString("description"));
                newsObject.setUrl(jsonObject1.getString("url"));
                newsObject.setUrlImage(jsonObject1.getString("urlToImage"));
                newsObject.setPublishedAt(jsonObject1.getString("publishedAt"));
                sourceList.add(newsObject);
            }

            return sourceList;

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
