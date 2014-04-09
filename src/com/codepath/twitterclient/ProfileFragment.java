package com.codepath.twitterclient;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;

public class ProfileFragment extends Fragment {
    private static final String ARG_USER = "ARG_USER";
    private User mUser;
    private TextView mTagline;
    private TextView mNumFollowing;
    private TextView mNumFollowers;
    private AsyncImageView mProfilePic;

    public ProfileFragment() {

    }

    public static ProfileFragment newInstance(User user) {
        ProfileFragment frag = new ProfileFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_USER, user);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profile, container, false);
        if (getArguments() != null) {
            mUser = (User) getArguments().getSerializable(ARG_USER);
            if (mUser != null) {
                mProfilePic.setUrl(mUser.imageUrl);
            }
        }
        mProfilePic = (AsyncImageView) v.findViewById(R.id.ivPostedBy);

        mNumFollowers = (TextView) v.findViewById(R.id.tvNumFollowers);
        mNumFollowing = (TextView) v.findViewById(R.id.tvNumTweets);
        mTagline = (TextView) v.findViewById(R.id.tvTagline);
        return v;
    }

    @Override
    public void onActivityCreated(Bundle arg0) {
        super.onActivityCreated(arg0);
        final Context c = getActivity();
        getUserData(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int arg0, JSONObject arg1) {
                try {
                    mUser = new User(arg1);
                    mNumFollowers.setText("Followers: " + mUser.numFollowers);
                    mNumFollowing.setText("Following: " + mUser.numFollowing);
                    mTagline.setText(mUser.tagline);
                    mProfilePic.setUrl(mUser.imageUrl);
                    getChildFragmentManager().beginTransaction()
                            .replace(R.id.tweets, UserTimelineFragment.newInstance(mUser.screenName)).commit();
                } catch (JSONException e) {
                    Toast.makeText(c, e.toString(), Toast.LENGTH_LONG).show();

                }
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

        });
    }

    protected void getUserData(AsyncHttpResponseHandler handler) {
        TwitterClient.getRestClient().getUser(handler, mUser.screenName);
    }
}
