package com.tw.hackmob.saferide;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.firebase.database.FirebaseDatabase;
import com.tw.hackmob.saferide.model.Location;
import com.tw.hackmob.saferide.model.Route;
import com.tw.hackmob.saferide.model.User;
import com.tw.hackmob.saferide.utils.Session;
import com.tw.hackmob.saferide.utils.Utils;

import java.util.Calendar;
import java.util.Locale;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NewRouteActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener{

    Place from;
    Place to;

    @BindView(R.id.from)
    TextInputEditText tietFrom;

    @BindView(R.id.to)
    TextInputEditText tietTo;

    @BindView(R.id.hora)
    TextInputEditText tietHora;

    @BindView(R.id.register)
    Button register;

    private final int FROM = 2;
    private final int TO = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_route);
        ButterKnife.bind(this);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(getResources().getString(R.string.adicionar_rota));
        actionBar.setDisplayHomeAsUpEnabled(true);

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
                            new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                                    .build(NewRouteActivity.this);
                    startActivityForResult(intent, TO);
                } catch (GooglePlayServicesRepairableException e) {
                    // TODO: Solucionar o erro.
                } catch (GooglePlayServicesNotAvailableException e) {
                    // TODO: Solucionar o erro.
                }
            }
        });

        tietHora.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar time = Calendar.getInstance();
                int hour = time.get(Calendar.HOUR_OF_DAY);
                int minute = time.get(Calendar.MINUTE);
                TimePickerDialog mTimerPickerDialog = new TimePickerDialog(NewRouteActivity.this, NewRouteActivity.this, hour, minute, true);
                mTimerPickerDialog.show();
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User user = Session.getUser(NewRouteActivity.this);

                Route r = new Route();
                r.setOwner(user.getUid());
                r.setTime(tietHora.getText().toString());
                r.setFrom(new Location(from.getLatLng().latitude, from.getLatLng().longitude));
                r.setTo(new Location(to.getLatLng().latitude, to.getLatLng().longitude));

                try {
                    FirebaseDatabase.getInstance().getReference().child("routes").child(Utils.getUUID()).setValue(r);
                } catch (Exception e) {
                    Log.e("Erro", e.getMessage());
                }

                finish();
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
                    from = place;
                    break;
                case TO:
                    tietTo.setText(place.getAddress());
                    to = place;
                    break;
            }
        }
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        tietHora.setText(hourOfDay + ":" + minute);
    }
}
