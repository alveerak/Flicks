package com.codepath.alveera.flicks;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.codepath.alveera.flicks.models.Movie;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import cz.msebera.android.httpclient.Header;

public class MovieTrailerActivity extends YouTubeBaseActivity {
    // instance fields
    Movie movie;
    String videoId;

    // the base URL for the API
    public final static String API_BASE_URL = "https://api.themoviedb.org/3";
    // the parameter name for the API key
    public final static String API_KEY_PARAM = "api_key";
    // tag for logging from this activity
    public final static String TAG = "MovieListActivity";

    AsyncHttpClient client;
    // the list of currently playing movies

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_trailer);

        client = new AsyncHttpClient();

        //unwrap the movie passed in via intent, using its simple name as a key
        movie = (Movie) Parcels.unwrap(getIntent().getParcelableExtra(Movie.class.getSimpleName()));
        Log.d("MovieDetailsActivity", String.format("Showing details for '%s'", movie.getTitle()));

        getTrailer();
    }

    private void getTrailer(){
        //create the url
        String url = API_BASE_URL + "/movie/" + movie.getId() + "/videos";
        // set the request parameters
        RequestParams params = new RequestParams();
        params.put(API_KEY_PARAM, getString(R.string.api_key));
        // execute a GET request expecting a JSON object response
        client.get(url, params, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                boolean got_trailer = false;
                // load the results into movies list
                try {
                    JSONArray results = response.getJSONArray("results");
                    // set default.
                    JSONObject trailer_first = results.getJSONObject(0);
                    videoId = trailer_first.getString("key");
                    // iterate through result set and create Movie obejcts
                    for (int i = 0; i < results.length(); i++) {
                        JSONObject list = results.getJSONObject(i);
                        String type = list.getString("type");
                        if (type.equals("Trailer")) {
                            videoId = list.getString("key");
                            got_trailer = true;
                            break;
                        }
                        Log.i(TAG, String.format("We got the key %s", videoId));
                    }
                    loadTrailer();
                    if (got_trailer == false) {
                        Toast.makeText(getApplicationContext(), "No trailer, go back", Toast.LENGTH_LONG).show();
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "Welcome to the trailer!", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    Log.i(TAG,"Failed to get video");
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.i(TAG,"Failed to get data from videos");
            }
        });
    }
    public void loadTrailer(){
        // resolve the player view from the layout
        YouTubePlayerView playerView = (YouTubePlayerView) findViewById(R.id.player);

        // initialize with API key stored in secrets.xml
        playerView.initialize(getString(R.string.youtube_api_key), new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider,
                                                YouTubePlayer youTubePlayer, boolean b) {
                // do any work here to cue video, play video, etc.
                youTubePlayer.cueVideo(videoId);
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider,
                                                YouTubeInitializationResult youTubeInitializationResult) {
                // log the error
                Log.e("MovieTrailerActivity", "Error initializing YouTube player");
            }
        });
    }
}
