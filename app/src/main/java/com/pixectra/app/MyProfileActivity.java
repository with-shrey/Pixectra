package com.pixectra.app;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pixectra.app.Models.User;
import com.pixectra.app.Utils.GlideHelper;
import com.pixectra.app.Utils.SessionHelper;

/**
 * Created by prashu on 2/16/2018.
 */

public class MyProfileActivity extends AppCompatActivity implements View.OnClickListener {

    TextView imagename, name, email, mobile, logout, help, delete;
    ImageView imageView;
    ProgressBar progress;
    FirebaseDatabase db;
    DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);
        db = FirebaseDatabase.getInstance();
        ref = db.getReference("Users");
        final FirebaseAuth mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() == null) {
            new SessionHelper(getApplicationContext()).logOutUser();
            startActivity(new Intent(this, LActivity.class));
            finish();
            return;
        }
        final String uid = new SessionHelper(this).getUid();
        ref.child(uid).child("Info").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                assert user != null;
                if (user.getName() != null)
                    name.setText(user.getName());
                else
                    name.setText("Not Found");
                if (user.getEmail() != null)
                    email.setText(user.getEmail());
                else
                    email.setText("Not Found");
                if (user.getEmail() != null)
                    mobile.setText(user.getPhoneNo());
                else
                    mobile.setText("Not Found");
                if (user.getProfilePic() != null)
                    GlideHelper.load(MyProfileActivity.this, user.getProfilePic(), imageView, progress);
                else
                    progress.setVisibility(View.GONE);
                ref.child(uid).child("Info").removeEventListener(this);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        Toolbar toolbar = findViewById(R.id.profile_tool);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Profile");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        // imagename=(TextView)findViewById(R.id.image_name);
        name = findViewById(R.id.profile_name);
        progress = findViewById(R.id.progress_profile);
        email = findViewById(R.id.profile_email);
        mobile = findViewById(R.id.profile_mobile);
        imageView = findViewById(R.id.image_profile);
        logout = findViewById(R.id.profile_logout);
        help = findViewById(R.id.profile_help);
        delete = findViewById(R.id.profile_delete);
        //imagename.setText("Salman");
        name.setText("Loading...");
        email.setText("Loading...");
        mobile.setText("Loading...");
        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.help_url)));
                if (browserIntent.resolveActivity(getPackageManager()) != null)
                    startActivity(browserIntent);
                else
                    Toast.makeText(view.getContext(), "Browser Not Found", Toast.LENGTH_SHORT).show();
                //help page
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog alertDialog = new AlertDialog.Builder(MyProfileActivity.this).create();
                alertDialog.setMessage("Are you Sure You Want To Logout?");
                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Log Out", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        new SessionHelper(getApplicationContext()).logOutUser();
                        Intent intent = new Intent(MyProfileActivity.this, LActivity.class);
                        startActivity(intent);
                        finishAffinity();


                    }
                });
                alertDialog.show();

            }
        });
        name.setOnClickListener(this);
        email.setOnClickListener(this);
        mobile.setOnClickListener(this);


    }

    @Override
    protected void onResume() {
        super.onResume();
        db = FirebaseDatabase.getInstance();
        ref = db.getReference("Users");
        final String uid = new SessionHelper(this).getUid();
        if (name != null && email != null && mobile != null && progress != null)
            ref.child(uid).child("Info").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    User user = dataSnapshot.getValue(User.class);
                    assert user != null;
                    if (user.getName() != null)
                        name.setText(user.getName());
                    else
                        name.setText("Not Found");
                    if (user.getEmail() != null)
                        email.setText(user.getEmail());
                    else
                        email.setText("Not Found");
                    if (user.getEmail() != null)
                        mobile.setText(user.getPhoneNo());
                    else
                        mobile.setText("Not Found");
                    if (user.getProfilePic() != null)
                        GlideHelper.load(MyProfileActivity.this, user.getProfilePic(), imageView, progress);
                    else
                        progress.setVisibility(View.GONE);
                    ref.child(uid).child("Info").removeEventListener(this);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(MyProfileActivity.this, "Failed To Fetch Information", Toast.LENGTH_SHORT).show();
                }
            });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.profile_name:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                View dialogView = View.inflate(this, R.layout.edit_text_dialog, null);
                builder.setView(dialogView);
                final EditText editText = dialogView.findViewById(R.id.edit_text);
                TextView title = dialogView.findViewById(R.id.title);
                title.setText("Enter Your Name");
                builder.setPositiveButton("SAVE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialogInterface, int i) {
                        final String name = editText.getText().toString();
                        db = FirebaseDatabase.getInstance();
                        ref = db.getReference("Users");
                        final String uid = new SessionHelper(MyProfileActivity.this).getUid();
                        ref.child(uid).child("Info").child("name").setValue(name, new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                dialogInterface.dismiss();
                                if (MyProfileActivity.this.name != null)
                                    MyProfileActivity.this.name.setText(name);
                                Toast.makeText(MyProfileActivity.this, "Name Changed Successfully", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
                builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                builder.show();
                break;
            case R.id.profile_email:
                AlertDialog.Builder builder2 = new AlertDialog.Builder(this);
                View dialogView2 = View.inflate(this, R.layout.edit_text_dialog, null);
                builder2.setView(dialogView2);
                final EditText editText2 = dialogView2.findViewById(R.id.edit_text);
                final TextView title2 = dialogView2.findViewById(R.id.title);
                title2.setText("Enter New Email");
                builder2.setNeutralButton("SAVE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialogInterface, int i) {
                        title2.setText("Saving ... ");
                        final String email = editText2.getText().toString();
                        db = FirebaseDatabase.getInstance();
                        ref = db.getReference("Users");
                        final String uid = new SessionHelper(MyProfileActivity.this).getUid();
                        if (android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches())
                            ref.child(uid).child("Info").child("email").setValue(email, new DatabaseReference.CompletionListener() {
                                @Override
                                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                    if (MyProfileActivity.this.email != null)
                                        MyProfileActivity.this.email.setText(email);
                                    dialogInterface.dismiss();
                                    Toast.makeText(MyProfileActivity.this, "Email Changed Successfully", Toast.LENGTH_SHORT).show();
                                }
                            });
                        else {
                            title2.setText("Enter Email ... ");
                            Toast.makeText(MyProfileActivity.this, "Invalid Email Entered", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                builder2.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                builder2.show();
                break;
            case R.id.profile_mobile:
                Intent intent = new Intent(this, MobileVerifyActivity.class);
                intent.putExtra("uid", new SessionHelper(this).getUid());
                startActivity(intent);
                break;
        }
    }
}
