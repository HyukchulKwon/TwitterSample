package com.example.hyukchulkwon.sampletwitter2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import twitter4j.ResponseList;
import twitter4j.Status;

/**
 * Created by hyukchul.kwon on 15/03/25.
 */
public class twitterAdapter extends BaseAdapter {
    private Context mContext;
    private ResponseList<Status> mItems;

    public twitterAdapter(Context context, ResponseList<Status> items){
        mContext = context;
        mItems = items;
    }

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public Object getItem(int position) {
        return mItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater =
                    (LayoutInflater) mContext.getSystemService
                            (Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.tweet_list_layout, parent, false);
        }

        TextView id = (TextView) convertView.findViewById(R.id.userIdText);
        TextView detail = (TextView) convertView.findViewById(R.id.tweetText);
        TextView time = (TextView) convertView.findViewById(R.id.timeText);

        id.setText(String.valueOf(mItems.get(position).getUser().getName()));
        detail.setText(mItems.get(position).getText());
        time.setText(mItems.get(position).getCreatedAt().toString());
        return convertView;
    }
}
