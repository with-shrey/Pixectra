package com.pixectra.app.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.pixectra.app.Models.Myorders;
import com.pixectra.app.Models.subscription;
import com.pixectra.app.R;
import com.pixectra.app.Models.Address;
import com.pixectra.app.ShippingAddressForm;
import com.pixectra.app.Utils.SessionHelper;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by prashu on 07/02/2018.
 */
public class subscriptionAdapter extends RecyclerView.Adapter<subscriptionAdapter.viewHolder> {
    ArrayList arrayList;
    Context c;

    public subscriptionAdapter(Context context,ArrayList list) {
        this.arrayList=list;
        c=context;
    }

    public static class viewHolder extends RecyclerView.ViewHolder{
        private CardView cardView;
        Context context;
        public viewHolder(CardView Views) {
            super(Views);
            cardView=Views;
            this.context=Views.getContext();
        }
    }
    @Override
    public subscriptionAdapter.viewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView itemView = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_subscription, parent, false);

        return new viewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final viewHolder holder, final int position) {
        final subscription ord=(subscription) arrayList.get(position);
        CardView cardView=holder.cardView;
        TextView name=(TextView)cardView.findViewById(R.id.subs_id);
        name.setText("Subcription Id : "+ord.getId());
        TextView number=(TextView)cardView.findViewById(R.id.subs_number);
        number.setText(ord.getNumber());
        TextView title=(TextView)cardView.findViewById(R.id.subs_title);
        title.setText(ord.getTitle());
        TextView type=(TextView)cardView.findViewById(R.id.subs_type);
        type.setText(ord.getType());
        TextView amount=(TextView)cardView.findViewById(R.id.subs_amount);
        amount.setText("₹"+ord.getAmount().toString());
        TextView date=(TextView)cardView.findViewById(R.id.subs_date);
        date.setText(ord.getStarting_date());
    }


    @Override
    public int getItemCount() {
        return arrayList.size();
    }
}
