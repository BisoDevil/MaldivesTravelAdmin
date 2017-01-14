package com.globalapp.maldivestravel;

        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.view.LayoutInflater;
        import android.view.MenuItem;
        import android.view.View;
        import android.view.ViewGroup;
        import android.view.animation.Animation;
        import android.view.animation.AnimationUtils;
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
        import com.kinvey.java.query.AbstractQuery;

public class FeedbackActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final ProgressBar PBarFeed = (ProgressBar)findViewById(R.id.PBarFeed);

        Client mKinveyClient = new Client.Builder(this.getApplicationContext()).build();
        Query query = mKinveyClient.query();

        query.addSort("Date", AbstractQuery.SortOrder.DESC);
        AsyncAppData<GenericJson> myData = mKinveyClient.appData("Feedback", GenericJson.class);
        myData.get(query, new KinveyListCallback<GenericJson>() {
            @Override
            public void onSuccess(GenericJson[] genericJsons) {
                try {
                    PBarFeed.setVisibility(View.INVISIBLE);
                    SetupListView(genericJsons);
                } catch (Exception ex) {

                }

            }

            @Override
            public void onFailure(Throwable throwable) {
                PBarFeed.setVisibility(View.INVISIBLE);
                Toast.makeText(FeedbackActivity.this, throwable.getMessage(), Toast.LENGTH_SHORT).show();

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

        ListView list = (ListView) findViewById(R.id.listViewFeedback);
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
                View view1 = linflater.inflate(R.layout.list_tecket_feedback, null);
                TextView txtTicket_Comment = (TextView) view1.findViewById(R.id.txtTicket_Comment);
                TextView txtTicket_PhoneNumber = (TextView) view1.findViewById(R.id.txtTicket_PhoneNumber);
                TextView txtTicketRate = (TextView) view1.findViewById(R.id.txtTicketRate);
                TextView txtTicket_date = (TextView) view1.findViewById(R.id.txtTicket_date);
                txtTicket_Comment.setText(genericJsons[position].get("Comments").toString());
                txtTicket_PhoneNumber.setText(genericJsons[position].get("Customer_Phone_No").toString());
                txtTicketRate.setText(genericJsons[position].get("Rating").toString());
                txtTicket_date.setText(genericJsons[position].get("Date").toString());
                Animation animation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.design_bottom_sheet_slide_in);
                view1.startAnimation(animation);

                return view1;
            }
        });


    }

}