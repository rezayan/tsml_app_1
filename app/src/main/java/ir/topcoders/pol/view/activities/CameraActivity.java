package ir.topcoders.pol.view.activities;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.LocationSource;
import com.otaliastudios.cameraview.CameraListener;

import java.io.File;
import java.io.FileOutputStream;
import java.util.UUID;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import ir.topcoders.pol.R;
import ir.topcoders.pol.utils.StringUtils;
import ir.topcoders.pol.databinding.ActivityCameraBinding;
import ir.topcoders.pol.utils.AppConstants;
import ir.topcoders.pol.utils.CompassSensorManager;
import ir.topcoders.pol.utils.GpsUtils;
import ir.topcoders.pol.utils.MyGpsLocationHelper;
import ir.topcoders.pol.utils.NumberUtils;
import ir.topcoders.pol.utils.Utils;

public class CameraActivity extends AppCompatActivity
        implements LocationSource.OnLocationChangedListener,
        CompassSensorManager.OnAzimuthChanged {

    private static final String PARAM_ENABLE_LOCATION_AND_AZIMUTH = "PARAM_ENABLE_LOCATION_AND_AZIMUTH";
    public static final String RESULT_DATA_TEMP_FILE = "RESULT_DATA_TEMP_FILE";
    public static final String RESULT_DATA_GPS_X = "RESULT_DATA_GPS_X";
    public static final String RESULT_DATA_GPS_Y = "RESULT_DATA_GPS_Y";
    public static final String RESULT_DATA_AZIMUTH = "RESULT_DATA_AZIMUTH";

    @Inject
    Utils utils;

    ActivityCameraBinding binding;
    MyGpsLocationHelper locationSource;
    CompassSensorManager compassSensorManager;
    boolean mEnableLocationAndAzimuth;
    boolean mShowingPreview;

    public static void showActivity(Activity activity, boolean enableLocationAndAzimuth, int requestCode) {
        Intent intent = new Intent(activity, CameraActivity.class);
        intent.putExtra(PARAM_ENABLE_LOCATION_AND_AZIMUTH, enableLocationAndAzimuth);
        activity.startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidInjection.inject(this);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_camera);

        initToolbar();
        initZoomControl();
        initCamera();
        loadParam();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_image_capture, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_capture) {
            captureImage();
            return true;
        } else if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        } else if (item.getItemId() == R.id.action_request_location) {
            if (mEnableLocationAndAzimuth && !utils.isLocationEnabled()) {
                showLocationDisabledAlert();
                return true;
            }

            if (mEnableLocationAndAzimuth && !utils.hasLocationAccess()) {
                showNoLocationAccessAlert();
                return true;
            }
            enableLocationAndAzimuth();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initToolbar() {
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    private void initZoomControl() {
        binding.zoomControl.setOnZoomInClickListener(view -> binding.camera.setZoom(binding.camera.getZoom() + 0.1f));
        binding.zoomControl.setOnZoomOutClickListener(view -> binding.camera.setZoom(binding.camera.getZoom() - 0.1f));
    }

    private void initCamera() {
        binding.camera.addCameraListener(cameraListener);
        binding.camera.setLifecycleOwner(this);
    }

    private void loadParam() {
        mEnableLocationAndAzimuth = getIntent().getBooleanExtra(PARAM_ENABLE_LOCATION_AND_AZIMUTH, false);
        binding.setLocationEnabled(mEnableLocationAndAzimuth);
    }

    @Override
    protected void onResume() {
        super.onResume();
        enableLocationAndAzimuth();
        if (compassSensorManager != null)
            compassSensorManager.onResume();
    }

    private void enableLocationAndAzimuth() {
        if (mEnableLocationAndAzimuth) {
            if (locationSource == null)
                locationSource = new MyGpsLocationHelper(getApplicationContext());
            locationSource.activate(this);

            if (compassSensorManager == null)
                compassSensorManager = new CompassSensorManager(this);
            compassSensorManager.setListener(this);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
            if (compassSensorManager != null)
                compassSensorManager.onPause();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void captureImage() {
        binding.camera.captureSnapshot();
    }

    private void showLocationDisabledAlert() {
        new AlertDialog.Builder(this)
                .setMessage("لطفا GPS را فعال کنید.")
                .setPositiveButton("تایید", null)
                .show();
    }

    private void showNoLocationAccessAlert() {
        new AlertDialog.Builder(this)
                .setMessage("لطفا در تنظیمات اجازه دسترسی به موقعیت مکانی را فعال نمایید.")
                .setPositiveButton("تایید", null)
                .show();
    }

    CameraListener cameraListener = new CameraListener() {
        @Override
        public void onPictureTaken(byte[] jpeg) {
            super.onPictureTaken(jpeg);
            if (jpeg == null)
                return;
            showPreview(jpeg);

            if (mEnableLocationAndAzimuth) {
                if (isValidData())
                    savePicture(jpeg);
                else
                    new AlertDialog.Builder(CameraActivity.this)
                            .setMessage("اطلاعات موقعیت و آزیموت کامل نیست، آیا میخواهید تصویر ثبت شود؟")
                            .setPositiveButton("بله، ثبت شود", (dialogInterface, i) -> savePicture(jpeg))
                            .setNegativeButton("خیر", (dialogInterface, i) -> showPreview(null))
                            .show();
            } else
                savePicture(jpeg);
        }
    };

    private void showPreview(byte[] jpeg) {
        mShowingPreview = jpeg != null;

        if (jpeg == null) {
            Glide.with(this).clear(binding.cameraPreviewImageView);
            binding.cameraPreviewImageView.setImageBitmap(null);
        } else
            Glide.with(this)
                    .load(jpeg)
                    .into(binding.cameraPreviewImageView);
    }

    private void savePicture(byte[] jpeg) {
        if (jpeg != null) {
            try {
                File filePath = new File(AppConstants.getTempStorageFolder(CameraActivity.this), UUID.randomUUID().toString());
                writeToFile(jpeg, filePath);
                Intent intent = new Intent();
                intent.putExtra(RESULT_DATA_TEMP_FILE, filePath.getAbsolutePath());
                if (mEnableLocationAndAzimuth) {
                    intent.putExtra(RESULT_DATA_GPS_X, NumberUtils.parseDouble(binding.gpsXValueTextView.getText().toString(), -1));
                    intent.putExtra(RESULT_DATA_GPS_Y, NumberUtils.parseDouble(binding.gpsYValueTextView.getText().toString(), -1));
                    intent.putExtra(RESULT_DATA_AZIMUTH, NumberUtils.parseFloat(binding.azimuthValueTextView.getText().toString(), -1));
                }
                setResult(RESULT_OK, intent);
                finish();
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(CameraActivity.this, "خطا در ذخیره تصویر!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean isValidData() {
        return !(binding.gpsYValueTextView.getText().toString().equalsIgnoreCase("-")
                || binding.gpsXValueTextView.getText().toString().equalsIgnoreCase("-")
                || binding.azimuthValueTextView.getText().toString().equalsIgnoreCase("-"));
    }

    private void writeToFile(byte[] jpeg, File filePath) throws Exception {
        filePath.getParentFile().mkdirs();
        filePath.createNewFile();
        FileOutputStream out = new FileOutputStream(filePath);
        out.write(jpeg);
    }

    @Override
    public void onLocationChanged(Location location) {
        if (!mShowingPreview) {
            binding.gpsXValueTextView.setText(String.valueOf(Double.parseDouble(StringUtils.format("%.7f", location.getLongitude()))));
            binding.gpsYValueTextView.setText(String.valueOf(Double.parseDouble(StringUtils.format("%.7f", location.getLatitude()))));
        }
    }

    @Override
    public void onAzimuthChanged(float azimuth) {
        if (!mShowingPreview) {
            float newAzimuth = GpsUtils.calcAzimuth(azimuth);
            binding.compass.setDegrees(newAzimuth);
            binding.azimuthValueTextView.setText(String.valueOf(newAzimuth));
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        try {
            if (locationSource != null)
                locationSource.deactivate();
            if (compassSensorManager != null)
                compassSensorManager.setListener(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}