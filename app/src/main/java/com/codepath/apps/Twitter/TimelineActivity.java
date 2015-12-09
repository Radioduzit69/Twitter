package com.codepath.apps.Twitter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.codepath.apps.Twitter.models.Tweet;
import com.codepath.apps.Twitter.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class TimelineActivity extends AppCompatActivity {
    private TwitterClient client;
    private ArrayList<Tweet> tweets;
    private TweetsArrayAdapter aTweets;
    private ListView lvTweets;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
        setCustomActionBar();

        lvTweets = (ListView) findViewById(R.id.lvTweets);
        // Create the arraylist (data source)
        tweets = new ArrayList<>();
        // Construct the adapter from data source
        aTweets = new TweetsArrayAdapter(this, tweets);
        // Connect adapter to list view
        lvTweets.setAdapter(aTweets);
        client = TwitterApplication.getRestClient();// singleton client
        populateTimeline();
    }

    private void setCustomActionBar() {
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("HomeTimeline");
        getSupportActionBar().setLogo(R.drawable.twitter_icon);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_timeline, menu);
        return true;
    }

    // send an API
    private void populateTimeline() {
        client.getHomeTimeline(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                super.onSuccess(statusCode, headers, response);

                // Deserialize json
                // Create models and add them to the adapter
                // Load model data into listview
                aTweets.addAll(Tweet.fromJSONArray(response));
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });

    }

    public void onClickCompose(MenuItem item) {
        Intent intent = new Intent(this, ComposeActivity.class);
        startActivityForResult(intent, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK && requestCode == 0) {
            Tweet composedTweet = (Tweet) data.getSerializableExtra("MyTweet");
            aTweets.add(composedTweet);

        }
    }
}
