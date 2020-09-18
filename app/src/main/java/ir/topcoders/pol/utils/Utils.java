package ir.topcoders.pol.utils;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.content.ContextCompat;

import java.util.Calendar;
import java.util.Date;

import javax.inject.Inject;

import ir.topcoders.pol.utils.persian_tools.PersianCalendar;

public class Utils {

    private final Context context;

    @Inject
    public Utils(Context context) {
        this.context = context;
    }

    public boolean isConnectedToInternet() {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity == null)
            return false;

        NetworkInfo[] networks = connectivity.getAllNetworkInfo();
        for (NetworkInfo info : networks)
            if (info.isConnected())
                return true;
        return false;
    }


    public boolean hasLocationAccess() {
        return ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    public boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        return locationManager != null && (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER));
    }

    public static String getPersianDateAndTime(String separator) {
        PersianCalendar cal = new PersianCalendar();
        cal.setTimeInMillis(System.currentTimeMillis());
        String currentDate = cal.getSimpleDateAsString(separator);

        Date currentTime = Calendar.getInstance().getTime();

        return currentDate + "_" + StringUtils.format("%02d", currentTime.getHours()) + separator + StringUtils.format("%02d", currentTime.getMinutes()) + separator + StringUtils.format("%02d", currentTime.getSeconds());
    }

    public static String getPersianDateAndTimeForRecord() {
        PersianCalendar cal = new PersianCalendar();
        cal.setTimeInMillis(System.currentTimeMillis());
        String currentDate = cal.getSimpleDateAsString("/");

        Date currentTime = Calendar.getInstance().getTime();

        return currentDate + " " + StringUtils.format("%02d", currentTime.getHours()) + ":" + StringUtils.format("%02d", currentTime.getMinutes()) + ":" + StringUtils.format("%02d", currentTime.getSeconds());
    }
}
