package com.pixectra.app.Adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.pixectra.app.Models.Images;
import com.pixectra.app.R;

import java.util.Collections;
import java.util.List;

/**
 * Created by Compu1 on 09-Feb-18.
 */

public class ImageSelectAdapter extends  RecyclerView.Adapter<ImageSelectAdapter.myViewHolder> {
    private LayoutInflater inflater;
    List<Images> data = Collections.emptyList();
Context c;
    public ImageSelectAdapter(Context context, List<Images> data){
        inflater = LayoutInflater.from(context);
        this.data = data;
        c=context;
    }


    @Override
    public myViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_recycler_item, parent, false);
        return new myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final myViewHolder holder, int position) {

        Images current = data.get(position);
        Glide.with(c).load(current.getThumbnail()).listener(new RequestListener<Drawable>() {
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
        }).into(holder.icon);


    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class myViewHolder extends RecyclerView.ViewHolder {

        ImageView icon;
        ProgressBar progress;

        public myViewHolder(View itemView) {
            super(itemView);
            icon  = (ImageView)itemView.findViewById(R.id.ListIcon);
            progress  = itemView.findViewById(R.id.image_loading_progress);
        }
    }
}
