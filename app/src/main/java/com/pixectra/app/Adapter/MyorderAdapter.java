package com.pixectra.app.Adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
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
    boolean onlyUploaded;

    public MyorderAdapter(Context context, ArrayList list, boolean onlyUploaded) {
        this.arrayList = list;
        c = context;
        this.onlyUploaded = onlyUploaded;
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
        if (onlyUploaded) {
            if ((!ord.isSuccess() || ord.isUploaded())) {
                cardView.setVisibility(View.GONE);
            }
        } else {
            if (ord.isSuccess())
                cardView.setCardBackgroundColor(ContextCompat.getColor(holder.context, R.color.userProfileCardViewBlue));
            else
                cardView.setCardBackgroundColor(ContextCompat.getColor(holder.context, R.color.colorAccent));
            TextView name = cardView.findViewById(R.id.Order_name);
            if (ord.getfKey() != null)
                name.setText(ord.getfKey());
            else {
                name.setText(" ------ ");
            }
            ImageView imageView = cardView.findViewById(R.id.Order_image);
            if (ord.isSuccess())
                Glide.with(holder.context).load(R.drawable.successfull).into(imageView);
            else
                Glide.with(holder.context).load(R.drawable.failed).into(imageView);
            TextView number = cardView.findViewById(R.id.Order_number);
            number.setText("TxnID " + ord.getPayId());
            TextView time = cardView.findViewById(R.id.Order_time);
            time.setText(ord.getDate() + "  " + ord.getTime());
            TextView uploaded = cardView.findViewById(R.id.Order_uploaded);
            uploaded.setText(ord.isUploaded() ? "Uploaded" : "Not Uploaded");
            TextView amount = cardView.findViewById(R.id.Order_amount);
            amount.setText("₹" + ord.getAmount().toString());
        }
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        Context context;
        private CardView cardView;

        public viewHolder(CardView Views) {
            super(Views);
            cardView = Views;
            this.context = Views.getContext();
            cardView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse("mailto:pixectra@gmail.com")); // only email apps should handle this
            intent.putExtra(Intent.EXTRA_EMAIL, "pixectra@gmail.com");
            intent.putExtra(Intent.EXTRA_SUBJECT, "SUPPORT:");
            intent.putExtra(Intent.EXTRA_TEXT, arrayList.get(getAdapterPosition()).toString()
                    + "\n UID:" + FirebaseAuth.getInstance().getCurrentUser().getUid());
            if (intent.resolveActivity(MyorderAdapter.this.c.getPackageManager()) != null) {
                MyorderAdapter.this.c.startActivity(Intent.createChooser(intent, "Interact using..."));
            }
        }
    }
}
