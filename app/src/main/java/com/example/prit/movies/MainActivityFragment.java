package com.example.prit.movies;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    private String[] tempImg={"http://image.tmdb.org/t/p/w185/rDT86hJCxnoOs4ARjrCiRej7pOi.jpg", "http://image.tmdb.org/t/p/w185/k1QUCjNAkfRpWfm1dVJGUmVHzGv.jpg", "http://image.tmdb.org/t/p/w185/zSouWWrySXshPCT4t3UKCQGayyo.jpg", "http://image.tmdb.org/t/p/w185/cGOPbv9wA5gEejkUN892JrveARt.jpg", "http://image.tmdb.org/t/p/w185/dlIPGXPxXQTp9kFrRzn0RsfUelx.jpg", "http://image.tmdb.org/t/p/w185/5TQ6YDmymBpnF005OyoB7ohZps9.jpg", "http://image.tmdb.org/t/p/w185/kqjL17yufvn9OVLyXYpvtyrFfak.jpg"};

    private Integer[] tempD={R.drawable.i1, R.drawable.i2, R.drawable.i3, R.drawable.i4, R.drawable.i5,R.drawable.i6, R.drawable.i7};

    public String[] images=null;

    private ImageAdapter imageAdapter=null;

    public MainActivityFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView=inflater.inflate(R.layout.fragment_main, container, false);
        FetchMovieData movieData=new FetchMovieData();
        movieData.execute("popular");
        GridView gridView=(GridView)rootView.findViewById(R.id.gridview);
        gridView.setAdapter(new ImageAdapter(getContext()));
        return rootView;
    }

    public NetworkInfo getNetworkStatus(){
        ConnectivityManager connMgr = (ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        return  connMgr.getActiveNetworkInfo();
    }



    private class ImageAdapter extends BaseAdapter{
        final String LOG_TAG=ImageAdapter.class.getSimpleName();
        private Context mContext;


        public ImageAdapter(Context c){
            mContext=c;
        }

        @Override
        public int getCount() {
            return tempImg.length;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView imageView;
            if (convertView==null){
                imageView=new ImageView(mContext);
            }
            else {
                imageView=(ImageView)convertView;
            }

            NetworkInfo netInfo=getNetworkStatus();
            if (netInfo!=null && netInfo.isConnected()){
                Log.v(LOG_TAG, "Hey");
                /*if (images!=null){
                    Picasso.with(getContext()).load(tempD[position]).noFade().resize(512, 764).into(imageView);
                    Log.v(LOG_TAG, "Signal is 1");
                }*/
                Picasso.with(getContext()).load(tempD[position]).noFade().resize(512, 764).into(imageView);
            }
            else{
                Toast.makeText(getContext(),"Connection Error!" ,Toast.LENGTH_SHORT).show();
            }

            return imageView;
        }
    }


    private class FetchMovieData extends AsyncTask<String, Void, String[]>{

        final String MOVIE_BASE_URL="http://api.themoviedb.org/3/movie/";
        final String API_KEY_PARAM="api_key";
        final String API_KEY="5797240513e0940dd87e4768fa6018bc";

        final String LOG_TAG=FetchMovieData.class.getSimpleName();

        public String[] getImgLinks(String movieDataString) throws JSONException{
            final String TAG_RESULTS="results";
            final String TAG_POSTER_PATH="poster_path";
            final String BASE_IMG_URL=" http://image.tmdb.org/t/p/";
            final String IMG_SIZE="w185";
            JSONObject jsonObject=new JSONObject(movieDataString);
            JSONArray jsonArray=jsonObject.getJSONArray(TAG_RESULTS);
            int numResults=jsonArray.length();
            String[] imgLinks=new String[numResults];
            for (int i=0;i<numResults; i++){
                imgLinks[i]=BASE_IMG_URL+IMG_SIZE+jsonArray.getJSONObject(i).getString(TAG_POSTER_PATH);
                Log.v(LOG_TAG, imgLinks[i]);
            }
            return  imgLinks;
        }

        @Override
        protected String[] doInBackground(String... params) {

            if (params.length==0){
                return null;
            }

            final String LIST_TYPE_PARAM=params[0]+"?";
            InputStream inputStream=null;
            BufferedReader reader=null;
            String movieDataString=null;
            HttpURLConnection connection=null;

            try {
                Uri builtUri=Uri.parse(MOVIE_BASE_URL).buildUpon().appendPath(LIST_TYPE_PARAM).appendQueryParameter(API_KEY_PARAM, API_KEY).build();
                //URL url=new URL(builtUri.toString());
                URL url=new URL("http://api.themoviedb.org/3/movie/top_rated?api_key=5797240513e0940dd87e4768fa6018bc");
                NetworkInfo netInfo=getNetworkStatus();
                if (netInfo!=null && netInfo.isConnected()){
                    connection=(HttpURLConnection)url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.connect();
                    int response=connection.getResponseCode();
                    Log.v(LOG_TAG,"The response is: "+response);
                    inputStream=connection.getInputStream();
                    if (inputStream==null){
                        Log.v(LOG_TAG, "inputStream is null");
                        return null;
                    }
                    StringBuffer buffer=new StringBuffer();
                    reader=new BufferedReader(new InputStreamReader((inputStream)));
                    String line;
                    while ((line=reader.readLine())!=null){
                        buffer.append(line+"\n");
                    }
                    if (buffer.length()==0){
                        Log.v(LOG_TAG, "buffer is null");
                        return  null;
                    }
                    movieDataString=buffer.toString();
                }
                else{
                    Log.e(LOG_TAG, "Connection Error!");
                }
            }
            catch (IOException e){
                Log.e(LOG_TAG, "Didn't get data");
                return null;
            }
            finally {
                if (connection!=null){
                    connection.disconnect();
                }
                if (reader!=null){
                    try {
                        reader.close();
                    }
                    catch (IOException e){
                        Log.e(LOG_TAG, "Error Closing reader stream", e);
                    }
                }
            }
            try {
                return getImgLinks(movieDataString);
            }
            catch (JSONException e){
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String[] results) {
            if (results!=null){
                images=results;
                for (int i=0;i<images.length; i++){
                    Log.v(LOG_TAG+"2nd", images[i]);
                }
            }
        }
    }
}
