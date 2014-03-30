package com.codepath.apps.restclienttemplate;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.loopj.android.http.JsonHttpResponseHandler;

public class TimelineActivity extends ActionBarActivity {

    private ArrayList<Tweet> mTweets = new ArrayList<Tweet>();
    private TweetAdapter mAdapter;
    private String mLastId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        mAdapter = new TweetAdapter(this, mTweets);
        ListView lv = (ListView) findViewById(R.id.lvTweets);
        lv.setAdapter(mAdapter);
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
                System.out.println("failure" + arg0);
            }

            @Override
            public void onSuccess(JSONArray response) {
                try {
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject jsonTweet = response.getJSONObject(i);
                        Tweet newTweet = new Tweet();
                        newTweet.createdAt = jsonTweet.getString("created_at");
                        newTweet.postedBy = jsonTweet.getJSONObject("user").getString("screen_name");
                        newTweet.postedByPicture = jsonTweet.getJSONObject("user").getString("profile_image_url_https");
                        newTweet.text = jsonTweet.getString("text");
                        mTweets.add(newTweet);
                        if (i == response.length() - 1) {
                            mLastId = jsonTweet.getString("id_str");
                        }
                    }
                    mAdapter.notifyDataSetChanged();
                } catch (JSONException e) {

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
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
