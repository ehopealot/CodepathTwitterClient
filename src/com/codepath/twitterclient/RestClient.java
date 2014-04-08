package com.codepath.twitterclient;

import org.scribe.builder.api.Api;
import org.scribe.builder.api.TwitterApi;

import android.content.Context;

import com.codepath.oauth.OAuthBaseClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/*
 * 
 * This is the object responsible for communicating with a REST API. 
 * Specify the constants below to change the API being communicated with.
 * See a full list of supported API classes: 
 *   https://github.com/fernandezpablo85/scribe-java/tree/master/src/main/java/org/scribe/builder/api
 * Key and Secret are provided by the developer site for the given API i.e dev.twitter.com
 * Add methods for each relevant endpoint in the API.
 * 
 * NOTE: You may want to rename this object based on the service i.e TwitterClient or FlickrClient
 * 
 */
public class RestClient extends OAuthBaseClient {
    public static final Class<? extends Api> REST_API_CLASS = TwitterApi.class; // Change
                                                                                // this
    public static final String REST_URL = "https://api.twitter.com/1.1";
    public static final String REST_CONSUMER_KEY = "ctpL1J3FKaWwGSXU9KYQ";
    public static final String REST_CONSUMER_SECRET = "ut0A0B1mOQFeEtPqFWAuYWQUFFikNP53riNjv9P8L0";
    public static final String REST_CALLBACK_URL = "oauth://twclient";

    public RestClient(Context context) {
        super(context, REST_API_CLASS, REST_URL, REST_CONSUMER_KEY, REST_CONSUMER_SECRET, REST_CALLBACK_URL);
    }

    // CHANGE THIS
    // DEFINE METHODS for different API endpoints here
    public void getUser(AsyncHttpResponseHandler handler, String screenName) {
        RequestParams params = new RequestParams();
        params.put("screen_name", screenName);
        String apiUrl = getApiUrl("users/show.json");
        client.get(apiUrl, params, handler);
    }

    public void getCurrentUser(AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("account/verify_credentials.json");
        client.get(apiUrl, new RequestParams(), handler);
    }

    public void getTimeline(AsyncHttpResponseHandler handler, String maxId, String screenName) {
        getContent(handler, getApiUrl("statuses/home_timeline.json"), maxId, screenName);
    }

    public void getMentions(AsyncHttpResponseHandler handler, String maxId, String screenName) {
        getContent(handler, getApiUrl("statuses/mentions_timeline.json"), maxId, screenName);
    }

    public void getContent(AsyncHttpResponseHandler handler, String apiUrl, String maxId, String screenName) {
        // Can specify query string params directly or through RequestParams.
        RequestParams params = new RequestParams();
        if (maxId != null) {
            params.put("max_id", maxId);
        }
        if (screenName != null) {
            params.put("screen_name", screenName);
        }
        params.put("format", "json");
        client.get(apiUrl, params, handler);
    }

    public void postTweet(AsyncHttpResponseHandler handler, String tweet) {
        String apiUrl = getApiUrl("statuses/update.json");
        RequestParams params = new RequestParams();
        params.put("status", tweet);
        client.post(apiUrl, params, handler);
    }

    /*
     * 1. Define the endpoint URL with getApiUrl and pass a relative path to the
     * endpoint i.e getApiUrl("statuses/home_timeline.json"); 2. Define the
     * parameters to pass to the request (query or body) i.e RequestParams
     * params = new RequestParams("foo", "bar"); 3. Define the request method
     * and make a call to the client i.e client.get(apiUrl, params, handler);
     * i.e client.post(apiUrl, params, handler);
     */
}