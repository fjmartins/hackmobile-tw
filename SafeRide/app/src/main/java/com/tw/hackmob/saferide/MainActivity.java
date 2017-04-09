package com.tw.hackmob.saferide;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
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
import com.tw.hackmob.saferide.adapter.RouteAdapter;
import com.tw.hackmob.saferide.async.NotificationAsync;
import com.tw.hackmob.saferide.listener.OnItemClickListener;
import com.tw.hackmob.saferide.model.Location;
import com.tw.hackmob.saferide.model.Request;
import com.tw.hackmob.saferide.model.Route;
import com.tw.hackmob.saferide.model.User;
import com.tw.hackmob.saferide.utils.Data;
import com.tw.hackmob.saferide.utils.Session;
import com.tw.hackmob.saferide.utils.Utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, GoogleApiClient.OnConnectionFailedListener, OnItemClickListener {

    private static final int ADD_NEW_ROUTE = 50;

    private int REQUEST_LOCATION = 54;
    private GoogleApiClient mGoogleApiClient;
    private Place to;
    @BindView(R.id.forWhere)
    EditText mForWhere;

    @BindView(R.id.coordinatorLayout)
    CoordinatorLayout coordinatorLayout;

    private RouteAdapter mAdapter;

    private FirebaseDatabase mDatabase;

    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;

    int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;

    private ProgressDialog mLoading;

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

        mDatabase = FirebaseDatabase.getInstance();

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

        mAdapter = new RouteAdapter(new ArrayList<Route>(), MainActivity.this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setAdapter(mAdapter);
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
        } else if (id == R.id.navRequests) {
            Intent intent = new Intent(MainActivity.this, RequestRidesActivity.class);
            startActivity(intent);
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
                mLoading = ProgressDialog.show(this, null, getString(R.string.loading), true);

                final Place place = PlaceAutocomplete.getPlace(this, data);
                final Location loc = Utils.getCurrentLocation(MainActivity.this);
                to = place;

                final List<Route> routes = new ArrayList<>();

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

                                    routes.add(bestRoute);
                                }
                            } else { //Outside
                            }
                        }

                        if(routes.size() > 0) {
                            Collections.reverse(routes);

                            mAdapter.setRoutes(routes);
                            mAdapter.notifyDataSetChanged();
                        } else {
                            Snackbar snackbar = Snackbar.make(coordinatorLayout, getString(R.string.no_rides), Snackbar.LENGTH_LONG);
                            snackbar.show();
                        }

                        mLoading.dismiss();
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

    @Override
    public void onItemClick(Route route) {
        String[] params = new String[] {
                route.getOwner().getToken(),
                "Pedido de Carona",
                Session.getUser(this).getName() + " está pedindo uma carona",
                "requestRoute" };

        Request request = new Request();
        request.setUid(Utils.getUUID());
        request.setRoute(route);
        request.setUserOwner(route.getOwner());
        request.setUserOwnerUid(route.getOwnerUid());
        request.setUserRequest(Session.getUser(this));
        request.setUserRequestUid(Session.getUser(this).getUid());

        mDatabase.getReference().child("requests").child(request.getUid()).setValue(request);

        Snackbar snackbar = Snackbar.make(coordinatorLayout, getString(R.string.success), Snackbar.LENGTH_LONG);
        snackbar.show();

        new NotificationAsync(this).execute(params);
    }

    @Override
    public boolean onItemLongClick(View view, Route route) {
        return false;
    }
}
