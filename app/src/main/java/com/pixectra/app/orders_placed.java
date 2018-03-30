package com.pixectra.app;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pixectra.app.Adapter.MyorderAdapter;
import com.pixectra.app.Models.Myorders;
import com.pixectra.app.Utils.SessionHelper;

import java.util.ArrayList;

/**
 * Created by prashu on 3/17/2018.
 */

public class orders_placed extends AppCompatActivity {
    RecyclerView recyclerview;
    DatabaseReference dataref;
    FirebaseDatabase db;
    MyorderAdapter adap;
    ArrayList<Myorders> list;
    DatabaseReference ref;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_placed);
        db = FirebaseDatabase.getInstance();
        ref = db.getReference("Users/" + new SessionHelper(this).getUid() + "/orders");
        Toolbar toolbar = findViewById(R.id.toolbar_order);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("My Orders");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        list = new ArrayList<>();
        adap = new MyorderAdapter(this, list);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                list.clear();
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    Myorders myorders = data.getValue(Myorders.class);
                    assert myorders != null;
                    myorders.setKey(data.getKey());
                    list.add(myorders);
                }
                findViewById(R.id.progress_order).setVisibility(View.GONE);
                adap.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                findViewById(R.id.progress_order).setVisibility(View.GONE);
            }
        });
        RecyclerView recycler = findViewById(R.id.orders_recycler);
        recycler.setAdapter(adap);
        LinearLayoutManager layout = new LinearLayoutManager(getApplicationContext());
        recycler.setLayoutManager(layout);

         /*   }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });*/
    }

}
