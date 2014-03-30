package com.codepath.apps.restclienttemplate;

import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;

public class Tweet implements Serializable {
    private static final long serialVersionUID = -951592298855268587L;
    public String text;
    public String postedBy;
    public String postedByPicture;
    public String createdAt;

    public Tweet(JSONObject jsonTweet) throws JSONException {
        createdAt = jsonTweet.getString("created_at");
        postedBy = jsonTweet.getJSONObject("user").getString("screen_name");
        postedByPicture = jsonTweet.getJSONObject("user").getString("profile_image_url_https");
        text = jsonTweet.getString("text");
    }
}
