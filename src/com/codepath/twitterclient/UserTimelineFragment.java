package com.codepath.twitterclient;

import android.os.Bundle;

import com.loopj.android.http.AsyncHttpResponseHandler;

public class UserTimelineFragment extends ContentFragment {

    private static final String ARG_USER_ID = "ARG_USER_ID";
    private String mScreenName;

    public static UserTimelineFragment newInstance(String userId) {
        UserTimelineFragment frag = new UserTimelineFragment();
        Bundle b = new Bundle();
        b.putString(ARG_USER_ID, userId);
        frag.setArguments(b);
        return frag;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        mScreenName = getArguments().getString(ARG_USER_ID);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void getContent(AsyncHttpResponseHandler handler, String lastId) {
        // TODO Auto-generated method stub
        TwitterClient.getRestClient().getTimeline(handler, lastId, mScreenName);
    }

    @Override
    protected int getMenuResource() {
        // TODO Auto-generated method stub
        return R.menu.user_timeline;
    }

    @Override
    protected boolean getShouldLaunchProfileActivites() {
        return false;
    }

}
