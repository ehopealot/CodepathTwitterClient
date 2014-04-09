package com.codepath.twitterclient;

import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;

public class User implements Serializable {
    public String screenName;
    public String numFollowing;
    public String numFollowers;
    public String imageUrl;
    public String tagline;

    public User(JSONObject userJSON) throws JSONException {
        numFollowing = Long.toString(userJSON.getLong("friends_count"));
        numFollowers = Long.toString(userJSON.getLong("followers_count"));
        tagline = userJSON.getString("description");
        imageUrl = userJSON.getString("profile_image_url");
        screenName = userJSON.getString("screen_name");
    }
}
