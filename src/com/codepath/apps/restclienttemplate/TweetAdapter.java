package com.codepath.apps.restclienttemplate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class TweetAdapter extends ArrayAdapter<Tweet> {

    final String TWITTER = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
    final SimpleDateFormat sf = new SimpleDateFormat(TWITTER);

    public TweetAdapter(Context context, List<Tweet> objects) {
        super(context, R.layout.layout_tweet, objects);
        sf.setLenient(true);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Tweet tweet = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.layout_tweet, parent, false);
        }

        final AsyncImageView ivPostedBy = (AsyncImageView) convertView.findViewById(R.id.ivPostedBy);
        TextView tvPostedBy = (TextView) convertView.findViewById(R.id.tvPostedBy);
        tvPostedBy.setText(tweet.postedBy);
        TextView tvPostedAt = (TextView) convertView.findViewById(R.id.tvTime);
        try {
            Date timePosted = sf.parse(tweet.createdAt);
            Date now = new Date();
            long diff = now.getTime() - timePosted.getTime();
            long diffDays = diff / (24 * 60 * 60 * 1000);
            long diffSeconds = diff / 1000 % 60;
            long diffMinutes = diff / (60 * 1000) % 60;
            long diffHours = diff / (60 * 60 * 1000) % 24;
            if (diffDays > 0) {
                tvPostedAt.setText("%d " + (diffDays == 1 ? "day" : "days") + " ago");
            } else if (diffHours > 0) {
                tvPostedAt.setText("%d " + (diffDays == 1 ? "hour" : "hours") + " ago");
            } else if (diffMinutes > 0) {
                tvPostedAt.setText("%d " + (diffDays == 1 ? "minute" : "minutes") + " ago");
            } else {
                tvPostedAt.setText("%d " + (diffDays == 1 ? "second" : "seconds") + " ago");
            }

            tvPostedAt.setText(sf.parse(tweet.createdAt).toString());
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        TextView text = (TextView) convertView.findViewById(R.id.tvTweet);
        text.setText(tweet.text);
        ivPostedBy.setUrl(tweet.postedByPicture);

        return convertView;
    }
}