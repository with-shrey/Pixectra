package com.pixectra.app;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.Serializable;

/**
 * Created by  prashu on 2/6/2018.
 * detail of the shipping address
 */

public class shipping_address extends AppCompatActivity {
    String name,mobile,ward,street,pincode,state,city,optional;
    EditText Tname,Tmobile,Tward,Tstreet,Tpincode,Tstate,Tcity,Toptional;
    Button save,cancle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shipping_address_activity);

        Toolbar toolbar = findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Delivery Address");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Tname=(EditText)findViewById(R.id.shipi_name);
        Tmobile=(EditText)findViewById(R.id.shipi_mobile);
        Tward=(EditText)findViewById(R.id.shipi_ward);
        Tstreet=(EditText)findViewById(R.id.shipi_street);
        Tpincode=(EditText)findViewById(R.id.shipi_pin);
        Tstate=(EditText)findViewById(R.id.shipi_state);
        Tcity=(EditText)findViewById(R.id.shipi_city);
        Toptional=(EditText)findViewById(R.id.shipi_optional);

        Intent intent = this.getIntent();
        int comefrom=intent.getIntExtra("status",1);
        if(comefrom==0)
        {
            Bundle bundle = intent.getExtras();
        shipaddClass adress=(shipaddClass)bundle.getSerializable("add");

            Tname.setText(adress.name);
            Tmobile.setText(adress.mobile);
            Tpincode.setText(adress.pincode);
            Tstreet.setText(adress.street);
            Tstate.setText(adress.state);
            Tcity.setText(adress.city);
            Tward.setText(adress.addNo);
            Toptional.setText(adress.optional);
        }
        Tmobile.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(!b)
                {
                    if(Tmobile.getText().toString().length()<10)
                    {
                        Tmobile.setError("Enter a valid input");
                    }
                }
            }
        });
        Tpincode.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(!b)
                {
                    if(Tpincode.getText().toString().length()<6)
                    {
                        Tpincode.setError("Enter a valid input");
                    }
                }
            }
        });
// save button
        findViewById(R.id.shipi_save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name=Tname.getText().toString();
                mobile=Tmobile.getText().toString();
                pincode=Tpincode.getText().toString();
                ward=Tward.getText().toString();
                street=Tstate.getText().toString();
                state=Tstate.getText().toString();
                optional=Toptional.getText().toString();
                city=Tcity.getText().toString();
                if(name.length()==0) {Alert("Name"); return;}
                if(mobile.length()==0){Alert("Mobile no."); return;}
                if(ward.length()==0){Alert("Apartment,Floor,Lane etc"); return;}
                if(street.length()==0){Alert("Street Address"); return;}
                if(city.length()==0){Alert("City/District"); return;}
                if(state.length()==0){Alert("State");return ;}
                if(pincode.length()==0){Alert("Pincode"); return;}
                shipaddClass add=new shipaddClass(name,ward,street,pincode,city,state,mobile,optional);//pojo class,have to save it on firebase
            }
        });
        //cancel button
        findViewById(R.id.shipi_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
// function for alerts
    void Alert(String Message)
    {
        AlertDialog alertDialog = new AlertDialog.Builder(shipping_address.this).create();
        alertDialog.setTitle("Alert");
        alertDialog.setMessage("Please enter your "+Message);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }
}
