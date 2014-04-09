package com.codepath.twitterclient;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

public class ProfileActivity extends ActionBarActivity {

    public static final String EXTRA_USER = "EXTRA_USER";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        User u = (User) getIntent().getSerializableExtra(EXTRA_USER);
        getSupportFragmentManager().beginTransaction().replace(R.id.container, ProfileFragment.newInstance(u)).commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

}
