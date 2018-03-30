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
import com.pixectra.app.Adapter.subscriptionAdapter;
import com.pixectra.app.Models.subscription;
import com.pixectra.app.Utils.SessionHelper;

import java.util.ArrayList;

/**
 * Created by prashu on 3/17/2018.
 */

public class subscription_list extends AppCompatActivity {
    RecyclerView recyclerview;
    DatabaseReference dataref;
    FirebaseDatabase db;
    subscriptionAdapter adap;
    ArrayList<subscription> list;
    DatabaseReference ref;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.subscription_list);
        db = FirebaseDatabase.getInstance();
        ref = db.getReference("CommonData/" + new SessionHelper(this).getUid() + "/Subscriptions");
        Toolbar toolbar = findViewById(R.id.toolbar_subs);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Subscriptions");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        list = new ArrayList<>();
        adap = new subscriptionAdapter(this, list);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                list.clear();
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    subscription subscriptions = data.getValue(subscription.class);
                    assert subscriptions != null;
                    subscriptions.setKey(data.getKey());
                    list.add(subscriptions);
                }
                findViewById(R.id.progress_subs).setVisibility(View.GONE);
                adap.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                findViewById(R.id.progress_subs).setVisibility(View.GONE);
            }
        });
        RecyclerView recycler = findViewById(R.id.subs_recycler);
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
