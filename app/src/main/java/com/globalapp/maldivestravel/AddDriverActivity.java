package com.globalapp.maldivestravel;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.kinvey.android.Client;
import com.kinvey.android.callback.KinveyUserCallback;
import com.kinvey.java.User;

public class AddDriverActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_driver);
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

    public void btnAddDriver(View view) {

        final ProgressDialog dialog =  new ProgressDialog(this);

        dialog.setMessage(getString(R.string.please_wait));
        dialog.show();
        final EditText txtDriverName = (EditText)findViewById(R.id.txtDriverName);
        final EditText txtDriverPhone = (EditText)findViewById(R.id.txtDriverPhone);
        final EditText txtDriverPassword = (EditText)findViewById(R.id.txtDriverPassword);
        final Client mKinveyClient = new Client.Builder(this.getApplicationContext()).build();
        mKinveyClient.user().logout().execute();
        mKinveyClient.user().put("full_Name",txtDriverName.getText().toString());
        mKinveyClient.user().put("Phone_Number",txtDriverPhone.getText().toString());
        mKinveyClient.user().put("Type","Driver");


        mKinveyClient.user().create(txtDriverPhone.getText().toString(), txtDriverPassword.getText().toString(), new KinveyUserCallback() {
            @Override
            public void onSuccess(User user) {
                dialog.dismiss();
                Toast.makeText(getApplicationContext(), " Account has been created.", Toast.LENGTH_SHORT).show();
                txtDriverName.setText("");
                txtDriverPhone.setText("");
                txtDriverPassword.setText("");

            }

            @Override
            public void onFailure(Throwable throwable) {
                dialog.dismiss();
                Toast.makeText(getApplicationContext(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }
}
