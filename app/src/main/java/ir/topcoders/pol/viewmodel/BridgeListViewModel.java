package ir.topcoders.pol.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import java.util.List;

import javax.inject.Inject;

import ir.topcoders.pol.data.model.Bridge;
import ir.topcoders.pol.data.source.BridgeRepository;
import ir.topcoders.pol.data.source.RoadRepository;

public class BridgeListViewModel extends ViewModel {

    private final BridgeRepository bridgeRepository;
    private final RoadRepository roadRepository;

    @Inject
    BridgeListViewModel(BridgeRepository bridgeRepository, RoadRepository roadRepository) {
        this.bridgeRepository = bridgeRepository;
        this.roadRepository = roadRepository;
    }

    public LiveData<List<Bridge>> getBridges(String roadId) {
        return bridgeRepository.getBridges(roadId);
    }

    public void deleteRoadWithBridges(String roadId) {
        roadRepository.removeRoad(roadId);
    }
}