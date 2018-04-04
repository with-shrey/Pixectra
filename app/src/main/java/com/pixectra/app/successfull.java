package com.pixectra.app;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by prashu on 4/4/2018.
 */

public class successfull extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.successfull);
        View view = findViewById(R.id.successimage);
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.enlarging);
        Button button = (Button) findViewById(R.id.successupload);
        TextView transaction_id=(TextView)findViewById(R.id.success_tansaction_id);
        TextView transaction_amount=(TextView)findViewById(R.id.successAmount);

                view.startAnimation(anim);
            }

    }

