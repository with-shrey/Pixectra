package com.pixectra.app.Utils;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.util.Pair;
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
import com.pixectra.app.R;
import com.pixectra.app.UploadService;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;
import java.util.Vector;

/**
 * Created by XCODER on 3/5/2018.
 */

public class ImageController {
    Context context;
    int i, j, totalJ;
    String folder;
    String dbkey;
    NotificationManager manager;
    private NotificationCompat.Builder mBuilder;
    CheckoutData data;

    public ImageController(String key, Context context) {
        this.context = context;
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
        this.data = data;
        Toast.makeText(context, "Service Started", Toast.LENGTH_SHORT).show();

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd~HH-mm-ss~", Locale.getDefault());
        if (folder == null) {
            folder = "Present/" + format.format(new Date());
            Random random = new Random();
            folder = folder + random.nextInt(1000) + 1;
        }
        mBuilder = new NotificationCompat.Builder(context, "default");
        mBuilder.setContentTitle("Starting Information ...");
        mBuilder.setProgress(100, 0, false);
        mBuilder.setPriority(NotificationCompat.PRIORITY_HIGH);
        mBuilder.setSmallIcon(R.drawable.ic_stat_name);
        manager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(1, mBuilder.build());
        startUpload();

    }

    public void startUpload() {
        StorageReference mStorageRef = FirebaseStorage.getInstance().getReference().child(folder);
        mStorageRef.child("info.txt").putBytes(data.toString().getBytes()).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                i = 0;
                j = 0;
                processUpload(i, j);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                showalert(3, "Failed Info Upload", " Tap To Retry ", 0, 0);
                Toast.makeText(context, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void processUpload(final int i, final int j) {
        Log.d("Image Uoload", i + " " + j);
        if (CartHolder.getInstance().getCart().size() != 0) {
            StorageReference mStorageRef = FirebaseStorage.getInstance().getReference().child(folder);
            Pair<Product, Vector<Bitmap>> pair = CartHolder.getInstance().getCart().get(i);
            totalJ = pair.second.size();
            mBuilder.setContentIntent(null);

            mBuilder.setContentTitle(pair.first.getType() + "(" + pair.first.getTitle() + ")");
            Bitmap image = pair.second.get(j);
            mBuilder.setContentText("Image " + (j + 1) + "/" + totalJ);
            mBuilder.setProgress(100, 0, false);
            manager.notify(1, mBuilder.build());
            StorageReference riversRef = mStorageRef.child(pair.first.getId() + "-" + i).child("" + j + ".png");
            riversRef.putBytes(getBytes(image)).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    mBuilder.setProgress(100,
                            (int) ((taskSnapshot.getBytesTransferred() * 100) / taskSnapshot.getTotalByteCount())
                            , false);
                    manager.notify(1, mBuilder.build());

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
                            mBuilder.setProgress(0, 0, false);
                            manager.notify(1, mBuilder.build());
                            Toast.makeText(context, exception.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                            showalert(1, "Upload Failed", "Tap To Retry", i, j);
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

    public void uploadUserDataAndStatus() {
        mBuilder.setContentTitle("Uploading Acknoledgement ...");
        mBuilder.setContentText("Uploading ...");
        mBuilder.setProgress(0, 0, false);
        manager.notify(1, mBuilder.build());
        StorageReference mStorageRef = FirebaseStorage.getInstance().getReference().child(folder);
        mStorageRef.child("CompleteAcknoledgement.txt").putBytes(computeAcknoledment().getBytes()).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                FirebaseDatabase db = FirebaseDatabase.getInstance();
                DatabaseReference ref = db.getReference("Users/" + new SessionHelper(context).getUid() + "/orders");
                ref.child(dbkey).child("fKey").setValue(folder);
                ref.child(dbkey).child("uploaded").setValue(true);
                mBuilder.setContentTitle("Upload Successfull");
                mBuilder.setProgress(0, 0, false);
                mBuilder.setContentText("");
                manager.notify(1, mBuilder.build());
                CartHolder.getInstance().getCart().clear();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                showalert(2, "Failed Acknoledgement Upload", " Tap To Retry ", 0, 0);
                Toast.makeText(context, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void uploadVideos(final int i) {
        StorageReference mStorageRef = FirebaseStorage.getInstance().getReference().child(folder);
        Pair<Product, Uri> pair = CartHolder.getInstance().getVideo().get(i);
        mBuilder.setContentIntent(null);
        mBuilder.setContentTitle(pair.first.getType() + "(" + pair.first.getTitle() + ")");
        mBuilder.setProgress(100, 0, false);
        mBuilder.setContentText("Uploading Video .. [" + i + 1 + "]");
        StorageReference riversRef = mStorageRef.child(pair.first.getId() + "~~" + i + ".mp4");
        riversRef.putFile(pair.second).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                mBuilder.setProgress(100, (int) ((taskSnapshot.getBytesTransferred() * 100)
                                / taskSnapshot.getTotalByteCount())
                        , false);
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
                        Toast.makeText(context, exception.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        showalert(0, "Upload Failed", "Tap To Retry", i, 0);

                        // Handle unsuccessful uploads
                        // ...
                    }
                });

    }

    void showalert(final int photo, String Message, String CancelText, final int index1, final int index2) {
        final Intent intent = new Intent(context, UploadService.class);
        intent.putExtra("i", index1);
        intent.putExtra("j", index2);
        intent.putExtra("photo", photo);
        final PendingIntent contentIntent = PendingIntent.getService(context, 0, intent, 0);
        mBuilder.setContentTitle(Message);
        mBuilder.setContentText(CancelText);
        mBuilder.setContentIntent(contentIntent);
        mBuilder.setProgress(0, 0, false);
        manager.notify(1, mBuilder.build());
    }

    String computeAcknoledment() {
        StringBuilder ack = new StringBuilder();
        ack.append("Total Types of Photo Products : ").append(CartHolder.getInstance().getCart()).append(",\n");
        ack.append("Total Types of Video Products : ").append(CartHolder.getInstance().getVideo()).append(",\n");
        for (Pair<Product, Vector<Bitmap>> details : CartHolder.getInstance().getCart()) {
            ack.append(details.first.getType()).append("(").append(details.first.getType()).append(")").append(" : ").append(details.second.size()).append(",\n");
        }
        return ack.toString();
    }

}
