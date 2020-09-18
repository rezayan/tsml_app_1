package ir.topcoders.pol.data.source.local;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import ir.topcoders.pol.data.model.BridgeDamage;
import ir.topcoders.pol.data.model.BridgeDamageWithImage;

@Dao
public interface BridgeDamageDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(BridgeDamage damage);

    @Query("SELECT * FROM Bridge_Damages where bridge_id= :bridgeId")
    LiveData<List<BridgeDamage>> queryDamagesByBridge(String bridgeId);

    @Query("SELECT Bridge_Damages.*, :roadId AS road_id, MIN(Damage_Images.damage_image_filename) AS image_file_name " +
            "FROM Bridge_Damages " +
            "LEFT JOIN Damage_Images " +
            "ON Bridge_Damages.damage_id = Damage_Images.damage_id " +
            "WHERE Bridge_Damages.bridge_id= :bridgeId " +
            "GROUP BY Bridge_Damages.damage_id " +
            "ORDER BY Bridge_Damages.element_code, Bridge_Damages.damage_code, Bridge_Damages.damage_level"
    )
    LiveData<List<BridgeDamageWithImage>> queryDamagesWithImageByBridge(String roadId, String bridgeId);

    @Query("SELECT * FROM Bridge_Damages WHERE damage_id= :damageId")
    LiveData<BridgeDamage> selectDamage(String damageId);

    @Query("DELETE FROM Damage_Images WHERE damage_id= :damageId")
    void removeDamageImages(String damageId);

    @Query("DELETE FROM Bridge_Damages WHERE damage_id= :damageId")
    void removeDamage(String damageId);
}
