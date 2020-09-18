package ir.topcoders.pol.data.source.local;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import ir.topcoders.pol.data.model.Bridge;

@Dao
public interface BridgeDao {

    @Insert
    long insert(Bridge bridge);

    @Update
    int update(Bridge bridge);

    @Query("SELECT * FROM Bridges where road_id= :roadId order by station desc")
    LiveData<List<Bridge>> queryBridgesByRoad(String roadId);

    @Query("SELECT * FROM Bridges WHERE bridge_id= :bridgeId")
    LiveData<Bridge> selectBridge(String bridgeId);

    @Query("DELETE FROM Damage_Images " +
            "WHERE damage_id " +
            "IN " +
            "(SELECT Bridge_Damages.damage_id FROM Bridge_Damages " +
            "WHERE Bridge_Damages.bridge_id = :bridgeId)")
    void removeAllDamageImagesByBridge(String bridgeId);

    @Query("DELETE FROM Bridge_Damages " +
            "WHERE bridge_id = :bridgeId")
    void removeAllDamagesByBridge(String bridgeId);

    @Query("DELETE FROM Bridges WHERE bridge_id= :bridgeId")
    void removeBridge(String bridgeId);
}
