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
import com.pixectra.app.R;
import com.pixectra.app.Models.Address;
import com.pixectra.app.ShippingAddressForm;
import com.pixectra.app.Utils.SessionHelper;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by prashu on 07/02/2018.
 * adapter to use in order_placed activity
 */
public class MyorderAdapter extends RecyclerView.Adapter<MyorderAdapter.viewHolder> {
    ArrayList arrayList;
    Context c;

    public MyorderAdapter(Context context,ArrayList list) {
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
    public MyorderAdapter.viewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView itemView = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_order, parent, false);

        return new viewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final viewHolder holder, final int position) {
        final Myorders ord=(Myorders) arrayList.get(position);
        CardView cardView=holder.cardView;
        TextView name=(TextView)cardView.findViewById(R.id.Order_name);
        name.setText(ord.getName());
        TextView number=(TextView)cardView.findViewById(R.id.Order_number);
        number.setText("Order No "+ord.getid());
        TextView time=(TextView)cardView.findViewById(R.id.Order_time);
        time.setText(ord.getDate()+"  "+ord.getTime());
        TextView uploaded=(TextView)cardView.findViewById(R.id.Order_uploaded);
        uploaded.setText(ord.getUploaded());
        TextView amount=(TextView)cardView.findViewById(R.id.Order_amount);
        amount.setText("₹"+ord.getAmount().toString());
    }


    @Override
    public int getItemCount() {
        return arrayList.size();
    }
}
