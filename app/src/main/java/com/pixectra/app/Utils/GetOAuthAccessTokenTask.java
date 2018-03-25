package com.pixectra.app.Utils;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.pixectra.app.Fragments.ImageFragment;

import java.io.IOException;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by Sanath on 2/24/2018.
 */

public class GetOAuthAccessTokenTask extends AsyncTask<Void,Void,Void> {

    private ImageFragment imageFragment;
    private GoogleSignInAccount account;
    private String accessToken;

    public GetOAuthAccessTokenTask(ImageFragment imageFragment, GoogleSignInAccount account)
    {
        this.imageFragment = imageFragment;
        this.account = account;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        if(accessToken!= null)
        imageFragment.getPicasaImagesJSON(accessToken);
        else
            Log.e("Error Retrieving token", "Oauth token was not retrieved");
    }

    @Override
    protected Void doInBackground(Void... voids) {
        try {
            String scope = "oauth2:https://picasaweb.google.com/data/";
            accessToken = GoogleAuthUtil.getToken(getApplicationContext(), account.getAccount(), scope, new Bundle());
            Log.d("accessToken", accessToken);//accessToken:ya29.Gl...

        } catch (IOException | GoogleAuthException e) {
            e.printStackTrace();
        }
        return null;
    }
}
