package com.pixectra.app.Utils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

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
    private static final String User_Email = "USER_EMAIL";

    // Password (make variable public to access from outside)
    public static final String User_Password = "USER_PASSWORD";


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
        user.put(User_Password, sharedPreferences.getString(User_Password, null));

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

    }


}
