package com.pixectra.app.Adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pixectra.app.Models.Myorders;
import com.pixectra.app.R;

import java.util.ArrayList;

/**
 * Created by prashu on 07/02/2018.
 * adapter to use in order_placed activity
 */
public class MyorderAdapter extends RecyclerView.Adapter<MyorderAdapter.viewHolder> {
    ArrayList arrayList;
    Context c;

    public MyorderAdapter(Context context, ArrayList list) {
        this.arrayList = list;
        c = context;
    }

    @Override
    public MyorderAdapter.viewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView itemView = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_order, parent, false);

        return new viewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final viewHolder holder, final int position) {
        final Myorders ord = (Myorders) arrayList.get(position);
        CardView cardView = holder.cardView;
        TextView name = cardView.findViewById(R.id.Order_name);
        name.setText(ord.getName());
        TextView number = cardView.findViewById(R.id.Order_number);
        number.setText("Order No " + ord.getid());
        TextView time = cardView.findViewById(R.id.Order_time);
        time.setText(ord.getDate() + "  " + ord.getTime());
        TextView uploaded = cardView.findViewById(R.id.Order_uploaded);
        uploaded.setText(ord.getUploaded());
        TextView amount = cardView.findViewById(R.id.Order_amount);
        amount.setText("₹" + ord.getAmount().toString());
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public static class viewHolder extends RecyclerView.ViewHolder {
        Context context;
        private CardView cardView;

        public viewHolder(CardView Views) {
            super(Views);
            cardView = Views;
            this.context = Views.getContext();
        }
    }
}
