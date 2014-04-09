package com.codepath.twitterclient;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.Tab;
import android.support.v7.app.ActionBar.TabListener;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

public class HomeActivity extends ActionBarActivity {

    private static final String SELECTED_TAB = "SELECTED_TAB";

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // TODO Auto-generated method stub

        outState.putInt(SELECTED_TAB, (Integer) getSupportActionBar().getSelectedNavigationIndex());
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        actionBar.setDisplayShowTitleEnabled(true);
        Tab tab = actionBar
                .newTab()
                .setText("Timeline")
                .setTag("TimelineFragment")
                .setTabListener(
                        new FragmentTabListener<TimelineFragment>(R.id.container, this, "timeline",
                                TimelineFragment.class));
        actionBar.addTab(tab);

        Tab tab2 = actionBar
                .newTab()
                .setText("Mentions")
                .setTag("MentionsFragment")
                .setTabListener(
                        new FragmentTabListener<MentionsFragment>(R.id.container, this, "mentions",
                                MentionsFragment.class));
        actionBar.addTab(tab2);

        Tab tab3 = actionBar
                .newTab()
                .setText("Profile")
                .setTag("ProfileFragment")
                .setTabListener(
                        new FragmentTabListener<CurrentUserProfileFragment>(R.id.container, this, "profile",
                                CurrentUserProfileFragment.class));
        actionBar.addTab(tab3);

        if (savedInstanceState != null) {
            int tabIndex = savedInstanceState.getInt(SELECTED_TAB);
            if (tabIndex == 2) {
                actionBar.selectTab(tab3);
            } else if (tabIndex == 1) {
                actionBar.selectTab(tab2);
            } else {
                actionBar.selectTab(tab);
            }
        } else {
            actionBar.selectTab(tab);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
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

    private class FragmentTabListener<T extends Fragment> implements TabListener {
        private Fragment mFragment;
        private final FragmentActivity mActivity;
        private final String mTag;
        private final Class<T> mClass;
        private final int mfragmentContainerId;

        // This version defaults to replacing the entire activity content area
        // new FragmentTabListener<SomeFragment>(this, "first",
        // SomeFragment.class))
        public FragmentTabListener(FragmentActivity activity, String tag, Class<T> clz) {
            mActivity = activity;
            mTag = tag;
            mClass = clz;
            mfragmentContainerId = android.R.id.content;
        }

        // This version supports specifying the container to replace with
        // fragment content
        // new FragmentTabListener<SomeFragment>(R.id.flContent, this, "first",
        // SomeFragment.class))
        public FragmentTabListener(int fragmentContainerId, FragmentActivity activity, String tag, Class<T> clz) {
            mActivity = activity;
            mTag = tag;
            mClass = clz;
            mfragmentContainerId = fragmentContainerId;
        }

        /* The following are each of the ActionBar.TabListener callbacks */

        protected Fragment instantiateFragment() {
            return Fragment.instantiate(mActivity, mClass.getName());
        }

        @Override
        public void onTabSelected(Tab tab, FragmentTransaction ft) {
            FragmentTransaction sft = mActivity.getSupportFragmentManager().beginTransaction();
            // Check if the fragment is already initialized
            if (mFragment == null) {
                // If not, instantiate and add it to the activity
                mFragment = instantiateFragment();
                sft.replace(mfragmentContainerId, mFragment, mTag);
            } else {
                // If it exists, simply attach it in order to show it
                sft.attach(mFragment);
            }
            sft.commit();
        }

        @Override
        public void onTabUnselected(Tab tab, FragmentTransaction ft) {
            FragmentTransaction sft = mActivity.getSupportFragmentManager().beginTransaction();
            if (mFragment != null) {
                // Detach the fragment, because another one is being attached
                sft.detach(mFragment);
            }
            sft.commit();
        }

        @Override
        public void onTabReselected(Tab tab, FragmentTransaction ft) {
            // User selected the already selected tab. Usually do nothing.
        }
    }
}
