package ir.topcoders.pol.view.activities;

import android.app.Dialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.media.ExifInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.redinput.compassview.CompassView;

import java.io.File;
import java.util.List;
import java.util.UUID;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import io.reactivex.CompletableObserver;
import io.reactivex.disposables.Disposable;
import ir.topcoders.pol.R;
import ir.topcoders.pol.utils.StringUtils;
import ir.topcoders.pol.data.model.BridgeDamage;
import ir.topcoders.pol.data.model.DamageImages;
import ir.topcoders.pol.data.model.DamageImagesWithFullInfo;
import ir.topcoders.pol.databinding.ActivityDamageDetailBinding;
import ir.topcoders.pol.utils.AppConstants;
import ir.topcoders.pol.utils.CompassSensorManager;
import ir.topcoders.pol.utils.DamageUtils;
import ir.topcoders.pol.utils.FileUtils;
import ir.topcoders.pol.utils.GpsUtils;
import ir.topcoders.pol.utils.ImageUtils;
import ir.topcoders.pol.utils.MyGpsLocationHelper;
import ir.topcoders.pol.utils.NumberUtils;
import ir.topcoders.pol.utils.Utils;
import ir.topcoders.pol.view.adapters.DamageImageAdapter;
import ir.topcoders.pol.viewmodel.DamageDetailViewModel;
import ir.topcoders.pol.viewmodel.ViewModelFactory;

public class DamageDetailActivity extends AppCompatActivity {

    private static final String PARAM_DAMAGE_ID = "PARAM_DAMAGE_ID";
    private static final String PARAM_BRIDGE_ID = "PARAM_BRIDGE_ID";
    private static final String PARAM_ROAD_ID = "PARAM_ROAD_ID";

    private static final int TAB_INDEX_INFO = 1;
    private static final int TAB_INDEX_IMAGES = 0;

    private static final int REQUEST_CODE_SELECT_IMAGE_FROM_CAMERA = 100;
    private static final int REQUEST_CODE_SELECT_IMAGE_FROM_GALLERY = 101;
    int countimage = 0;

    Spinner spinner_investigationStatus;
    Spinner investigation_problemValue;

    @Inject
    ViewModelFactory viewModelFactory;
    @Inject
    Utils utils;

    DamageDetailViewModel damageDetailViewModel;
    ActivityDamageDetailBinding binding;
    BridgeDamage mDamage;
    String mRoadId;
    DamageImageAdapter adapter;
    MyGpsLocationHelper locationSource;
    boolean mLocationRequested;
    CompassSensorManager mCompassSensorManager;

    public static void showActivityForAdd(Context context, String roadId, String bridgeId) {
        Intent intent = new Intent(context, DamageDetailActivity.class);
        intent.putExtra(PARAM_ROAD_ID, roadId);
        intent.putExtra(PARAM_BRIDGE_ID, bridgeId);
        context.startActivity(intent);
    }

    public static void showActivityForEdit(Context context, String roadId, String damageId) {
        Intent intent = new Intent(context, DamageDetailActivity.class);
        intent.putExtra(PARAM_ROAD_ID, roadId);
        intent.putExtra(PARAM_DAMAGE_ID, damageId);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidInjection.inject(this);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_damage_detail);

        mCompassSensorManager = new CompassSensorManager(this);
        initToolbar();
        initTabBar();
        initDamagesRecycler();
        initDamageSpinner();
        setupViewModel();

        spinner_investigationStatus = findViewById(R.id.investigation_statusValue);
        investigation_problemValue = findViewById(R.id.investigation_problemValue);

        spinner_investigationStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // your code here
                if(spinner_investigationStatus.getSelectedItemId()==0){
                    investigation_problemValue.setEnabled(false);
                }
                else {
                    investigation_problemValue.setEnabled(true);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                //
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_save_and_detail, menu);
        String damageId = getIntent().getStringExtra(PARAM_DAMAGE_ID);
        if (TextUtils.isEmpty(damageId))
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
            deleteDamage();
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
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("اطلاعات"));
        binding.tabLayout.getTabAt(TAB_INDEX_INFO).select();
        binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                hideAllLayouts();
                switch (tab.getPosition()) {
                    case TAB_INDEX_IMAGES:
                        binding.images.getRoot().setVisibility(View.VISIBLE);
                        break;
                    case TAB_INDEX_INFO:
                        binding.info.getRoot().setVisibility(View.VISIBLE);
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
        binding.images.getRoot().setVisibility(View.GONE);
        binding.info.getRoot().setVisibility(View.GONE);
    }

    private void initDamagesRecycler() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        binding.images.recycler.setLayoutManager(layoutManager);
        adapter = new DamageImageAdapter();
        binding.images.recycler.setAdapter(adapter);
    }

    private void initDamageSpinner() {
        binding.info.elementValue.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                binding.info.damageValue.setAdapter(new ArrayAdapter<>(
                        DamageDetailActivity.this, android.R.layout.simple_spinner_dropdown_item,
                        DamageUtils.generateDamageListByElement(position)

                ));


                if (mDamage.elementCode == position)
                    binding.info.damageValue.setSelection(DamageUtils.getDamageLocalIndex(mDamage.elementCode, mDamage.damageCode));
                else
                    binding.info.damageValue.setSelection(0);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                binding.info.damageValue.setAdapter(new ArrayAdapter<>(
                        DamageDetailActivity.this, android.R.layout.simple_spinner_dropdown_item,
                        DamageUtils.generateDamageListByElement(0)
                ));
            }
        });
    }


    private void setupViewModel() {
        damageDetailViewModel = ViewModelProviders.of(this, viewModelFactory).get(DamageDetailViewModel.class);

        mRoadId = getIntent().getStringExtra(PARAM_ROAD_ID);
        String damageId = getIntent().getStringExtra(PARAM_DAMAGE_ID);
        if (!TextUtils.isEmpty(damageId)) {
            setTitle(R.string.title_edit_damage);
            binding.setLoadingImages(true);
            damageDetailViewModel.selectDamage(damageId).observe(this, damage -> {
                if (damage != null) {
                    mDamage = damage;
                    binding.setDamage(mDamage);

                    binding.info.elementValue.setSelection(mDamage.elementCode);

                    binding.info.investigationStatusValue.setSelection(mDamage.investigationStatus);
                    binding.info.investigationProblemValue.setSelection(mDamage.investigationProblem);
                    countimage += 2;
                    damageDetailViewModel.getDamageImages(mRoadId, damage.bridgeId, damage.damageId)
                            .observe(this, this::setupImages);
                }
            });
        } else {
            setTitle(R.string.title_new_damage);
            String bridgeId = getIntent().getStringExtra(PARAM_BRIDGE_ID);
            mDamage = new BridgeDamage();
            mDamage.damageId = UUID.randomUUID().toString();
            mDamage.bridgeId = bridgeId;

            mDamage.investigationStatus = binding.info.investigationStatusValue.getSelectedItemPosition();
            mDamage.investigationProblem = binding.info.investigationProblemValue.getSelectedItemPosition();

            binding.setDamage(mDamage);
            damageDetailViewModel.getDamageImages(mRoadId, mDamage.bridgeId, mDamage.damageId)
                    .observe(this, this::setupImages);
        }

        damageDetailViewModel.onDamageSave()
                .observe(this, success -> {
                    if (countimage <= 1){
                        new AlertDialog.Builder(this)
                                .setMessage("تعداد تصاویر برای هر آسیب باید بیشتر از 2 تصویر باشد")
                                .setNegativeButton("تایید", null)
                                .show();
                    }else if (success && countimage >= 2) {
                        finish();
                        countimage = 0;
                        Toast.makeText(this, "آسیب با موفقیت ذخیره شد!", Toast.LENGTH_SHORT).show();
                    } else
                        Toast.makeText(this, "خطا در ذخیره سازی", Toast.LENGTH_SHORT).show();
                });
    }

    private void setupImages(List<DamageImagesWithFullInfo> images) {
        binding.setLoadingImages(false);
        binding.setIsEmptyImages(images == null || images.size() == 0);
        adapter.setData(images);
    }

    private void doSave() {
        if (isValidData())
            damageDetailViewModel.insertDamage(mDamage);
        else
            Toast.makeText(this, "اطلاعات را تکمیل کنید!", Toast.LENGTH_SHORT).show();
    }

    private boolean isValidData() {
        boolean validate = true;

        binding.info.elementTitle.setError(null);
        binding.info.damageTitle.setError(null);
        binding.info.damageLevelTitle.setError(null);

        mDamage.elementCode = binding.info.elementValue.getSelectedItemPosition();
        mDamage.damageCode = DamageUtils.getDamageIndexByName(binding.info.damageValue.getSelectedItem().toString());

        //---------------------- investigationStatus , investigationProblem به مدل ارسال می شوند.
        mDamage.investigationStatus = binding.info.investigationStatusValue.getSelectedItemPosition();


        if (mDamage.elementCode == 0) {
            binding.info.elementTitle.setError("تکمیل شود");
            validate = false;
        }

        if (mDamage.damageCode == 0) {
            binding.info.damageTitle.setError("تکمیل شود");
            validate = false;
        }

        if (mDamage.damageLevel == 0) {
            binding.info.damageLevelTitle.setError("تکمیل شود");
            validate = false;
        }

        if (mDamage.investigationStatus == 0){
            mDamage.investigationProblem = 0;
        }else{
            mDamage.investigationProblem = binding.info.investigationProblemValue.getSelectedItemPosition();
        }



        if (mDamage.investigationStatus == 1 && mDamage.investigationProblem == 0) {
            binding.info.investigationProblemTitle.setError("علت را انتخاب نمایید");
            validate = false;
        }




        /*if (mDamage.investigationProblem == 0) {
            binding.info.investigationProblemTitle.setError("تکمیل شود");
            validate = false;
        }*/

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

    public void onNewImageClick(View view) {
        final String items[] = new String[]{getString(R.string.title_from_camera), getString(R.string.title_from_gallery)};

        new AlertDialog.Builder(this)
                .setTitle(R.string.title_select)
                .setItems(items, (d, choice) -> {
                    if (choice == 0) {
                        CameraActivity.showActivity(this, true, REQUEST_CODE_SELECT_IMAGE_FROM_CAMERA);
                    } else if (choice == 1)
                        ImageUtils.launchGallery(this, REQUEST_CODE_SELECT_IMAGE_FROM_GALLERY);
                }).setNegativeButton(R.string.action_cancel, (dialog, which) -> dialog.cancel()).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_CANCELED)
            return;

        if (requestCode == REQUEST_CODE_SELECT_IMAGE_FROM_CAMERA) {
            String tempFilePath = data.getStringExtra(CameraActivity.RESULT_DATA_TEMP_FILE);
            float azimuth = data.getFloatExtra(CameraActivity.RESULT_DATA_AZIMUTH, -1);
            double gpsX = data.getDoubleExtra(CameraActivity.RESULT_DATA_GPS_X, -1);
            double gpsY = data.getDoubleExtra(CameraActivity.RESULT_DATA_GPS_Y, -1);
            doSaveImage(null, tempFilePath, Utils.getPersianDateAndTimeForRecord(), azimuth, gpsX, gpsY);

        } else {
            String tempFilePath = ImageUtils.getSelectedImage(this, requestCode, resultCode, data, AppConstants.getTempStorageFolder(this));
            openSaveImageInfoDialog(tempFilePath);
        }
    }

    private void openSaveImageInfoDialog(String tempFilePath) {
        View view = getLayoutInflater().inflate(R.layout.dialog_damage_image_info, null);
        Dialog dialog = new Dialog(this);
        dialog.setContentView(view);

        EditText dateValue = view.findViewById(R.id.dateValue);
        CompassView compassView = view.findViewById(R.id.compass);
        EditText azimuthValue = view.findViewById(R.id.azimuthValue);
        EditText gpsXValue = view.findViewById(R.id.gpsXValue);
        EditText gpsYValue = view.findViewById(R.id.gpsYValue);
        Button setFromGpsServiceButton = view.findViewById(R.id.setFromGpsServiceButton);
        Button saveButton = view.findViewById(R.id.saveButton);
        Button receiveFromCompassButton = view.findViewById(R.id.receiveFromCompassButton);

        dateValue.setText(Utils.getPersianDateAndTimeForRecord());

        setFromGpsServiceButton.setOnClickListener(v -> requestLocation(gpsXValue, gpsYValue));

        saveButton.setOnClickListener(v -> saveImage(dialog, tempFilePath, dateValue, azimuthValue, gpsXValue, gpsYValue));

        mCompassSensorManager.setListener(azimuth -> {
            compassView.setDegrees(GpsUtils.calcAzimuth(azimuth));
        });
        receiveFromCompassButton.setOnClickListener(v ->
                azimuthValue.setText(String.valueOf(GpsUtils.calcAzimuth(mCompassSensorManager.getAzimuth()))));

        dialog.show();

        dialog.setOnDismissListener(dialogInterface -> mCompassSensorManager.setListener(null));

        Window window = dialog.getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    private void requestLocation(EditText gpsXValue, EditText gpsYValue) {
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

        gpsXValue.setText("");
        gpsYValue.setText("");

        mLocationRequested = true;
        locationSource.activate(location -> {
            if (mLocationRequested) {
                mLocationRequested = false;
                gpsXValue.setText(String.valueOf(Double.parseDouble(StringUtils.format("%.7f", location.getLongitude()))));
                gpsYValue.setText(String.valueOf(Double.parseDouble(StringUtils.format("%.7f", location.getLatitude()))));
                locationSource.deactivate();
            }
        });

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

    private void saveImage(Dialog dialog, String tempFilePath, EditText dateValue, EditText azimuthValue, EditText gpsXValue, EditText gpsYValue) {
        if (isValidImageData(dateValue, azimuthValue, gpsXValue, gpsYValue)) {
            String date = dateValue.getText().toString();
            float azimuth = NumberUtils.parseFloat(azimuthValue.getText().toString(), -1);
            double gpsX = NumberUtils.parseDouble(gpsXValue.getText().toString(), -1);
            double gpsY = NumberUtils.parseDouble(gpsYValue.getText().toString(), -1);

            if (gpsX == -1 || gpsY == -1 || azimuth == -1)
                new AlertDialog.Builder(this)
                        .setMessage("اطلاعات موقعیت وارد نشده، آیا میخواهید تصویر ثبت شود؟")
                        .setPositiveButton("بله، ثبت شود", (dialogInterface, i) -> doSaveImage(dialog, tempFilePath, date, azimuth, gpsX, gpsY))
                        .setNegativeButton("خیر", null)
                        .show();
            else
                doSaveImage(dialog, tempFilePath, date, azimuth, gpsX, gpsY);
        } else
            Toast.makeText(this, "اطلاعات را تکمیل کنید!", Toast.LENGTH_SHORT).show();
    }


    private void doSaveImage(Dialog dialog, String tempFilePath, String date, float azimuth, double gpsX, double gpsY) {
        DamageImages damageImage = new DamageImages();
        damageImage.captureDate = date;
        damageImage.azimuth = azimuth;
        damageImage.gpsX = gpsX;
        damageImage.gpsY = gpsY;
        damageImage.damageId = mDamage.damageId;
        damageImage.imageId = UUID.randomUUID().toString();
        String dateTime = Utils.getPersianDateAndTime("-");
        damageImage.damageImageFilename = damageImage.imageId + "__" + dateTime + ".jpg";
        File targetFile = moveTempFile(tempFilePath, damageImage.damageImageFilename);
        writeExifData(targetFile, dateTime, damageImage);
        damageDetailViewModel.insertDamageImage(damageImage)
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onComplete() {
                        countimage++;
                        Toast.makeText(DamageDetailActivity.this, "تصویر ذخیره شد!", Toast.LENGTH_SHORT).show();

                        if (dialog != null)
                            dialog.dismiss();
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        Toast.makeText(DamageDetailActivity.this, "خطا در ذخیره سازی", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private File moveTempFile(String tempFilePath, String damageImageFilename) {
        String baseTargetPath = AppConstants.getFilesStorageFolder(this)
                + mRoadId
                + "/" + mDamage.bridgeId
                + "/" + mDamage.damageId;
        File baseTarget = new File(baseTargetPath);
        baseTarget.mkdirs();

        File targetFile = new File(baseTarget, damageImageFilename);
        FileUtils.moveFile(new File(tempFilePath), targetFile);
        return targetFile;
    }


    private void writeExifData(File file, String dateTime, DamageImages damageImage) {
        try {
            ExifInterface exif = new ExifInterface(file.getAbsolutePath());
            exif.setAttribute("ImageDescription",
                    "file:" + file.getName() +
                            "\r\nRoadId:" + mRoadId +
                            "\r\nBridgeId:" + mDamage.bridgeId +
                            "\r\nDamageId:" + mDamage.damageId +
                            "\r\nDamageImageId:" + damageImage.imageId +
                            "\r\nElementCode:" + mDamage.elementCode +
                            "\r\nDamageCode:" + mDamage.damageCode +
                            "\r\nDamageLevel:" + mDamage.damageLevel +
                            "\r\nAzimuth:" + damageImage.azimuth);
            exif.setAttribute(ExifInterface.TAG_DATETIME, dateTime);
            exif.setAttribute(ExifInterface.TAG_GPS_LATITUDE, GpsUtils.convert(damageImage.gpsY));
            exif.setAttribute(ExifInterface.TAG_GPS_LATITUDE_REF, GpsUtils.latitudeRef(damageImage.gpsY));
            exif.setAttribute(ExifInterface.TAG_GPS_LONGITUDE, GpsUtils.convert(damageImage.gpsX));
            exif.setAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF, GpsUtils.longitudeRef(damageImage.gpsX));
            exif.saveAttributes();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean isValidImageData(EditText dateValue, EditText azimuthValue, EditText gpsXValue, EditText gpsYValue) {
        boolean validate = true;

        dateValue.setError(null);
        azimuthValue.setError(null);
        gpsXValue.setError(null);
        gpsYValue.setError(null);

        if (TextUtils.isEmpty(dateValue.getText().toString().trim())) {
            dateValue.setError("تکمیل شود");
            validate = false;
        }

        float azimuth = NumberUtils.parseFloat(azimuthValue.getText().toString(), 0);
        if (azimuth < 0 || azimuth > 360) {
            azimuthValue.setError("مقدار اشتباه");
            validate = false;
        }

        double gpsX = NumberUtils.parseDouble(gpsXValue.getText().toString(), 0);
        if (gpsX > 0 && (gpsX < 40 || gpsX >= 66)) {
            gpsXValue.setError("موقعیت نادرست");
            validate = false;
        }

        double gpsY = NumberUtils.parseDouble(gpsYValue.getText().toString(), 0);
        if (gpsY > 0 && (gpsY < 24 || gpsY >= 41)) {
            gpsYValue.setError("موقعیت نادرست");
            validate = false;
        }

        return validate;
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (locationSource != null)
            locationSource.deactivate();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mCompassSensorManager.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mCompassSensorManager.onPause();
    }

    private void deleteDamage() {
        new AlertDialog.Builder(this)
                .setMessage("این آسیب حذف خواهد شد! آیا مطمئن هستید؟")
                .setPositiveButton("بله، حذف کن", (dialogInterface, i) -> {
                    doDeleteDamage();
                })
                .setNegativeButton("خیر", null)
                .show();
    }

    private void doDeleteDamage() {
        AsyncTask.execute(() -> {
            String damagePath = AppConstants.getFilesStorageFolder(this) + mRoadId + "/" + mDamage.bridgeId + "/" + mDamage.damageId;
            FileUtils.deleteFolder(damagePath);
            damageDetailViewModel.deleteDamage(mDamage.damageId);
            runOnUiThread(this::finish);
        });
    }

    public void openDamageImage(DamageImages damageImage) {
        String damageImagePath = AppConstants.getFilesStorageFolder(this) + mRoadId + "/" + mDamage.bridgeId + "/" + mDamage.damageId + "/" + damageImage.damageImageFilename;
        ImageUtils.openImageByDefaultApp(this, new File(damageImagePath));
    }

    public void removeDamageImage(DamageImages damageImage) {
        new AlertDialog.Builder(this)
                .setMessage("آیا میخواهید این تصویر را حذف کنید؟")
                .setPositiveButton("بله، حذف کن", (dialogInterface, i) -> {
                    doDeleteDamageImage(damageImage);
                })
                .setNegativeButton("خیر", null)
                .show();
    }

    private void doDeleteDamageImage(DamageImages damageImage) {
        AsyncTask.execute(() -> {
            String damageImagePath = AppConstants.getFilesStorageFolder(this) + mRoadId + "/" + mDamage.bridgeId + "/" + mDamage.damageId + "/" + damageImage.damageImageFilename;
            FileUtils.deleteFile(damageImagePath);
            damageDetailViewModel.deleteDamageImage(damageImage.imageId);
        });
    }
}