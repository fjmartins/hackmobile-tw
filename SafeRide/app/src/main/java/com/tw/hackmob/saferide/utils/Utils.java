package com.tw.hackmob.saferide.utils;

import android.content.Context;
import android.location.LocationManager;
import android.util.Log;

import com.tw.hackmob.saferide.model.Location;

import java.util.List;
import java.util.UUID;

import static android.content.Context.LOCATION_SERVICE;

/**
 * Created by phgm on 08/04/2017.
 */

public class Utils {
    public static String getUUID() {
        return UUID.randomUUID().toString();
    }

    public static String emailToUser(String email) {
        return email.split("@")[0].replaceAll("[^\\w\\s\\-_]", "");
    }

    public static Location getCurrentLocation(Context pContext) throws SecurityException {
        LocationManager mLocationManager;

        mLocationManager = (LocationManager) pContext.getSystemService(LOCATION_SERVICE);
        List<String> providers = mLocationManager.getProviders(true);
        android.location.Location bestLocation = null;
        for (String provider : providers) {
            android.location.Location l = mLocationManager.getLastKnownLocation(provider);
            if (l == null) continue;
            if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                bestLocation = l;
            }
        }

        if (bestLocation == null) return null;

        Location loc = new Location(bestLocation.getLatitude(), bestLocation.getLongitude());

        return loc;
    }
}
