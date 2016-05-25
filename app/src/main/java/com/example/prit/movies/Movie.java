package com.example.prit.movies;

import android.os.Bundle;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Prit on 18-05-2016.
 */
public class Movie {

    public final String EXTRA_MOVIE="MyMovieData";
    private final String LOG_TAG=Movie.class.getSimpleName();
    private final String BASE_POSTER_PATH="http://image.tmdb.org/t/p/";
    private final String POSTER_SIZE="w185";
    private final String BACKDROP_SIZE="w500";

    private final String POSTER_PATH_TAG="poster_path";
    private final String ADULT_TAG="adult";
    private final String OVERVIEW_TAG="overview";
    private final String RELEASE_DATE_TAG="release_date";
    private final String GENRE_IDS_TAG="genre_ids";
    private final String ID_TAG="id";
    private final String ORIGINAL_TITLE_TAG="original_title";
    private final String ORIGINAL_LANGUAGE_TAG="original_language";
    private final String TITLE_TAG="title";
    private final String BACKDROP_PATH_TAG="backdrop_path";
    private final String POPULARITY_TAG="popularity";
    private final String VOTE_COUNT_TAG="vote_count";
    private final String VIDEO_TAG="video";
    private final String VOTE_AVERAGE_TAG="vote_average";

    public String POSTER_PATH;
    public boolean ADULT;
    public String OVERVIEW;
    public Date RELEASE_DATE;
    public int[] GENRE_IDS;
    public int ID;
    public String ORIGINAL_TITLE;
    public String ORIGINAL_LANGUAGE;
    public String TITLE;
    public String BACKDROP_PATH;
    public double POPULARITY;
    public int VOTE_COUNT;
    public boolean VIDEO;
    public double VOTE_AVERAGE;



    public Movie(JSONObject jsonObject) throws JSONException{
        try {
            this.POSTER_PATH=BASE_POSTER_PATH+POSTER_SIZE+jsonObject.getString(POSTER_PATH_TAG);
            this.ADULT=jsonObject.getBoolean(ADULT_TAG);
            this.OVERVIEW=jsonObject.getString(OVERVIEW_TAG);
            //this.GENRE_IDS=jsonObject.
            this.ID=jsonObject.getInt(ID_TAG);
            this.ORIGINAL_TITLE=jsonObject.getString(ORIGINAL_TITLE_TAG);
            this.ORIGINAL_LANGUAGE=jsonObject.getString(ORIGINAL_LANGUAGE_TAG);
            this.TITLE=jsonObject.getString(TITLE_TAG);
            this.BACKDROP_PATH=BASE_POSTER_PATH+BACKDROP_SIZE+jsonObject.getString(BACKDROP_PATH_TAG);
            this.POPULARITY=jsonObject.getDouble(POPULARITY_TAG);
            this.VOTE_COUNT=jsonObject.getInt(VOTE_COUNT_TAG);
            this.VIDEO=jsonObject.getBoolean(VIDEO_TAG);
            this.VOTE_AVERAGE=jsonObject.getDouble(VOTE_AVERAGE_TAG);
            this.RELEASE_DATE=GetDate(jsonObject.getString(RELEASE_DATE_TAG));
        }
        catch (ParseException e){
            Log.e(LOG_TAG, e.getMessage(), e);
        }
    }

    public Movie(Bundle bundle) throws ParseException{
        POSTER_PATH=bundle.getString(POSTER_PATH_TAG);
        ADULT=bundle.getBoolean(ADULT_TAG);
        OVERVIEW=bundle.getString(OVERVIEW_TAG);
        RELEASE_DATE=GetDate(bundle.getString(RELEASE_DATE_TAG));
        ID=bundle.getInt(ID_TAG);
        TITLE=bundle.getString(TITLE_TAG);
        ORIGINAL_LANGUAGE=bundle.getString(ORIGINAL_LANGUAGE_TAG);
        POPULARITY=bundle.getDouble(POPULARITY_TAG);
        VOTE_AVERAGE=bundle.getDouble(VOTE_AVERAGE_TAG);
        VOTE_COUNT=bundle.getInt(VOTE_COUNT_TAG);
        VIDEO=bundle.getBoolean(VIDEO_TAG);
    }


    private Date GetDate(String string) throws ParseException{
        try {
            return new SimpleDateFormat("yyyy-MM-dd").parse(string);
        }
        catch (ParseException p){
            Log.e(LOG_TAG, "Unable to parse Date");
            return null;
        }
    }

    public Bundle toBundle(){
        Bundle bundle=new Bundle();
        bundle.putString(POSTER_PATH_TAG, POSTER_PATH);
        bundle.putBoolean(ADULT_TAG, ADULT);
        bundle.putString(OVERVIEW_TAG, OVERVIEW);
        bundle.putString(RELEASE_DATE_TAG, (new SimpleDateFormat("yyyy-MM-dd")).format(RELEASE_DATE));
        bundle.putInt(ID_TAG, ID);
        bundle.putString(TITLE_TAG, TITLE);
        bundle.putString(ORIGINAL_LANGUAGE_TAG, ORIGINAL_LANGUAGE);
        bundle.putDouble(POPULARITY_TAG, POPULARITY);
        bundle.putDouble(VOTE_AVERAGE_TAG, VOTE_AVERAGE);
        bundle.putInt(VOTE_COUNT_TAG, VOTE_COUNT);
        bundle.putBoolean(VIDEO_TAG, VIDEO);

        return bundle;
    }
}
