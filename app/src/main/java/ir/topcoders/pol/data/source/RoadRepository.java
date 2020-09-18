package ir.topcoders.pol.data.source;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.db.SimpleSQLiteQuery;

import java.util.List;

import javax.inject.Inject;

import ir.topcoders.pol.data.model.Road;
import ir.topcoders.pol.data.model.RoadWithBridgeCount;
import ir.topcoders.pol.data.source.local.RawDao;
import ir.topcoders.pol.data.source.local.RoadDao;

public class RoadRepository {

    private final RoadDao roadDao;

    @Inject
    public RoadRepository(RoadDao roadDao) {
        this.roadDao = roadDao;
    }

    public LiveData<List<Road>> getRoads() {
        return roadDao.queryRoads();
    }

    public List<Road> getRoadsSync() {
        return roadDao.queryRoadsSync();
    }

    public LiveData<List<RoadWithBridgeCount>> getRoadsWithBridgeCount() {
        return roadDao.queryRoadsWithBridgeCount();
    }

    public LiveData<Road> selectRoad(String roadId) {
        return roadDao.selectRoad(roadId);
    }

    public boolean insertRoad(Road road) {
        if (road.isNew)
            return roadDao.insert(road) > 0;
        else
            return roadDao.update(road) > 0;
    }

    public void removeRoad(String roadId) {
        roadDao.removeAllDamageImagesByRoad(roadId);
        roadDao.removeAllDamagesByRoad(roadId);
        roadDao.removeAllBridgesByRoad(roadId);
        roadDao.removeRoad(roadId);
    }
}
