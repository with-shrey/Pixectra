package com.pixectra.app.Utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.VectorEnabledTintResources;
import android.util.Pair;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.pixectra.app.Models.CheckoutData;
import com.pixectra.app.Models.Product;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Random;
import java.util.Vector;

/**
 * Created by XCODER on 3/5/2018.
 */

public class ImageController {

    Context context;
    int i, j,totalI,totalJ;
    private ProgressDialog mProgress;

    public ImageController(Context context) {
        this.context = context;


    }


    //<--Put Index 0,0 for new order

    public void placeOrder(final CheckoutData data, int productindex, int imageindex) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd~HH-mm-ss~", Locale.getDefault());
        String folder = "Present/" + format.format(new Date());
        Random random = new Random();
        folder = folder + random.nextInt(1000) + 1;
        StorageReference mStorageRef = FirebaseStorage.getInstance().getReference().child(folder);
        mStorageRef.child("info.txt").putBytes(data.toString().getBytes()).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

            }
        });
        i = productindex;
        for (Pair<Product, Vector<Bitmap>> pair : CartHolder.getInstance().getCart()) {
            j = imageindex;


            totalJ = pair.second.size();


            for (Bitmap image : pair.second) {
                StorageReference riversRef = mStorageRef.child(pair.first.getId() + "~~" + i).child("" + j + ".png");
                riversRef.putBytes(getBytes(image))
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {




                                if (CartHolder.getInstance().getCart().size() != 0) {
                                    totalI = CartHolder.getInstance().getCart().size();

                                } else {
                                    totalI = 10;
                                }

                            //<--Set up dialog box
                                mProgress = new ProgressDialog(context);
                                mProgress.setTitle("Album" + i + "/" + totalI);
                                mProgress.setMessage("Image" + j + "/" + totalJ);
                                mProgress.setCancelable(false);
                                mProgress.setIndeterminate(true);

                                // Get a URL to the uploaded content


                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {

                                 //<!--Start from same index
                                showalert("Upload Failed", "Retry", data, i, j);

                                // Handle unsuccessful uploads
                                // ...
                            }
                        });
                j++;
            }
            i++;
        }
    }


    static byte[] getBytes(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }

    void showalert(String Message, String CancelText, final CheckoutData data, final int index1, final int index2) {


        AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
        builder1.setMessage(Message);
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                CancelText,
                new DialogInterface.OnClickListener()

                {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });


        AlertDialog alert11 = builder1.create();
        alert11.show();

        alert11.setOnCancelListener(
                new
                        DialogInterface.OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialogInterface) {


                                Toast.makeText(context, "Retrying", Toast.LENGTH_LONG).show();
                                placeOrder(data, index1, index2);

                            }
                        }


        );
    }


}
