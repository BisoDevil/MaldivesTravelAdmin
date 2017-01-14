package com.globalapp.maldivestravel;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.api.client.json.GenericJson;
import com.kinvey.android.AsyncAppData;
import com.kinvey.android.Client;
import com.kinvey.android.callback.KinveyDeleteCallback;
import com.kinvey.java.core.KinveyClientCallback;
import com.kinvey.java.model.KinveyDeleteResponse;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CancelActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cancel);

    }

    @Override
    protected void onStart() {
        super.onStart();
        TextView txtEditID=(TextView) findViewById(R.id.txtEditID);
        TextView txtCancelCustomerName=(TextView) findViewById(R.id.txtCancelCustomerName);
        TextView txtCancelCustomerPhone=(TextView) findViewById(R.id.txtCancelCustomerPhone);
        TextView txtCancelCustomerLocation=(TextView) findViewById(R.id.txtCancelCustomerLocation);
        TextView txtCancelCustomerDestination=(TextView) findViewById(R.id.txtCancelCustomerDestination);
        TextView txtCancelDriverNameAss=(TextView) findViewById(R.id.txtCancelDriverNameAss);
        TextView txtCancelDriverPhoneAssign=(TextView) findViewById(R.id.txtCancelDriverPhoneAssign);
        TextView txtCancelNote=(TextView) findViewById(R.id.txtCancelNote);
        TextView txtCancelDate=(TextView) findViewById(R.id.txtCancelDate);
        TextView txtCancelTime=(TextView) findViewById(R.id.txtCancelTime);
        try {
            Bundle bu = getIntent().getExtras();
            txtEditID.setText(bu.getString("_id"));
            txtCancelCustomerName.setText(bu.getString("Customer_Name"));
            txtCancelCustomerPhone.setText(bu.getString("Customer_Phone_No"));
            txtCancelCustomerLocation.setText(bu.getString("Customer_Location"));
            txtCancelCustomerDestination.setText(bu.getString("Customer_Destination"));
            txtCancelDriverNameAss.setText(bu.getString("Driver_Name"));
            txtCancelDriverPhoneAssign.setText(bu.getString("Driver_Phone_No"));
            txtCancelDate.setText(bu.getString("Date"));
            txtCancelTime.setText(bu.getString("Time"));
            txtCancelNote.setText(bu.getString("Note"));
        }catch (Exception ex){}
    }
    public void btnSave(View view){
        TextView txtEditID=(TextView) findViewById(R.id.txtEditID);
        TextView txtCancelCustomerName=(TextView) findViewById(R.id.txtCancelCustomerName);
        TextView txtCancelCustomerPhone=(TextView) findViewById(R.id.txtCancelCustomerPhone);
        TextView txtCancelCustomerLocation=(TextView) findViewById(R.id.txtCancelCustomerLocation);
        TextView txtCancelCustomerDestination=(TextView) findViewById(R.id.txtCancelCustomerDestination);
        TextView txtCancelDriverNameAss=(TextView) findViewById(R.id.txtCancelDriverNameAss);
        TextView txtCancelDriverPhoneAssign=(TextView) findViewById(R.id.txtCancelDriverPhoneAssign);
        TextView txtCancelDate=(TextView) findViewById(R.id.txtCancelDate);
        TextView txtCancelTime=(TextView) findViewById(R.id.txtCancelTime);
        TextView txtCancelNote=(TextView) findViewById(R.id.txtCancelNote);
//        String date = new SimpleDateFormat("dd/MM/yyyy", Locale.US).format(new Date());
        Client mKinveyClient = new Client.Builder(this.getApplicationContext()).build();
        GenericJson appdata = new GenericJson();
        appdata.put("_id",txtEditID.getText().toString());
        appdata.put("Customer_Name", txtCancelCustomerName.getText().toString());
        appdata.put("Customer_Phone_No", txtCancelCustomerPhone.getText().toString());
        appdata.put("Customer_Location", txtCancelCustomerLocation.getText().toString());
        appdata.put("Customer_Destination", txtCancelCustomerDestination.getText().toString());
        appdata.put("Driver_Name", txtCancelDriverNameAss.getText().toString());
        appdata.put("Driver_Phone_No", txtCancelDriverPhoneAssign.getText().toString());
        appdata.put("Note", txtCancelNote.getText().toString());
        appdata.put("Date", txtCancelDate.getText().toString());
        appdata.put("Time", txtCancelTime.getText().toString());
        AsyncAppData<GenericJson> Travels = mKinveyClient.appData("Travels", GenericJson.class);
        Travels.save(appdata, new KinveyClientCallback<GenericJson>() {
            @Override
            public void onSuccess(GenericJson genericJson) {
                Toast.makeText(CancelActivity.this, "Trip has been saved", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Throwable throwable) {
                Toast.makeText(CancelActivity.this, throwable.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

    }
    public void btnCancel(View view){
        TextView txtEditID=(TextView) findViewById(R.id.txtEditID);
        Client mKinveyClient = new Client.Builder(this.getApplicationContext()).build();
        AsyncAppData<GenericJson> Travels = mKinveyClient.appData("Travels", GenericJson.class);
        Travels.delete(txtEditID.getText().toString(), new KinveyDeleteCallback() {
            @Override
            public void onSuccess(KinveyDeleteResponse kinveyDeleteResponse) {
                Toast.makeText(CancelActivity.this, "Trip has been canceled", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        });

    }
}
