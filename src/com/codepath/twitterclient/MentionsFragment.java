package com.codepath.twitterclient;

import com.loopj.android.http.AsyncHttpResponseHandler;

public class MentionsFragment extends ContentFragment {

    @Override
    protected void getContent(AsyncHttpResponseHandler handler, String lastId) {
        // TODO Auto-generated method stub
        TwitterClient.getRestClient().getMentions(handler, lastId);
    }

    @Override
    protected int getMenuResource() {
        // TODO Auto-generated method stub
        return R.menu.timeline;
    }

}
