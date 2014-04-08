package com.codepath.twitterclient;

import com.loopj.android.http.AsyncHttpResponseHandler;

public class TimelineFragment extends ContentFragment {

    @Override
    protected void getContent(AsyncHttpResponseHandler handler, String lastId) {
        // TODO Auto-generated method stub
        TwitterClient.getRestClient().getTimeline(handler, lastId, null);
    }

    @Override
    protected int getMenuResource() {
        // TODO Auto-generated method stub
        return R.menu.timeline;
    }
}
