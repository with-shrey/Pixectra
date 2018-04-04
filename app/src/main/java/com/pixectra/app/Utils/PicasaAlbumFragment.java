package com.pixectra.app.Utils;

import android.animation.Animator;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.pixectra.app.Models.Images;
import com.pixectra.app.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;


/**
 * Created by Sanath on 3/25/2018.
 */

public class PicasaAlbumFragment extends Fragment {

    RecyclerView galleryGridView;
    ArrayList<Images> imageList = new ArrayList<>();
    String albumName = "";
    PicasaSingleAlbumAdapter adapter;
    int maxP;
    String key;
    String albumId;
    RequestQueue queue;
    String accessToken;

/*
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_album);
        setFinishOnTouchOutside(false);
        Intent intent = getIntent();
        albumName = intent.getStringExtra("albumName");
        //setTitle(album_name);
        ((TextView) findViewById(R.id.toolbar_text)).setText(albumName);
        (findViewById(R.id.toolbar_back)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        queue = Volley.newRequestQueue(this);
        galleryGridView = findViewById(R.id.galleryGridView);
        galleryGridView.setLayoutManager(new GridLayoutManager(this, 3));
        adapter = new PicasaSingleAlbumAdapter(this, imageList);
        galleryGridView.setAdapter(adapter);
        key = intent.getStringExtra("key");
        maxP = intent.getIntExtra("maxP", 0);
        albumId = intent.getStringExtra("albumId");
        accessToken = intent.getStringExtra("accessToken");
        fetchAlbumImagesJSON();
    }*/

    public PicasaAlbumFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_album, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        albumName = getArguments().getString("albumName");
        //setTitle(album_name);
        ((TextView) view.findViewById(R.id.toolbar_text)).setText(albumName);
        (view.findViewById(R.id.toolbar_back)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().popBackStack("picasaAlbumActivity", FragmentManager.POP_BACK_STACK_INCLUSIVE);
            }
        });

        queue = Volley.newRequestQueue(getActivity());
        galleryGridView = view.findViewById(R.id.galleryGridView);
        galleryGridView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        adapter = new PicasaSingleAlbumAdapter(getActivity(), imageList);
        galleryGridView.setAdapter(adapter);
        key = getArguments().getString("key");
        maxP = getArguments().getInt("maxPics", 0);
        albumId = getArguments().getString("albumId");
        accessToken = getArguments().getString("accessToken");
        fetchAlbumImagesJSON();

    }


    private void fetchAlbumImagesJSON() {
        String url = "https://picasaweb.google.com/data/feed/api/user/default/albumid/" + albumId + "?alt=json&kind=photo";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            getPicasaImages(response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params;//=  super.getHeaders();
                //if(params==null)
                params = new HashMap<>();
                params.put("Authorization", "Bearer " + accessToken);
                params.put("GData-Version", "3");
                return params;
            }
        };

        queue.add(jsonObjectRequest);
    }

    void getPicasaImages(JSONObject response) throws JSONException {
        JSONArray entries = response.getJSONObject("feed").getJSONArray("entry");
        int l = entries.length();
        for (int i = 0; i < l; i++) {
            JSONObject entry = entries.getJSONObject(i);
            String contentType = entry.getJSONObject("content").getString("type");
            if (contentType.contains("gif") || !contentType.contains("image"))
                continue;
            String imageUrl = entry.getJSONObject("content").getString("src");
            String thumbnailUrl = entry.getJSONObject("media$group").getJSONArray("media$thumbnail")
                    .getJSONObject(1).getString("url");

            imageList.add(new Images(imageUrl, thumbnailUrl));
        }
        adapter.notifyDataSetChanged();
    }

    public class PicasaSingleAlbumAdapter extends RecyclerView.Adapter<PicasaSingleAlbumAdapter.PicasaSingleAlbumViewHolder> {
        int w;
        private Activity activity;
        private List<Images> data;

        PicasaSingleAlbumAdapter(Activity a, List<Images> d) {
            activity = a;
            data = d;
            DisplayMetrics dm = new DisplayMetrics();
            (getActivity()).getWindowManager().getDefaultDisplay().getMetrics(dm);
            w = (dm.widthPixels / 3) - (int) (PicasaAlbumFragment.this.getResources().getDimension(R.dimen.image_cell_padding) * 5);
        }

        @Override
        public PicasaSingleAlbumViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(activity).inflate(
                    R.layout.image_recycler_item, parent, false);
            return new PicasaSingleAlbumViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final PicasaSingleAlbumViewHolder holder, int position) {
            RequestOptions requestOptions = new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL);
            Images current = data.get(position);
            Glide.with(activity).load(current.getThumbnail()).apply(requestOptions)

                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            holder.galleryImage.setImageResource(R.drawable.ic_picture);

                            holder.progress.setVisibility(View.GONE);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            holder.progress.setVisibility(View.GONE);
                            return false;
                        }
                    })
                    .into(holder.galleryImage);

        }


        public long getItemId(int position) {
            return position;
        }

        @Override
        public int getItemCount() {

            return data.size();
        }

        public class PicasaSingleAlbumViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            ImageView galleryImage, overlay;
            ProgressBar progress;

            PicasaSingleAlbumViewHolder(View itemView) {
                super(itemView);
                galleryImage = itemView.findViewById(R.id.ListIcon);
                overlay = itemView.findViewById(R.id.selected_view);
                progress = itemView.findViewById(R.id.image_loading_progress);
                itemView.getLayoutParams().height = w;
                itemView.setOnClickListener(this);
            }

            @Override
            public void onClick(View view) {
                if (CartHolder.getInstance().getSize(key) < maxP) {
                    if (overlay.getVisibility() == View.GONE) {
                        progress.setVisibility(View.VISIBLE);
                        try {
                            GlideHelper.getBitmap(getActivity(), data.get(getAdapterPosition()).getUrl(), new RequestListener() {
                                @Override
                                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target target, boolean isFirstResource) {
                                    progress.setVisibility(View.GONE);
                                    Toast.makeText(getActivity(), "Unable To Fetch Full Size Image", Toast.LENGTH_SHORT).show();
                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(Object resource, Object model, Target target, DataSource dataSource, boolean isFirstResource) {
                                    progress.setVisibility(View.GONE);
                                    overlay.setVisibility(View.VISIBLE);
                                    overlay.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            overlay.animate().alpha(0f).setDuration(1000).setListener(new Animator.AnimatorListener() {
                                                @Override
                                                public void onAnimationStart(Animator animator) {

                                                }

                                                @Override
                                                public void onAnimationEnd(Animator animator) {
                                                    overlay.setVisibility(View.GONE);
                                                    overlay.setAlpha(1f);
                                                }

                                                @Override
                                                public void onAnimationCancel(Animator animator) {

                                                }

                                                @Override
                                                public void onAnimationRepeat(Animator animator) {
                                                }
                                            });
                                        }
                                    }, 500);
                                    CartHolder.getInstance().addImage(key, (Bitmap) resource);
                                    return false;
                                }
                            });
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                    }
                } else {
                    Toast.makeText(getActivity(), "Max. No Of Images Selected", Toast.LENGTH_SHORT).show();
                }
                if (overlay.getVisibility() == View.VISIBLE) {
                    try {
                        GlideHelper.getBitmap(getActivity(), data.get(getAdapterPosition()).getUrl(), new RequestListener() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target target, boolean isFirstResource) {
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Object resource, Object model, Target target, DataSource dataSource, boolean isFirstResource) {
                                CartHolder.getInstance().removeImage(key, (Bitmap) resource);
                                overlay.setVisibility(View.GONE);
                                return false;
                            }
                        });
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
