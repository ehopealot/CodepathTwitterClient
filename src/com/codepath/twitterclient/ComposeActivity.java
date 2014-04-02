package com.codepath.twitterclient;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.codepath.apps.restclienttemplate.R;
import com.loopj.android.http.JsonHttpResponseHandler;

public class ComposeActivity extends ActionBarActivity {

    public static final String EXTRA_TWEET = "EXTRA_TWEET";

    private static final String SIS_TWEET_IN_PROGRESS = "SIS_TWEET_IN_PROGRESS";
    private View mOverlay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);
        mOverlay = findViewById(R.id.pbOverlay);
        if (savedInstanceState != null) {
            ((EditText) findViewById(R.id.etCompose)).setText(savedInstanceState.getString(SIS_TWEET_IN_PROGRESS));
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // TODO Auto-generated method stub
        String tweet = ((EditText) findViewById(R.id.etCompose)).getText().toString();

        outState.putString(SIS_TWEET_IN_PROGRESS, tweet);
        super.onSaveInstanceState(outState);
    }

    public void onSendTweet(View v) {
        String tweet = ((EditText) findViewById(R.id.etCompose)).getText().toString();
        mOverlay.setVisibility(View.VISIBLE);
        TwitterClient.getRestClient().postTweet(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(JSONObject arg0) {
                mOverlay.setVisibility(View.GONE);
                try {
                    Tweet resp = new Tweet(arg0);
                    Intent intent = new Intent();
                    intent.putExtra(EXTRA_TWEET, resp);
                    setResult(RESULT_OK, intent);
                    finish();
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Throwable arg0) {
                // TODO Auto-generated method stub
                mOverlay.setVisibility(View.GONE);
            }
        }, tweet);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.compose, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

}
