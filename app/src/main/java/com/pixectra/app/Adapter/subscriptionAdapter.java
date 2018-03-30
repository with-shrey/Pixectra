package com.pixectra.app.Adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pixectra.app.Models.subscription;
import com.pixectra.app.R;

import java.util.ArrayList;

/**
 * Created by prashu on 07/02/2018.
 */
public class subscriptionAdapter extends RecyclerView.Adapter<subscriptionAdapter.viewHolder> {
    ArrayList arrayList;
    Context c;

    public subscriptionAdapter(Context context, ArrayList list) {
        this.arrayList = list;
        c = context;
    }

    @Override
    public subscriptionAdapter.viewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView itemView = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_subscription, parent, false);

        return new viewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final viewHolder holder, final int position) {
        final subscription ord = (subscription) arrayList.get(position);
        CardView cardView = holder.cardView;
        TextView name = cardView.findViewById(R.id.subs_id);
        name.setText("Subcription Id : " + ord.getId());
        TextView number = cardView.findViewById(R.id.subs_number);
        number.setText(ord.getNumber());
        TextView title = cardView.findViewById(R.id.subs_title);
        title.setText(ord.getTitle());
        TextView type = cardView.findViewById(R.id.subs_type);
        type.setText(ord.getType());
        TextView amount = cardView.findViewById(R.id.subs_amount);
        amount.setText("₹" + ord.getAmount().toString());
        TextView date = cardView.findViewById(R.id.subs_date);
        date.setText(ord.getStarting_date());
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
