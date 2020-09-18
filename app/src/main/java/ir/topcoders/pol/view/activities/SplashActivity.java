package ir.topcoders.pol.view.activities;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;

import ir.topcoders.pol.R;
import ir.topcoders.pol.data.LocalSettings;

public class SplashActivity extends AppCompatActivity {

    private static final int DELAY_MILLIS = 1000;

    private Handler mHandler;
    private Runnable mRunnable;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        showMainActivityWithDelay();
    }

    private void showMainActivityWithDelay() {
        mHandler = new Handler();
        mRunnable = this::checkForRegistration;
        mHandler.postDelayed(mRunnable, DELAY_MILLIS);
    }

    private void checkForRegistration() {
        LocalSettings settings = new LocalSettings(this);
        if (TextUtils.isEmpty(settings.getOperatorName()))
            RegisterActivity.showActivity(this);
        else
            RoadListActivity.showActivity(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        cancelShowMainActivity();
    }

    private void cancelShowMainActivity() {
        if (mHandler != null && mRunnable != null)
            mHandler.removeCallbacks(mRunnable);
    }
}
