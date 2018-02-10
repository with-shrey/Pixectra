package com.pixectra.app;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pixectra.app.Models.User;
import com.pixectra.app.Utils.SessionHelper;

import java.util.Arrays;

public class LActivity extends AppCompatActivity {
    private ImageView imageView, facebookimageview1;
    String fFirstName,fLastName, fEmail;
    Uri fImageurl;
    String gpersonName, gpersonEmail;
    Uri gImageUrl;
    int RC_SIGN_IN = 1;
    Dialog errorDialog;
    private static final String TAG = "HANDLESIGNINRESULT";
    CallbackManager mCallbackManager;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    GoogleSignInClient mGoogleSignInClient;
    ProgressBar progressBar;
FirebaseDatabase db;
DatabaseReference ref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_l);
        db=FirebaseDatabase.getInstance();
        ref=db.getReference("Users");
        mAuth = FirebaseAuth.getInstance();
        imageView = (ImageView) findViewById(R.id.google_login_button);
        facebookimageview1 = (ImageView) findViewById(R.id.facebook_login_button);
        progressBar=(ProgressBar)findViewById(R.id.simpleProgressBar);
        mCallbackManager = CallbackManager.Factory.create();
        FacebookSdk.sdkInitialize(getApplicationContext());
        LoginManager.getInstance().registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d("Login", "Login sucess");
                firebaseAuthWithFacebook(loginResult.getAccessToken());
               // Toast.makeText(LActivity.this,fFirstName,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancel() {
                Toast.makeText(LActivity.this, "Login cancelled", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(getApplicationContext(), "" + error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("802114397478-121sl0s0bfr0ts2ojchta7b22lc98q5n.apps.googleusercontent.com")
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.google_login_button:
                        if (checkPlayServices())
                            signIn();
                        break;
                    // ...
                }
            }
        });
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    updateUI(user);
                 Log.d("Tag","user already Exists");
                }
            }
        };

    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    public void onFacebookLogInClicked(View view) {
        LoginManager
                .getInstance()
                .logInWithReadPermissions(
                        this,
                        Arrays.asList("public_profile", "user_friends", "email","user_photos")
                );
        progressBar.setVisibility(View.VISIBLE);
    }

    private void firebaseAuthWithFacebook(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);

        final AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());
                        if (task.isSuccessful()) {
                        boolean isNew = task.getResult().getAdditionalUserInfo().isNewUser();
                        Log.d("Facebook Sign In", "onComplete: " + (isNew ? "new user" : "old user"));
                        Toast.makeText(LActivity.this, "onComplete: " + (isNew ? "new user" : "old user"), Toast.LENGTH_SHORT).show();
                        if (isNew) {
                            // mAuth.getCurrentUser().getUid();
                            Profile profile=Profile.getCurrentProfile();
                            fFirstName=profile.getFirstName();
                            fLastName=profile.getLastName();
                            fEmail=mAuth.getCurrentUser().getEmail();
                            fImageurl=profile.getProfilePictureUri(400,400);
                            ref.child(mAuth.getCurrentUser().getUid()).child("Info").setValue(new User(fFirstName+" "+fLastName,fEmail,fImageurl.toString()));
                            new SessionHelper(LActivity.this).setUserDetails(mAuth.getCurrentUser().getUid()
                                    ,fFirstName+" "+fLastName
                                    ,fEmail
                                    ,fImageurl);
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                            Toast.makeText(LActivity.this, fFirstName+fEmail, Toast.LENGTH_SHORT).show();
                        }else{
                            ref.child(mAuth.getCurrentUser().getUid()).child("Info").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    User user=dataSnapshot.getValue(User.class);
                                    new SessionHelper(LActivity.this).setUserDetails(mAuth.getCurrentUser().getUid()
                                            ,user.getName()
                                            ,user.getEmail()
                                            ,Uri.parse(user.getProfilePic()));
                                    FirebaseUser userfb = mAuth.getCurrentUser();
                                    updateUI(userfb);
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        }
                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.


                        } else {
                            Log.w(TAG, "signInWithCredential", task.getException());
                            Toast.makeText(LActivity.this, "Already Signed In Using Google",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

//    private void handleFacebookAccessToken(AccessToken token) {
//        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
//        mAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//            @Override
//            public void onComplete(@NonNull Task<AuthResult> task) {
//                if (task.isSuccessful()) {
//                    Log.d("FacebookTag", "signincredentials", task.getException());
//                    Toast.makeText(LActivity.this, "Login success", Toast.LENGTH_SHORT).show();
//
//                } else {
//                    Toast.makeText(LActivity.this, "Authentication error", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//    }

    //showing error dialog of user accounts
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
        progressBar.setVisibility(View.VISIBLE);
    }

    private void firebaseAuthWithGoogle(final GoogleSignInAccount acct, final GoogleSignInResult result) {
        Log.d("Google Sign In", "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("Google Sign In", "signInWithCredential:success");
                            boolean isNew = task.getResult().getAdditionalUserInfo().isNewUser();
                            Log.d("Google Sign In", "onComplete: " + (isNew ? "new user" : "old user"));
                            Toast.makeText(LActivity.this, "onComplete: " + (isNew ? "new user" : "old user"), Toast.LENGTH_SHORT).show();
                            if (isNew) {
                                // mAuth.getCurrentUser().getUid();
                                Log.d("TAG", "handleSignInResult:" + result.isSuccess());
                                if (result.isSuccess()) {
                                    // Signed in successfully, show authenticated UI.
                                    GoogleSignInAccount acct = result.getSignInAccount();

                                    gpersonName = acct.getDisplayName();
                                    gImageUrl = acct.getPhotoUrl();
                                    gpersonEmail = acct.getEmail();
                                    ref.child(mAuth.getCurrentUser().getUid()).child("Info").setValue(new User(gpersonName,gpersonEmail,gImageUrl.toString()));
                                    new SessionHelper(LActivity.this).setUserDetails(mAuth.getCurrentUser().getUid()
                                            ,gpersonName
                                            ,gpersonEmail
                                            ,gImageUrl);
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    updateUI(user);
                                    if (gImageUrl != null) {
                                        Toast.makeText(LActivity.this, "found image url", Toast.LENGTH_SHORT).show();
                                    }
                                    Toast.makeText(LActivity.this, gpersonName + "\n" + gpersonEmail, Toast.LENGTH_SHORT).show();
                                }
                            }else{
                                ref.child(mAuth.getCurrentUser().getUid()).child("Info").addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        User user=dataSnapshot.getValue(User.class);
                                        new SessionHelper(LActivity.this).setUserDetails(mAuth.getCurrentUser().getUid()
                                                ,user.getName()
                                                ,user.getEmail()
                                                ,Uri.parse(user.getProfilePic()));
                                        FirebaseUser userfire = mAuth.getCurrentUser();
                                        updateUI(userfire);
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                            }

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.v("Google Signin", "signInWithCredential:failure", task.getException());
                            Toast.makeText(LActivity.this, "Failed Auth", Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // ...
                    }
                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (resultCode == RESULT_OK && requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener..
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);

            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account,result);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.v("Google Sign In", "Google sign in failed", e);
                // ...
            }

        }
        else
        {
        }
    }


    private void handleSignInResult(GoogleSignInResult result) {

    }


    void updateUI(Object o) {
        if (o != null) {
            progressBar.setVisibility(View.GONE);
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Null", Toast.LENGTH_SHORT).show();
        }
    }
    private boolean checkPlayServices() {

        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();

        int resultCode = googleApiAvailability.isGooglePlayServicesAvailable(this);

        if (resultCode != ConnectionResult.SUCCESS) {
            if (googleApiAvailability.isUserResolvableError(resultCode)) {

                if (errorDialog == null) {
                    errorDialog = googleApiAvailability.getErrorDialog(this, resultCode, 2404);
                    errorDialog.setCancelable(false);
                }

                if (!errorDialog.isShowing())
                    errorDialog.show();

            }
        }

        return resultCode == ConnectionResult.SUCCESS;
    }

}

