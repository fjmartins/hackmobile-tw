package com.tw.hackmob.saferide;

import android.app.Activity;
import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;

public class NewRouteActivity extends AppCompatActivity {

    private TextInputEditText tietFrom;
    private TextInputEditText tietTo;

    private final int FROM = 2;
    private final int TO = 3;
    private final int TIME = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_route);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(getResources().getString(R.string.adicionar_rota));
        actionBar.setDisplayHomeAsUpEnabled(true);

        tietFrom = (TextInputEditText) findViewById(R.id.from);
        tietTo = (TextInputEditText) findViewById(R.id.to);

        tietFrom.setFocusable(false);
        tietFrom.setFocusableInTouchMode(false);
        tietFrom.setClickable(true);

        tietTo.setFocusable(false);
        tietTo.setFocusableInTouchMode(false);
        tietTo.setClickable(true);

        tietFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent =
                            new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                                    .build(NewRouteActivity.this);
                    startActivityForResult(intent, FROM);
                } catch (GooglePlayServicesRepairableException e) {
                    // TODO: Solucionar o erro.
                } catch (GooglePlayServicesNotAvailableException e) {
                    // TODO: Solucionar o erro.
                }
            }
        });

        tietTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent =
                            new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
                                    .build(NewRouteActivity.this);
                    startActivityForResult(intent, TO);
                } catch (GooglePlayServicesRepairableException e) {
                    // TODO: Solucionar o erro.
                } catch (GooglePlayServicesNotAvailableException e) {
                    // TODO: Solucionar o erro.
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK) {
            Place place = PlaceAutocomplete.getPlace(this, data);

            switch (requestCode) {
                case FROM:
                    tietFrom.setText(place.getAddress());
                    break;
                case TO:
                    tietTo.setText(place.getAddress());
                    break;
            }
        }
    }
}
