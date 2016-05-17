package com.example.prit.movies;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    private String[] tempImg={"http://image.tmdb.org/t/p/w185/rDT86hJCxnoOs4ARjrCiRej7pOi.jpg", "http://image.tmdb.org/t/p/w185/k1QUCjNAkfRpWfm1dVJGUmVHzGv.jpg", "http://image.tmdb.org/t/p/w185/zSouWWrySXshPCT4t3UKCQGayyo.jpg", "http://image.tmdb.org/t/p/w185/cGOPbv9wA5gEejkUN892JrveARt.jpg", "http://image.tmdb.org/t/p/w185/dlIPGXPxXQTp9kFrRzn0RsfUelx.jpg", "http://image.tmdb.org/t/p/w185/5TQ6YDmymBpnF005OyoB7ohZps9.jpg", "http://image.tmdb.org/t/p/w185/kqjL17yufvn9OVLyXYpvtyrFfak.jpg"};

    private Integer[] tempD={R.drawable.i1, R.drawable.i2, R.drawable.i3, R.drawable.i4, R.drawable.i5,R.drawable.i6, R.drawable.i7};
    private ImageAdapter imageAdapter=null;

    public MainActivityFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_fragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id=item.getItemId();
        if (id==R.id.action_refresh){
            updateData();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView=inflater.inflate(R.layout.fragment_main, container, false);
        GridView gridView=(GridView)rootView.findViewById(R.id.gridview);
        imageAdapter=new ImageAdapter(getContext());
        gridView.setAdapter(imageAdapter);
        updateData();
        return rootView;
    }

    public NetworkInfo getNetworkStatus(){
        ConnectivityManager connMgr = (ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        return  connMgr.getActiveNetworkInfo();
    }

    public void updateData(){
        FetchMovieData movieData=new FetchMovieData();
        movieData.execute("popular");
    }



    private class ImageAdapter extends BaseAdapter{
        final String LOG_TAG=ImageAdapter.class.getSimpleName();
        private Context mContext;
        ArrayList<String> imgLinks;


        public ImageAdapter(Context c){
            mContext=c;
            imgLinks=new ArrayList<String>();
        }

        @Override
        public int getCount() {
            return imgLinks.size();
        }

        @Override
        public Object getItem(int position) {
            return imgLinks.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        public void addAll(String[] strings){

            for (int i=0; i<strings.length; i++){
                imgLinks.add(strings[i]);
            }
            notifyDataSetChanged();
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
                Picasso.with(mContext).load(imgLinks.get(position).toString()).noFade().resize(512, 764).into(imageView);
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
        final String API_PAGE_PARAM = "page";
        final String API_KEY="5797240513e0940dd87e4768fa6018bc";

        final String LOG_TAG=FetchMovieData.class.getSimpleName();

        public String[] getImgLinks(String movieDataString) throws JSONException{
            final String TAG_RESULTS="results";
            final String TAG_POSTER_PATH="poster_path";
            final String BASE_IMG_URL="http://image.tmdb.org/t/p/";
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

            final String LIST_TYPE_PARAM=params[0];
            InputStream inputStream=null;
            BufferedReader reader=null;
            String movieDataString=null;
            HttpURLConnection connection=null;

            try {
                Uri builtUri=Uri.parse(MOVIE_BASE_URL).buildUpon().appendPath(LIST_TYPE_PARAM).appendQueryParameter(API_PAGE_PARAM, String.valueOf(2)).appendQueryParameter(API_KEY_PARAM, API_KEY).build();
                URL url=new URL(builtUri.toString());
                Log.v(LOG_TAG, builtUri.toString());

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
                imageAdapter.addAll(results);
            }
        }
    }
}
