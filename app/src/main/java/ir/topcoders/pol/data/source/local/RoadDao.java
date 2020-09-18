package ir.topcoders.pol.data.source.local;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import ir.topcoders.pol.data.model.Road;
import ir.topcoders.pol.data.model.RoadWithBridgeCount;

@Dao
public interface RoadDao {

    @Insert
    long insert(Road road);

    @Update
    int update(Road road);

    @Query("SELECT * FROM Roads order by registration_date desc")
    LiveData<List<Road>> queryRoads();

    @Query("SELECT * FROM Roads order by registration_date desc")
    List<Road> queryRoadsSync();

    @Query("SELECT Roads.* " +
            ",COUNT(Bridges.bridge_id) as bridgeCount " +
            "FROM Roads " +
            "LEFT JOIN Bridges " +
            "ON Roads.road_id = Bridges.road_id " +
            "GROUP BY Roads.road_id " +
            "ORDER BY Roads.registration_date desc")
    LiveData<List<RoadWithBridgeCount>> queryRoadsWithBridgeCount();

    @Query("SELECT * FROM Roads WHERE road_id= :roadId")
    LiveData<Road> selectRoad(String roadId);

    @Query("DELETE FROM Damage_Images " +
            "WHERE damage_id " +
            "IN " +
            "(SELECT Bridge_Damages.damage_id FROM Bridge_Damages " +
            "WHERE Bridge_Damages.bridge_id " +
            "IN " +
            "(SELECT Bridges.bridge_id FROM Bridges WHERE Bridges.road_id = :roadId)" +
            ")")
    void removeAllDamageImagesByRoad(String roadId);

    @Query("DELETE FROM Bridge_Damages " +
            "WHERE bridge_id " +
            "IN " +
            "(SELECT Bridges.bridge_id FROM Bridges WHERE Bridges.road_id = :roadId)")
    void removeAllDamagesByRoad(String roadId);

    @Query("DELETE FROM Bridges where road_id= :roadId")
    void removeAllBridgesByRoad(String roadId);

    @Query("DELETE FROM Roads WHERE road_id= :roadId")
    void removeRoad(String roadId);
}
