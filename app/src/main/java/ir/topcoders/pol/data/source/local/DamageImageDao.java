package ir.topcoders.pol.data.source.local;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import ir.topcoders.pol.data.model.DamageImages;
import ir.topcoders.pol.data.model.DamageImagesWithFullInfo;

@Dao
public interface DamageImageDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(DamageImages image);

    @Query("SELECT *, :roadId AS road_id, :bridgeId AS bridge_id FROM Damage_Images where damage_id= :damageId")
    LiveData<List<DamageImagesWithFullInfo>> queryImagesByDamage(String roadId,String bridgeId, String damageId);

    @Query("SELECT * FROM Damage_Images WHERE image_id= :imageId")
    LiveData<DamageImages> selectImage(String imageId);

    @Query("DELETE FROM Damage_Images WHERE image_id= :imageId")
    void removeImage(String imageId);
}
