package com.codepath.apps.restclienttemplate;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AbsListView;

import com.loopj.android.http.JsonHttpResponseHandler;

import eu.erikw.PullToRefreshListView;

public class TimelineActivity extends ActionBarActivity {

    private static final int COMPOSE_REQ_CODE = 432;

    private ArrayList<Tweet> mTweets = new ArrayList<Tweet>();
    private TweetAdapter mAdapter;
    private String mLastId;
    private PullToRefreshListView mLv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        mAdapter = new TweetAdapter(this, mTweets);
        mLv = (PullToRefreshListView) findViewById(R.id.lvTweets);
        mLv.setAdapter(mAdapter);
        mLv.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                // TODO Auto-generated method stub
                if (mLastId != null) {
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
        getTimeline();
    }

    public void getTimeline() {
        if (mLastId == null) {
            mTweets.clear();
        }
        RestClientApp.getRestClient().getTimeline(new JsonHttpResponseHandler() {
            @Override
            public void onFailure(Throwable arg0) {
                // TODO Auto-generated method stub
                mLv.onRefreshComplete();
                System.out.println("failure: " + arg0.toString());
            }

            @Override
            public void onSuccess(JSONArray response) {
                try {
                    if (mLastId == null) {
                        mLv.onRefreshComplete();
                    }

                    for (int i = 0; i < response.length(); i++) {
                        JSONObject jsonTweet = response.getJSONObject(i);
                        Tweet newTweet = new Tweet(jsonTweet);
                        mTweets.add(newTweet);
                        if (i == response.length() - 1) {
                            mLastId = jsonTweet.getString("id_str");
                        }
                    }
                    mAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    mLv.onRefreshComplete();
                }
            }
        }, mLastId);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.timeline, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_compose) {
            Intent intent = new Intent(this, ComposeActivity.class);
            startActivityForResult(intent, COMPOSE_REQ_CODE);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int rc, int result, Intent data) {
        // TODO Auto-generated method stub
        if (rc == COMPOSE_REQ_CODE && result == RESULT_OK && data != null) {
            Tweet t = (Tweet) data.getSerializableExtra(ComposeActivity.EXTRA_TWEET);
            mTweets.add(0, t);
            mAdapter.notifyDataSetChanged();
        }
    }
}
