package com.pixectra.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.google.firebase.database.FirebaseDatabase;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

@SuppressWarnings("deprecation")
public class SplashScreenActivity extends AppCompatActivity {

    Thread mythread;
    private ImageView splashimage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath(getString(R.string.default_font_path))
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
        try {
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        }catch(Exception e){
            e.printStackTrace();
        }
        setContentView(R.layout.activity_splash_screen);
        splashimage = findViewById(R.id.splash_image);
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
