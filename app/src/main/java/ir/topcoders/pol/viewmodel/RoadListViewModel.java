package ir.topcoders.pol.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import java.util.List;

import javax.inject.Inject;

import ir.topcoders.pol.data.model.RoadWithBridgeCount;
import ir.topcoders.pol.data.source.RoadRepository;

public class RoadListViewModel extends ViewModel {

    private final RoadRepository roadRepository;
    private LiveData<List<RoadWithBridgeCount>> roads;

    @Inject
    RoadListViewModel(RoadRepository roadRepository) {
        this.roadRepository = roadRepository;
        init();
    }

    private void init() {
        roads = roadRepository.getRoadsWithBridgeCount();
    }

    public LiveData<List<RoadWithBridgeCount>> getRoads() {
        return roads;
    }
}