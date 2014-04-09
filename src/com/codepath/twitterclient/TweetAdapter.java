package com.codepath.twitterclient;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class TweetAdapter extends ArrayAdapter<Tweet> {

    final String TWITTER = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
    final SimpleDateFormat sf = new SimpleDateFormat(TWITTER);

    final boolean mLaunchProfileActivities;

    public TweetAdapter(Context context, List<Tweet> objects, boolean launchProfileActivites) {
        super(context, R.layout.layout_tweet, objects);
        sf.setLenient(true);
        mLaunchProfileActivities = launchProfileActivites;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        int count = super.getCount();
        if (count > 0) {
            count += 1;
        }
        return count;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        boolean isLastRow = position == super.getCount();
        if (convertView == null || (!isLastRow && convertView.getClass() != RelativeLayout.class)) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.layout_tweet, parent, false);
        } else if (convertView == null || (isLastRow && convertView.getClass() != LinearLayout.class)) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.bottom_tweet, parent, false);
        }
        if (isLastRow) {
            return convertView;
        }
        final Tweet tweet = getItem(position);

        final AsyncImageView ivPostedBy = (AsyncImageView) convertView.findViewById(R.id.ivPostedBy);
        TextView tvPostedBy = (TextView) convertView.findViewById(R.id.tvPostedBy);
        tvPostedBy.setText(tweet.postedBy);
        TextView tvPostedAt = (TextView) convertView.findViewById(R.id.tvTime);
        try {
            Date timePosted = sf.parse(tweet.createdAt);
            Date now = new Date();
            long diff = now.getTime() - timePosted.getTime();
            long diffDays = diff / (24 * 60 * 60 * 1000);
            long diffHours = diff / (60 * 60 * 1000) % 24;
            long diffMinutes = diff / (60 * 1000) % 60;
            long diffSeconds = diff / 1000 % 60;

            if (diffDays > 0) {
                tvPostedAt.setText(diffDays + " " + (diffDays == 1 ? "day" : "days") + " ago");
            } else if (diffHours > 0) {
                tvPostedAt.setText(diffHours + " " + (diffHours == 1 ? "hour" : "hours") + " ago");
            } else if (diffMinutes > 0) {
                tvPostedAt.setText(diffMinutes + " " + (diffMinutes == 1 ? "minute" : "minutes") + " ago");
            } else {
                tvPostedAt.setText(diffSeconds + " " + (diffSeconds == 1 ? "second" : "seconds") + " ago");
            }

        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        TextView text = (TextView) convertView.findViewById(R.id.tvTweet);
        text.setText(tweet.text);
        ivPostedBy.setUrl(tweet.postedByPicture);
        if (mLaunchProfileActivities) {
            ivPostedBy.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    User user = new User();
                    user.screenName = tweet.postedBy;
                    user.imageUrl = tweet.postedByPicture;
                    Intent i = new Intent(getContext(), ProfileActivity.class);
                    i.putExtra(ProfileActivity.EXTRA_USER, user);
                    getContext().startActivity(i);
                }
            });
        }
        return convertView;
    }
}
