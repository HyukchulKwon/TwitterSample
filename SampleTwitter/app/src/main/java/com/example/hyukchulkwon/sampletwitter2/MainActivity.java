package com.example.hyukchulkwon.sampletwitter2;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;


public class MainActivity extends ActionBarActivity {
    private ListView listView;
    private Button mTweetButton;
    private twitterAdapter mAdapter;
    private Twitter mTwitter;
    private EditText mTweetEditText;

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
            mTweetButton = (Button)findViewById(R.id.tweetButton);

            mTweetButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mTweetEditText = (EditText) findViewById(R.id.tweetEditText);
                    tweet(mTweetEditText.getText().toString());

                }
            });

            mTwitter = TwitterUtils.getTwitterInstance(this);
            reloadTimeLine();


        }
    }

    private void reloadTimeLine() {
        AsyncTask<Void, Void, ResponseList<twitter4j.Status>> task =
                new AsyncTask<Void, Void, ResponseList<twitter4j.Status>>() {
            @Override
            protected ResponseList<twitter4j.Status> doInBackground(Void... params) {
                try {
                    ResponseList<twitter4j.Status> timeline =
                            mTwitter.getHomeTimeline();
                    return timeline;
                } catch (TwitterException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(ResponseList<twitter4j.Status> result) {
                if (result != null) {
                    mAdapter = new twitterAdapter(getApplicationContext(), result);
                    listView.setAdapter(mAdapter);
                } else {
                    showToast("タイムラインの取得に失敗しました。。。");
                }
            }
        };
        task.execute();
    }

    private void tweet(String tweetWord) {
        AsyncTask<String, Void, Boolean> task =
                new AsyncTask<String, Void, Boolean>() {
                    @Override
                    protected Boolean doInBackground(String... params) {
                        try {
                            mTwitter.updateStatus(params[0]);
                            return true;

                        } catch (TwitterException e) {
                            return false;
                        }
                    }

                    @Override
                    protected void onPostExecute(Boolean result) {
                        if(result){
                            mTweetEditText.setText("");
                            showToast("ツイートしました。");
                        }else{
                            showToast("ツイートに失敗しました。");
                        }
                    }
                };

        task.execute(tweetWord);
    }


    private void showToast(String text) {

        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
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
}
