package com.pixectra.app.Adapter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.pixectra.app.Models.SubscriptionDetails;
import com.pixectra.app.R;

import java.util.ArrayList;

/**
 * Created by Suhail on 3/14/2018.
 */

public class SubscriptionAdaptor extends RecyclerView.Adapter<SubscriptionAdaptor.ViewHolder> {
    ArrayList<SubscriptionDetails> data;
    Context context;

    int[] colorsActive;
    int[] colorsInactive;
    Activity activity;
    int size;

    public SubscriptionAdaptor(ArrayList<SubscriptionDetails> data, Context context, Activity activity) {


        this.activity = activity;
        this.data = data;
        this.context = context;
        colorsActive = context.getResources().getIntArray(R.array.array_dot_active);
        colorsInactive = context.getResources().getIntArray(R.array.array_dot_inactive);

        size = data.size();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.subscription_card, parent, false);

        return new SubscriptionAdaptor.ViewHolder(view);
    }


    @Override
    public int getItemViewType(int position) {
//        ChangeIndicator changeIndicator = new ChangeIndicator(colorsActive, colorsInactive, context, activity, data.size());
//        changeIndicator.setdots(position);
        return super.getItemViewType(position);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.Title.setText(data.get(position).getTitle());
        holder.SubTitle.setText(data.get(position).getSub_title());
        holder.description.setText(Html.fromHtml(data.get(position).getDetails()));
        holder.index.setText(String.valueOf(position+1) + "/" + String.valueOf(size));

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView description;
        TextView Title;
        TextView SubTitle;
        Button purchaseButton;
        TextView index;

        public ViewHolder(View itemView) {
            super(itemView);
            index = itemView.findViewById(R.id.index_subscription_card);
            description = itemView.findViewById(R.id.Subscription_detail_TextView);
            Title = itemView.findViewById(R.id.Subscription_Title_TextView);
            SubTitle = itemView.findViewById(R.id.Subscription_Sub_Title_TextView);
            purchaseButton = itemView.findViewById(R.id.subscription_button);

        }
    }
}
