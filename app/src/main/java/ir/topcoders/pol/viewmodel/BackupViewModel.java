package ir.topcoders.pol.viewmodel;

import android.app.Application;
import android.arch.lifecycle.ViewModel;

import java.io.File;
import java.util.ArrayList;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import ir.topcoders.pol.data.model.Road;
import ir.topcoders.pol.data.source.RoadRepository;
import ir.topcoders.pol.data.source.local.MyDatabase;
import ir.topcoders.pol.utils.BackupUtils;

public class BackupViewModel extends ViewModel {

    private Application context;
    private MyDatabase myDatabase;

    @Inject
    BackupViewModel(Application context, MyDatabase myDatabase) {
        this.context = context;
        this.myDatabase = myDatabase;
    }

    public Single<File> createBackup(ArrayList<String> selectedRoadIds) {
        return Single.fromCallable(() -> BackupUtils.createBackup(context, myDatabase, selectedRoadIds))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<File> createBackup( Road selectedRoad) {
        return Single.fromCallable(() -> BackupUtils.createBackup(context, myDatabase, selectedRoad))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}