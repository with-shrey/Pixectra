package com.pixectra.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

@SuppressWarnings("deprecation")
public class SplashScreenActivity extends AppCompatActivity {

    private ImageView splashimage;
    Thread mythread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        splashimage = (ImageView) findViewById(R.id.splash_image);
        new Thread() {
            @Override
            public void run() {
                try {
                    sleep(3000);
                    Intent intent = new Intent(getApplicationContext(), WelcomeActivity.class);
                    startActivity(intent);
                    finish();
                } catch (InterruptedException e) {
                    e.printStackTrace();

                }
            }
        }.start();


    }

// called after oncreate method is created.
    @Override
    protected void onResume() {
        super.onResume();

    }


}
