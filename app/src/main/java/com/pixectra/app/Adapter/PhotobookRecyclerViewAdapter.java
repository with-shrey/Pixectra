package com.pixectra.app.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.pixectra.app.R;
import com.squareup.picasso.Picasso;

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
    public void onBindViewHolder(MyViewHolder holder, int position) {

        if (title[position] != null) {

           //<<-- setting image title
            holder.image_title.setText(title[position]);

        } else {
            Log.d("Recycler View ", "Empty Image title at : " + position);
        }

        if (image_links[position] != null) {

           //<-- loading image into view from links
            Picasso.with(mcontext).setLoggingEnabled(true);
            Picasso.with(mcontext).load(image_links[position]).into(holder.mimageview);

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

        public MyViewHolder(View itemView) {
            super(itemView);

            image_title = itemView.findViewById(R.id.image_title_poster);
            mimageview = itemView.findViewById(R.id.imageview_poster_recycler_view);


        }
    }
}
