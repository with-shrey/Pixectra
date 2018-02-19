package com.pixectra.app;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pixectra.app.Models.User;
import com.pixectra.app.Utils.GlideHelper;

/**
 * Created by prashu on 2/16/2018.
 */

public class profile extends AppCompatActivity {

    TextView imagename,name,email,mobile,logout,help,delete;
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
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        String uid=mAuth.getCurrentUser().getUid();
        ref.child(uid).child("Info").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user=dataSnapshot.getValue(User.class);
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
                GlideHelper.load(profile.this,user.getProfilePic(),imageView,progress);
                else
                    progress.setVisibility(View.GONE);
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
        name=(TextView)findViewById(R.id.profile_name);
        progress=findViewById(R.id.progress_profile);
        email=(TextView)findViewById(R.id.profile_email);
        mobile=(TextView)findViewById(R.id.profile_mobile);
        imageView=(ImageView)findViewById(R.id.image_profile) ;
        logout=(TextView)findViewById(R.id.profile_logout);
        help=(TextView)findViewById(R.id.profile_help);
        delete=(TextView)findViewById(R.id.profile_delete);
        //imagename.setText("Salman");
        name.setText("Loading...");
        email.setText("Loading...");
        mobile.setText("Loading...");
        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //help page
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog alertDialog = new AlertDialog.Builder(profile.this).create();
                alertDialog.setMessage("Log out of piXectra");
                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Log Out", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //log out
                    }
                });
                alertDialog.show();

            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog alertDialog = new AlertDialog.Builder(profile.this).create();
                alertDialog.setMessage("Delete Account ?");
                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Deactivate", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //Deactivate
                    }
                });
                alertDialog.show();


            }
        });





    }
}
