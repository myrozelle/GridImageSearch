package com.androidapp.manunya.gridimagesearch.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Manunya on 5/12/2015.
 */
public class ImageResult implements Serializable{
    public String fullUrl;
    public String thumbUrl;
    public String title;
    public int tbWidth;
    public int tbHeight;
    //private static final long
    // new ImageResult(...)
    public ImageResult(JSONObject json) {
        try {
            this.fullUrl = json.getString("url");
            this.thumbUrl = json.getString("tbUrl");
            this.title = json.getString("title");
            this.tbWidth = Integer.valueOf(json.getString("tbWidth"));
            this.tbHeight = Integer.valueOf(json.getString("tbHeight"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // ImageResult.fromJSONArray([..,..])
    public static ArrayList<ImageResult> fromJSONArray(JSONArray array) {
        ArrayList<ImageResult> results = new ArrayList<ImageResult>();
        for (int i = 0; i < array.length(); i++) {
            try {
                results.add(new ImageResult(array.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return results;
    }

}
