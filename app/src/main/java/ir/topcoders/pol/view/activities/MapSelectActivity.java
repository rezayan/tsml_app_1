package ir.topcoders.pol.view.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.LocationSource;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

import java.io.File;
import java.util.UUID;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import ir.topcoders.pol.R;
import ir.topcoders.pol.databinding.ActivityMapSelectBinding;
import ir.topcoders.pol.utils.AppConstants;
import ir.topcoders.pol.utils.ImageUtils;
import ir.topcoders.pol.utils.MyLocationSource;
import ir.topcoders.pol.utils.Utils;

public class MapSelectActivity extends AppCompatActivity implements OnMapReadyCallback, LocationSource.OnLocationChangedListener {

    public static final String RESULT_PARAM_TEMP_IMAGE_PATH = "RESULT_PARAM_TEMP_IMAGE_PATH";
    private static final int LOCATION_ACCESS_PERMISSION_REQUEST_CODE = 1000;
    private static final int ENABLE_LOCATION_REQUEST_CODE = 2000;

    @Inject
    Utils utils;

    ActivityMapSelectBinding binding;
    GoogleMap googleMap;
    MyLocationSource locationSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidInjection.inject(this);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_map_select);

        initToolbar();
        setupMap();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_map, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save_image:
                Toast.makeText(this, "در حال آماده سازی تصویر...", Toast.LENGTH_SHORT).show();
                googleMap.snapshot(bitmap -> {
                    try {
                        String tempFile = AppConstants.getTempStorageFolder(this) + UUID.randomUUID() + ".jpg";
                        ImageUtils.writeToFile(bitmap, new File(tempFile));
                        Intent intent = new Intent();
                        intent.putExtra(RESULT_PARAM_TEMP_IMAGE_PATH, tempFile);
                        setResult(RESULT_OK, intent);
                        finish();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
                return true;
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initToolbar() {
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    private void setupMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        this.googleMap.setMyLocationEnabled(true);
        this.googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);

        checkLocationPermission();
    }

    private void checkLocationPermission() {
        if (utils.hasLocationAccess())
            checkLocationEnabled();
        else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_ACCESS_PERMISSION_REQUEST_CODE);
            binding.setMessage(getString(R.string.message_location_access_need));
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        if (requestCode == LOCATION_ACCESS_PERMISSION_REQUEST_CODE && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            checkLocationEnabled();
    }

    private void checkLocationEnabled() {
        if (utils.isLocationEnabled())
            setupLocation();
        else
            binding.setMessage(getString(R.string.message_location_disabled));
    }

    @SuppressLint("MissingPermission")
    private void setupLocation() {
        if (this.googleMap != null && utils.hasLocationAccess()) {
            binding.setMessage(null);
            locationSource = new MyLocationSource(getApplicationContext(), this);
            this.googleMap.setMyLocationEnabled(true);
            this.googleMap.setLocationSource(locationSource);
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        moveCameraToLocation(location);
    }

    private void moveCameraToLocation(Location location) {
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(location.getLatitude(), location.getLongitude()))
                .zoom(16)
                .build();
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    public void onLocationRequestClick(View view) {
        if (!utils.hasLocationAccess())
            checkLocationPermission();
        else
            requestEnableLocation();
    }

    private void requestEnableLocation() {
        Intent gpsOptionsIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        startActivityForResult(gpsOptionsIntent, ENABLE_LOCATION_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ENABLE_LOCATION_REQUEST_CODE)
            checkLocationEnabled();
    }
}
