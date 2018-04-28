package com.pixectra.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;

import io.branch.indexing.BranchUniversalObject;
import io.branch.referral.Branch;
import io.branch.referral.BranchError;
import io.branch.referral.InstallListener;
import io.branch.referral.util.LinkProperties;

@SuppressWarnings("deprecation")
public class SplashScreenActivity extends AppCompatActivity {

    Thread mythread;
    private ImageView splashimage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        InstallListener listener = new InstallListener();
        listener.onReceive(getApplicationContext(), getIntent());

        //setContentView(R.layout.activity_splash_screen);
        new Thread() {
            @Override
            public void run() {
                try {
                    sleep(2000);
                    Intent intent = new Intent(getApplicationContext(), WelcomeActivity.class);
                    startActivity(intent);
                    finish();
                } catch (InterruptedException e) {
                    e.printStackTrace();

                }
            }
        }.start();


    }

    @Override
    protected void onStart() {
        super.onStart();
        Branch branch = Branch.getInstance();
        branch.initSession(new Branch.BranchUniversalReferralInitListener() {
            @Override
            public void onInitFinished(BranchUniversalObject branchUniversalObject, LinkProperties linkProperties, BranchError error) {
                if (error == null && branchUniversalObject != null) {
                    // This code will execute when your app is opened from a Branch deep link, which
                    // means that you can route to a custom activity depending on what they clicked.
                    // In this example, we'll just print out the data from the link that was clicked.

                    Log.i("BranchTestBed", "title " + branchUniversalObject.getTitle());
                    Log.i("ContentMetaData", "metadata " + branchUniversalObject.getMetadata());
                }
            }
        }, this.getIntent().getData(), this);
    }


    @Override
    public void onNewIntent(Intent intent) {
        this.setIntent(intent);
    }
// called after oncreate method is created.
    @Override
    protected void onResume() {
        super.onResume();

    }


}
