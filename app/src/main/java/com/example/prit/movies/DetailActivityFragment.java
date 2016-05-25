package com.example.prit.movies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment {

    private final String LOG_TAG=DetailActivityFragment.class.getSimpleName();
    Movie movie=null;

    public DetailActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView=inflater.inflate(R.layout.fragment_detail, container, false);
        Intent intent=getActivity().getIntent();
        try {
            movie=new Movie(intent.getBundleExtra("MyMovieData"));
            Log.v(LOG_TAG, movie.TITLE);
        }
        catch (ParseException e){
            Log.e(LOG_TAG, e.getMessage());
        }
        TextView movieTitle=(TextView)rootView.findViewById(R.id.movie_title);
        TextView movieReleaseDate=(TextView)rootView.findViewById(R.id.movie_release_date);
        TextView movieAdult=(TextView)rootView.findViewById(R.id.movie_adult);
        TextView movieRating=(TextView)rootView.findViewById(R.id.movie_rating);
        TextView movieOverview=(TextView)rootView.findViewById(R.id.movie_overview);
        ImageView moviePoster=(ImageView)rootView.findViewById(R.id.movie_poster);
        RatingBar ratingBar=(RatingBar)rootView.findViewById(R.id.movie_rating_bar);

        movieTitle.setText(movie.TITLE);
        movieReleaseDate.setText("Released:\n"+(new SimpleDateFormat("d MMMM, y")).format(movie.RELEASE_DATE));
        if (movie.ADULT){
            movieAdult.setText("A");
        }
        else {
            movieAdult.setText("U/A");
        }
        movieRating.setText("Rating: "+movie.VOTE_AVERAGE+"/10");
        ratingBar.setRating((float) (movie.VOTE_AVERAGE/2));
        movieOverview.setText(movie.OVERVIEW);
        Picasso.with(getContext()).load(movie.POSTER_PATH).placeholder(R.drawable.loading).noFade().resize(764, 1024).into(moviePoster);

        return rootView;
    }
}
