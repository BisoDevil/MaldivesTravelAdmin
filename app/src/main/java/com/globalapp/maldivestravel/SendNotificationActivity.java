package com.globalapp.maldivestravel;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.api.client.json.GenericJson;
import com.kinvey.android.Client;
import com.kinvey.java.core.KinveyClientCallback;

public class SendNotificationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_notification);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true
        );
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void btnSend(View view) {
        TextView txtNotification = (TextView)findViewById(R.id.txtNotification);
        Client mKinveyClient = new Client.Builder(getApplicationContext()).build();
        GenericJson myInput = new GenericJson();
        myInput.put("message", txtNotification.getText().toString());

        mKinveyClient.customEndpoints(mKinveyClient.getUserClass()).callEndpoint("Notifications", myInput, new KinveyClientCallback<GenericJson>() {
            @Override
            public void onSuccess(GenericJson genericJson) {
                Toast.makeText(SendNotificationActivity.this, "Message sent", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        });
    }
}