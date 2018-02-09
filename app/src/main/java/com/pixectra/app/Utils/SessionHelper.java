package com.pixectra.app.Utils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;

import com.google.firebase.auth.FirebaseAuth;
import com.pixectra.app.MainActivity;

import java.util.HashMap;

/**
 * Created by Suhail on 2/6/2018.
 */

public class SessionHelper extends MainActivity {

    SharedPreferences sharedPreferences;

    //Editor for shared preferences..
    SharedPreferences.Editor editor;
    Context context;

    int PRIVATE_MODE = 0;
    private static final String PREF_NAME = "LoginSharedPreferences";

    // All Shared Preferences Keys

    private static final String IS_LOGIN = "IsLoggedIn";

    // Email address (make variable public to access from outside)
    public static final String User_Email = "USER_EMAIL";
    public static final String User_Image = "USER_Image";
    public static final String User_Name = "USER_NAME";
    public static final String User_Uid = "USER_UID";

    // Password (make variable public to access from outside)
    public static final String User_Password = "USER_PASSWORD";
    private static final String IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch";


    /**
     * Constructor to initialise
     *
     * @param context
     */

    public SessionHelper(Context context) {
        this.context = context;
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        editor = sharedPreferences.edit();
    }
    public void setFirstTimeLaunch(boolean isFirstTime) {
        editor.putBoolean(IS_FIRST_TIME_LAUNCH, isFirstTime);
        editor.commit();
    }

    public boolean isFirstTimeLaunch() {
        return sharedPreferences.getBoolean(IS_FIRST_TIME_LAUNCH, true);
    }
    /**
     * @param email
     * @param password
     */

    public void createLoginSession(String email, String password) {
        editor.putString(User_Email, email);
        editor.putString(User_Password, password);
        editor.apply();

    }

    /**
     * HashMap to get the user currently logged in
     *
     * @return
     */

    public HashMap<String, String> getUserDetails() {

        HashMap<String, String> user = new HashMap<String, String>();

        // user Email
        user.put(User_Email, sharedPreferences.getString(User_Email, null));

        // user password
        user.put(User_Image, sharedPreferences.getString(User_Image, null));
        user.put(User_Name, sharedPreferences.getString(User_Name, null));
        user.put(User_Uid, sharedPreferences.getString(User_Uid, null));

        // return user
        return user;

    }

    /**
     * To return login status
     *
     * @return
     */

    public boolean isLoggedIn() {

        if (sharedPreferences != null) {
            return sharedPreferences.getBoolean(IS_LOGIN, false);
        }
        return false;

    }


    /* to access password
  *@param null
  */
    public String getUser_Password() {

        if (sharedPreferences != null) {
            return sharedPreferences.getString(User_Password, null);

        }

        return null;
    }


    /* to access email
     *@param null
     */
    public String getUser_Email() {

        if (sharedPreferences != null) {
            return sharedPreferences.getString(User_Email, null);

        }

        return null;
    }

    /**
     * Check if no user logged in after starting main activity and if not logged in send to the login activity
     */

    public void checkLoginStatus() {
        if(!isLoggedIn()) {


            Intent i = new Intent(context,

                    MainActivity.class   //go to login activity if user has not logged in

                    // replace MainActivity with login activity
            );

            //Clear all the previous activities if user not logged in
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            // Add new Flag to start new Activity
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            context.startActivity(i);
        }
    }



    /**
     * While Signing out user
     */
    public void logOutUser() {

        //Clearing all data from the shared preferences
        editor.clear();
        editor.commit();
        FirebaseAuth.getInstance().signOut();

    }
    public void setUserDetails(String uid,String name,String email,Uri url){
        editor.putString(User_Image, url.toString());
        editor.putString(User_Name, name);
        editor.putString(User_Uid, uid);
        editor.putString(User_Email, email);
        editor.apply();
    }

    public String getUid(){
        return sharedPreferences.getString(User_Uid,null);
    }

}
