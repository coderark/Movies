package com.example.prit.movies;

import android.net.Uri;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Prit on 18-05-2016.
 */
public class Movie {
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

    public URL POSTER_PATH;
    public boolean ADULT;
    public String OVERVIEW;
    public Date RELEASE_DATE;
    public int[] GENRE_IDS;
    public int ID;
    public String ORIGINAL_TITLE;
    public String ORIGINAL_LANGUAGE;
    public String TITLE;
    public URL BACKDROP_PATH;
    public double POPULARITY;
    public int VOTE_COUNT;
    public boolean VIDEO;
    public double VOTE_AVERAGE;



    public Movie(JSONObject jsonObject) throws JSONException{
        try {
            BuildUrl(jsonObject.getString(POSTER_PATH_TAG));
            this.ADULT=jsonObject.getBoolean(ADULT_TAG);
            this.OVERVIEW=jsonObject.getString(OVERVIEW_TAG);
            //this.GENRE_IDS=jsonObject.
            this.ID=jsonObject.getInt(ID_TAG);
            this.ORIGINAL_TITLE=jsonObject.getString(ORIGINAL_TITLE_TAG);
            this.ORIGINAL_LANGUAGE=jsonObject.getString(ORIGINAL_LANGUAGE_TAG);
            this.TITLE=jsonObject.getString(TITLE_TAG);
            this.POPULARITY=jsonObject.getDouble(POPULARITY_TAG);
            this.VOTE_COUNT=jsonObject.getInt(VOTE_COUNT_TAG);
            this.VIDEO=jsonObject.getBoolean(VIDEO_TAG);
            this.VOTE_AVERAGE=jsonObject.getDouble(VOTE_AVERAGE_TAG);
        }
        catch (IOException e){
            Log.e(LOG_TAG, e.getMessage(), e);
        }
    }

    private void BuildUrl(String string) throws MalformedURLException{
        try {
            this.POSTER_PATH=new URL(Uri.parse(BASE_POSTER_PATH).buildUpon().appendPath(POSTER_SIZE).appendPath(string).build().toString());
            this.BACKDROP_PATH=new URL(Uri.parse(BASE_POSTER_PATH).buildUpon().appendPath(BACKDROP_SIZE).appendPath(string).build().toString());
        }
        catch (MalformedURLException e){
            Log.e(LOG_TAG, e.getMessage(), e);
        }
    }

    private void GetDate(String string) throws ParseException{
        try {
            this.RELEASE_DATE=new SimpleDateFormat("yyyy-MM-dd").parse(string);
        }
        catch (ParseException p){
            Log.e(LOG_TAG, "Unable to parse Date");
        }
    }
}
