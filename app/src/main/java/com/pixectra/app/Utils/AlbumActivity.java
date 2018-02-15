package com.pixectra.app.Utils;

/**
 * Created by Suhail on 2/12/2018.
 */


import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.MergeCursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.pixectra.app.R;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;


public class AlbumActivity extends Activity {
    RecyclerView galleryGridView;
    ArrayList<HashMap<String, String>> imageList = new ArrayList<HashMap<String, String>>();
    String album_name = "";
    LoadAlbumImages loadAlbumTask;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_album);
        Intent intent = getIntent();
        album_name = intent.getStringExtra("name");
//        setTitle(album_name);


        galleryGridView = findViewById(R.id.galleryGridView);
        galleryGridView.setLayoutManager(new GridLayoutManager(this, 3));


        loadAlbumTask = new LoadAlbumImages();
        loadAlbumTask.execute();


    }


    class LoadAlbumImages extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            imageList.clear();
        }

        protected String doInBackground(String... args) {
            String xml = "";

            String path = null;
            String album = null;
            String timestamp = null;
            Uri uriExternal = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
            Uri uriInternal = android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI;

            String[] projection = {MediaStore.MediaColumns.DATA,
                    MediaStore.Images.Media.BUCKET_DISPLAY_NAME, MediaStore.MediaColumns.DATE_MODIFIED};

            Cursor cursorExternal = getContentResolver().query(uriExternal, projection, "bucket_display_name = \"" + album_name + "\"", null, null);
            Cursor cursorInternal = getContentResolver().query(uriInternal, projection, "bucket_display_name = \"" + album_name + "\"", null, null);
            Cursor cursor = new MergeCursor(new Cursor[]{cursorExternal, cursorInternal});
            while (cursor.moveToNext()) {

                path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA));
                album = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME));
                timestamp = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATE_MODIFIED));
                if (timestamp == null) {
                    timestamp = "1518331969";
                }
                imageList.add(Function.mappingInbox(album, path, timestamp, Function.converToTime(timestamp), null));
            }
            cursor.close();
            Collections.sort(imageList, new MapComparator(Function.KEY_TIMESTAMP, "dsc")); // Arranging photo album by timestamp decending
            return xml;
        }

        @Override
        protected void onPostExecute(String xml) {

            SingleAlbumAdapter adapter = new SingleAlbumAdapter(AlbumActivity.this, imageList);
            galleryGridView.setAdapter(adapter);

        }
    }


    class SingleAlbumAdapter extends RecyclerView.Adapter<SingleAlbumAdapter.SingleAlbumViewHolder> {
        private Activity activity;
        private ArrayList<HashMap<String, String>> data;

        public SingleAlbumAdapter(Activity a, ArrayList<HashMap<String, String>> d) {
            activity = a;
            data = d;
        }

        @Override
        public SingleAlbumViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(activity).inflate(
                    R.layout.image_recycler_item, parent, false);
            return new SingleAlbumViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final SingleAlbumViewHolder holder, int position) {
            HashMap<String, String> song = data.get(position);

            Glide.with(activity)
                    .load(new File(song.get(Function.KEY_PATH))).listener(new RequestListener<Drawable>() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                    holder.loader.setVisibility(View.GONE);
                    return false;
                }

                @Override
                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                    holder.loader.setVisibility(View.GONE);
                    return false;
                }
            }) // Uri of the picture
                    .into(holder.galleryImage);
        }

        public long getItemId(int position) {
            return position;
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        class SingleAlbumViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            ImageView galleryImage, overlay;
            ProgressBar loader;

            public SingleAlbumViewHolder(View itemView) {
                super(itemView);
                galleryImage = itemView.findViewById(R.id.ListIcon);
                overlay = itemView.findViewById(R.id.selected_view);
                loader = itemView.findViewById(R.id.image_loading_progress);

                itemView.setOnClickListener(this);
            }

            @Override
            public void onClick(View view) {
                if (overlay.getVisibility() == View.GONE) {
                    overlay.setVisibility(View.VISIBLE);
                    // CartHolder.getInstance().addImage("x", data.get(getAdapterPosition()));
                } else {
                    overlay.setVisibility(View.GONE);
                    //CartHolder.getInstance().removeImage("x", data.get(getAdapterPosition()));
                }
            }
        }
    }


}