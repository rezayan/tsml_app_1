package ir.topcoders.pol.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableCompletableObserver;
import io.reactivex.schedulers.Schedulers;
import ir.topcoders.pol.utils.StringUtils;
import ir.topcoders.pol.data.model.Road;
import ir.topcoders.pol.data.source.RoadRepository;

public class RoadDetailViewModel extends ViewModel {

    private final RoadRepository roadRepository;
    private DisposableCompletableObserver disposableObserver;
    private MutableLiveData<Integer> onRoadSave = new MutableLiveData<>();

    public static final int SAVE_RESULT_OK = 0;
    public static final int SAVE_RESULT_UNIQUE = 1;
    public static final int SAVE_RESULT_ERROR = 2;

    @Inject
    RoadDetailViewModel(RoadRepository roadRepository) {
        this.roadRepository = roadRepository;
    }

    public LiveData<Road> selectRoad(String roadId) {
        return roadRepository.selectRoad(roadId);
    }

    public MutableLiveData<Integer> onRoadSave() {
        return onRoadSave;
    }

    public void insertRoad(Road road) {
        trim(road);
        disposableObserver = new DisposableCompletableObserver() {
            @Override
            public void onComplete() {
                onRoadSave.postValue(SAVE_RESULT_OK);
            }

            @Override
            public void onError(Throwable e) {
                if (e.getMessage().startsWith("UNIQUE"))
                    onRoadSave.postValue(SAVE_RESULT_UNIQUE);
                else
                    onRoadSave.postValue(SAVE_RESULT_ERROR);
            }
        };

        Completable.fromAction(() -> {
            if (!roadRepository.insertRoad(road))
                throw new Exception("error in insert!");
        }).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(disposableObserver);
    }

    private void trim(Road road) {
        road.registrationDate = road.registrationDate.trim();
        road.startName = road.startName.trim();
        road.stopName = road.stopName.trim();
        if (road.code != null && road.code.trim().length() == 0)
            road.code = null;
        if (road.code != null)
            road.code = road.code.trim();
        road.startGpsX = Double.parseDouble(StringUtils.format("%.7f", road.startGpsX));
        road.startGpsY = Double.parseDouble(StringUtils.format("%.7f", road.startGpsY));
    }
}