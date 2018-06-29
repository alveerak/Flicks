package com.codepath.alveera.flicks;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.widget.RatingBar;
import android.widget.TextView;

import com.codepath.alveera.flicks.models.Movie;

import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;

// add some basic code to retrieve and unwrap the Movie from the Intent
public class MovieDetailsActivity extends AppCompatActivity{

    // the movie to display
    Movie movie;

    // the view objects
    @BindView(R.id.tvTitle) TextView tvTitle;
    @BindView(R.id.tvOverview) TextView tvOverview;
    @BindView(R.id.rbVoteAverage) RatingBar rbVoteAverage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        ButterKnife.bind(this);
        //resolve the view objects
        //tvTitle = (TextView) findViewById(R.id.tvTitle);
        //tvOverview = (TextView) findViewById(R.id.tvOverview);
        //rbVoteAverage = (RatingBar) findViewById(R.id.rbVoteAverage);

        tvOverview.setMovementMethod(new ScrollingMovementMethod());

        //unwrap the movie passed in via intent, using its simple name as a key
        movie = (Movie) Parcels.unwrap(getIntent().getParcelableExtra(Movie.class.getSimpleName()));
        Log.d("MovieDetailsActivity", String.format("Showing details for '%s'", movie.getTitle()));

        // set the title and overview
        tvTitle.setText(movie.getTitle());
        tvOverview.setText(movie.getOverview());

        // vote average is 0...10, convert to 0..5 by dividing by 2
        float voteAverage = movie.getVoteAverage().floatValue();
        rbVoteAverage.setRating(voteAverage = voteAverage > 0 ? voteAverage / 2.0f : voteAverage);

        // unwrap the movie passed in via intent, using its simple name as a key
        movie = (Movie) Parcels.unwrap(getIntent().getParcelableExtra(Movie.class.getSimpleName()));
        Log.d("MovieDetailsActivity", String.format("Showing details for '%s'", movie.getTitle()));
    }

    /*public void onAddItem(View v) {
        Intent i = new Intent (this, MovieTrailerActivity.class);
        startActivity(i);
    }*/
}
