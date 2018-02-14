package com.pixectra.app;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;

public class LinkedAccounts extends AppCompatActivity {
CardView facebook;
CardView google;
CardView insta;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.linked_accounts);
    }
}
