package com.codepath.twitterclient;

import com.loopj.android.http.AsyncHttpResponseHandler;

public class CurrentUserProfileFragment extends ProfileFragment {

    @Override
    protected void getUserData(AsyncHttpResponseHandler handler) {
        TwitterClient.getRestClient().getCurrentUser(handler);
    }

}
