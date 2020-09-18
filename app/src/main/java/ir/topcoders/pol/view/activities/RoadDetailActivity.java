package ir.topcoders.pol.view.activities;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.google.android.gms.maps.LocationSource;

import java.util.UUID;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import ir.topcoders.pol.R;
import ir.topcoders.pol.utils.ProvinceUtils;
import ir.topcoders.pol.utils.StringUtils;
import ir.topcoders.pol.data.model.Road;
import ir.topcoders.pol.databinding.ActivityRoadDetailBinding;
import ir.topcoders.pol.utils.MyGpsLocationHelper;
import ir.topcoders.pol.utils.Utils;
import ir.topcoders.pol.viewmodel.RoadDetailViewModel;
import ir.topcoders.pol.viewmodel.ViewModelFactory;

public class RoadDetailActivity extends AppCompatActivity implements LocationSource.OnLocationChangedListener {

    private static final String PARAM_ROAD_ID = "PARAM_ROAD_ID";

    @Inject
    ViewModelFactory viewModelFactory;
    @Inject
    Utils utils;

    RoadDetailViewModel roadDetailViewModel;
    ActivityRoadDetailBinding binding;
    Road mRoad;
    MyGpsLocationHelper locationSource;
    boolean mLocationRequested;

    public static void showActivity(Context context, @Nullable String roadId) {
        Intent intent = new Intent(context, RoadDetailActivity.class);
        intent.putExtra(PARAM_ROAD_ID, roadId);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidInjection.inject(this);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_road_detail);

        initToolbar();
        initProvinceSpinner();
        setupViewModel();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_save_only, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_save) {
            doSave();
            return true;
        } else if (item.getItemId() == android.R.id.home) {
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

    private void initProvinceSpinner() {
        binding.detail.provinceValue.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {

                int provinceCode = ProvinceUtils.getProvinceCodeByIndex(position);

                binding.detail.countyValue.setAdapter(new ArrayAdapter<>(
                        RoadDetailActivity.this, android.R.layout.simple_spinner_dropdown_item,
                        ProvinceUtils.getCountiesByProvinceCode(provinceCode)
                ));

                if (mRoad.province == provinceCode)
                    binding.detail.countyValue.setSelection(ProvinceUtils.getCountyIndexByCode(mRoad.province, mRoad.county));
                else
                    binding.detail.countyValue.setSelection(0);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                binding.detail.countyValue.setAdapter(new ArrayAdapter<>(
                        RoadDetailActivity.this, android.R.layout.simple_spinner_dropdown_item,
                        ProvinceUtils.getCountiesByProvinceCode(0)
                ));
            }
        });

        binding.detail.provinceValue.setAdapter(new ArrayAdapter<>(
                RoadDetailActivity.this, android.R.layout.simple_spinner_dropdown_item,
                ProvinceUtils.getProvinces()));
    }

    private void setupViewModel() {
        roadDetailViewModel = ViewModelProviders.of(this, viewModelFactory).get(RoadDetailViewModel.class);

        String roadId = getIntent().getStringExtra(PARAM_ROAD_ID);
        if (!TextUtils.isEmpty(roadId)) {
            setTitle(R.string.title_edit_road);
            roadDetailViewModel.selectRoad(roadId)
                    .observe(this, road -> {
                        mRoad = road;
                        binding.setRoad(mRoad);
                        binding.detail.provinceValue.setSelection(ProvinceUtils.getProvinceIndexByCode(mRoad.province));
                    });
        } else {
            setTitle(R.string.title_new_road);
            mRoad = new Road();
            mRoad.isNew = true;
            mRoad.roadId = UUID.randomUUID().toString();
            mRoad.registrationDate = Utils.getPersianDateAndTimeForRecord();
            binding.setRoad(mRoad);

            new Handler().postDelayed(() -> {
//                binding.detail.provinceValue.setSelection(1);
                binding.detail.roadStartGpsXValue.setText("");
                binding.detail.roadStartGpsYValue.setText("");
                binding.detail.roadStartNameValue.requestFocus();
            }, 200);
        }


        roadDetailViewModel.onRoadSave()
                .observe(this, resultCode -> {
                    if (resultCode == RoadDetailViewModel.SAVE_RESULT_OK) {
                        mRoad.isNew = false;
                        finish();
                        Toast.makeText(this, "ذخیره شد!", Toast.LENGTH_SHORT).show();
                    } else if (resultCode == RoadDetailViewModel.SAVE_RESULT_UNIQUE)
                        new AlertDialog.Builder(this)
                                .setMessage("کد مسیر تکراری میباشد!")
                                .setPositiveButton("تایید", null)
                                .show();
                    else
                        Toast.makeText(this, "خطا در ذخیره سازی", Toast.LENGTH_SHORT).show();
                });
    }

    public void onSetRoadStartFromGpsClick(View view) {
        if (!utils.isLocationEnabled()) {
            showLocationDisabledAlert();
            return;
        }

        if (!utils.hasLocationAccess()) {
            showNoLocationAccessAlert();
            return;
        }

        if (locationSource == null)
            locationSource = new MyGpsLocationHelper(getApplicationContext());

        binding.detail.roadStartGpsYValue.setText("");
        binding.detail.roadStartGpsXValue.setText("");
        mLocationRequested = true;
        locationSource.activate(this);
        Toast.makeText(this, "لطفا کمی صبر کنید...", Toast.LENGTH_SHORT).show();
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

    private void doSave() {
        if (isValidData())
            roadDetailViewModel.insertRoad(mRoad);
        else
            Toast.makeText(this, "اطلاعات را تکمیل کنید!", Toast.LENGTH_SHORT).show();
    }

    private boolean isValidData() {
        boolean validate = true;

        binding.detail.registerDateTitle.setError(null);
        binding.detail.provinceTitle.setError(null);
        binding.detail.countyTitle.setError(null);
        binding.detail.roadStartNameTitle.setError(null);
        binding.detail.roadEndNameTitle.setError(null);
        binding.detail.roadStartGpsTitle.setError(null);
        binding.detail.roadDirectionTitle.setError(null);
        binding.detail.roadTypeTitle.setError(null);

        mRoad.province = ProvinceUtils.getProvinceCodeByIndex(binding.detail.provinceValue.getSelectedItemPosition());
        mRoad.county = ProvinceUtils.getCountyCodeByIndex(mRoad.province, binding.detail.countyValue.getSelectedItemPosition());

        if (TextUtils.isEmpty(mRoad.registrationDate)) {
            binding.detail.registerDateTitle.setError("تکمیل شود");
            validate = false;
        }

        if (mRoad.province == 0) {
            binding.detail.provinceTitle.setError("تکمیل شود");
            validate = false;
        }

        if (mRoad.county == 0) {
            binding.detail.countyTitle.setError("تکمیل شود");
            validate = false;
        }

        if (TextUtils.isEmpty(mRoad.startName)) {
            binding.detail.roadStartNameTitle.setError("تکمیل شود");
            validate = false;
        }

        if (TextUtils.isEmpty(mRoad.stopName)) {
            binding.detail.roadEndNameTitle.setError("تکمیل شود");
            validate = false;
        }

        if (mRoad.startGpsX == 0 || mRoad.startGpsY == 0) {
            binding.detail.roadStartGpsTitle.setError("تکمیل شود");
            validate = false;
        }

        if (mRoad.startGpsX < 40 || mRoad.startGpsX >= 66) {
            binding.detail.roadStartGpsTitle.setError("موقعیت نادرست");
            validate = false;
        }

        if (mRoad.startGpsY < 24 || mRoad.startGpsY >= 41) {
            binding.detail.roadStartGpsTitle.setError("موقعیت نادرست");
            validate = false;
        }

        if (mRoad.direction == 0) {
            binding.detail.roadDirectionTitle.setError("تکمیل شود");
            validate = false;
        }

        if (mRoad.type == 0) {
            binding.detail.roadTypeTitle.setError("تکمیل شود");
            validate = false;
        }

        return validate;
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setMessage("آیا میخواهید از صفحه خارج شوید؟\n\nتغییرات ذخیره نخواهند شد!")
                .setPositiveButton("بله، خارج شو", (dialogInterface, i) -> super.onBackPressed())
                .setNegativeButton("خیر", null)
                .show();
    }

    @Override
    public void onLocationChanged(Location location) {
        if (mLocationRequested) {
            mLocationRequested = false;
            binding.detail.roadStartGpsXValue.setText(String.valueOf(Double.parseDouble(StringUtils.format("%.7f", location.getLongitude()))));
            binding.detail.roadStartGpsYValue.setText(String.valueOf(Double.parseDouble(StringUtils.format("%.7f", location.getLatitude()))));
            locationSource.deactivate();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (locationSource != null)
            locationSource.deactivate();
    }
}