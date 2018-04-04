package com.pixectra.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.pixectra.app.Models.Myorders;
import com.pixectra.app.Utils.CartHolder;
import com.pixectra.app.Utils.ImageController;
import com.pixectra.app.Utils.SessionHelper;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by prashu on 4/4/2018.
 */

public class PaymentStatus extends AppCompatActivity {
    String key = "";
    ImageController uploader;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.successfull);
        uploader = new ImageController(key, PaymentStatus.this, getWindow());
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.enlarging);
        Button button = findViewById(R.id.successupload);
        TextView transaction_id = findViewById(R.id.success_tansaction_id);
        TextView status = findViewById(R.id.status_text);
        TextView transaction_amount = findViewById(R.id.successAmount);
        ImageView image = findViewById(R.id.successimage);
        boolean txnstatus = getIntent().getBooleanExtra("status", false);
        String trxnid = getIntent().getStringExtra("transaction_id");
        double amount = getIntent().getDoubleExtra("amount", 0.0);
        String uid = getIntent().getStringExtra("id");
        boolean onetime = getIntent().getBooleanExtra("isOneTime", true);
        transaction_id.setText(trxnid);
        if (txnstatus) {
            Glide.with(this).load(R.drawable.failed).into(image);
            status.setText("Successfull");
            status.setTextColor(ContextCompat.getColor(this, android.R.color.holo_green_dark));
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH-mm", Locale.getDefault());
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
            FirebaseDatabase db = FirebaseDatabase.getInstance();
            DatabaseReference ref = db.getReference("Users/" + new SessionHelper(this).getUid() + "/orders");
            key = ref.push().getKey();
            ref.child(key).setValue(new Myorders("", trxnid, timeFormat.format(new Date()), dateFormat.format(new Date()), false, amount));
            if (onetime) {
                button.setVisibility(View.VISIBLE);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        uploader
                                .placeOrder(CartHolder.getInstance().getCheckout());
                    }
                });
            } else {
                button.setText("Countinue Shopping");
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(PaymentStatus.this, MainActivity.class);
                        startActivity(intent);
                        finishAffinity();
                    }
                });
            }
        } else {
            Glide.with(this).load(R.drawable.successfull).into(image);
            status.setText("Failed");
            status.setTextColor(ContextCompat.getColor(this, R.color.colorAccent));

        }
        image.startAnimation(anim);

    }

    }

