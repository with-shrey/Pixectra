package com.pixectra.app;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Pair;
import android.view.View;
import android.widget.Button;

import com.pixectra.app.Utils.CartHolder;


/**
 * Created by prashu on 4/4/2018.
 */

public class VideoSelection extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vedioupload);
        Button btn = findViewById(R.id.uploadvedio);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("video/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Video"), 1);
            }
        });
    }
                public void onActivityResult(int requestCode, int resultCode, Intent data) {
                    if (resultCode == RESULT_OK) {
                        if (requestCode == 1) {
                            Uri selectedImageUri = data.getData();
                            if (selectedImageUri != null) {
                                CartHolder.getInstance().getVideo().add(new Pair<>(CartHolder.getInstance().getDetails
                                        (getIntent().getStringExtra("key")), selectedImageUri));
                                Intent intent = new Intent(this, Checkout.class);
                                startActivity(intent);
                                finish();
                            }
                        }
                    }
                }

            }



