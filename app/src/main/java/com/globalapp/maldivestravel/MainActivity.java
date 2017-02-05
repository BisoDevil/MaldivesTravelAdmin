package com.globalapp.maldivestravel;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.app.ActivityCompat;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.api.client.json.GenericJson;
import com.kinvey.android.AsyncAppData;
import com.kinvey.android.Client;
import com.kinvey.android.callback.KinveyListCallback;
import com.kinvey.java.User;
import com.kinvey.java.core.KinveyClientCallback;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback {
    private GoogleMap mMap;
    private Location GPS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //Enabling Broadcast
        try {
            Client mKinveyClient = new Client.Builder(this.getApplicationContext()).build();
            mKinveyClient.push().initialize(getApplication());
        } catch (Exception ex) {
            Toast.makeText(this, ex.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        switch (id) {
            case (R.id.nav_addDriver):
                Intent starter = new Intent(getApplicationContext(), AddDriverActivity.class);
                startActivity(starter);
                break;
            case (R.id.nav_AssignTrip):
                Intent coming = new Intent(getApplicationContext(), AssignTripActivity.class);
                startActivity(coming);


                break;
            case (R.id.nav_myNotificatios):
                Intent Notify = new Intent(getApplicationContext(), SendNotificationActivity.class);
                startActivity(Notify);


                break;
            case (R.id.nav_feedback):
                Intent fdBack = new Intent(getApplicationContext(), FeedbackActivity.class);
                startActivity(fdBack);

                break;
            case (R.id.nav_todayTrips):

                Client mKinveyClient = new Client.Builder(this.getApplicationContext()).build();
                mKinveyClient.user().logout().execute();
                Intent Main = new Intent(getApplicationContext(), TodayTripActivity.class);
                startActivity(Main);

                break;

        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    final int MY_PERMISSIONS_REQUEST_Location = 123;

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {

            case (MY_PERMISSIONS_REQUEST_Location):
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {


                } else {
                    Toast.makeText(this, "Sorry, we need it", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if ((int) Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.ACCESS_FINE_LOCATION)) {

                    // Show an expanation to the user *asynchronously* -- don't block
                    // this thread waiting for the user's response! After the user
                    // sees the explanation, try again to request the permission.

                } else {

                    // No explanation needed, we can request the permission.

                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                            MY_PERMISSIONS_REQUEST_Location);

                    // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                    // app-defined int constant. The callback method gets the
                    // result of the request.

                }
            }
            return;
        }

        mMap.setMyLocationEnabled(true);
        mMap.setBuildingsEnabled(true);
        mMap.setIndoorEnabled(true);
        mMap.setTrafficEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        LocationManager Locationmanager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        GPS = Locationmanager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (GPS == null) {
            GPS = Locationmanager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
        }

        try {
            LatLng Center = new LatLng(GPS.getLatitude(), GPS.getLongitude());
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(Center, 15));
        } catch (Exception ex) {
            ex.printStackTrace();

        }

        Tracking();

        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {
                LinearLayout info = new LinearLayout(getApplicationContext());
                info.setOrientation(LinearLayout.VERTICAL);

                TextView title = new TextView(getApplicationContext());
                title.setTextColor(Color.BLACK);
                title.setGravity(Gravity.CENTER);
                title.setTypeface(null, Typeface.BOLD);
                title.setText(marker.getTitle());

                TextView snippet = new TextView(getApplicationContext());
                snippet.setTextColor(Color.GRAY);
                snippet.setText(marker.getSnippet());

                info.addView(title);
                info.addView(snippet);

                return info;
            }
        });
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(marker.getPosition(), 16));
                marker.showInfoWindow();
                return true;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onStart() {
        super.onStart();
        Loginuser();
    }

    Marker[] mMarker = {null, null, null, null, null, null, null, null, null, null, null
            , null, null, null, null, null, null, null, null, null, null, null, null
            , null, null, null, null, null, null, null};

    CountDownTimer timer = null;

    private void Tracking() {

        timer = new CountDownTimer(10000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

                Client mKinveyClient = new Client.Builder(getApplicationContext()).build();
                AsyncAppData<GenericJson> myevents = mKinveyClient.appData("Tracking", GenericJson.class);
                myevents.get(new KinveyListCallback<GenericJson>() {
                    @Override
                    public void onSuccess(GenericJson[] genericJsons) {
                        try {

                            for (int n = 0; n < genericJsons.length; n++) {

                                LatLng latlong = new LatLng(Double.valueOf(genericJsons[n].get("lat").toString()),
                                        Double.valueOf(genericJsons[n].get("long").toString()));
                                String driver = genericJsons[n].get("driver").toString();
                                String car = "Car No : " + genericJsons[n].get("car").toString();

                                String speed = "Speed : " + genericJsons[n].get("speed").toString() + " Km/h";


                                if (mMarker[n] == null) {

                                    mMarker[n] = mMap.addMarker(new MarkerOptions().position(latlong)
                                            .title(driver)
                                            .snippet(car + "\n" + speed)

                                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.car_pin))

                                    );
                                    mMarker[n].showInfoWindow();
                                } else {
                                    mMarker[n].setSnippet(car + "\n" + speed);
                                    animateMarker(mMarker[n], latlong, false);
                                    if (mMarker[n].isInfoWindowShown()) {
                                        mMarker[n].showInfoWindow();

                                    } else {
                                        mMarker[n].hideInfoWindow();
                                    }


                                }
//

                            }
                        } catch (Exception ex) {
                            //    Toast.makeText(MainActivity.this, ex.getMessage(), Toast.LENGTH_SHORT).show();

                        }

                    }

                    @Override
                    public void onFailure(Throwable throwable) {
                        //   Toast.makeText(MainActivity.this, throwable.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                });

            }

            @Override
            public void onFinish() {

                Loginuser();
                timer.start();

            }
        };
        timer.start();

    }

    public void animateMarker(final Marker marker, final LatLng toPosition,
                              final boolean hideMarker) {
        final Handler handler = new Handler();
        final long start = SystemClock.uptimeMillis();
        Projection proj = mMap.getProjection();
        Point startPoint = proj.toScreenLocation(marker.getPosition());
        final LatLng startLatLng = proj.fromScreenLocation(startPoint);
        final long duration = 500;

        final Interpolator interpolator = new LinearInterpolator();

        handler.post(new Runnable() {
            @Override
            public void run() {
                long elapsed = SystemClock.uptimeMillis() - start;
                float t = interpolator.getInterpolation((float) elapsed
                        / duration);
                double lng = t * toPosition.longitude + (1 - t)
                        * startLatLng.longitude;
                double lat = t * toPosition.latitude + (1 - t)
                        * startLatLng.latitude;
                marker.setPosition(new LatLng(lat, lng));

                if (t < 1.0) {
                    // Post again 16ms later.
                    handler.postDelayed(this, 16);
                } else {
                    if (hideMarker) {
                        marker.setVisible(false);
                    } else {
                        marker.setVisible(true);
                    }
                }
            }
        });
    }

    public void Loginuser() {
        Client mKinveyClient = new Client.Builder(getApplicationContext()).build();
        if (!mKinveyClient.user().isUserLoggedIn()) {
            mKinveyClient.user().login("Admin", "Admin", new KinveyClientCallback<User>() {
                @Override
                public void onSuccess(User user) {

                }

                @Override
                public void onFailure(Throwable throwable) {

                }
            });
        }


    }
}
