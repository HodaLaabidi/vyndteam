package com.vyndsolutions.vyndteam.utils;


import android.content.Context;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Hoda on 29/04/2018.
 */


public class ConnectivityService {
    /*----Method to Check GPS is enable or disable ----- */
    public static Boolean displayGpsStatus(LocationManager location_manager) {
        //Settings.Secure.isLocationProviderEnabled(contentResolver, LocationManager.GPS_PROVIDER);
        return location_manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    /*----Method to Check Network is enable or disable ----- */
    public static boolean isOnline(Context context) {
        if (context != null) {
            ConnectivityManager cm =
                    (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo network = cm.getActiveNetworkInfo();
            return network != null &&
                    network.isConnectedOrConnecting();
        }
        return false;
    }

}
