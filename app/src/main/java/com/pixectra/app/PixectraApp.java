package com.pixectra.app;

import android.annotation.SuppressLint;
import android.app.Application;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.answers.Answers;
import com.google.firebase.database.FirebaseDatabase;

import io.branch.referral.Branch;
import io.fabric.sdk.android.Fabric;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

@SuppressLint("Registered")
public class PixectraApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        try {
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath(getString(R.string.default_font_path))
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
        Fabric.with(this, new Crashlytics(), new Answers());
        Branch.getAutoInstance(this);
    }
}
