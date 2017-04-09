package com.tw.hackmob.saferide;

import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tw.hackmob.saferide.async.DirectionAsync;
import com.tw.hackmob.saferide.listener.DirectionListener;
import com.tw.hackmob.saferide.model.Location;
import com.tw.hackmob.saferide.model.Route;
import com.tw.hackmob.saferide.utils.Session;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.Random;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback, DirectionListener {

    private static final int ADD_NEW_ROUTE = 50;
    private GoogleMap mMap;
    private FloatingActionButton fab;

    private FirebaseDatabase mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(getString(R.string.minhas_rotas));
        actionBar.setDisplayHomeAsUpEnabled(true);

        fab = (FloatingActionButton) findViewById(R.id.addRota);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MapActivity.this, NewRouteActivity.class);
                startActivityForResult(intent, ADD_NEW_ROUTE);
            }
        });

        mDatabase = FirebaseDatabase.getInstance();
        readAll();
    }

    public void readAll() {
        mDatabase.getReference().child("routes").orderByChild("owner").equalTo(Session.getUser(this).getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                //List<Route> routes = new ArrayList<Route>();

                Route route;
                LatLng latlngFrom = null, latlngTo;
                for (DataSnapshot routeSnapshot : snapshot.getChildren()) {
                    route = routeSnapshot.getValue(Route.class);
                    new DirectionAsync(MapActivity.this).execute(route);
                    latlngFrom = getLocation(route.getFrom());
                    latlngTo = getLocation(route.getTo());
                    mMap.addMarker(new MarkerOptions().position(latlngFrom));
                    mMap.addMarker(new MarkerOptions().position(latlngTo));
                }
                if(latlngFrom != null) {
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latlngFrom, 13));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                if (databaseError != null) {

                }
            }
        });
    }


    private LatLng getLocation(Location from){
        return new LatLng(from.getLatitude(), from.getLongitude());
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
    }

    public ArrayList<LatLng> getDirection(Document doc) {
        NodeList nl1, nl2, nl3;
        ArrayList<LatLng> listGeopoints = new ArrayList<LatLng>();
        nl1 = doc.getElementsByTagName("step");
        if (nl1.getLength() > 0) {
            for (int i = 0; i < nl1.getLength(); i++) {
                Node node1 = nl1.item(i);
                nl2 = node1.getChildNodes();

                Node locationNode = nl2
                        .item(getNodeIndex(nl2, "start_location"));
                nl3 = locationNode.getChildNodes();
                Node latNode = nl3.item(getNodeIndex(nl3, "lat"));
                double lat = Double.parseDouble(latNode.getTextContent());
                Node lngNode = nl3.item(getNodeIndex(nl3, "lng"));
                double lng = Double.parseDouble(lngNode.getTextContent());
                listGeopoints.add(new LatLng(lat, lng));

                locationNode = nl2.item(getNodeIndex(nl2, "polyline"));
                nl3 = locationNode.getChildNodes();
                latNode = nl3.item(getNodeIndex(nl3, "points"));
                ArrayList<LatLng> arr = decodePoly(latNode.getTextContent());
                for (int j = 0; j < arr.size(); j++) {
                    listGeopoints.add(new LatLng(arr.get(j).latitude, arr
                            .get(j).longitude));
                }

                locationNode = nl2.item(getNodeIndex(nl2, "end_location"));
                nl3 = locationNode.getChildNodes();
                latNode = nl3.item(getNodeIndex(nl3, "lat"));
                lat = Double.parseDouble(latNode.getTextContent());
                lngNode = nl3.item(getNodeIndex(nl3, "lng"));
                lng = Double.parseDouble(lngNode.getTextContent());
                listGeopoints.add(new LatLng(lat, lng));
            }
        }

        return listGeopoints;
    }

    private int getNodeIndex(NodeList nl, String nodename) {
        for (int i = 0; i < nl.getLength(); i++) {
            if (nl.item(i).getNodeName().equals(nodename))
                return i;
        }
        return -1;
    }

    private ArrayList<LatLng> decodePoly(String encoded) {
        ArrayList<LatLng> poly = new ArrayList<LatLng>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;
        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;
            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng position = new LatLng((double) lat / 1E5, (double) lng / 1E5);
            poly.add(position);
        }
        return poly;
    }

    @Override
    public void getDocument(Document document) {
        Random rnd = new Random();
        int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));

        ArrayList<LatLng> directionPoint = getDirection(document);
        PolylineOptions rectLine = new PolylineOptions().width(3).color(color);

        for (int i = 0; i < directionPoint.size(); i++) {
            rectLine.add(directionPoint.get(i));
        }

        Polyline polylin = mMap.addPolyline(rectLine);
        mMap.setOnPolylineClickListener(new GoogleMap.OnPolylineClickListener() {
            @Override
            public void onPolylineClick(Polyline polyline) {

            }
        });
    }
}
