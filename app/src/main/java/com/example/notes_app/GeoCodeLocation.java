package com.example.notes_app;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class GeoCodeLocation {
    public interface LocationCallback {
        void onLocationResult(String result);
    }
    private static final String TAG = "GeoCodeLocation";
    public static void getAddressFromLocation(final String locationAddress,
                                              final Context context,
                                              final LocationCallback callback) {
        Thread thread = new Thread() {
            @Override
            public void run() {
                Geocoder geocoder = new Geocoder(context, Locale.getDefault());
                String result = null;
                try {
                    List<Address> addressList = geocoder.getFromLocationName(locationAddress, 1);
                    if (addressList != null && addressList.size() > 0) {
                        Address address = addressList.get(0);
                        StringBuilder sb = new StringBuilder();
                        sb.append(locationAddress).append(" ");
                        sb.append(address.getLatitude()).append(" ");
                        sb.append(address.getLongitude()).append(" ");
                        result = sb.toString();
                    }
                } catch (IOException e) {
                    Log.e(TAG, "Unable to connect to Geocoder", e);
                } finally {
                    if (callback != null) {
                        callback.onLocationResult(result);
                    }
                }
            }
        };
        thread.start();
    }
}
