package com.example.hyukchulkwon.sampletwitter;

import android.app.ListActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.hyukchulkwon.sampletwitter.Util.TwitterUtils;

import java.util.ArrayList;
import java.util.List;

import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;


public class MainActivity extends ActionBarActivity {
    private ListView listView;
    private ArrayAdapter<String> mAdapter;
    private Twitter mTwitter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (!TwitterUtils.hasAccessToken(this)) {
            Intent intent = new Intent(this, TwitterOAuthActivity.class);
            startActivity(intent);
            finish();
        }else{
            listView = (ListView)findViewById(R.id.twitterList);

            mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
            listView.setAdapter(mAdapter);

            mTwitter = TwitterUtils.getTwitterInstance(this);
            reloadTimeLine();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void reloadTimeLine() {
        AsyncTask<Void, Void, List<String>> task = new AsyncTask<Void, Void, List<String>>() {
            @Override
            protected List<String> doInBackground(Void... params) {
                try {
                    ResponseList<twitter4j.Status> timeline = mTwitter.getHomeTimeline();
                    ArrayList<String> list = new ArrayList<String>();
                    for (twitter4j.Status status : timeline) {
                        list.add(status.getText());
                    }
                    return list;
                } catch (TwitterException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(List<String> result) {
                if (result != null) {
                    mAdapter.clear();
                    for (String status : result) {
                        mAdapter.add(status);
                    }
                    listView.setSelection(0);
                } else {
                    showToast("タイムラインの取得に失敗しました。。。");
                }
            }
        };
        task.execute();
    }

    private void showToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }
}
