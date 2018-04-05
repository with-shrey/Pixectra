package com.pixectra.app.Utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.util.Pair;
import android.view.Window;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.pixectra.app.Models.CheckoutData;
import com.pixectra.app.Models.Product;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;
import java.util.Vector;

import static android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;

/**
 * Created by XCODER on 3/5/2018.
 */

public class ImageController {
    Context context;
    int i, j, totalJ;
    String folder;
    Window window;
    String dbkey;
    private ProgressDialog mProgress;
    CheckoutData data;

    public ImageController(String key, Context context, Window window) {
        this.context = context;
        this.window = window;
        folder = null;
        i = 0;
        j = 0;
        dbkey = key;
    }


    //<--Put Index 0,0 for new order

    static byte[] getBytes(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }

    public void placeOrder(final CheckoutData data) {
        window.addFlags(FLAG_KEEP_SCREEN_ON);
        this.data = data;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd~HH-mm-ss~", Locale.getDefault());
        if (folder == null) {
            folder = "Present/" + format.format(new Date());
            Random random = new Random();
            folder = folder + random.nextInt(1000) + 1;
        }
        mProgress = new ProgressDialog(context);
        mProgress.setTitle("Uploading");
        mProgress.setMessage("Starting...");
        mProgress.setCancelable(false);
        mProgress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mProgress.setSecondaryProgress(0);
        mProgress.show();
        i = 0;
        j = 0;
        processUpload(i, j);
    }

    void processUpload(final int i, final int j) {
        Log.d("Image Uoload", i + " " + j);
        if (CartHolder.getInstance().getCart().size() != 0) {
            mProgress.setSecondaryProgress((i * 100) / CartHolder.getInstance().getCart().size());
            StorageReference mStorageRef = FirebaseStorage.getInstance().getReference().child(folder);
            Pair<Product, Vector<Bitmap>> pair = CartHolder.getInstance().getCart().get(i);
            totalJ = pair.second.size();
            mProgress.setTitle(pair.first.getType() + "(" + pair.first.getTitle() + ")");
            Bitmap image = pair.second.get(j);
            mProgress.setMessage("Image " + (j + 1) + "/" + totalJ);
            StorageReference riversRef = mStorageRef.child(pair.first.getId() + "-" + i).child("" + j + ".png");
            riversRef.putBytes(getBytes(image)).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    mProgress.setProgress((int) ((taskSnapshot.getBytesTransferred() * 100) / taskSnapshot.getTotalByteCount()));
                }
            })
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            int n = CartHolder.getInstance().getCart().size();
                            int product = i;
                            int photo = j;
                            if (j < totalJ - 1) {
                                photo++;
                                processUpload(product, photo);
                            } else {
                                if (product < n - 1) {
                                    product++;
                                    photo = 0;
                                    processUpload(product, photo);
                                } else {
                                    ImageController.this.i = 0;
                                    if (CartHolder.getInstance().getVideo().size() > 0)
                                        uploadVideos(0);
                                    else
                                        uploadUserDataAndStatus();
                                }
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {

                            //<!--Start from same index
                            mProgress.dismiss();
                            window.clearFlags(FLAG_KEEP_SCREEN_ON);
                            Toast.makeText(context, exception.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                            showalert(true, "Upload Failed", "RETRY", i, j);
                            // Handle unsuccessful uploads
                            // ...
                        }
                    });
        } else {
            ImageController.this.i = 0;
            if (CartHolder.getInstance().getVideo().size() > 0)
                uploadVideos(0);
        }

    }

    void uploadUserDataAndStatus() {
        StorageReference mStorageRef = FirebaseStorage.getInstance().getReference().child(folder);
        mStorageRef.child("info.txt").putBytes(data.toString().getBytes()).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                FirebaseDatabase db = FirebaseDatabase.getInstance();
                DatabaseReference ref = db.getReference("Users/" + new SessionHelper(context).getUid() + "/orders");
                ref.child(dbkey).child("fKey").setValue(folder);
                ref.child(dbkey).child("uploaded").setValue(true);
                mProgress.dismiss();
                window.clearFlags(FLAG_KEEP_SCREEN_ON);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    void uploadVideos(final int i) {
        mProgress.setSecondaryProgress((i * 100) / CartHolder.getInstance().getVideo().size());
        StorageReference mStorageRef = FirebaseStorage.getInstance().getReference().child(folder);
        Pair<Product, Uri> pair = CartHolder.getInstance().getVideo().get(i);
        mProgress.setTitle(pair.first.getType() + "(" + pair.first.getTitle() + ")");
        mProgress.setMessage("Uploading Video .. [" + i + "]");
        StorageReference riversRef = mStorageRef.child(pair.first.getId() + "~~" + i + ".mp4");
        riversRef.putFile(pair.second).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                mProgress.setProgress((int) ((taskSnapshot.getBytesTransferred() * 100) / taskSnapshot.getTotalByteCount()));
            }
        })
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        int n = CartHolder.getInstance().getVideo().size();
                        int product = i;
                        if (product + 1 < n) {
                            product++;
                            uploadVideos(product);
                        } else {
                            uploadUserDataAndStatus();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {

                        //<!--Start from same index
                        mProgress.dismiss();
                        window.clearFlags(FLAG_KEEP_SCREEN_ON);
                        Toast.makeText(context, exception.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        showalert(false, "Upload Failed", "RETRY", i, 0);

                        // Handle unsuccessful uploads
                        // ...
                    }
                });

    }

    void showalert(final boolean photo, String Message, String CancelText, final int index1, final int index2) {


        AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
        builder1.setMessage(Message);
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                CancelText,
                new DialogInterface.OnClickListener()

                {
                    public void onClick(DialogInterface dialog, int id) {
                        Toast.makeText(context, "Retrying", Toast.LENGTH_LONG).show();
                        window.addFlags(FLAG_KEEP_SCREEN_ON);
                        mProgress = new ProgressDialog(context);
                        mProgress.setMessage("Starting Upload..");
                        mProgress.setCancelable(false);
                        mProgress.setIndeterminate(false);
                        mProgress.show();
                        if (photo)
                            processUpload(index1, index2);
                        else
                            uploadVideos(index1);

                        dialog.dismiss();
                    }
                });


        AlertDialog alert11 = builder1.create();
        alert11.show();

        alert11.setOnCancelListener(
                new
                        DialogInterface.OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialogInterface) {


                            }
                        }


        );
    }

    public void saveTofirebase(Uri selectedVedioUri) {
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        final StorageReference photoRef = storageRef.child("user").child("NameYoWantToAdd");
// add File/URI
        photoRef.putFile(selectedVedioUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {

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
