package ir.topcoders.pol.view.activities;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import ir.topcoders.pol.R;
import ir.topcoders.pol.data.model.Road;
import ir.topcoders.pol.databinding.ActivityBackupBinding;
import ir.topcoders.pol.databinding.ActivityRoadListBinding;
import ir.topcoders.pol.utils.AppConstants;
import ir.topcoders.pol.view.adapters.RoadAdapter;
import ir.topcoders.pol.viewmodel.BackupViewModel;
import ir.topcoders.pol.viewmodel.RoadListViewModel;
import ir.topcoders.pol.viewmodel.ViewModelFactory;

public class BackupActivity extends AppCompatActivity {

    private static final String PARAM_SELECTED_ROADS = "PARAM_SELECTED_ROADS";
    private static final String PARAM_SELECTED_SINGLE_ROAD = "PARAM_SELECTED_SINGLE_ROAD";

    @Inject
    ViewModelFactory viewModelFactory;

    BackupViewModel backupViewModel;
    ActivityBackupBinding binding;
    boolean allowExit = true;

    public static void showActivity(Context context, ArrayList<String> selectedRoadIds) {
        Intent intent = new Intent(context, BackupActivity.class);
        intent.putStringArrayListExtra(PARAM_SELECTED_ROADS, selectedRoadIds);
        context.startActivity(intent);
    }

    public static void showActivity(Context context, Road road) {
        Intent intent = new Intent(context, BackupActivity.class);
        intent.putExtra(PARAM_SELECTED_SINGLE_ROAD, road);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidInjection.inject(this);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_backup);

        initToolbar();
        setupViewModel();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
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

    private void setupViewModel() {
        backupViewModel = ViewModelProviders.of(this, viewModelFactory).get(BackupViewModel.class);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        receivedSelectedRoads();
    }

    private void receivedSelectedRoads() {
        ArrayList<String> selectedRoadIds = getIntent().getStringArrayListExtra(PARAM_SELECTED_ROADS);
        Road selectedRoad = (Road) getIntent().getSerializableExtra(PARAM_SELECTED_SINGLE_ROAD);
        if (selectedRoad == null && (selectedRoadIds == null || selectedRoadIds.size() == 0))
            finish();
        else {
            if (selectedRoad != null)
                createBackup(selectedRoad);
            else
                createBackup(selectedRoadIds);
        }
    }

    private void createBackup(ArrayList<String> selectedRoadIds) {
        doCreateBackup(backupViewModel.createBackup(selectedRoadIds));
    }

    private void createBackup(Road selectedRoad) {
        doCreateBackup(backupViewModel.createBackup(selectedRoad));
    }

    private void doCreateBackup(Single<File> callable) {
        clearTempFolder();

        callable.subscribe(new SingleObserver<File>() {
            @Override
            public void onSubscribe(Disposable d) {
                allowExit = false;
                binding.setLoading(true);
            }

            @Override
            public void onSuccess(File zipFile) {
                binding.setLoading(false);
                Toast.makeText(BackupActivity.this, "فایل zip ساخته شد", Toast.LENGTH_SHORT).show();
                shareZipFile(zipFile);
                allowExit = true;
                binding.targetFolderAddressTextView.setText("ذخیره شده در مسیر:" + "\n\n" + zipFile.getAbsolutePath());
                setTitle("خروجی ساخته شد");
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                showErrorInMakeBackup();
                allowExit = true;
            }
        });
    }

    private void clearTempFolder() {
        try {
            File dir = new File(AppConstants.getTempStorageFolder(this));
            org.apache.commons.io.FileUtils.deleteDirectory(dir);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showErrorInMakeBackup() {
        new AlertDialog.Builder(this)
                .setMessage("خطا در ساخت فایل خروجی")
                .setPositiveButton("تایید", null)
                .setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        finish();
                    }
                })
                .show();
    }

    private void shareZipFile(File zipFile) {
        try {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(zipFile));
            sendIntent.setType("application/zip");
            startActivity(sendIntent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onBackClick(View view) {
        finish();
    }

    @Override
    public void onBackPressed() {
        if (allowExit)
            super.onBackPressed();
        else
            new AlertDialog.Builder(this)
                    .setMessage("صبر کنید فایل خروجی ساخته شود!")
                    .setPositiveButton("تایید", null)
                    .show();
    }
}
