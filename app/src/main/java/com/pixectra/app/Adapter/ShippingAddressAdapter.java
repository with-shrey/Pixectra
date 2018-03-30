package com.pixectra.app.Adapter;

import android.app.Activity;
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
import com.pixectra.app.Models.Address;
import com.pixectra.app.R;
import com.pixectra.app.SelectAddressActivity;
import com.pixectra.app.ShippingAddressForm;
import com.pixectra.app.Utils.SessionHelper;

import java.util.ArrayList;

/**
 * Created by prashu on 07/02/2018.
 */
public class ShippingAddressAdapter extends RecyclerView.Adapter<ShippingAddressAdapter.viewHolder> {
    ArrayList arrayList;
    Context c;
Intent intent;
    public ShippingAddressAdapter(Context context,ArrayList list,Intent intent) {
       this.arrayList=list;
       c=context;
       this.intent=intent;
    }

    @Override
    public ShippingAddressAdapter.viewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView itemView = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_shipping, parent, false);

        return new viewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final viewHolder holder, final int position) {
         final Address add=(Address)arrayList.get(position);
        CardView cardView=holder.cardView;
        TextView name = cardView.findViewById(R.id.card_name);
        name.setText(add.getName());
        TextView ward = cardView.findViewById(R.id.card_ard);
        ward.setText(add.getAddNo()+","+add.getStreet());
        TextView fulladd = cardView.findViewById(R.id.card_full_add);
        String fulladdress=add.getCity()+"-"+add.getPincode()+" , "+add.getState();
        fulladd.setText(fulladdress);
        TextView mobile = cardView.findViewById(R.id.card_mobile);
        mobile.setText("Mobile:"+add.getMobile());

        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Intent intent= new Intent(holder.context,ShippingAddressForm.class);
               Bundle b=new Bundle();
               b.putSerializable("add",add);
               intent.putExtras(b);
               intent.putExtra("status",0);//0 indicates that ShippingAddressForm calls from edit button
                holder.context.startActivity(intent);
            }

        });
        holder.remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 //firebase code to remove the selected address
                Toast.makeText(holder.context,"Removed",Toast.LENGTH_SHORT).show();
                FirebaseDatabase db= FirebaseDatabase.getInstance();
                DatabaseReference ref=db.getReference("Users/"+new SessionHelper(c).getUid()+"/ShippingAddress");
                ref.child(add.getKey()).setValue(null);
                arrayList.remove(position);
                notifyItemRemoved(position);

                //remove

            }
        });

        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               if(intent.getBooleanExtra("select",false)){
                   Intent intent1=new Intent();
                   Bundle b=new Bundle();
                   b.putSerializable("address",add);
                   intent1.putExtras(b);
                   ((SelectAddressActivity)c).setResult(Activity.RESULT_OK,intent1);
                   ((SelectAddressActivity)c).finish();

               }
                Toast.makeText(holder.context,"click on card",Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public static class viewHolder extends RecyclerView.ViewHolder {
        Button edit, remove;
        Context context;
        private CardView cardView;

        public viewHolder(CardView Views) {
            super(Views);
            cardView = Views;
            this.edit = Views.findViewById(R.id.card_edit);
            this.remove = Views.findViewById(R.id.card_remove);
            this.context = Views.getContext();
        }
    }
}
