package com.globalapp.maldivestravel;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.ClipData;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.api.client.json.GenericJson;
import com.kinvey.android.AsyncAppData;
import com.kinvey.android.Client;
import com.kinvey.android.callback.KinveyListCallback;
import com.kinvey.java.Query;
import com.kinvey.java.User;
import com.kinvey.java.core.KinveyClientCallback;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class AssignTripActivity extends AppCompatActivity {
    Spinner spinner;
    ArrayList<ItemData> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assign_trip);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        loadDrivers();
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TextView txtDriverNameAss = (TextView) findViewById(R.id.txtDriverNameAss);
                TextView txtDriverPhoneAssign = (TextView) findViewById(R.id.txtDriverPhoneAssign);
                txtDriverNameAss.setText(list.get(position).Name);
                txtDriverPhoneAssign.setText(list.get(position).Phone);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
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

    public void btnAddTrip(View view) {
        final ProgressDialog dialog = new ProgressDialog(this);

        dialog.setMessage(getString(R.string.please_wait));
        dialog.show();

        EditText txtCustomerPhone = (EditText) findViewById(R.id.txtCustomerPhone);

        final Client mKinveyClient = new Client.Builder(this.getApplicationContext()).build();
        CreateData();
        mKinveyClient.user().logout().execute();
        mKinveyClient.user().put("Type", "Customer");
        mKinveyClient.user().create(txtCustomerPhone.getText().toString(), txtCustomerPhone.getText().toString(), new KinveyClientCallback<User>() {
            @Override
            public void onSuccess(User user) {
                dialog.dismiss();
            }

            @Override
            public void onFailure(Throwable throwable) {
                dialog.dismiss();
                Toast.makeText(AssignTripActivity.this, throwable.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }

            //  if (throwable.getMessage().contains("UserAlreadyExists")){

        });
        loginuser();
    }

    public void CreateData() {
        EditText txtCustomerName = (EditText) findViewById(R.id.txtCustomerName);
        EditText txtCustomerPhone = (EditText) findViewById(R.id.txtCustomerPhone);
        EditText txtCustomerLocation = (EditText) findViewById(R.id.txtCustomerLocation);
        EditText txtCustomerDestination = (EditText) findViewById(R.id.txtCustomerDestination);
        EditText txtDriverNameAss = (EditText) findViewById(R.id.txtDriverNameAss);
        EditText txtDriverPhoneAssign = (EditText) findViewById(R.id.txtDriverPhoneAssign);
        EditText txtDate = (EditText) findViewById(R.id.txtDate);
        EditText txtTime = (EditText) findViewById(R.id.txtTime);
        EditText txtNote = (EditText) findViewById(R.id.txtNote);
//        String date = new SimpleDateFormat("EEE, MMM d, yyyy").format(new Date());
//        String IdDate = new SimpleDateFormat("ddMMyyyy").format(new Date());

        final Client mKinveyClient = new Client.Builder(this.getApplicationContext()).build();


        GenericJson appdata = new GenericJson();
        appdata.put("_id", txtCustomerPhone.getText().toString() + txtDate.getText().toString());
        appdata.put("Customer_Name", txtCustomerName.getText().toString());
        appdata.put("Customer_Phone_No", txtCustomerPhone.getText().toString());
        appdata.put("Customer_Location", txtCustomerLocation.getText().toString());
        appdata.put("Customer_Destination", txtCustomerDestination.getText().toString());
        appdata.put("Driver_Name", txtDriverNameAss.getText().toString());
        appdata.put("Driver_Phone_No", txtDriverPhoneAssign.getText().toString());
        appdata.put("Note", txtNote.getText().toString());
        appdata.put("Time", txtTime.getText().toString());
        appdata.put("Date", txtDate.getText().toString());
        AsyncAppData<GenericJson> Travels = mKinveyClient.appData("Travels", GenericJson.class);
        Travels.save(appdata, new KinveyClientCallback<GenericJson>() {


            @Override
            public void onSuccess(GenericJson genericJson) {
                Toast.makeText(AssignTripActivity.this, "Trip has been posted", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Throwable throwable) {
                Toast.makeText(AssignTripActivity.this, throwable.getMessage(), Toast.LENGTH_LONG).show();
            }


        });

    }

    public void loginuser() {
        Client mKinveyClient = new Client.Builder(this.getApplicationContext()).build();
        mKinveyClient.user().login("Admin", "Admin", new KinveyClientCallback<User>() {
            @Override
            public void onSuccess(User user) {

            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        });
    }

    public void getDate(final View view) {
        DatePicker date = new DatePicker(this);
        DatePickerDialog dialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                int mon = month + 1;
                TextView txt = (TextView) findViewById(R.id.txtDate);
                txt.setText(dayOfMonth + "/" + mon + "/" + year);

            }
        }, date.getYear(), date.getMonth(), date.getDayOfMonth());
        dialog.show();
    }

    public void getTime(View view) {
        TimePickerDialog dialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                TextView txtTime = (TextView) findViewById(R.id.txtTime);
                txtTime.setText(hourOfDay + ":" + minute);
            }
        }, 0, 0, true);
        dialog.show();
    }

    public class SpinnerAdapter extends ArrayAdapter<ItemData> {
        Activity context;
        ArrayList<ItemData> list;
        LayoutInflater inflater;

        public SpinnerAdapter(Activity context, int id, ArrayList<ItemData> list) {
            super(context, id, list);
            this.list = list;
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View itemView = inflater.inflate(R.layout.item_data, parent, false);
            TextView txtItemDataName = (TextView) itemView.findViewById(R.id.txtItemDataName);
            TextView txtItemDataPhone = (TextView) itemView.findViewById(R.id.txtItemDataPhone);
            ItemData row = list.get(position);
            txtItemDataName.setText(row.Name);
            txtItemDataPhone.setText(row.Phone);
            return itemView;
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            return getView(position, convertView, parent);
        }
    }

    public void loadDrivers() {
        spinner = (Spinner) findViewById(R.id.spinner);
        list = new ArrayList<ItemData>();
        final SpinnerAdapter adapter = new SpinnerAdapter(this, R.id.MainData, list);

        Client mKinveyClient = new Client.Builder(this.getApplicationContext()).build();
        Query query = mKinveyClient.query();
        query.equals("Type", "Driver");
        mKinveyClient.user().retrieve(query, new KinveyListCallback<User>() {
            @Override
            public void onSuccess(User[] users) {
                for (int n = 0; n < users.length; n++) {
                    String name = users[n].get("full_Name").toString();
                    String phone = users[n].getUsername();
                    list.add(new ItemData(name, phone));
                }

                spinner.setAdapter(adapter);
            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        });

    }
}
