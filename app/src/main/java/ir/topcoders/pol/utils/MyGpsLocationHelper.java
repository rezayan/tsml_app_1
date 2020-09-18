package ir.topcoders.pol.utils;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;

import com.google.android.gms.maps.LocationSource;

public class MyGpsLocationHelper implements LocationSource, LocationListener {

    private OnLocationChangedListener mListener;
    private LocationManager locationManager;

    public MyGpsLocationHelper(Context context) {
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
    }

    @Override
    public void activate(OnLocationChangedListener listener) {
        mListener = listener;
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, AppConstants.LOCATION_UPDATE_INTERVALS_IN_MILLIS, AppConstants.MAX_DISTANCE_CHANGE_TOLERANCE_IN_METERS, this);
    }

    /* Deactivates this provider.
     * This method is automatically invoked by disabling my-location layer. */
    @Override
    public void deactivate() {
        // Remove location updates from Location Manager
        locationManager.removeUpdates(this);

        mListener = null;
    }

    @Override
    public void onLocationChanged(Location location) {
        if (mListener != null)
            mListener.onLocationChanged(location);
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {
    }

    @Override
    public void onProviderEnabled(String s) {
    }

    @Override
    public void onProviderDisabled(String s) {
    }
}