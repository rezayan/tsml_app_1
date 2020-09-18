package ir.topcoders.pol.view.activities;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import ir.topcoders.pol.R;
import ir.topcoders.pol.data.model.Road;
import ir.topcoders.pol.databinding.ActivityBridgeListBinding;
import ir.topcoders.pol.utils.AppConstants;
import ir.topcoders.pol.utils.FileUtils;
import ir.topcoders.pol.view.adapters.BridgeAdapter;
import ir.topcoders.pol.viewmodel.BridgeListViewModel;
import ir.topcoders.pol.viewmodel.ViewModelFactory;

public class BridgeListActivity extends AppCompatActivity {

    private static final String PARAM_ROAD = "PARAM_ROAD";

    @Inject
    ViewModelFactory viewModelFactory;

    BridgeListViewModel bridgeListViewModel;
    ActivityBridgeListBinding binding;
    BridgeAdapter adapter;
    Road mRoad;

    public static void showActivity(Context context, Road road) {
        Intent intent = new Intent(context, BridgeListActivity.class);
        intent.putExtra(PARAM_ROAD, road);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidInjection.inject(this);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_bridge_list);

        if (loadParams()) {
            initToolbar();
            initRecycler();
            setupViewModel();
        }
    }

    private boolean loadParams() {
        mRoad = (Road) getIntent().getSerializableExtra(PARAM_ROAD);
        if (mRoad == null) {
            finish();
            return false;
        } else {
            binding.setRoad(mRoad);
            return true;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_bridge_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_edit:
                RoadDetailActivity.showActivity(this, mRoad.roadId);
                return true;
            case R.id.action_delete:
                deleteRoad();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initToolbar() {
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    private void initRecycler() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        binding.recycler.setLayoutManager(layoutManager);
        adapter = new BridgeAdapter();
        binding.recycler.setAdapter(adapter);
    }

    private void setupViewModel() {
        binding.setLoading(true);
        bridgeListViewModel = ViewModelProviders.of(this, viewModelFactory).get(BridgeListViewModel.class);

        bridgeListViewModel.getBridges(mRoad.roadId)
                .observe(this, bridges -> {
                    binding.setLoading(false);
                    binding.setIsEmpty(bridges == null || bridges.size() == 0);
                    adapter.setData(bridges);
                });
    }

    public void onFabClick(View view) {
        BridgeDetailActivity.showActivityForAdd(this, mRoad.roadId);
    }

    private void deleteRoad() {
        new AlertDialog.Builder(this)
                .setMessage("آیا میخواهید این مسیر را حذف کنید؟")
                .setPositiveButton("بله، حذف کن", (dialogInterface, i) -> {
                    checkBridgesForDeleteRoad();
                })
                .setNegativeButton("خیر", null)
                .show();
    }

    private void checkBridgesForDeleteRoad() {
        if (adapter == null || adapter.getItemCount() > 0)
            new AlertDialog.Builder(this)
                    .setMessage("تمام پل های داخل این مسیر حذف خواهند شد! آیا مطمئن هستید؟")
                    .setPositiveButton("بله، حذف کن", (dialogInterface, i) -> {
                        doDeleteRoad();
                    })
                    .setNegativeButton("خیر", null)
                    .show();
        else
            doDeleteRoad();
    }

    private void doDeleteRoad() {
        AsyncTask.execute(() -> {
            FileUtils.deleteFolder(AppConstants.getFilesStorageFolder(this) + mRoad.roadId);
            bridgeListViewModel.deleteRoadWithBridges(mRoad.roadId);
            runOnUiThread(this::finish);
        });
    }
}
