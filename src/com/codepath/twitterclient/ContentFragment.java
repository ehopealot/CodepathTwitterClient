package com.codepath.twitterclient;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;

import eu.erikw.PullToRefreshListView;

public abstract class ContentFragment extends Fragment {

    private static final int COMPOSE_REQ_CODE = 432;

    private static final String SIS_LAST_ID = "SIS_LAST_ID";
    private static final String SIS_TWEETS = "SIS_TWEETS";

    private ArrayList<Tweet> mTweets = new ArrayList<Tweet>();
    private TweetAdapter mAdapter;
    private String mLastId;
    private String mLoadingId;
    private PullToRefreshListView mLv;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_timeline, container, false);
        mLv = (PullToRefreshListView) v.findViewById(R.id.lvTweets);
        mLv.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                // TODO Auto-generated method stub
                if (mLastId != null && mLastId != mLoadingId) {
                    int scrolledThroughItems = firstVisibleItem + visibleItemCount;
                    if (scrolledThroughItems == mTweets.size()) {
                        getTimeline();
                    }
                }
            }
        });
        mLv.setOnRefreshListener(new PullToRefreshListView.OnRefreshListener() {

            @Override
            public void onRefresh() {
                mLastId = null;
                getTimeline();
            }
        });
        mLv.setRefreshing();
        return v;
    }

    protected boolean getShouldLaunchProfileActivites() {
        return true;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);
        mAdapter = new TweetAdapter(getActivity(), mTweets, getShouldLaunchProfileActivites());
        mLv.setAdapter(mAdapter);
        getTimeline();

    }

    protected abstract void getContent(AsyncHttpResponseHandler handler, String lastId);

    public void getTimeline() {
        if (mLastId == null) {
            mTweets.clear();
        }
        mLoadingId = mLastId;
        final Context c = getActivity();
        getContent(new JsonHttpResponseHandler() {
            @Override
            public void onFailure(Throwable arg0) {
                mLv.onRefreshComplete();
                mLoadingId = null;
                System.out.println("failure: " + arg0.toString());
            }

            @Override
            public void onFinish() {
                mLoadingId = null;
                mLv.onRefreshComplete();
            }

            @Override
            protected void handleFailureMessage(Throwable arg0, String message) {
                try {
                    JSONObject obj = new JSONObject(message);
                    Toast.makeText(c, obj.getJSONArray("errors").getJSONObject(0).getString("message"),
                            Toast.LENGTH_LONG).show();

                } catch (JSONException e) {
                    Toast.makeText(c, "Error making request", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onSuccess(JSONArray response) {
                try {
                    if (mLastId == null) {
                        mLv.onRefreshComplete();
                        // clear any self-tweets that were showing
                        mTweets.clear();
                    }

                    for (int i = 0; i < response.length(); i++) {
                        JSONObject jsonTweet = response.getJSONObject(i);
                        Tweet newTweet = new Tweet(jsonTweet);
                        mTweets.add(newTweet);
                        if (i == response.length() - 1) {
                            mLastId = Long.valueOf(Long.parseLong(jsonTweet.getString("id_str")) - 1).toString();
                        }
                    }
                    mAdapter.notifyDataSetChanged();
                    mLoadingId = null;
                } catch (JSONException e) {
                    mLv.onRefreshComplete();
                    mLoadingId = null;
                }
            }
        }, mLastId);
    }

    protected abstract int getMenuResource();

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // TODO Auto-generated method stub
        inflater.inflate(getMenuResource(), menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_compose) {
            Intent intent = new Intent(getActivity(), ComposeActivity.class);
            startActivityForResult(intent, COMPOSE_REQ_CODE);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    protected boolean getShouldInsertNewTweet() {
        return false;
    }

    @Override
    public void onActivityResult(int rc, int result, Intent data) {
        // TODO Auto-generated method stub
        if (rc == COMPOSE_REQ_CODE && result == Activity.RESULT_OK && data != null && getShouldInsertNewTweet()) {
            Tweet t = (Tweet) data.getSerializableExtra(ComposeActivity.EXTRA_TWEET);
            mTweets.add(0, t);
            mAdapter.notifyDataSetChanged();
        }
    }
}
