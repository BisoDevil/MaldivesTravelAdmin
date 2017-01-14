package com.globalapp.maldivestravel;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.kinvey.android.Client;
import com.kinvey.android.callback.KinveyUserCallback;
import com.kinvey.java.User;

public class LoginActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


    }

    public void LoginButton(View view) {

        final ProgressDialog dialog = new ProgressDialog(this);

        dialog.setMessage(getString(R.string.please_wait));
        dialog.show();

        final EditText txtUserName = (EditText) findViewById(R.id.txtUserName);
        final EditText txtPassword = (EditText) findViewById(R.id.txtPassword);

        final Client mKinveyClient = new Client.Builder(this.getApplicationContext()).build();
        final Intent myintet = new Intent(this, MainActivity.class);
        mKinveyClient.user().logout().execute();

        mKinveyClient.user().login(txtUserName.getText().toString(), txtPassword.getText().toString(), new KinveyUserCallback() {


            @Override
            public void onSuccess(User user) {
                dialog.dismiss();
                Toast.makeText(getApplicationContext(), getString(R.string.welcome) + " " + user.getUsername(), Toast.LENGTH_SHORT).show();
                startActivity(myintet);
                finish();

            }

            @Override
            public void onFailure(Throwable throwable) {
                dialog.dismiss();
                Toast.makeText(getApplicationContext(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }



}
