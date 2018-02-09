package com.pixectra.app;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by prashu on 07/02/2018.
 */
public class adapter extends RecyclerView.Adapter<adapter.viewHolder> {
    ArrayList arrayList;

    public adapter(ArrayList list) {
       this.arrayList=list;
    }

    public static class viewHolder extends RecyclerView.ViewHolder{
        private CardView cardView;
        Button edit,remove;
        Context context;
        public viewHolder(CardView Views) {
            super(Views);
            cardView=Views;
            this.edit=(Button)Views.findViewById(R.id.card_edit);
            this.remove=(Button)Views.findViewById(R.id.card_remove);
            this.context=Views.getContext();
        }
    }
    @Override
    public adapter.viewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView itemView = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_shipping, parent, false);

        return new viewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final viewHolder holder, int position) {
         final shipaddClass add=(shipaddClass)arrayList.get(position);
        CardView cardView=holder.cardView;
        TextView name=(TextView)cardView.findViewById(R.id.card_name);
        name.setText(add.name);
        TextView ward=(TextView)cardView.findViewById(R.id.card_ard);
        ward.setText(add.addNo+","+add.street);
        TextView fulladd=(TextView)cardView.findViewById(R.id.card_full_add);
        String fulladdress=add.city+"-"+add.pincode+" , "+add.state;
        fulladd.setText(fulladdress);
        TextView mobile=(TextView)cardView.findViewById(R.id.card_mobile);
        mobile.setText("Mobile:"+add.mobile);

        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Intent intent= new Intent(holder.context,shipping_address.class);
               Bundle b=new Bundle();
               b.putSerializable("add",add);
               intent.putExtras(b);
               intent.putExtra("status",0);//0 indicates that shipping_address calls from edit button
                holder.context.startActivity(intent);
            }

        });
        holder.remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 //firebase code to remove the selected address
                Toast.makeText(holder.context,"Removed",Toast.LENGTH_SHORT).show();

                //remove

            }
        });

        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //add code to set shipping address
                Toast.makeText(holder.context,"click on card",Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public int getItemCount() {
        return arrayList.size();
    }
}
