package com.tw.hackmob.saferide;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TimePicker;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NewRouteActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener{

    @BindView(R.id.from)
    TextInputEditText tietFrom;

    @BindView(R.id.to)
    TextInputEditText tietTo;

    @BindView(R.id.hora)
    TextInputEditText tietHora;

    private final int FROM = 2;
    private final int TO = 3;
    private final int TIME = 4;

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

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        tietHora.setText(hourOfDay + ":" + minute);
    }
}
