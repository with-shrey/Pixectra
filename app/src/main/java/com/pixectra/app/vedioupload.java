package com.pixectra.app;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


/**
 * Created by prashu on 4/4/2018.
 */

public class vedioupload extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vedioupload);
        Button btn=(Button)findViewById(R.id.uploadvedio);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("video/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Video"), 1);
            }
        });
    }
                public void onActivityResult(int requestCode, int resultCode, Intent data) {
                    if (resultCode == RESULT_OK) {
                        if (requestCode == 1) {
                            Uri selectedImageUri = data.getData();
                            if (selectedImageUri != null) {
                                saveTofirebase(selectedImageUri);
                            }
                        }
                    }
                }
                void saveTofirebase(Uri selectedVedioUri)
                {
                    StorageReference storageRef = FirebaseStorage.getInstance().getReference();
                    final StorageReference photoRef = storageRef.child("user").child("NameYoWantToAdd");
// add File/URI
                    photoRef.putFile(selectedVedioUri)
                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    // Upload succeeded
                                    Toast.makeText(getApplicationContext(), "Upload Success...", Toast.LENGTH_SHORT).show();

                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception exception) {
                                    // Upload failed
                                    Toast.makeText(getApplicationContext(), "Upload failed...", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnProgressListener(
                            new OnProgressListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                    //calculating progress percentage
                                }
                            });
                }
            }



