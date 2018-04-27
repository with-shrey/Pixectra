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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.pixectra.app.Models.Coupon;
import com.pixectra.app.Models.Myorders;
import com.pixectra.app.Utils.CartHolder;
import com.pixectra.app.Utils.LogManager;
import com.pixectra.app.Utils.SessionHelper;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import io.branch.referral.Branch;

/**
 * Created by prashu on 4/4/2018.
 */

public class PaymentStatus extends AppCompatActivity {
    String key = "";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.successfull);
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            new SessionHelper(getApplicationContext()).logOutUser();
            startActivity(new Intent(this, LActivity.class));
            finish();
            return;
        }
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.enlarging);
        Button button = findViewById(R.id.successupload);
        TextView transaction_id = findViewById(R.id.success_tansaction_id);
        TextView status = findViewById(R.id.status_text);
        TextView transaction_amount = findViewById(R.id.successAmount);
        ImageView image = findViewById(R.id.successimage);
        boolean txnstatus = getIntent().getBooleanExtra("status", false);
        String trxnid = getIntent().getStringExtra("transaction_id");
        double amount = getIntent().getDoubleExtra("amount", 0.0);
        String uid = new SessionHelper(this).getUid();
        boolean onetime = getIntent().getBooleanExtra("isOneTime", true);
        transaction_id.setText(trxnid);
        transaction_amount.setText(getString(R.string.Rs) + " " + amount);
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH-mm", Locale.getDefault());
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference ref = db.getReference("Users/" + uid + "/orders");
        key = ref.push().getKey();
        ref.child(key).setValue(new Myorders("", trxnid, timeFormat.format(new Date())
                , dateFormat.format(new Date()), false, txnstatus, amount));
        if (txnstatus) {
            final DatabaseReference used = FirebaseDatabase.getInstance().getReference("Users").child(new SessionHelper(this).getUid())
                    .child("Used");
            final DatabaseReference earned = FirebaseDatabase.getInstance().getReference("Users").child(new SessionHelper(this).getUid())
                    .child("Earned");

            Branch.getInstance().userCompletedAction("order");
            Coupon coupon = CartHolder.getInstance().getCoupon();
            used.child(coupon.getCouponCode()).setValue(coupon);
            LogManager.couponUsed(coupon.getCouponCode(), new SessionHelper(this).getUid());
            earned.child(coupon.getCouponCode()).setValue(null);
            if (CartHolder.getInstance().getCreditsUsed() > 0)
                Branch.getInstance().redeemRewards(CartHolder.getInstance().getCreditsUsed());

            CartHolder.getInstance().setDiscount(null);
            CartHolder.getInstance().setCoupon(null);
            CartHolder.getInstance().setCreditsUsed(0);

            Glide.with(this).load(R.drawable.successfull).into(image);
            status.setText("Successfull");
            status.setTextColor(ContextCompat.getColor(this, android.R.color.holo_green_dark));

            if (onetime) {
                button.setVisibility(View.VISIBLE);
                button.setText("    Starting Upload In Background \nDo Not Clear App From Recents");
                Intent intent = new Intent(getApplicationContext(), UploadService.class);
                intent.putExtra("key", key);
                startService(intent);

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
            button.setVisibility(View.GONE);
            Glide.with(this).load(R.drawable.failed).into(image);
            status.setText("Failed");
            status.setTextColor(ContextCompat.getColor(this, R.color.colorAccent));

        }
        image.startAnimation(anim);

    }

    }

