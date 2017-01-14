package com.globalapp.maldivestravel;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.api.client.json.GenericJson;
import com.kinvey.android.AsyncAppData;
import com.kinvey.android.Client;
import com.kinvey.android.callback.KinveyListCallback;
import com.kinvey.java.Query;
import com.kinvey.java.User;
import com.kinvey.java.core.KinveyClientCallback;
import com.kinvey.java.query.AbstractQuery;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TodayTripActivity extends AppCompatActivity {
    String Date = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_today_trip);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
       final ProgressBar PBarToday = (ProgressBar)findViewById(R.id.PBarToday);

        Client mKinveyClient = new Client.Builder(this.getApplicationContext()).build();

        mKinveyClient.user().login("Admin", "Admin", new KinveyClientCallback<User>() {
            @Override
            public void onSuccess(User user) {

            }

            @Override
            public void onFailure(Throwable throwable) {
                Toast.makeText(TodayTripActivity.this, throwable.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        String date = new SimpleDateFormat("dd/MM/yyyy", Locale.US).format(new Date());
        Query query = mKinveyClient.query();
        query.equals("Date", date);
        query.addSort("Date", AbstractQuery.SortOrder.DESC);
        AsyncAppData<GenericJson> myData = mKinveyClient.appData("Travels", GenericJson.class);
        myData.get(query,new KinveyListCallback<GenericJson>() {
            @Override
            public void onSuccess(GenericJson[] genericJsons) {
              //  Toast.makeText(TodayTripActivity.this, String.valueOf(genericJsons.length), Toast.LENGTH_SHORT).show();
                try {
                    PBarToday.setVisibility(View.INVISIBLE);
                    SetupListView(genericJsons);
                } catch (Exception e) {
                }

            }

            @Override
            public void onFailure(Throwable throwable) {
                PBarToday.setVisibility(View.INVISIBLE);
                Toast.makeText(TodayTripActivity.this, throwable.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
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

    private void SetupListView(final GenericJson[] genericJsons) {

        ListView list = (ListView) findViewById(R.id.listTodayTravels);

        list.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return genericJsons.length;
            }

            @Override
            public Object getItem(int position) {
                return null;
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                LayoutInflater linflater = getLayoutInflater();
                View view1 = linflater.inflate(R.layout.list_tecket_today, null);
                TextView txtTicketTravelDriverName = (TextView) view1.findViewById(R.id.txtTicketTravelDriverName);
                TextView txtTicketTravelCustomerName = (TextView) view1.findViewById(R.id.txtTicketTravelCustomerName);
                TextView txtTicketCustomerPhone = (TextView) view1.findViewById(R.id.txtTicketTravelCustomerPhone);
                TextView txtID = (TextView) view1.findViewById(R.id.txtID);
                TextView txtTicketTravelLocation = (TextView) view1.findViewById(R.id.txtTicketTravelLocation);
                TextView txtTicketTravelDestination = (TextView) view1.findViewById(R.id.txtTicketTravelDestination);
                TextView txtTicketTravelNote = (TextView) view1.findViewById(R.id.txtTicketTravelNote);
                TextView txtTicketTravelDriverPhone = (TextView) view1.findViewById(R.id.txtTicketTravelDriverPhone);
                TextView txtTicketTravelTime = (TextView) view1.findViewById(R.id.txtTicketTravelTime);
                txtID.setText(genericJsons[position].get("_id").toString());
                txtTicketTravelDriverName.setText(genericJsons[position].get("Driver_Name").toString());
                txtTicketTravelCustomerName.setText(genericJsons[position].get("Customer_Name").toString());
                txtTicketCustomerPhone.setText(genericJsons[position].get("Customer_Phone_No").toString());
                txtTicketTravelLocation.setText(genericJsons[position].get("Customer_Location").toString());
                txtTicketTravelDestination.setText(genericJsons[position].get("Customer_Destination").toString());
                txtTicketTravelNote.setText(genericJsons[position].get("Note").toString());
                txtTicketTravelDriverPhone.setText( genericJsons[position].get("Driver_Phone_No").toString());
                Date = genericJsons[position].get("Date").toString();
                txtTicketTravelTime.setText(genericJsons[position].get("Time").toString());
                Animation animation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.design_bottom_sheet_slide_in);
                view1.startAnimation(animation);

                return view1;
            }
        });
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView txtTicketTravelDriverName = (TextView) view.findViewById(R.id.txtTicketTravelDriverName);
                TextView txtTicketTravelCustomerName = (TextView) view.findViewById(R.id.txtTicketTravelCustomerName);
                TextView txtTicketCustomerPhone = (TextView)view. findViewById(R.id.txtTicketTravelCustomerPhone);
                TextView txtID = (TextView)view. findViewById(R.id.txtID);
                TextView txtTicketTravelLocation = (TextView) view.findViewById(R.id.txtTicketTravelLocation);
                TextView txtTicketTravelDestination = (TextView) view.findViewById(R.id.txtTicketTravelDestination);
                TextView txtTicketTravelNote = (TextView)view. findViewById(R.id.txtTicketTravelNote);
                TextView txtTicketTravelDriverPhone = (TextView)view. findViewById(R.id.txtTicketTravelDriverPhone);
                TextView txtTicketTravelTime = (TextView)view. findViewById(R.id.txtTicketTravelTime);
                Intent starter = new Intent(getApplicationContext(), CancelActivity.class);
                Bundle bu = new Bundle();
                bu.putString("_id", txtID.getText().toString());
                bu.putString("Driver_Name", txtTicketTravelDriverName.getText().toString());
                bu.putString("Customer_Name", txtTicketTravelCustomerName.getText().toString());
                bu.putString("Customer_Phone_No", txtTicketCustomerPhone.getText().toString());
                bu.putString("Customer_Location", txtTicketTravelLocation.getText().toString());
                bu.putString("Customer_Destination", txtTicketTravelDestination.getText().toString());
                bu.putString("Driver_Phone_No",txtTicketTravelDriverPhone.getText().toString());
                bu.putString("Date",Date);
                bu.putString("Time",txtTicketTravelTime.getText().toString());
                bu.putString("Note", txtTicketTravelNote.getText().toString());
                starter.putExtras(bu);
                startActivity(starter);


            }
        });


    }

}