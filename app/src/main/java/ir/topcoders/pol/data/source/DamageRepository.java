package ir.topcoders.pol.data.source;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.db.SimpleSQLiteQuery;

import java.util.List;

import javax.inject.Inject;

import ir.topcoders.pol.data.model.BridgeDamage;
import ir.topcoders.pol.data.model.BridgeDamageWithImage;
import ir.topcoders.pol.data.source.local.BridgeDamageDao;

public class DamageRepository {

    private final BridgeDamageDao bridgeDamageDao;

    @Inject
    DamageRepository(BridgeDamageDao bridgeDamageDao) {
        this.bridgeDamageDao = bridgeDamageDao;
    }

    public LiveData<List<BridgeDamageWithImage>> getBridgeDamagesWithImage(String roadId, String bridgeId) {
        return bridgeDamageDao.queryDamagesWithImageByBridge(roadId, bridgeId);
    }

    public LiveData<BridgeDamage> selectDamage(String damageId) {
        return bridgeDamageDao.selectDamage(damageId);
    }

    public void insertDamage(BridgeDamage damage) {
        bridgeDamageDao.insert(damage);
    }

    public void removeDamage(String damageId) {
        bridgeDamageDao.removeDamageImages(damageId);
        bridgeDamageDao.removeDamage(damageId);
    }
}
