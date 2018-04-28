package com.pixectra.app.Utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PointF;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.dlazaro66.qrcodereaderview.QRCodeReaderView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pixectra.app.Models.Coupon;
import com.pixectra.app.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class QReader extends Activity implements QRCodeReaderView.OnQRCodeReadListener {

    String couponCode = null;
    String location = null;
    Coupon details = null;
    SimpleDateFormat format;
    boolean isBack, torchEnabled;
    private QRCodeReaderView qrCodeReaderView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_qreader);
            init();
        } catch (Exception e) {
            Toast.makeText(this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    void init() {
        format = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        isBack = true;
        torchEnabled = false;
        qrCodeReaderView = findViewById(R.id.qrdecoderview);
        qrCodeReaderView.setOnQRCodeReadListener(this);
        ImageButton rotate = findViewById(R.id.rotateCamera);
        ImageButton flash = findViewById(R.id.flashIcon);
        final View qrBlock = findViewById(R.id.qrblock);
        qrBlock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                qrCodeReaderView.startCamera();
                qrCodeReaderView.forceAutoFocus();
            }
        });
        rotate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                qrCodeReaderView.stopCamera();
                if (isBack)
                    qrCodeReaderView.setFrontCamera();
                else {
                    qrCodeReaderView.setBackCamera();
                }
                isBack = !isBack;
                qrCodeReaderView.startCamera();
            }
        });

        flash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                qrCodeReaderView.setTorchEnabled(!torchEnabled);
                torchEnabled = !torchEnabled;
            }
        });


        // Use this function to enable/disable decoding
        qrCodeReaderView.setQRDecodingEnabled(true);

        // Use this function to change the autofocus interval (default is 5 secs)
        qrCodeReaderView.setAutofocusInterval(2000L);

        // Use this function to enable/disable Torch
        qrCodeReaderView.setTorchEnabled(false);

        // Use this function to set front camera preview
        // qrCodeReaderView.setFrontCamera();

        // Use this function to set back camera preview
        qrCodeReaderView.setBackCamera();
        qrCodeReaderView.startCamera();
    }

    // Called when a QR is decoded
    // "text" : the text encoded in QR
    // "points" : points where QR control points are placed in View
    @Override
    public void onQRCodeRead(String text, PointF[] points) {
//        resultTextView.setText(text);
        qrCodeReaderView.stopCamera();
        couponCode = null;
        location = null;
        details = null;
        try {
            new JSONObject(text);
        } catch (Exception e) {
            StringBuffer buffer = new StringBuffer(text.trim());
            buffer.insert(text.indexOf("\"\n\"") + 1, ",");
            text = buffer.toString();
        }
        try {
            JSONObject coupon = new JSONObject(text);
            couponCode = coupon.getString("code");
            if (coupon.has("location"))
                location = coupon.getString("location");
            if (coupon.has("details")) {
                details = new Coupon();
                JSONObject det = coupon.getJSONObject("details");
                details.setStartDate(det.getString("startDate"));
                details.setEndDate(det.getString("endDate"));
                details.setCouponCode(coupon.getString("code"));
                details.setDiscount(det.getInt("discount"));
                details.setType(det.getInt("type"));
                details.setThreshold(det.getInt("threshold"));
            }

        } catch (JSONException e) {
            e.printStackTrace();

        }
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        final DatabaseReference earned = db.getReference("Users/" + new SessionHelper(this).getUid() + "/Earned");
        final DatabaseReference used = db.getReference("Users/" + new SessionHelper(this).getUid() + "/Used");
        used.keepSynced(true);
        earned.keepSynced(true);
        if (details != null) { //If Details Available In QR
            try {
                if (format.parse(details.getEndDate()).compareTo(format.parse(format.format(new Date()))) >= 0) {
                    used.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (!dataSnapshot.hasChild(couponCode)) {
                                earned.child(details.getCouponCode()).setValue(details).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(QReader.this, "Coupon Earned Successfully", Toast.LENGTH_SHORT).show();
                                        showADialog("Success", "Coupon Code Is " + details.getCouponCode()
                                                , new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {
                                                        Intent intent = new Intent();
                                                        intent.putExtra("code", details.getCouponCode());
                                                        setResult(RESULT_OK, intent);
                                                        dialogInterface.dismiss();
                                                        finish();
                                                    }
                                                });
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(QReader.this, "Request Failed . Please Try Again Later", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent();
                                        setResult(RESULT_CANCELED, intent);
                                        finish();
                                    }
                                });
                            } else {
                                qrCodeReaderView.startCamera();
                                Toast.makeText(QReader.this, "Offer Already Used", Toast.LENGTH_SHORT).show();
                            }
                            used.removeEventListener(this);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Toast.makeText(QReader.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                            qrCodeReaderView.startCamera();
                        }
                    });
                } else {
                    qrCodeReaderView.startCamera();
                    Toast.makeText(this, "Coupon Expired", Toast.LENGTH_SHORT).show();

                }
            } catch (ParseException e) {
                e.printStackTrace();
            }

        } else if (location != null) { //Location In QR

            final DatabaseReference ref = db.getReference(location).child(couponCode);
            ref.keepSynced(true);
            Toast.makeText(this, location + couponCode, Toast.LENGTH_SHORT).show();
            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        Toast.makeText(QReader.this, "Valid Coupon Code", Toast.LENGTH_SHORT).show();
                        details = dataSnapshot.getValue(Coupon.class);
                        try {
                            if (format.parse(details.getEndDate()).compareTo(format.parse(format.format(new Date()))) >= 0) {

                                used.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        if (!dataSnapshot.hasChild(couponCode)) {
                                            earned.child(details.getCouponCode()).setValue(details).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Toast.makeText(QReader.this, "Coupon Earned Successfully", Toast.LENGTH_SHORT).show();
                                                    showADialog("Success", "Coupon Code Is " + details.getCouponCode()
                                                            , new DialogInterface.OnClickListener() {
                                                                @Override
                                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                                    Intent intent = new Intent();
                                                                    intent.putExtra("code", details.getCouponCode());
                                                                    setResult(RESULT_OK, intent);
                                                                    dialogInterface.dismiss();
                                                                    finish();
                                                                }
                                                            });
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(QReader.this, "Request Failed . Please Try Again Later", Toast.LENGTH_SHORT).show();
                                                    Intent intent = new Intent();
                                                    setResult(RESULT_CANCELED, intent);
                                                    finish();
                                                }
                                            });
                                        } else {
                                            qrCodeReaderView.startCamera();
                                            Toast.makeText(QReader.this, "Offer Already Used", Toast.LENGTH_SHORT).show();
                                        }
                                        used.removeEventListener(this);
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                        Toast.makeText(QReader.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                                        qrCodeReaderView.startCamera();

                                    }
                                });

                            } else {
                                qrCodeReaderView.startCamera();
                                Toast.makeText(QReader.this, "Offer Expired ", Toast.LENGTH_SHORT).show();
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    } else {
                        qrCodeReaderView.startCamera();
                        Toast.makeText(QReader.this, "No Coupon Details Found", Toast.LENGTH_SHORT).show();
                    }
                    ref.removeEventListener(this);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(QReader.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });


        } else {
            Toast.makeText(this, "QR not valid \n Kindly Contact Us", Toast.LENGTH_SHORT).show();
            Toast.makeText(this, text, Toast.LENGTH_LONG).show();
            qrCodeReaderView.startCamera();
        }


    }

    @Override
    protected void onResume() {
        super.onResume();
        qrCodeReaderView.startCamera();
    }

    @Override
    protected void onPause() {
        super.onPause();
        qrCodeReaderView.stopCamera();
    }


    void showADialog(String Title, String Message, DialogInterface.OnClickListener listner) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setPositiveButton("OK", listner);
        builder.setMessage(Message);
        builder.setTitle(Title);
        builder.show();
    }
}
