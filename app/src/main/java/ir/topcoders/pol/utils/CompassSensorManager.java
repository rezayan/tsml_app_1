package ir.topcoders.pol.utils;

import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.view.Surface;
import android.view.WindowManager;

public class CompassSensorManager implements SensorEventListener {

    private WindowManager windowManager;
    private SensorManager sensorManager;
    private Sensor accelerometerSensor;
    private Sensor magneticFieldSensor;

    private final float[] temporaryRotationMatrix = new float[9];
    private final float[] rotationMatrix = new float[9];
    private final float[] orientationData = new float[3];
    private float[] accelerometerData = new float[3];
    private float[] magneticData = new float[3];
    private float azimuth;
    private boolean isSuspended = true;
    private OnAzimuthChanged mListener;

    public interface OnAzimuthChanged {
        void onAzimuthChanged(float azimuth);
    }

    public CompassSensorManager(Context context) {
        windowManager = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE));
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        magneticFieldSensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
    }

    public void onResume() {
        isSuspended = false;
        sensorManager.registerListener(this, accelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, magneticFieldSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void onPause() {
        isSuspended = true;
        sensorManager.unregisterListener(this, accelerometerSensor);
        sensorManager.unregisterListener(this, magneticFieldSensor);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        int sensorType = event.sensor.getType();
        if (sensorType == Sensor.TYPE_ACCELEROMETER)
            accelerometerData = event.values;
        else if (sensorType == Sensor.TYPE_MAGNETIC_FIELD)
            magneticData = event.values;

        if (accelerometerData != null && magneticData != null) {
            SensorManager.getRotationMatrix(temporaryRotationMatrix, null, accelerometerData, magneticData);
            configureDeviceAngle();
            SensorManager.getOrientation(rotationMatrix, orientationData);
            azimuth = (float) Math.toDegrees(orientationData[0]);

            if (mListener != null)
                mListener.onAzimuthChanged(azimuth);
        }
    }

    private void configureDeviceAngle() {
        switch (windowManager.getDefaultDisplay().getRotation()) {
            case Surface.ROTATION_0: // Portrait
                SensorManager.remapCoordinateSystem(temporaryRotationMatrix, SensorManager.AXIS_Z, SensorManager.AXIS_Y, rotationMatrix);
                break;
            case Surface.ROTATION_90: // Landscape
                SensorManager.remapCoordinateSystem(temporaryRotationMatrix, SensorManager.AXIS_Y, SensorManager.AXIS_MINUS_Z, rotationMatrix);
                break;
            case Surface.ROTATION_180: // Portrait
                SensorManager.remapCoordinateSystem(temporaryRotationMatrix, SensorManager.AXIS_MINUS_Z, SensorManager.AXIS_MINUS_Y, rotationMatrix);
                break;
            case Surface.ROTATION_270: // Landscape
                SensorManager.remapCoordinateSystem(temporaryRotationMatrix, SensorManager.AXIS_MINUS_Y, SensorManager.AXIS_Z, rotationMatrix);
                break;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    public float getAzimuth() {
        return azimuth;
    }

    public static boolean isDeviceCompatible(Context context) {
        return context.getPackageManager() != null
                && context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_SENSOR_ACCELEROMETER)
                && context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_SENSOR_COMPASS);
    }

    boolean isSuspended() {
        return isSuspended;
    }

    public void setListener(OnAzimuthChanged listener) {
        mListener = listener;
    }
}