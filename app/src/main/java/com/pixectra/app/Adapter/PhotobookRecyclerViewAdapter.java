package com.pixectra.app.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.pixectra.app.ImageSelectActivity;
import com.pixectra.app.Models.Product;
import com.pixectra.app.R;
import com.pixectra.app.SubscriptionActivity;
import com.pixectra.app.Utils.CartHolder;
import com.pixectra.app.Utils.GlideHelper;
import com.pixectra.app.Utils.LogManager;
import com.pixectra.app.VideoSelection;

import java.util.ArrayList;
import java.util.Locale;


/**
 * Created by Suhail on 2/6/2018.
 */

public class PhotobookRecyclerViewAdapter extends RecyclerView.Adapter<PhotobookRecyclerViewAdapter.MyViewHolder> {


    int layout;
    Context mcontext;
    ArrayList<Product> list;
    int subscription;
    // subs variable add to recognise that the adapter is called for subscription fragment
    public PhotobookRecyclerViewAdapter(Context mcontext, int layout, ArrayList<Product> data,int subs) {
        if (mcontext != null && layout != 0)

        {
            list = data;
            this.mcontext = mcontext;        //<-- context
            this.layout = layout; //<-- layout
            this.subscription=subs;

        } else {
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
        Product product = list.get(position);
        if (product.getTitle() != null) {
            setPrice(holder.price, product.getPrice(), product.getPriceDesc());
            //<<-- setting image title
            holder.image_title.setText(product.getTitle());
            String max = "0";
            if (product.getMaxPics() == -1)
                max = mcontext.getString(R.string.infinite);
            else
                max = "" + product.getMaxPics();
            setPicsCount(holder.count, product.getMinPics() + "-" + max);
        } else {
            Log.d("Recycler View ", "Empty Image title at : " + position);
        }

        if (product.getUrl() != null) {
            GlideHelper.load(mcontext, product.getUrl(), holder.mimageview, holder.progress);
        } else {
            Log.d("Recycler View ", "Empty image link at : " + position);
        }

        if (product.getFalsePrice() != -1) {
            holder.falsePrice.setText(String.format(Locale.getDefault(), "%s %d", mcontext.getResources().getString(R.string.Rs), product.getFalsePrice()));
        }

    }

    void setPrice(TextView textView, int price, String text) {
        textView.setText(String.format(Locale.getDefault(), "%s %d/%s", mcontext.getResources().getString(R.string.Rs), price, Html.fromHtml(text)));
    }

    void setPicsCount(TextView textView, String count) {
        textView.setText(String.format(Locale.getDefault(), "%s PHOTOS", count));
    }
    @Override
    public int getItemCount() {
        return list.size();
    }

    public class
    MyViewHolder extends RecyclerView.ViewHolder {

        ImageView mimageview;
        TextView image_title, price, count, falsePrice;
        ProgressBar progress;

        public MyViewHolder(View itemView) {
            super(itemView);
            progress = itemView.findViewById(R.id.progress_bar_photobook);
            image_title = itemView.findViewById(R.id.image_title_poster);
            falsePrice = itemView.findViewById(R.id.false_price);
            price = itemView.findViewById(R.id.image_price_poster);
            count = itemView.findViewById(R.id.image_count_poster);
            mimageview = itemView.findViewById(R.id.imageview_poster_recycler_view);
            falsePrice.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(subscription!=1)//for image selection
                    {LogManager.viewContent(list.get(getAdapterPosition()).getId(), list.get(getAdapterPosition()).getTitle()
                            , list.get(getAdapterPosition()).getType());
                    CartHolder.getInstance().addDetails(list.get(getAdapterPosition()).getId(), list.get(getAdapterPosition()));
                    Intent intent = new Intent();
                    if (list.get(getAdapterPosition()).getTypeOfUpload() == 0)
                        intent = new Intent(mcontext, ImageSelectActivity.class);
                    else {
                        intent = new Intent(mcontext, VideoSelection.class);
                    }
                    intent.putExtra("minPics", list.get(getAdapterPosition()).getMinPics());
                    if (list.get(getAdapterPosition()).getMaxPics() == -1)
                        intent.putExtra("maxPics", Integer.MAX_VALUE);
                    else
                        intent.putExtra("maxPics", list.get(getAdapterPosition()).getMaxPics());
                    intent.putExtra("key", list.get(getAdapterPosition()).getId());
                    mcontext.startActivity(intent);}
                    else//for subscription
                    {
                      Intent intent=new Intent(mcontext, SubscriptionActivity.class);
                      mcontext.startActivity(intent);
                    }
                }
            });

        }
    }
}
