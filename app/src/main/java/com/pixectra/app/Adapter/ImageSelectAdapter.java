package com.pixectra.app.Adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.pixectra.app.ImageSelectActivity;
import com.pixectra.app.Models.Images;
import com.pixectra.app.R;
import com.pixectra.app.Utils.CartHolder;
import com.pixectra.app.Utils.GlideHelper;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by Compu1 on 09-Feb-18.
 */

public class ImageSelectAdapter extends RecyclerView.Adapter<ImageSelectAdapter.myViewHolder> {
    List<Images> data = Collections.emptyList();
    private String key;
    Context c;
    private int w, maxP;
    private LayoutInflater inflater;
    HashMap<String, Boolean> selectedImages;

    public ImageSelectAdapter(Context context, String key, int maxPics, List<Images> data) {
        inflater = LayoutInflater.from(context);
        selectedImages = ((ImageSelectActivity) context).selected;
        this.data = data;
        this.key = key;
        maxP = maxPics;
        DisplayMetrics dm = new DisplayMetrics();
        ((ImageSelectActivity) context).getWindowManager().getDefaultDisplay().getMetrics(dm);
        w = (dm.widthPixels / 3) - (int) (context.getResources().getDimension(R.dimen.image_cell_padding) * 3);
        c = context;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_recycler_item, parent, false);
        return new myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final myViewHolder holder, int position) {
        Images current = data.get(position);
        if (selectedImages.containsKey(current.getUrl())) {
            holder.overlay.setVisibility(View.VISIBLE);
        } else {
            holder.overlay.setVisibility(View.GONE);
        }
        RequestOptions requestOptions = new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL);
        Glide.with(c).load(current.getThumbnail()).apply(requestOptions)

                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        holder.icon.setImageResource(R.drawable.ic_picture);

                        holder.progress.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        holder.progress.setVisibility(View.GONE);
                        return false;
                    }
                })
                .into(holder.icon);
    }


    @Override
    public int getItemCount() {
        return data.size();
    }

    class myViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        boolean selected;
        ImageView icon, overlay;
        ProgressBar progress;

        public myViewHolder(View itemView) {
            super(itemView);
            selected = false;
            icon = itemView.findViewById(R.id.ListIcon);
            overlay = itemView.findViewById(R.id.selected_view);
            progress = itemView.findViewById(R.id.image_loading_progress);
            itemView.setOnClickListener(this);
            itemView.getLayoutParams().height = w;
        }

        @Override
        public void onClick(View view) {
            if (overlay.getVisibility() == View.GONE && CartHolder.getInstance().getSize(key) < maxP) {
                    progress.setVisibility(View.VISIBLE);
                    try {
                        GlideHelper.getBitmap(c, data.get(getAdapterPosition()).getUrl(), new RequestListener() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target target, boolean isFirstResource) {
                                progress.setVisibility(View.GONE);
                                Toast.makeText(c, "Unable To Fetch Full Size Image", Toast.LENGTH_SHORT).show();
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Object resource, Object model, Target target, DataSource dataSource, boolean isFirstResource) {
                                progress.setVisibility(View.GONE);
                                overlay.setVisibility(View.VISIBLE);
                                selectedImages.put(data.get(getAdapterPosition()).getUrl(), true);
                                CartHolder.getInstance().addImage(key, resource);
                                return false;
                            }
                        });
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

            } else if (overlay.getVisibility() == View.VISIBLE) {
                try {
                    GlideHelper.getBitmap(c, data.get(getAdapterPosition()).getUrl(), new RequestListener() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target target, boolean isFirstResource) {
                            Toast.makeText(c, "Unable To Remove", Toast.LENGTH_SHORT).show();
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Object resource, Object model, Target target, DataSource dataSource, boolean isFirstResource) {
                            selectedImages.remove(data.get(getAdapterPosition()).getUrl());
                            overlay.setVisibility(View.GONE);
                            CartHolder.getInstance().removeImage(key, resource);
                            selected = false;
                            return false;
                        }
                    });
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(c, "Max. No Of Images Selected", Toast.LENGTH_SHORT).show();
            }
        }

    }
}