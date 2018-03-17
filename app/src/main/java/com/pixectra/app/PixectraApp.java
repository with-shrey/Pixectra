package com.pixectra.app;

import android.annotation.SuppressLint;
import android.app.Application;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.answers.Answers;

import io.branch.referral.Branch;
import io.fabric.sdk.android.Fabric;

@SuppressLint("Registered")
public class PixectraApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics(), new Answers());
        Branch.getAutoInstance(this);
    }
}
