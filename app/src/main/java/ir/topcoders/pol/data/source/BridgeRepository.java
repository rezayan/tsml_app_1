package ir.topcoders.pol.data.source;

import android.arch.lifecycle.LiveData;

import java.util.List;

import javax.inject.Inject;

import ir.topcoders.pol.data.model.Bridge;
import ir.topcoders.pol.data.source.local.BridgeDao;

public class BridgeRepository {

    private final BridgeDao bridgeDao;

    @Inject
    BridgeRepository(BridgeDao bridgeDao) {
        this.bridgeDao = bridgeDao;
    }

    public LiveData<List<Bridge>> getBridges(String roadId) {
        return bridgeDao.queryBridgesByRoad(roadId);
    }

    public LiveData<Bridge> selectBridge(String bridgeId) {
        return bridgeDao.selectBridge(bridgeId);
    }

    public boolean insertBridge(Bridge bridge) {
        if (bridge.isNew)
            return bridgeDao.insert(bridge) > 0;
        else
            return bridgeDao.update(bridge) > 0;
    }

    public void removeBridge(String bridgeId) {
        bridgeDao.removeAllDamageImagesByBridge(bridgeId);
        bridgeDao.removeAllDamagesByBridge(bridgeId);
        bridgeDao.removeBridge(bridgeId);
    }
}
