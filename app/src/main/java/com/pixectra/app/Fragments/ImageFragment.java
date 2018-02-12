package com.pixectra.app.Fragments;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.Profile;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.pixectra.app.Adapter.ImageSelectAdapter;
import com.pixectra.app.Models.Images;
import com.pixectra.app.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static android.support.v4.provider.FontsContractCompat.FontRequestCallback.RESULT_OK;
import static com.facebook.FacebookSdk.getApplicationContext;

public class ImageFragment extends Fragment {

    //LinearLayout ImageFromPhone;
   // Button SelectImageText;
    private RecyclerView recyclerView;
    private ImageSelectAdapter adapter;
    ImageView noLoginView;
   // ImageView LoadPhoneImage;
    int category;
    List<Images> imageData;
    GraphResponse lastGraphResponse;
    final int PICK_IMAGE_REQUEST = 1;
    GridView imagegrid;
    Button selectBtn ;
    /*
   Images from device ->
    */
    private int count;
    private Bitmap[] thumbnails;
    private boolean[] thumbnailsselection;
    private String[] arrPath;
    private ImageAdapter imageAdapter;


    public class ImageAdapter extends BaseAdapter {
        private LayoutInflater mInflater;

        public ImageAdapter() {
            mInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        public int getCount() {
            return count;
        }

        public Object getItem(int position) {
            return position;
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = mInflater.inflate(
                        R.layout.grid_phone_images, null);
                holder.imageview = (ImageView) convertView.findViewById(R.id.thumbImage);
                holder.checkbox = (CheckBox) convertView.findViewById(R.id.itemCheckBox);

                convertView.setTag(holder);
            }
            else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.checkbox.setId(position);
            holder.imageview.setId(position);
            holder.checkbox.setOnClickListener(new OnClickListener() {

                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    CheckBox cb = (CheckBox) v;
                    int id = cb.getId();
                    if (thumbnailsselection[id]){
                        cb.setChecked(false);
                        thumbnailsselection[id] = false;
                    } else {
                        cb.setChecked(true);
                        thumbnailsselection[id] = true;
                    }
                }
            });
            holder.imageview.setOnClickListener(new OnClickListener() {

                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    int id = v.getId();
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.setDataAndType(Uri.parse("file://" + arrPath[id]), "image/*");
                    startActivity(intent);
                }
            });
            holder.imageview.setImageBitmap(thumbnails[position]);
            holder.checkbox.setChecked(thumbnailsselection[position]);
            holder.id = position;
            return convertView;
        }
    }


    class ViewHolder {
        ImageView imageview;
        CheckBox checkbox;
        int id;
    }


    //-------------------------->
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


        //select
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View layout = inflater.inflate(R.layout.image_select_fragment, null);
       // ImageFromPhone = layout.findViewById(R.id.select_imagefromphone_Llayout);
       // SelectImageText = layout.findViewById(R.id.button_select_imagefromphone);
        imageData = new ArrayList<>();
       // LoadPhoneImage = layout.findViewById(R.id.imageview_select_imagefromphone);
        recyclerView = layout.findViewById(R.id.Imagelist);
        noLoginView = layout.findViewById(R.id.no_login_view);
        adapter = new ImageSelectAdapter(getActivity(), imageData);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        imagegrid = layout.findViewById(R.id.PhoneImageGrid);
        selectBtn = layout.findViewById(R.id.selectBtn);
        checkAndLoadData();




        return layout;

    }

void loadimages()
{
    //<----
    final String[] columns = { MediaStore.Images.Media.DATA, MediaStore.Images.Media._ID };
    final String orderBy = MediaStore.Images.Media._ID;
    Cursor imagecursor = getActivity().getContentResolver().query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, null,
            null, orderBy);
    int image_column_index = imagecursor.getColumnIndex(MediaStore.Images.Media._ID);
    this.count = imagecursor.getCount();
    this.thumbnails = new Bitmap[this.count];
    this.arrPath = new String[this.count];
    this.thumbnailsselection = new boolean[this.count];
    for (int i = 0; i < this.count; i++) {
        imagecursor.moveToPosition(i);
        int id = imagecursor.getInt(image_column_index);
        int dataColumnIndex = imagecursor.getColumnIndex(MediaStore.Images.Media.DATA);
        thumbnails[i] = MediaStore.Images.Thumbnails.getThumbnail(
                getApplicationContext().getContentResolver(), id,
                MediaStore.Images.Thumbnails.MICRO_KIND, null);
        arrPath[i]= imagecursor.getString(dataColumnIndex);
    }

    imageAdapter = new ImageAdapter();
    if (imageAdapter==null)
    {Log.d("imageadapter","null");}
    imagegrid.setAdapter(imageAdapter);
    imagecursor.close();


    selectBtn.setOnClickListener(new OnClickListener() {

        public void onClick(View v) {
            // TODO Auto-generated method stub
            final int len = thumbnailsselection.length;
            int cnt = 0;
            String selectImages = "";
            for (int i =0; i<len; i++)
            {
                if (thumbnailsselection[i]){
                    cnt++;
                    selectImages = selectImages + arrPath[i] + "|";
                }
            }
            if (cnt == 0){
                Toast.makeText(getApplicationContext(),
                        "Please select at least one image",
                        Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getApplicationContext(),
                        "You've selected Total " + cnt + " image(s).",
                        Toast.LENGTH_LONG).show();
                Log.d("SelectedImages", selectImages);
            }
        }
    });

}

    /*
    <-- To add and remove views in 1st fragment
     */
    /*void ChangeButtonText(int id) {
        if (id == 1) {
            SelectImageText.setText("Select Different Image");

        } else if (id == 2) {
            SelectImageText.setText("Select Image");

        }

    }


    //<-- To ADD OR REMOVE LAYOUT OF 1ST FRAG
    void Addremovelayout(int id) {
        if (id == 1) {
            ImageFromPhone.setVisibility(View.GONE);
        } else if (id == 2) {
            ImageFromPhone.setVisibility(View.VISIBLE);
        }
    }
*/

    /**
     * Main Driver Function Checks And Fetch Data from Different Places
     */
    void checkAndLoadData() {
        switch (category) {
            case 0: //Device Photos

               // Addremovelayout(2);
                if (ActivityCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)//Check For permission if not given show button
                    userLoggedIn(false);
                else {

                /*    ChangeButtonText(2);
                    SelectImageText.setOnClickListener(
                            new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    //<-- Choosing Image from device
                                    Intent intent = new Intent();
                                    // Show only images, no videos or anything else
                                    intent.setType("image/*");
                                    intent.setAction(Intent.ACTION_PICK);
                                    // Always show the chooser (if there are multiple options available)
                                    startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);


                                }
                            }
                    );


                */
                loadimages();
                }
                break;
            case 1:

               // Addremovelayout(1); //<-- To remove 1st frag layout

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
            case 2://Instagram


                //Addremovelayout(1);  //<-- To remove 1st frag layout


                if (true) {  // User Is Not Logged In
                    userLoggedIn(false);   // Display Sign In Button set true to remove button
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
            case 3:  //Google Photos

               // Addremovelayout(1);  //<-- To remove 1st frag layout

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
    void getFacebookImages(Profile profile) {
        Bundle params = new Bundle();
        params.putString("fields", "images");
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
            noLoginView.setOnClickListener(null);
            noLoginView.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        } else {
            noLoginView.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.INVISIBLE);
            setImage();
            if (category == 0) {
                noLoginView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        requestPermissions(
                                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                2);
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 2) {
            if (permissions[0].equals(Manifest.permission.READ_EXTERNAL_STORAGE)
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                userLoggedIn(true);
                checkAndLoadData();
            }
        }
    }


}
