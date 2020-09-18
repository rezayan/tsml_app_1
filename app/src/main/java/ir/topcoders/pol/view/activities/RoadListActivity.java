package ir.topcoders.pol.view.activities;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import ir.topcoders.pol.R;
import ir.topcoders.pol.databinding.ActivityRoadListBinding;
import ir.topcoders.pol.view.adapters.RoadAdapter;
import ir.topcoders.pol.viewmodel.RoadListViewModel;
import ir.topcoders.pol.viewmodel.ViewModelFactory;

public class RoadListActivity extends AppCompatActivity {

    @Inject
    ViewModelFactory viewModelFactory;

    RoadListViewModel roadListViewModel;
    ActivityRoadListBinding binding;
    RoadAdapter adapter;

    public static void showActivity(Context context) {
        Intent intent = new Intent(context, RoadListActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidInjection.inject(this);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_road_list);

        initToolbar();
        initRecycler();
        setupViewModel();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(isSelectionMode() ? R.menu.menu_export : R.menu.menu_roads, menu);
        return true;
    }

    private boolean isSelectionMode() {
        return adapter == null || adapter.isSelectionMode();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_export) {
            enableSelectionMode(true);
            return true;
        } else if (item.getItemId() == R.id.action_about) {
            AboutActivity.showActivity(this);
            return true;
        } else if (item.getItemId() == R.id.action_export_confirm) {
            createBackup();
            return true;
        } else if (item.getItemId() == android.R.id.home) {
            enableSelectionMode(false);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initToolbar() {
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
    }

    private void initRecycler() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        binding.recycler.setLayoutManager(layoutManager);
        adapter = new RoadAdapter();
        binding.recycler.setAdapter(adapter);
    }

    private void setupViewModel() {
        binding.setLoading(true);
        roadListViewModel = ViewModelProviders.of(this, viewModelFactory).get(RoadListViewModel.class);

        roadListViewModel.getRoads()
                .observe(this, roads -> {
                    binding.setLoading(false);
                    binding.setIsEmpty(roads == null || roads.size() == 0);
                    adapter.setData(roads);
                });
    }

    public void onFabClick(View view) {
        RoadDetailActivity.showActivity(this, null);
    }

    private void enableSelectionMode(boolean enable) {
        binding.setSelectedCount(0);
        binding.setSelectionMode(enable);
        adapter.setSelectionMode(enable);
        getSupportActionBar().setDisplayHomeAsUpEnabled(enable);
        getSupportActionBar().setDisplayShowHomeEnabled(enable);
        invalidateOptionsMenu();
    }

    public void selectionUpdated(int position) {
        adapter.notifyItemChanged(position);
        binding.setSelectedCount(adapter.getSelectedRoadIds().size());
    }

    private void createBackup() {
        ArrayList<String> selectedRoadIds = adapter.getSelectedRoadIds();
        if (selectedRoadIds == null || selectedRoadIds.size() == 0)
            new AlertDialog.Builder(this)
                    .setMessage("حداقل یک مسیر را انتخاب کنید.")
                    .setPositiveButton("تایید", null)
                    .show();
        else
            new AlertDialog.Builder(this)
                    .setMessage("آیا میخواهید فایل خروجی ساخته شود؟")
                    .setPositiveButton("بله", (dialogInterface, i) -> {
                        if (selectedRoadIds.size() == 1)
                            BackupActivity.showActivity(this, adapter.getSelectedRoad());
                        else
                            BackupActivity.showActivity(this, selectedRoadIds);
                        enableSelectionMode(false);
                    })
                    .setNegativeButton("خیر", null)
                    .show();
    }

    @Override
    public void onBackPressed() {
        if (adapter.isSelectionMode())
            enableSelectionMode(false);
        else
            super.onBackPressed();
    }
}
