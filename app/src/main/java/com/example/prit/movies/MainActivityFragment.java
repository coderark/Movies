package com.example.prit.movies;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    /*private String[] tempImg={"http://image.tmdb.org/t/p/rDT86hJCxnoOs4ARjrCiRej7pOi.jpg", "http://image.tmdb.org/t/p/k1QUCjNAkfRpWfm1dVJGUmVHzGv.jpg", "http://image.tmdb.org/t/p/zSouWWrySXshPCT4t3UKCQGayyo.jpg", "http://image.tmdb.org/t/p/cGOPbv9wA5gEejkUN892JrveARt.jpg", "http://image.tmdb.org/t/p/dlIPGXPxXQTp9kFrRzn0RsfUelx.jpg", "http://image.tmdb.org/t/p/5TQ6YDmymBpnF005OyoB7ohZps9.jpg", "http://image.tmdb.org/t/p/kqjL17yufvn9OVLyXYpvtyrFfak.jpg"};*/

    private Integer[] tempD={R.drawable.i1, R.drawable.i2, R.drawable.i3, R.drawable.i4, R.drawable.i5,R.drawable.i6, R.drawable.i7};

    public MainActivityFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView=inflater.inflate(R.layout.fragment_main, container, false);

        GridView gridView=(GridView)rootView.findViewById(R.id.gridview);
        gridView.setAdapter(new ImageAdapter(getContext()));

        return rootView;
    }

    public class ImageAdapter extends BaseAdapter{

        private Context mContext;

        public ImageAdapter(Context c){
            mContext=c;
        }

        @Override
        public int getCount() {
            return tempD.length;
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
            Picasso.with(getContext()).load(tempD[position]).noFade().resize(512, 512).into(imageView);

            return imageView;
        }
    }
}
