package com.pixectra.app.Fragments;


import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.MergeCursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.Profile;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.pixectra.app.Adapter.ImageSelectAdapter;
import com.pixectra.app.Instagram.ApplicationData;
import com.pixectra.app.Instagram.InstagramApp;
import com.pixectra.app.Models.Images;
import com.pixectra.app.R;
import com.pixectra.app.Utils.AlbumActivity;
import com.pixectra.app.Utils.Function;
import com.pixectra.app.Utils.MapComparator;
import com.pixectra.app.Utils.SessionHelper;
import com.pixectra.app.Utils.VolleyQueue;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import static com.facebook.FacebookSdk.getApplicationContext;

;

public class ImageFragment extends Fragment {

    private RecyclerView recyclerView;
    private ImageSelectAdapter adapter;
    ImageView noLoginView;
int category;
    InstagramApp mApp;//Instagram
    List<Images> imageData ;
GraphResponse lastGraphResponse;
    public static final String TAG_DATA = "data";
    public static final String TAG_IMAGES = "images";
    public static final String TAG_THUMBNAIL = "thumbnail";
    public static final String TAG_URL = "url";

    final int PICK_IMAGE_REQUEST = 1;
    GridView imagegrid;
    Button selectBtn;



    /*
      <-- Images from device
    */

    static final int REQUEST_PERMISSION_KEY = 1;
    LoadAlbum loadAlbumTask;
    GridView galleryGridView;
    ArrayList<HashMap<String, String>> albumList = new ArrayList<HashMap<String, String>>();

    public ImageFragment() {
    }

    public static ImageFragment newInstance(int type) {
        ImageFragment fragment = new ImageFragment();
        Bundle args = new Bundle();
        args.putInt("type", type);
        // args.putString("someTitle", title);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        category = getArguments().getInt("type", -1);

    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View layout = inflater.inflate(R.layout.image_select_fragment, null);
        imageData = new ArrayList<>();
        recyclerView = layout.findViewById(R.id.Imagelist);
        noLoginView = layout.findViewById(R.id.no_login_view);
        adapter = new ImageSelectAdapter(getActivity(), imageData);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        // imagegrid = layout.findViewById(R.id.PhoneImageGrid);
        //   selectBtn = layout.findViewById(R.id.selectBtn);


        //<--For first fragment

        galleryGridView = layout.findViewById(R.id.galleryGridView);
        galleryGridView.setNumColumns(3);

        float iDisplayWidth = getResources().getDisplayMetrics().widthPixels;

        Resources resources = getApplicationContext().getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float dp = iDisplayWidth / (metrics.densityDpi / 160f);

        if (dp < 360) {
            dp = (dp - 17) / 2;
            float px = Function.convertDpToPixel(dp, getApplicationContext());
            galleryGridView.setColumnWidth(Math.round(px));
        }




        checkAndLoadData();

        return layout;

    }


    //<---Methods and classes for first fragment------------------------------START-----------------
    class LoadAlbum extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            albumList.clear();
        }

        protected String doInBackground(String... args) {
            String xml = "";

            String path = null;
            String album = null;
            String timestamp = null;
            String countPhoto = null;
            Uri uriExternal = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
            Uri uriInternal = android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI;


            String[] projection = {MediaStore.MediaColumns.DATA,
                    MediaStore.Images.Media.BUCKET_DISPLAY_NAME, MediaStore.MediaColumns.DATE_MODIFIED};
            Cursor cursorExternal = getActivity().getContentResolver().query(uriExternal, projection, "_data IS NOT NULL) GROUP BY (bucket_display_name",
                    null, null);
            Cursor cursorInternal = getActivity().getContentResolver().query(uriInternal, projection, "_data IS NOT NULL) GROUP BY (bucket_display_name",
                    null, null);
            Cursor cursor = new MergeCursor(new Cursor[]{cursorExternal, cursorInternal});

            while (cursor.moveToNext()) {

                path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA));
                album = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME));
                timestamp = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATE_MODIFIED));
                countPhoto = Function.getCount(getApplicationContext(), album);


                Log.v("album", path + "," + album + "," + timestamp + "," + countPhoto);
                if (timestamp == null) {
                    timestamp = "1518331969";
                }

                albumList.add(Function.mappingInbox(album, path, timestamp, Function.converToTime(timestamp), countPhoto));
            }
            cursor.close();
            Collections.sort(albumList, new MapComparator(Function.KEY_TIMESTAMP, "dsc")); // Arranging photo album by timestamp decending
            return xml;
        }

        @Override
        protected void onPostExecute(String xml) {

            AlbumAdapter adapter = new AlbumAdapter(getActivity(), albumList);
            galleryGridView.setAdapter(adapter);
            galleryGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View view,
                                        final int position, long id) {
                    Intent intent = new Intent(getActivity(), AlbumActivity.class);
                    intent.putExtra("name", albumList.get(+position).get(Function.KEY_ALBUM));
                    startActivity(intent);
                }
            });
        }
    }


//    @Override
//    public void onResume() {
//        super.onResume();
//
//        String[] PERMISSIONS = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
//        if (!Function.hasPermissions(getActivity(), PERMISSIONS)) {
//            ActivityCompat.requestPermissions(getActivity(), PERMISSIONS, REQUEST_PERMISSION_KEY);
//        } else {
//
//        }
//
//    }


    class AlbumAdapter extends BaseAdapter {
        private Activity activity;
        private ArrayList<HashMap<String, String>> data;

        public AlbumAdapter(Activity a, ArrayList<HashMap<String, String>> d) {
            activity = a;
            data = d;
        }

        public int getCount() {
            return data.size();
        }

        public Object getItem(int position) {
            return position;
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            AlbumViewHolder holder = null;
            if (convertView == null) {
                holder = new AlbumViewHolder();
                convertView = LayoutInflater.from(activity).inflate(
                        R.layout.album_row, parent, false);

                holder.galleryImage = convertView.findViewById(R.id.galleryImage);
                holder.gallery_count = convertView.findViewById(R.id.gallery_count);
                holder.gallery_title = convertView.findViewById(R.id.gallery_title);

                convertView.setTag(holder);
            } else {
                holder = (AlbumViewHolder) convertView.getTag();
            }
            holder.galleryImage.setId(position);
            holder.gallery_count.setId(position);
            holder.gallery_title.setId(position);

            HashMap<String, String> song = new HashMap<String, String>();
            song = data.get(position);
            try {
                holder.gallery_title.setText(song.get(Function.KEY_ALBUM));
                holder.gallery_count.setText(song.get(Function.KEY_COUNT));

                Glide.with(activity)
                        .load(new File(song.get(Function.KEY_PATH))) // Uri of the picture
                        .into(holder.galleryImage);


            } catch (Exception e) {
            }
            return convertView;
        }
    }


    class AlbumViewHolder {
        ImageView galleryImage;
        TextView gallery_count, gallery_title;
    }

//<---Methods and classes for first fragment------------------END-----------------------------------


    /**
     * Main Driver Function Checks And Fetch Data from Different Places
     */
    void checkAndLoadData() {
        switch (category) {
            case 0: //Device Photos

                galleryGridView.setVisibility(View.VISIBLE);
                if (ActivityCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)//Check For permission if not given show button
                    userLoggedIn(false);
                else {

                    loadAlbumTask = new LoadAlbum();
                    loadAlbumTask.execute();

                }
                break;
            case 1://facebook

                galleryGridView.setVisibility(View.GONE); //<-- To remove 1st frag layout

                if (AccessToken.getCurrentAccessToken() == null)
                    userLoggedIn(false);
                else {
                    Profile profile = Profile.getCurrentProfile();
                    getFacebookImages(profile);
                    recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                        @Override
                        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                            super.onScrollStateChanged(recyclerView, newState);
                            if (!recyclerView.canScrollVertically(1)) {
                                getNextImages(lastGraphResponse);
                            }
                        }
                    });
                }
                break;
            case 2:

                mApp = new InstagramApp(getActivity(), ApplicationData.CLIENT_ID,
                        ApplicationData.CLIENT_SECRET, ApplicationData.CALLBACK_URL);

                if(!mApp.hasAccessToken()) {
                    // User Is Not Logged In
                    userLoggedIn(false);   // Display Sign In Button set true to remove button

                    mApp.setListener( new InstagramApp.OAuthAuthenticationListener() {

                        @Override
                        public void onSuccess() {
                            // tvSummary.setText("Connected as " + mApp.getUserName());
                            userLoggedIn(true);
                            getInstagramImages();
                        }

                        @Override
                        public void onFail(String error) {
                            Toast.makeText(getActivity(), "Connot Load Images", Toast.LENGTH_SHORT)
                                    .show();
                        }
                    });
                }else{
                    getInstagramImages();

                    //TODO:LOAD DATA

                    recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {  //Load More Data on scroll
                        @Override
                        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                            super.onScrollStateChanged(recyclerView, newState);
                            Toast.makeText(getActivity(), "Api In Sandbox mode 20 only", Toast.LENGTH_SHORT)
                                    .show();
                        }
                    });
                }
                break;
            case 3:  //Google Photos

                galleryGridView.setVisibility(View.GONE);

                if (GoogleSignIn.getLastSignedInAccount(getActivity()) == null) {  // User Is Not Logged In
                    userLoggedIn(false);              // Display Sign In Button set true to remove button
                } else {
                    //TODO:LOAD DATA

                    recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {  //Load More Data on scroll
                        @Override
                        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                            super.onScrollStateChanged(recyclerView, newState);
                        }
                    });
                }
                break;

        }
    }


    /**
     * Parse Response And Add To List
     *
     * @param response
     */
    void parseFacebookJson(GraphResponse response) {
        JSONObject main = response.getJSONObject();
        try {
            JSONArray data = main.getJSONArray("data");
            int n = data.length();
            for (int i = 0; i < n; i++) {
                JSONObject temp = data.getJSONObject(i);
                JSONArray images = temp.getJSONArray("images");
                int imgs = images.length();
                for (int j = 0; j < imgs; j++) {
                    JSONObject img = images.getJSONObject(j);
                    Images image = null;
                    if (j == 0) {
                        image = new Images();
                        image.setUrl(img.getString("source"));
                        image.setThumbnail(images.getJSONObject(imgs - 1).getString("source"));
                        imageData.add(image);
                    }

                }
            }
            lastGraphResponse = response;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        adapter.notifyDataSetChanged();
    }

    /**
     * Fetch Images From Facebook Make Request
     *
     * @param profile
     */
   void getFacebookImages(Profile profile){
        Bundle params  = new Bundle();
        Log.v("permissions",AccessToken.getCurrentAccessToken().getPermissions().toString());
        params.putString("fields","images");
        new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/" + profile.getId() + "/photos",
                params,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
                        parseFacebookJson(response);
                    }
                }
        ).executeAsync();
    }

    /**
     * Get More Images When User Scrolls
     *
     * @param lastGraphResponse
     */
    void getNextImages(GraphResponse lastGraphResponse) {
        GraphRequest nextResultsRequests = lastGraphResponse.getRequestForPagedResults(GraphResponse.PagingDirection.NEXT);
        if (nextResultsRequests != null) {
            nextResultsRequests.setCallback(new GraphRequest.Callback() {
                @Override
                public void onCompleted(GraphResponse response) {
                    parseFacebookJson(response);
                }
            });
            nextResultsRequests.executeAsync();
        }
    }

    /**
     * Display Buttons Of Respective Fragments
     *
     * @param status true if logged in else false
     */
    void userLoggedIn(boolean status) {
        if (status) {
            if (category == 0){
                galleryGridView.setVisibility(View.VISIBLE);
            }else{
                recyclerView.setVisibility(View.VISIBLE);
            }
            noLoginView.setOnClickListener(null);
            noLoginView.setVisibility(View.GONE);
        } else {
            if (category == 0){
                galleryGridView.setVisibility(View.GONE);
            }else {
                recyclerView.setVisibility(View.GONE);
            }
            noLoginView.setVisibility(View.VISIBLE);
            setImage();
            if (category == 0) {
                noLoginView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        requestPermissions(
                                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                REQUEST_PERMISSION_KEY);
                    }
                });
            }



            if (category == 2){
                noLoginView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mApp.authorize();
                    }
                });
            }
        }
    }

    /**
     * Set Login Button Image As Per Fragment
     */
    void setImage() {
        switch (category) {
            case 0:
                noLoginView.setImageResource(R.drawable.allow);
                break;
            case 1:
                noLoginView.setImageResource(R.drawable.loginwithfb);
                break;
            case 2:
                noLoginView.setImageResource(R.drawable.instasignin);
                break;
            case 3:
                noLoginView.setImageResource(R.drawable.loginwithgp);

                break;
        }
    }

    /* @Override
     public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
         if (requestCode == 2) {
             if (permissions[0].equals(Manifest.permission.READ_EXTERNAL_STORAGE)
                     && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                 userLoggedIn(true);
                 checkAndLoadData();
             }
         }
     }
 */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_PERMISSION_KEY: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    userLoggedIn(true);
                    loadAlbumTask = new LoadAlbum();
                    loadAlbumTask.execute();
                } else {
                    Toast.makeText(getActivity(), "You must accept permissions.", Toast.LENGTH_LONG).show();
                }
                break;
            }

        }
    }
    private void getInstagramImages() {
      // ProgressDialog pd = ProgressDialog.show(getActivity(), "", "Loading images...");
        String url="https://api.instagram.com/v1/users/"
                + new SessionHelper(getActivity()).getId()
                + "/media/recent/?client_id="
                + ApplicationData.CLIENT_ID
                +"&access_token="
                +new SessionHelper(getActivity()).getAccessToken();
        StringRequest jsonRequest = new StringRequest(Request.Method.GET,url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.v("response",response);
                JSONObject jsonObject= null;
                try {
                    jsonObject = new JSONObject(response);
                    parseInstagramJson(jsonObject);
                } catch (JSONException e) {
                        e.printStackTrace();
                    }
                adapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

                String message = "Cannot Complete Request Kindly Contact Us";
                if (volleyError instanceof NetworkError) {
                    message = "Cannot connect to Internet...Please check your connection!";
                } else if (volleyError instanceof ServerError) {
                    message = "The server could not be found. Please try again after some time!!";
                } else if (volleyError instanceof AuthFailureError) {
                    message = "Cannot connect to Internet...Please check your connection!";
                } else if (volleyError instanceof ParseError) {
                    message = "Parsing error! Please try again after some time!!";
                } else if (volleyError instanceof TimeoutError) {
                    message = "Connection TimeOut! Please check your internet connection.";
                }
                Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();

            }
        });



        int socketTimeout = 10000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsonRequest.setRetryPolicy(policy);

        VolleyQueue.getInstance(getActivity()).getRequestQueue().add(jsonRequest);

    }

    void parseInstagramJson(JSONObject jsonObject) throws JSONException {
        JSONArray data = jsonObject.getJSONArray(TAG_DATA);
        for (int data_i = 0; data_i < data.length(); data_i++) {
            JSONObject data_obj = data.getJSONObject(data_i);

            JSONObject images_obj = data_obj
                    .getJSONObject(TAG_IMAGES);

            JSONObject thumbnail_obj = images_obj
                    .getJSONObject(TAG_THUMBNAIL);
            JSONObject standard=images_obj
                    .getJSONObject("standard_resolution");
            // String str_height =
            // thumbnail_obj.getString(TAG_HEIGHT);
            //
            // String str_width =
            // thumbnail_obj.getString(TAG_WIDTH);

            String thumb = thumbnail_obj.getString(TAG_URL);
            String stand = standard.getString(TAG_URL);
            imageData.add(new Images(stand,thumb));
        }
    }
}
