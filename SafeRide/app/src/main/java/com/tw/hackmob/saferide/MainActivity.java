package com.tw.hackmob.saferide;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tw.hackmob.saferide.model.Location;
import com.tw.hackmob.saferide.model.Route;
import com.tw.hackmob.saferide.model.User;
import com.tw.hackmob.saferide.utils.Data;
import com.tw.hackmob.saferide.utils.Session;
import com.tw.hackmob.saferide.utils.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, GoogleApiClient.OnConnectionFailedListener {

    private static final int ADD_NEW_ROUTE = 50;

    private int REQUEST_LOCATION = 54;
    private GoogleApiClient mGoogleApiClient;
    private Place to;
    @BindView(R.id.forWhere)
    EditText mForWhere;

    int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.inflateHeaderView(R.layout.nav_header_main);

        User user = Session.getUser(this);

        TextView name = (TextView) headerView.findViewById(R.id.name);
        name.setText(user.getName());

        TextView email = (TextView) headerView.findViewById(R.id.email);
        email.setText(user.getEmail());

        mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(this, this)
                .build();

        mForWhere.setFocusable(false);
        mForWhere.setFocusableInTouchMode(false);
        mForWhere.setClickable(true);
        mForWhere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent intent =
                            new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN).build(MainActivity.this);
                    startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
                } catch (GooglePlayServicesRepairableException e) {
                    // TODO: Solucionar o erro.
                } catch (GooglePlayServicesNotAvailableException e) {
                    // TODO: Solucionar o erro.
                }
            }
        });

        requestLocation();
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


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.navPedirCarona) {
            // Handle the camera action
        } else if (id == R.id.navAdicionarRota) {
            Intent intent = new Intent(MainActivity.this, MapActivity.class);
            startActivityForResult(intent, ADD_NEW_ROUTE);
        } else if (id == R.id.logout) {
            FirebaseAuth.getInstance().signOut();

            Data.saveUser(this, null);

            Intent i = new Intent(MainActivity.this, StartActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                final Place place = PlaceAutocomplete.getPlace(this, data);
                to = place;
                final Location loc = Utils.getCurrentLocation(MainActivity.this);

                FirebaseDatabase.getInstance().getReference().child("routes").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Route bestRoute = null;
                        float bestFrom = Float.MAX_VALUE;
                        float bestTo = Float.MAX_VALUE;

                        for (DataSnapshot data : dataSnapshot.getChildren()) {
                            Route route = data.getValue(Route.class);

                            float radius = 50000;

                            float[] distanceFrom = new float[2];
                            float[] distanceTo = new float[2];

                            android.location.Location.distanceBetween(route.getFrom().getLatitude(), route.getFrom().getLongitude(),
                                    loc.getLatitude(), loc.getLongitude(), distanceFrom);

                            android.location.Location.distanceBetween(route.getTo().getLatitude(), route.getTo().getLongitude(),
                                    to.getLatLng().latitude, to.getLatLng().longitude, distanceTo);

                            if (distanceFrom[0] < radius && distanceTo[0] < radius) { //Inside
                                if (distanceFrom[0] < bestFrom && distanceTo[0] < bestTo) {
                                    bestRoute = route;
                                    bestFrom = distanceFrom[0];
                                    bestTo = distanceTo[0];
                                }
                            } else { //Outside

                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                mForWhere.setText(place.getName());
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
    }

    private void requestLocation() {
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)) {
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        REQUEST_LOCATION);
            }
        }
    }

}
