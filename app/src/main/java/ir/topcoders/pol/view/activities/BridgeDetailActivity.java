package ir.topcoders.pol.view.activities;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.location.Location;
import android.media.ExifInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.LocationSource;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import ir.topcoders.pol.R;
import ir.topcoders.pol.utils.StringUtils;
import ir.topcoders.pol.data.model.Bridge;
import ir.topcoders.pol.data.model.BridgeDamageWithImage;
import ir.topcoders.pol.databinding.ActivityBridgeDetailBinding;
import ir.topcoders.pol.utils.AppConstants;
import ir.topcoders.pol.utils.FileUtils;
import ir.topcoders.pol.utils.GpsUtils;
import ir.topcoders.pol.utils.ImageUtils;
import ir.topcoders.pol.utils.MyGpsLocationHelper;
import ir.topcoders.pol.utils.Utils;
import ir.topcoders.pol.view.adapters.DamageAdapter;
import ir.topcoders.pol.viewmodel.BridgeDetailViewModel;
import ir.topcoders.pol.viewmodel.ViewModelFactory;

public class BridgeDetailActivity extends AppCompatActivity implements LocationSource.OnLocationChangedListener {

    private static final String PARAM_ROAD_ID = "PARAM_ROAD_ID";
    private static final String PARAM_BRIDGE_ID = "PARAM_BRIDGE_ID";

    private static final int TAB_INDEX_IDENTICAL = 4;
    private static final int TAB_INDEX_GEOMETRIC = 3;
    private static final int TAB_INDEX_DAMAGES = 2;
    private static final int TAB_INDEX_RANK = 1;
    private static final int TAB_INDEX_PICTURES = 0;

    private static final int REQUEST_CODE_SELECT_SATELLITE_MAP = 100;
    private static final int REQUEST_CODE_SELECT_OVERVIEW_CAMERA = 101;
    private static final int REQUEST_CODE_SELECT_CROSSOVER_CAMERA = 102;
    private static final int REQUEST_CODE_SELECT_STREET_CAMERA = 103;
    private static final int REQUEST_CODE_SELECT_BALADAST_CAMERA = 104;
    private static final int REQUEST_CODE_SELECT_PAEANDAST_CAMERA = 105;
    private static final int REQUEST_CODE_SELECT_SATELLITE_GALLERY = 200;
    private static final int REQUEST_CODE_SELECT_OVERVIEW_GALLERY = 201;
    private static final int REQUEST_CODE_SELECT_CROSSOVER_GALLERY = 202;
    private static final int REQUEST_CODE_SELECT_STREET_GALLERY = 203;
    private static final int REQUEST_CODE_SELECT_BALADAST_GALLERY = 204;
    private static final int REQUEST_CODE_SELECT_PAEANDAST_GALLERY = 205;

    private static final String SATELLITE_IMAGE_FILE_NAME = "Pic_Satellite__%.jpg";
    private static final String OVERVIEW_IMAGE_FILE_NAME = "Pic_Overview__%.jpg";
    private static final String CROSSOVER_IMAGE_FILE_NAME = "Pic_Crossover__%.jpg";
    private static final String STREET_IMAGE_FILE_NAME = "Pic_Street__%.jpg";
    private static final String BALADAST_IMAGE_FILE_NAME = "Pic_baladast__%.jpg";
    private static final String PAEANDAST_IMAGE_FILE_NAME = "Pic_Paeandast__%.jpg";

    @Inject
    ViewModelFactory viewModelFactory;
    @Inject
    Utils utils;

    BridgeDetailViewModel bridgeDetailViewModel;
    ActivityBridgeDetailBinding binding;
    String satelliteTempImageFile;
    String overviewTempImageFile;
    String crossoverTempImageFile;
    String streetTempImageFile;
    String baladastTempImageFile;
    String paeandastTempImageFile;
    MyGpsLocationHelper locationSource;
    boolean mLocationRequested;
    DamageAdapter damageAdapter;

    public static void showActivityForAdd(Context context, @Nullable String roadId) {
        Intent intent = new Intent(context, BridgeDetailActivity.class);
        intent.putExtra(PARAM_ROAD_ID, roadId);
        context.startActivity(intent);
    }

    public static void showActivityForEdit(Context context, @Nullable String bridgeId) {
        Intent intent = new Intent(context, BridgeDetailActivity.class);
        intent.putExtra(PARAM_BRIDGE_ID, bridgeId);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidInjection.inject(this);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_bridge_detail);

        initToolbar();
        initTabBar();
        initDamagesRecycler();
        setupViewModel();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_save_and_detail, menu);

        String bridgeId = getIntent().getStringExtra(PARAM_BRIDGE_ID);
        if (TextUtils.isEmpty(bridgeId))
            menu.findItem(R.id.action_delete).setVisible(false);
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
        } else if (item.getItemId() == R.id.action_delete) {
            deleteBridge();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initToolbar() {
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    private void initTabBar() {
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("تصاویر"));
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("رتبه بندی"));
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("آسیب"));
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("هندسه"));
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("شناسنامه"));
        binding.tabLayout.getTabAt(TAB_INDEX_IDENTICAL).select();
        binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                hideAllLayouts();
                switch (tab.getPosition()) {
                    case TAB_INDEX_DAMAGES:
                        binding.damages.getRoot().setVisibility(View.VISIBLE);
                        break;
                    case TAB_INDEX_RANK:
                        binding.rank.getRoot().setVisibility(View.VISIBLE);
                        break;
                    case TAB_INDEX_PICTURES:
                        binding.pictures.getRoot().setVisibility(View.VISIBLE);
                        break;
                    case TAB_INDEX_GEOMETRIC:
                        binding.geometric.getRoot().setVisibility(View.VISIBLE);
                        break;
                    case TAB_INDEX_IDENTICAL:
                        binding.identical.getRoot().setVisibility(View.VISIBLE);
                        break;
                }
                binding.scroller.scrollTo(0, 0);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }

    private void hideAllLayouts() {
        binding.identical.getRoot().setVisibility(View.GONE);
        binding.geometric.getRoot().setVisibility(View.GONE);
        binding.pictures.getRoot().setVisibility(View.GONE);
        binding.damages.getRoot().setVisibility(View.GONE);
        binding.rank.getRoot().setVisibility(View.GONE);
    }

    private void initDamagesRecycler() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        binding.damages.recycler.setLayoutManager(layoutManager);
        damageAdapter = new DamageAdapter();
        binding.damages.recycler.setAdapter(damageAdapter);
    }

    private void setupViewModel() {
        bridgeDetailViewModel = ViewModelProviders.of(this, viewModelFactory).get(BridgeDetailViewModel.class);

        String bridgeId = getIntent().getStringExtra(PARAM_BRIDGE_ID);
        if (!TextUtils.isEmpty(bridgeId)) {
            setTitle(R.string.title_edit_bridge);
            binding.setLoadingDamage(true);
            bridgeDetailViewModel.selectBridge(bridgeId)
                    .observe(this, bridge -> {
                        if (bridge != null) {
                            bridgeDetailViewModel.setBridge(bridge);
                            binding.setBridge(bridgeDetailViewModel.getBridge());
                            damageAdapter.setRoadId(bridgeDetailViewModel.getBridge().roadId);

                            binding.rank.rankTypeValue.setSelection(bridgeDetailViewModel.getBridge().rank);

                            bridgeDetailViewModel.getBridgeDamages(bridgeDetailViewModel.getBridge().roadId, bridgeDetailViewModel.getBridge().bridgeId)
                                    .observe(this, this::setupDamages);
                        }
                    });
        } else {
            setTitle(R.string.title_new_bridge);
            String roadId = getIntent().getStringExtra(PARAM_ROAD_ID);
            damageAdapter.setRoadId(roadId);

            bridgeDetailViewModel.newBridge(roadId);
            binding.setBridge(bridgeDetailViewModel.getBridge());

            bridgeDetailViewModel.getBridge().rank = binding.rank.rankTypeValue.getSelectedItemPosition();


            bridgeDetailViewModel.getBridgeDamages(bridgeDetailViewModel.getBridge().roadId, bridgeDetailViewModel.getBridge().bridgeId)
                    .observe(this, this::setupDamages);

            new Handler().postDelayed(() -> {
                binding.identical.stationValue.setText("");
                binding.identical.gpsXValue.setText("");
                binding.identical.gpsYValue.setText("");
                binding.geometric.craterCountValue.setText("");
                binding.geometric.widestCraterLengthValue.setText("");
                binding.geometric.widthValue.setText("");
                binding.geometric.heightValue.setText("");
                binding.geometric.laneCountValue.setText("");
                binding.geometric.freeHeightValue.setText("");
                binding.identical.bridgeCodeValue.requestFocus();
            }, 200);
        }

        bridgeDetailViewModel.onBridgeSave()
                .observe(this, resultCode -> {
                    if (resultCode == BridgeDetailViewModel.SAVE_RESULT_OK) {
                        bridgeDetailViewModel.getBridge().isNew = false;
                        if (binding.tabLayout.getSelectedTabPosition() == TAB_INDEX_PICTURES) {
                            checkForValidateAndExit();
                        } else
                            binding.tabLayout.getTabAt(binding.tabLayout.getSelectedTabPosition() - 1).select();

                        Toast.makeText(this, "ذخیره شد!", Toast.LENGTH_SHORT).show();
                    } else if (resultCode == BridgeDetailViewModel.SAVE_RESULT_UNIQUE)
                        new AlertDialog.Builder(this)
                                .setMessage("کد پل تکراری میباشد!")
                                .setPositiveButton("تایید", null)
                                .show();
                    else
                        Toast.makeText(this, "خطا در ذخیره سازی", Toast.LENGTH_SHORT).show();
                });
    }

    private void checkForValidateAndExit() {
        if (isPicturesAndDamagesFilled())
            doFinish();
        else
            new AlertDialog.Builder(this)
                    .setMessage("اطلاعات تصاویر و آسیب ها باید کامل وارد شوند، آیا میخواهید خارج شوید؟")
                    .setPositiveButton("بله، خارج شو", (dialogInterface, i) -> {
                        doFinish();
                    })
                    .setNegativeButton("خیر", null)
                    .show();
    }

    private boolean isPicturesAndDamagesFilled() {
        return !TextUtils.isEmpty(bridgeDetailViewModel.getBridge().crossoverViewImageFilename) &&
                !TextUtils.isEmpty(bridgeDetailViewModel.getBridge().overviewViewImageFilename) &&
                !TextUtils.isEmpty(bridgeDetailViewModel.getBridge().streetViewImageFilename) &&
                !TextUtils.isEmpty(bridgeDetailViewModel.getBridge().baladastViewImageFilename) &&
                !TextUtils.isEmpty(bridgeDetailViewModel.getBridge().paeandastViewImageFilename) &&
                damageAdapter != null && damageAdapter.getItemCount() > 0;
    }

    private void doFinish() {
        clearTempFolder();
        finish();
    }

    private void setupDamages(List<BridgeDamageWithImage> damages) {
        binding.setLoadingDamage(false);
        binding.setIsEmptyDamage(damages == null || damages.size() == 0);
        damageAdapter.setData(damages);
    }

    private void doSave() {
        if (isValidData()) {
            moveTempImageFiles();
            bridgeDetailViewModel.insertBridge(bridgeDetailViewModel.getBridge());
        } else
            Toast.makeText(this, "اطلاعات را تکمیل کنید!", Toast.LENGTH_SHORT).show();
    }

    private boolean isValidData() {
        boolean validate = true;

        binding.identical.registerDateTitle.setError(null);
        binding.identical.stationTitle.setError(null);
        binding.identical.constructionYearTitle.setError(null);
        binding.identical.gpsTitle.setError(null);
        binding.identical.structuralSystemTitle.setError(null);
        binding.identical.crossoverDamageTypeTitle.setError(null);
        binding.geometric.craterCountTitle.setError(null);
        binding.geometric.widestCraterLengthTitle.setError(null);
        binding.geometric.widthTitle.setError(null);
        binding.geometric.heightTitle.setError(null);
        binding.geometric.laneCountTitle.setError(null);
        binding.geometric.materialTypeTitle.setError(null);
        binding.geometric.hasAlternativeRouteTitle.setError(null);
        binding.geometric.freeHeightTitle.setError(null);
        binding.rank.rankTypeTitle.setError(null);

        bridgeDetailViewModel.getBridge().rank = binding.rank.rankTypeValue.getSelectedItemPosition();

        if (TextUtils.isEmpty(bridgeDetailViewModel.getBridge().registrationDate)) {
            binding.identical.registerDateTitle.setError("تکمیل شود");
            validate = false;
        }

        if (bridgeDetailViewModel.getBridge().station == 0) {
            binding.identical.stationTitle.setError("تکمیل شود");
            validate = false;
        }

        if (binding.identical.constructionYearValue.getSelectedItemPosition() == 0) {
            binding.identical.constructionYearTitle.setError("تکمیل شود");
            validate = false;
        }

        if (bridgeDetailViewModel.getBridge().gpsX == 0 || bridgeDetailViewModel.getBridge().gpsY == 0) {
            binding.identical.gpsTitle.setError("تکمیل شود");
            validate = false;
        }

        if (bridgeDetailViewModel.getBridge().gpsX < 40 || bridgeDetailViewModel.getBridge().gpsX >= 66) {
            binding.identical.gpsTitle.setError("موقعیت نادرست");
            validate = false;
        }

        if (bridgeDetailViewModel.getBridge().gpsY < 24 || bridgeDetailViewModel.getBridge().gpsY >= 41) {
            binding.identical.gpsTitle.setError("موقعیت نادرست");
            validate = false;
        }

        if (bridgeDetailViewModel.getBridge().structuralSystem == 0) {
            binding.identical.structuralSystemTitle.setError("تکمیل شود");
            validate = false;
        }

        if (bridgeDetailViewModel.getBridge().crossoverDamageType == 0) {
            binding.identical.crossoverDamageTypeTitle.setError("تکمیل شود");
            validate = false;
        }

        if (!validate) {
            binding.tabLayout.getTabAt(TAB_INDEX_IDENTICAL).select();
            return validate;
        }


        if (bridgeDetailViewModel.getBridge().craterCount == 0) {
            binding.geometric.craterCountTitle.setError("تکمیل شود");
            validate = false;
        }

        if (bridgeDetailViewModel.getBridge().widestCraterLength == 0) {
            binding.geometric.widestCraterLengthTitle.setError("تکمیل شود");
            validate = false;
        }

        if (bridgeDetailViewModel.getBridge().width == 0) {
            binding.geometric.widthTitle.setError("تکمیل شود");
            validate = false;
        }

        if (bridgeDetailViewModel.getBridge().height == 0) {
            binding.geometric.heightTitle.setError("تکمیل شود");
            validate = false;
        }

        if (bridgeDetailViewModel.getBridge().laneCount == 0) {
            binding.geometric.laneCountTitle.setError("تکمیل شود");
            validate = false;
        }

        if (bridgeDetailViewModel.getBridge().materialType == 0) {
            binding.geometric.materialTypeTitle.setError("تکمیل شود");
            validate = false;
        }

        if (!binding.geometric.hasAlternativeRoute.isChecked() && !binding.geometric.hasNotAlternativeRoute.isChecked()) {
            binding.geometric.hasAlternativeRouteTitle.setError("تکمیل شود");
            validate = false;
        }

        if (bridgeDetailViewModel.getBridge().freeHeight == 0) {
            binding.geometric.freeHeightTitle.setError("تکمیل شود");
            validate = false;
        }

        if (!validate) {
            binding.tabLayout.getTabAt(TAB_INDEX_GEOMETRIC).select();
            return validate;
        }

        if (bridgeDetailViewModel.getBridge().rank == 0) {
            binding.rank.rankTypeTitle.setError("تکمیل شود");
            validate = false;
        }

        if (!validate) {
            binding.tabLayout.getTabAt(TAB_INDEX_RANK).select();
            return validate;
        }

        return validate;
    }

    private void moveTempImageFiles() {
        String baseTargetPath = AppConstants.getFilesStorageFolder(this)
                + bridgeDetailViewModel.getBridge().roadId
                + "/" + bridgeDetailViewModel.getBridge().bridgeId;
        File baseTarget = new File(baseTargetPath);
        baseTarget.mkdirs();
        String dateTime = Utils.getPersianDateAndTime("-");

        if (!TextUtils.isEmpty(satelliteTempImageFile)) {
            deletePicFile(bridgeDetailViewModel.getBridge().satelliteViewImageFilename);
            String targetFileName = SATELLITE_IMAGE_FILE_NAME.replace("%", dateTime);
            FileUtils.moveFile(new File(satelliteTempImageFile), new File(baseTarget, targetFileName));
            writeExifData(new File(baseTarget, targetFileName), dateTime, bridgeDetailViewModel.getBridge());
            bridgeDetailViewModel.getBridge().satelliteViewImageFilename = targetFileName;
            satelliteTempImageFile = null;
        }

        if (!TextUtils.isEmpty(overviewTempImageFile)) {
            deletePicFile(bridgeDetailViewModel.getBridge().overviewViewImageFilename);
            String targetFileName = OVERVIEW_IMAGE_FILE_NAME.replace("%", dateTime);
            FileUtils.moveFile(new File(overviewTempImageFile), new File(baseTarget, targetFileName));
            writeExifData(new File(baseTarget, targetFileName), dateTime, bridgeDetailViewModel.getBridge());
            bridgeDetailViewModel.getBridge().overviewViewImageFilename = targetFileName;
            overviewTempImageFile = null;
        }

        if (!TextUtils.isEmpty(crossoverTempImageFile)) {
            deletePicFile(bridgeDetailViewModel.getBridge().crossoverViewImageFilename);
            String targetFileName = CROSSOVER_IMAGE_FILE_NAME.replace("%", dateTime);
            FileUtils.moveFile(new File(crossoverTempImageFile), new File(baseTarget, targetFileName));
            writeExifData(new File(baseTarget, targetFileName), dateTime, bridgeDetailViewModel.getBridge());
            bridgeDetailViewModel.getBridge().crossoverViewImageFilename = targetFileName;
            crossoverTempImageFile = null;
        }

        if (!TextUtils.isEmpty(streetTempImageFile)) {
            deletePicFile(bridgeDetailViewModel.getBridge().streetViewImageFilename);
            String targetFileName = STREET_IMAGE_FILE_NAME.replace("%", dateTime);
            FileUtils.moveFile(new File(streetTempImageFile), new File(baseTarget, targetFileName));
            writeExifData(new File(baseTarget, targetFileName), dateTime, bridgeDetailViewModel.getBridge());
            bridgeDetailViewModel.getBridge().streetViewImageFilename = targetFileName;
            streetTempImageFile = null;
        }

        if (!TextUtils.isEmpty(baladastTempImageFile)) {
            deletePicFile(bridgeDetailViewModel.getBridge().baladastViewImageFilename);
            String targetFileName = BALADAST_IMAGE_FILE_NAME.replace("%", dateTime);
            FileUtils.moveFile(new File(baladastTempImageFile), new File(baseTarget, targetFileName));
            writeExifData(new File(baseTarget, targetFileName), dateTime, bridgeDetailViewModel.getBridge());
            bridgeDetailViewModel.getBridge().baladastViewImageFilename = targetFileName;
            baladastTempImageFile = null;
        }

        if (!TextUtils.isEmpty(paeandastTempImageFile)) {
            deletePicFile(bridgeDetailViewModel.getBridge().paeandastViewImageFilename);
            String targetFileName = PAEANDAST_IMAGE_FILE_NAME.replace("%", dateTime);
            FileUtils.moveFile(new File(paeandastTempImageFile), new File(baseTarget, targetFileName));
            writeExifData(new File(baseTarget, targetFileName), dateTime, bridgeDetailViewModel.getBridge());
            bridgeDetailViewModel.getBridge().paeandastViewImageFilename = targetFileName;
            paeandastTempImageFile = null;
        }
    }

    private void writeExifData(File file, String dateTime, Bridge bridge) {
        try {
            ExifInterface exif = new ExifInterface(file.getAbsolutePath());
            exif.setAttribute("ImageDescription",
                    "file:" + file.getName() +
                            "\r\nRoadId:" + bridge.roadId +
                            "\r\nBridgeId:" + bridge.bridgeId);
            exif.setAttribute(ExifInterface.TAG_DATETIME, dateTime);
            exif.setAttribute(ExifInterface.TAG_GPS_LATITUDE, GpsUtils.convert(bridge.gpsY));
            exif.setAttribute(ExifInterface.TAG_GPS_LATITUDE_REF, GpsUtils.latitudeRef(bridge.gpsY));
            exif.setAttribute(ExifInterface.TAG_GPS_LONGITUDE, GpsUtils.convert(bridge.gpsX));
            exif.setAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF, GpsUtils.longitudeRef(bridge.gpsX));
            exif.saveAttributes();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void deletePicFile(String fileName) {
        if (!TextUtils.isEmpty(fileName))
            FileUtils.deleteFile(AppConstants.getFilesStorageFolderByBridge(this, bridgeDetailViewModel.getBridge(), fileName));
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setMessage("آیا میخواهید از صفحه خارج شوید؟\n\nتغییرات ذخیره نخواهند شد!")
                .setPositiveButton("بله، خارج شو", (dialogInterface, i) -> {
                    clearTempFolder();
                    super.onBackPressed();
                })
                .setNegativeButton("خیر", null)
                .show();
    }

    private void clearTempFolder() {
        try {
            File dir = new File(AppConstants.getTempStorageFolder(this));
            org.apache.commons.io.FileUtils.deleteDirectory(dir);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onSetFromGpsClick(View view) {
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

        binding.identical.gpsYValue.setText("");
        binding.identical.gpsXValue.setText("");
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

    public void onSatelliteImageSelect(View view) {
        if (bridgeDetailViewModel.getBridge().isNew)
            showSaveRequire();
        else {
            final String items[] = new String[]{getString(R.string.title_from_map), getString(R.string.title_from_gallery)};
            new AlertDialog.Builder(this)
                    .setTitle(R.string.title_select)
                    .setItems(items, (d, choice) -> {
                        if (choice == 0) {
                            Intent intent = new Intent(this, MapSelectActivity.class);
                            startActivityForResult(intent, REQUEST_CODE_SELECT_SATELLITE_MAP);
                        } else if (choice == 1)
                            ImageUtils.launchGallery(this, REQUEST_CODE_SELECT_SATELLITE_GALLERY);
                    }).setNegativeButton(R.string.action_cancel, (dialog, which) -> dialog.cancel()).show();
        }
    }

    public void onOverviewImageSelect(View view) {
        if (bridgeDetailViewModel.getBridge().isNew)
            showSaveRequire();
        else {
            final String items[] = new String[]{getString(R.string.title_from_camera), getString(R.string.title_from_gallery)};
            new AlertDialog.Builder(this)
                    .setTitle(R.string.title_select)
                    .setItems(items, (d, choice) -> {
                        if (choice == 0) {
                            CameraActivity.showActivity(this, false, REQUEST_CODE_SELECT_OVERVIEW_CAMERA);
                        } else if (choice == 1)
                            ImageUtils.launchGallery(this, REQUEST_CODE_SELECT_OVERVIEW_GALLERY);
                    }).setNegativeButton(R.string.action_cancel, (dialog, which) -> dialog.cancel()).show();
        }
    }

    public void onCrossoverImageSelect(View view) {
        if (bridgeDetailViewModel.getBridge().isNew)
            showSaveRequire();
        else {
            final String items[] = new String[]{getString(R.string.title_from_camera), getString(R.string.title_from_gallery)};
            new AlertDialog.Builder(this)
                    .setTitle(R.string.title_select)
                    .setItems(items, (d, choice) -> {
                        if (choice == 0) {
                            CameraActivity.showActivity(this, false, REQUEST_CODE_SELECT_CROSSOVER_CAMERA);
                        } else if (choice == 1)
                            ImageUtils.launchGallery(this, REQUEST_CODE_SELECT_CROSSOVER_GALLERY);
                    }).setNegativeButton(R.string.action_cancel, (dialog, which) -> dialog.cancel()).show();
        }
    }

    public void onStreetImageSelect(View view) {
        if (bridgeDetailViewModel.getBridge().isNew)
            showSaveRequire();
        else {
            final String items[] = new String[]{getString(R.string.title_from_camera), getString(R.string.title_from_gallery)};
            new AlertDialog.Builder(this)
                    .setTitle(R.string.title_select)
                    .setItems(items, (d, choice) -> {
                        if (choice == 0) {
                            CameraActivity.showActivity(this, false, REQUEST_CODE_SELECT_STREET_CAMERA);
                        } else if (choice == 1)
                            ImageUtils.launchGallery(this, REQUEST_CODE_SELECT_STREET_GALLERY);
                    }).setNegativeButton(R.string.action_cancel, (dialog, which) -> dialog.cancel()).show();
        }
    }

    public void onBaladastImageSelect(View view) {
        if (bridgeDetailViewModel.getBridge().isNew)
            showSaveRequire();
        else {
            final String items[] = new String[]{getString(R.string.title_from_camera), getString(R.string.title_from_gallery)};
            new AlertDialog.Builder(this)
                    .setTitle(R.string.title_select)
                    .setItems(items, (d, choice) -> {
                        if (choice == 0) {
                            CameraActivity.showActivity(this, false, REQUEST_CODE_SELECT_BALADAST_CAMERA);
                        } else if (choice == 1)
                            ImageUtils.launchGallery(this, REQUEST_CODE_SELECT_BALADAST_GALLERY);
                    }).setNegativeButton(R.string.action_cancel, (dialog, which) -> dialog.cancel()).show();
        }
    }

    public void onPaeandastImageSelect(View view) {
        if (bridgeDetailViewModel.getBridge().isNew)
            showSaveRequire();
        else {
            final String items[] = new String[]{getString(R.string.title_from_camera), getString(R.string.title_from_gallery)};
            new AlertDialog.Builder(this)
                    .setTitle(R.string.title_select)
                    .setItems(items, (d, choice) -> {
                        if (choice == 0) {
                            CameraActivity.showActivity(this, false, REQUEST_CODE_SELECT_PAEANDAST_CAMERA);
                        } else if (choice == 1)
                            ImageUtils.launchGallery(this, REQUEST_CODE_SELECT_PAEANDAST_GALLERY);
                    }).setNegativeButton(R.string.action_cancel, (dialog, which) -> dialog.cancel()).show();
        }
    }

    public void onSatelliteImageRemove(View view) {
        new AlertDialog.Builder(this)
                .setMessage("آیا میخواهید «عکس هوایی» این پل را حذف کنید؟")
                .setPositiveButton("بله، حذف کن", (dialogInterface, i) -> {
                    satelliteTempImageFile = null;
                    bridgeDetailViewModel.getBridge().satelliteViewImageFilename = null;
                    clearImage(binding.pictures.satelliteImage);
                })
                .setNegativeButton("خیر", null)
                .show();
    }

    public void onOverviewImageRemove(View view) {
        new AlertDialog.Builder(this)
                .setMessage("آیا میخواهید «عکس نمای کلی» این پل را حذف کنید؟")
                .setPositiveButton("بله، حذف کن", (dialogInterface, i) -> {
                    overviewTempImageFile = null;
                    bridgeDetailViewModel.getBridge().overviewViewImageFilename = null;
                    clearImage(binding.pictures.overviewImage);
                })
                .setNegativeButton("خیر", null)
                .show();
    }

    public void onCrossoverImageRemove(View view) {
        new AlertDialog.Builder(this)
                .setMessage("آیا میخواهید «عکس نمای متقاطع» این پل را حذف کنید؟")
                .setPositiveButton("بله، حذف کن", (dialogInterface, i) -> {
                    crossoverTempImageFile = null;
                    bridgeDetailViewModel.getBridge().crossoverViewImageFilename = null;
                    clearImage(binding.pictures.crossoverImage);
                })
                .setNegativeButton("خیر", null)
                .show();
    }

    public void onStreetImageRemove(View view) {
        new AlertDialog.Builder(this)
                .setMessage("آیا میخواهید «عکس نمای سواره رو» این پل را حذف کنید؟")
                .setPositiveButton("بله، حذف کن", (dialogInterface, i) -> {
                    streetTempImageFile = null;
                    bridgeDetailViewModel.getBridge().streetViewImageFilename = null;
                    clearImage(binding.pictures.streetImage);
                })
                .setNegativeButton("خیر", null)
                .show();
    }

    public void onBaladastImageRemove(View view) {
        new AlertDialog.Builder(this)
                .setMessage("آیا میخواهید «عکس نمای بالا دست رو» این پل را حذف کنید؟")
                .setPositiveButton("بله، حذف کن", (dialogInterface, i) -> {
                    baladastTempImageFile = null;
                    bridgeDetailViewModel.getBridge().baladastViewImageFilename = null;
                    clearImage(binding.pictures.streetImage);
                })
                .setNegativeButton("خیر", null)
                .show();
    }

    public void onPaeandastImageRemove(View view) {
        new AlertDialog.Builder(this)
                .setMessage("آیا میخواهید «عکس نمای پایین دست رو» این پل را حذف کنید؟")
                .setPositiveButton("بله، حذف کن", (dialogInterface, i) -> {
                    paeandastTempImageFile = null;
                    bridgeDetailViewModel.getBridge().paeandastViewImageFilename = null;
                    clearImage(binding.pictures.streetImage);
                })
                .setNegativeButton("خیر", null)
                .show();
    }


    public void onOverviewImageClick(View view) {
        openImage(overviewTempImageFile, bridgeDetailViewModel.getBridge().overviewViewImageFilename);
    }

    public void onSatelliteImageClick(View view) {
        openImage(satelliteTempImageFile, bridgeDetailViewModel.getBridge().satelliteViewImageFilename);
    }

    public void onStreetImageClick(View view) {
        openImage(streetTempImageFile, bridgeDetailViewModel.getBridge().streetViewImageFilename);
    }

    public void onCrossoverImageClick(View view) {
        openImage(crossoverTempImageFile, bridgeDetailViewModel.getBridge().crossoverViewImageFilename);
    }

    public void onBaladasttreetImageClick(View view) {
        openImage(baladastTempImageFile, bridgeDetailViewModel.getBridge().baladastViewImageFilename);
    }

    public void onPaeandastImageClick(View view) {
        openImage(paeandastTempImageFile, bridgeDetailViewModel.getBridge().paeandastViewImageFilename);
    }

    private void openImage(String tempFilePath, String savedFileName) {
        if (tempFilePath != null)
            ImageUtils.openImageByDefaultApp(this, new File(overviewTempImageFile));
        else if (savedFileName != null) {
            String baseTargetPath = AppConstants.getFilesStorageFolder(this)
                    + bridgeDetailViewModel.getBridge().roadId
                    + "/" + bridgeDetailViewModel.getBridge().bridgeId;
            File baseTarget = new File(baseTargetPath);
            ImageUtils.openImageByDefaultApp(this, new File(baseTarget, savedFileName));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_CANCELED)
            return;

        if (data != null && requestCode == REQUEST_CODE_SELECT_SATELLITE_MAP) {
            String tempFilePath = data.getStringExtra(MapSelectActivity.RESULT_PARAM_TEMP_IMAGE_PATH);
            satelliteTempImageFile = tempFilePath;
            loadImage(tempFilePath, binding.pictures.satelliteImage);
        } else if (data != null && (requestCode == REQUEST_CODE_SELECT_OVERVIEW_CAMERA || requestCode == REQUEST_CODE_SELECT_CROSSOVER_CAMERA || requestCode == REQUEST_CODE_SELECT_STREET_CAMERA || requestCode == REQUEST_CODE_SELECT_BALADAST_CAMERA || requestCode == REQUEST_CODE_SELECT_PAEANDAST_CAMERA)) {
            String tempFilePath = data.getStringExtra(CameraActivity.RESULT_DATA_TEMP_FILE);
            switch (requestCode) {
                case REQUEST_CODE_SELECT_OVERVIEW_CAMERA:
                    overviewTempImageFile = tempFilePath;
                    loadImage(tempFilePath, binding.pictures.overviewImage);
                    break;
                case REQUEST_CODE_SELECT_CROSSOVER_CAMERA:
                    crossoverTempImageFile = tempFilePath;
                    loadImage(tempFilePath, binding.pictures.crossoverImage);
                    break;
                case REQUEST_CODE_SELECT_STREET_CAMERA:
                    streetTempImageFile = tempFilePath;
                    loadImage(tempFilePath, binding.pictures.streetImage);
                    break;
                case REQUEST_CODE_SELECT_BALADAST_CAMERA:
                    baladastTempImageFile = tempFilePath;
                    loadImage(tempFilePath, binding.pictures.baladastImage);
                    break;
                case REQUEST_CODE_SELECT_PAEANDAST_CAMERA:
                    paeandastTempImageFile = tempFilePath;
                    loadImage(tempFilePath, binding.pictures.paeandastImage);
                    break;

            }
        } else {
            String tempFilePath = ImageUtils.getSelectedImage(this, requestCode, resultCode, data, AppConstants.getTempStorageFolder(this));
            int actualRequestCode = ImageUtils.getActualRequestCode(requestCode);

            switch (actualRequestCode) {
                case REQUEST_CODE_SELECT_SATELLITE_GALLERY:
                    satelliteTempImageFile = tempFilePath;
                    loadImage(tempFilePath, binding.pictures.satelliteImage);
                    break;
                case REQUEST_CODE_SELECT_OVERVIEW_GALLERY:
                    overviewTempImageFile = tempFilePath;
                    loadImage(tempFilePath, binding.pictures.overviewImage);
                    break;
                case REQUEST_CODE_SELECT_CROSSOVER_GALLERY:
                    crossoverTempImageFile = tempFilePath;
                    loadImage(tempFilePath, binding.pictures.crossoverImage);
                    break;
                case REQUEST_CODE_SELECT_STREET_GALLERY:
                    streetTempImageFile = tempFilePath;
                    loadImage(tempFilePath, binding.pictures.streetImage);
                    break;
                case REQUEST_CODE_SELECT_BALADAST_GALLERY:
                    baladastTempImageFile = tempFilePath;
                    loadImage(tempFilePath, binding.pictures.baladastImage);
                    break;
                case REQUEST_CODE_SELECT_PAEANDAST_GALLERY:
                    paeandastTempImageFile = tempFilePath;
                    loadImage(tempFilePath, binding.pictures.paeandastImage);
                    break;

            }
        }
    }

    private void loadImage(String filePath, ImageView imageView) {
        Glide.with(this)
                .load(filePath)
                .into(imageView);
    }

    private void clearImage(ImageView imageView) {
        Glide.with(this)
                .clear(imageView);
        imageView.setImageBitmap(null);
    }

    @Override
    public void onLocationChanged(Location location) {
        if (mLocationRequested) {
            mLocationRequested = false;
            binding.identical.gpsXValue.setText(String.valueOf(Double.parseDouble(StringUtils.format("%.7f", location.getLongitude()))));
            binding.identical.gpsYValue.setText(String.valueOf(Double.parseDouble(StringUtils.format("%.7f", location.getLatitude()))));
            locationSource.deactivate();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (locationSource != null)
            locationSource.deactivate();
    }

    public void onNewDamageClick(View view) {
        if (bridgeDetailViewModel.getBridge().isNew)
            showSaveRequire();
        else
            DamageDetailActivity.showActivityForAdd(this, bridgeDetailViewModel.getBridge().roadId, bridgeDetailViewModel.getBridge().bridgeId);
    }

    private void showSaveRequire() {
        new AlertDialog.Builder(this)
                .setMessage("لطفا پل را ذخیره کنید.")
                .setPositiveButton("تایید", null)
                .show();
    }

    private void deleteBridge() {
        new AlertDialog.Builder(this)
                .setMessage("این پل با تمام آسیب های ثبت شده آن حذف خواهند شد! آیا مطمئن هستید؟")
                .setPositiveButton("بله، حذف کن", (dialogInterface, i) -> {
                    doDeleteBridge();
                })
                .setNegativeButton("خیر", null)
                .show();
    }

    private void doDeleteBridge() {
        AsyncTask.execute(() -> {
            String bridgePath = AppConstants.getFilesStorageFolder(this) + bridgeDetailViewModel.getBridge().roadId + "/" + bridgeDetailViewModel.getBridge().bridgeId;
            FileUtils.deleteFolder(bridgePath);
            bridgeDetailViewModel.deleteCurrentBridge();
            runOnUiThread(this::finish);
        });
    }
}