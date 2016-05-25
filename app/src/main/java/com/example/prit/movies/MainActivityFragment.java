package com.example.prit.movies;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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

    private ImageAdapter imageAdapter=null;

    public ArrayList<Movie> movieList;
    public int totalPages;
    private int currentPage=1;

    public MainActivityFragment() {
        movieList=new ArrayList<Movie>();
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
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(getActivity(), DetailActivity.class);
                intent.putExtra(imageAdapter.getItem(position).EXTRA_MOVIE, imageAdapter.getItem(position).toBundle());
                startActivity(intent);
            }
        });
        updateData();
        return rootView;
    }

    public NetworkInfo getNetworkStatus(){
        ConnectivityManager connMgr = (ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        return  connMgr.getActiveNetworkInfo();
    }

    public void updateData(){
        FetchMovieData movieData=new FetchMovieData();
        movieData.execute(currentPage);
    }



    private class ImageAdapter extends BaseAdapter{
        final String LOG_TAG=ImageAdapter.class.getSimpleName();
        private Context mContext;
        ArrayList<Movie> imgLinks;


        public ImageAdapter(Context c){
            mContext=c;
            imgLinks=new ArrayList<Movie>();
        }

        @Override
        public int getCount() {
            return imgLinks.size();
        }

        @Override
        public Movie getItem(int position) {
            return imgLinks.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        public void addAll(ArrayList<Movie> movies){
            imgLinks.addAll(movies);
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
                if (imgLinks.size()-position<2){
                    currentPage++;
                    updateData();
                }
                Picasso.with(mContext).load(imgLinks.get(position).POSTER_PATH).placeholder(R.drawable.loading).noFade().resize(512, 764).into(imageView);
            }
            else{
                Toast.makeText(getContext(),"Connection Error!" ,Toast.LENGTH_SHORT).show();
            }

            return imageView;
        }
    }



    private class FetchMovieData extends AsyncTask<Integer, Void, ArrayList<Movie>>{

        final String MOVIE_BASE_URL="http://api.themoviedb.org/3/movie/";
        final String API_KEY_PARAM="api_key";
        final String API_PAGE_PARAM = "page";
        final String API_KEY="5797240513e0940dd87e4768fa6018bc";
        String API_SORTING_PARAM;

        final String LOG_TAG=FetchMovieData.class.getSimpleName();

        public ArrayList<Movie> getImgLinks(String movieDataString) throws JSONException{
            final String TAG_RESULTS="results";
            final String TAG_TOTAL_PAGES="total_pages";

            JSONObject jsonObject=new JSONObject(movieDataString);
            JSONArray jsonArray=jsonObject.getJSONArray(TAG_RESULTS);
            totalPages=jsonObject.getInt(TAG_TOTAL_PAGES);
            int numResults=jsonArray.length();
            ArrayList<Movie> imgLinks=new ArrayList<Movie>();
            for (int i=0;i<numResults; i++){
                imgLinks.add(new Movie(jsonArray.getJSONObject(i)));
                Log.v(LOG_TAG, imgLinks.get(i).POSTER_PATH);
            }
            return  imgLinks;
        }

        @Override
        protected ArrayList<Movie> doInBackground(Integer... params) {

            if (params.length==0){
                return null;
            }

            int page=params[0];
            InputStream inputStream=null;
            BufferedReader reader=null;
            String movieDataString=null;
            HttpURLConnection connection=null;

            API_SORTING_PARAM= PreferenceManager.getDefaultSharedPreferences(getActivity()).getString(getString(R.string.settings_entry_key), getString(R.string.settings_default_entryValue));

            try {

                Uri builtUri=Uri.parse(MOVIE_BASE_URL).buildUpon().appendPath(API_SORTING_PARAM).appendQueryParameter(API_PAGE_PARAM, String.valueOf(page)).appendQueryParameter(API_KEY_PARAM, API_KEY).build();
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
        protected void onPostExecute(ArrayList<Movie> results) {
            if (results!=null){
                imageAdapter.addAll(results);
            }
        }
    }
}
