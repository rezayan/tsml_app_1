package ir.topcoders.pol.data.source;

import android.arch.lifecycle.LiveData;

import java.util.List;

import javax.inject.Inject;

import ir.topcoders.pol.data.model.DamageImages;
import ir.topcoders.pol.data.model.DamageImagesWithFullInfo;
import ir.topcoders.pol.data.source.local.DamageImageDao;

public class DamageImageRepository {

    private final DamageImageDao damageImageDao;

    @Inject
    DamageImageRepository(DamageImageDao damageImageDao) {
        this.damageImageDao = damageImageDao;
    }

    public LiveData<List<DamageImagesWithFullInfo>> getDamageImages(String roadId, String bridgeId, String damageId) {
        return damageImageDao.queryImagesByDamage(roadId, bridgeId, damageId);
    }

    public LiveData<DamageImages> selectImage(String imageId) {
        return damageImageDao.selectImage(imageId);
    }

    public void insertImage(DamageImages image) {
        damageImageDao.insert(image);
    }

    public void removeImage(String imageId) {
        damageImageDao.removeImage(imageId);
    }
}
