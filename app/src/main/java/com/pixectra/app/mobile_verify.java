package com.pixectra.app;

import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.security.SecureRandom;

/**
 * Created by prashu on 2/11/2018.
 */

public class mobile_verify extends AppCompatActivity {
    View view;
    EditText e1, e2, e3, e4, mobile;
    String entered_otp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mobile_verification);
       final Button b=(Button)findViewById(R.id.verify_next);
       mobile=(EditText)findViewById(R.id.verify_mobile);
       mobile.addTextChangedListener(new TextWatcher() {
           @Override
           public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

           }

           @Override
           public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

           }

           @Override
           public void afterTextChanged(Editable editable) {
               if(mobile.length()==10)
               {
                   b.setEnabled(true);
                   b.setBackgroundColor(getResources().getColor(R.color.userProfileCardViewBlue));

               }
               else
                   b.setEnabled(false);
                   b.setBackgroundColor(getResources().getColor(R.color.dot_dark_screen2));

           }
       });


          b.findViewById(R.id.verify_next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String mobile_no = mobile.getText().toString().trim();
                final int PERMISSION_REQUEST_CODE = 1;

                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                    if (checkSelfPermission(android.Manifest.permission.SEND_SMS)
                            == PackageManager.PERMISSION_DENIED) {

                        String[] permissions = {android.Manifest.permission.SEND_SMS};

                        requestPermissions(permissions, PERMISSION_REQUEST_CODE);

                    }
                }

                LayoutInflater factory = LayoutInflater.from(mobile_verify.this);
                final View deleteDialogView = factory.inflate(R.layout.dialog_otp, null);
                final AlertDialog deleteDialog = new AlertDialog.Builder(mobile_verify.this).create();
                deleteDialog.setView(deleteDialogView);
                e1 = deleteDialogView.findViewById(R.id.char1);
                e2 = deleteDialogView.findViewById(R.id.char2);
                e3 = deleteDialogView.findViewById(R.id.char3);
                e4 = deleteDialogView.findViewById(R.id.char4);
                entered_otp = e1.getText().toString() + e2.getText().toString() + e3.getText().toString() + e4.getText().toString();
                deleteDialog.show();
                e1.addTextChangedListener(new GenericTextWatcher(e1));
                e2.addTextChangedListener(new GenericTextWatcher(e2));
                e3.addTextChangedListener(new GenericTextWatcher(e3));
                e4.addTextChangedListener(new GenericTextWatcher(e4));
                deleteDialog.setCancelable(false);
                deleteDialog.findViewById(R.id.verify_otp).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //code
                    }
                });
                deleteDialogView.findViewById(R.id.dialog_cancel).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        deleteDialog.dismiss();
                    }
                });
                deleteDialogView.findViewById(R.id.dialog_resend).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //code for resend
                    }
                });
                deleteDialogView.findViewById(R.id.dialog_another).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        deleteDialog.dismiss();
                        mobile.setText("");
                        //code
                    }
                });

            }
        });
    }
                public class GenericTextWatcher implements TextWatcher
                {
                    private View view;
                    private GenericTextWatcher(View view)
                    {
                        this.view = view;
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        // TODO Auto-generated method stub
                        String text = editable.toString();
                        switch(view.getId())
                        {

                            case R.id.char1:
                                if(text.length()==1)
                                    e2.requestFocus();
                                break;
                            case R.id.char2:
                                if(text.length()==1)
                                    e3.requestFocus();
                                break;
                            case R.id.char3:
                                if(text.length()==1)
                                    e4.requestFocus();
                                break;
                            case R.id.char4:
                                break;
                        }
                    }

                    @Override
                    public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                        // TODO Auto-generated method stub
                    }

                    @Override
                    public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                        // TODO Auto-generated method stub
                    }



    }
 /*   void send_msg(String mobile_no)
    {
        SecureRandom test = new SecureRandom();
        int result = test.nextInt(1000000);
        String resultStr = result + "";
        if (resultStr.length() != 6)
            for (int x = resultStr.length(); x < 6; x++) resultStr = "0" + resultStr;
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(mobile_no, null, resultStr, null, null);
            Toast.makeText(getApplicationContext(), "SMS Sent!",
                    Toast.LENGTH_LONG).show();
        }
        catch (Exception e) {
            Toast.makeText(getApplicationContext(),
                    "SMS faild, please try again later!",
                    Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }*/
}
