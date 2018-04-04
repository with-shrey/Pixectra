package com.pixectra.app.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.pixectra.app.Models.SubscriptionDetails;
import com.pixectra.app.R;
import com.pixectra.app.ShippingAddressForm;
import com.pixectra.app.successfull;

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


    public SubscriptionAdaptor(ArrayList<SubscriptionDetails> data, Context context, Activity activity) {


        this.activity = activity;
        this.data = data;
        this.context = context;
        colorsActive = context.getResources().getIntArray(R.array.array_dot_active);
        colorsInactive = context.getResources().getIntArray(R.array.array_dot_inactive);
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
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        holder.Title.setText(data.get(position).getTitle());
        holder.Type.setText(data.get(position).getType());
        holder.description.setText(Html.fromHtml(data.get(position).getDesc()));
        holder.index.setText(String.valueOf(position+1) + "/" + String.valueOf(data.size()));
        holder.price.setText("₹ "+data.get(position).getPrice()+"/"+data.get(position).getNo_of_books()+" books");
        holder.purchaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(holder.c0,successfull.class);
                holder.c0.startActivity(intent);
            }

        });

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView description;
        TextView Title;
        TextView Type;
        Button purchaseButton;
        TextView price;
        TextView index;
        Context c0;

        public ViewHolder(final View itemView) {
            super(itemView);
            index = itemView.findViewById(R.id.index_subscription_card);
            description = itemView.findViewById(R.id.Subscription_detail_TextView);
            Title = itemView.findViewById(R.id.Subscription_Title_TextView);
            Type = itemView.findViewById(R.id.Subscription_type);
            purchaseButton = itemView.findViewById(R.id.subscription_button);
            price=itemView.findViewById(R.id.subscription_Price);
            this.c0 = itemView.getContext();

        }
    }
}
