package com.pixectra.app.Adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.pixectra.app.R;


/**
 * Created by Suhail on 2/6/2018.
 */

public class PhotobookRecyclerViewAdapter extends RecyclerView.Adapter<PhotobookRecyclerViewAdapter.MyViewHolder> {

    public String[] title;
    int layout;
    Context mcontext;
    public String[] image_links;


    public PhotobookRecyclerViewAdapter(Context mcontext, int layout, String[] image_links, String[] title) {
        if (image_links != null && mcontext != null && title != null && layout != 0)

        {
            this.image_links = image_links;  //<-- image links
            this.mcontext = mcontext;        //<-- context
            this.layout = layout;            //<-- layout
            this.title = title;              //<-- image title
        }
        else
        {
            Log.d("PhtbkRcyclrViewAdapter", "Empty Constructor Params");
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        if (title[position] != null) {

           //<<-- setting image title
            holder.image_title.setText(title[position]);

        } else {
            Log.d("Recycler View ", "Empty Image title at : " + position);
        }

        if (image_links[position] != null) {
            Glide.with(mcontext).load(image_links[position]).listener(new RequestListener<Drawable>() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                    return false;
                }

                @Override
                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                    holder.progress.setVisibility(View.GONE);
                    return false;
                }
            }).into(holder.mimageview);

        } else {
            Log.d("Recycler View ", "Empty image link at : " + position);
        }

    }

    @Override
    public int getItemCount() {
        return image_links.length;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView mimageview;
        TextView image_title;
        ProgressBar progress;
        public MyViewHolder(View itemView) {
            super(itemView);
            progress=itemView.findViewById(R.id.progress_bar_photobook);
            image_title = itemView.findViewById(R.id.image_title_poster);
            mimageview = itemView.findViewById(R.id.imageview_poster_recycler_view);


        }
    }
}
