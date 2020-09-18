package ir.topcoders.pol.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableCompletableObserver;
import io.reactivex.schedulers.Schedulers;
import ir.topcoders.pol.utils.StringUtils;
import ir.topcoders.pol.data.model.BridgeDamage;
import ir.topcoders.pol.data.model.DamageImages;
import ir.topcoders.pol.data.model.DamageImagesWithFullInfo;
import ir.topcoders.pol.data.source.DamageImageRepository;
import ir.topcoders.pol.data.source.DamageRepository;

public class DamageDetailViewModel extends ViewModel {

    private final DamageRepository damageRepository;
    private final DamageImageRepository damageImageRepository;
    private DisposableCompletableObserver disposableObserver;
    private MutableLiveData<Boolean> onDamageSave = new MutableLiveData<>();

    @Inject
    DamageDetailViewModel(DamageRepository damageRepository, DamageImageRepository damageImageRepository) {
        this.damageRepository = damageRepository;
        this.damageImageRepository = damageImageRepository;
    }

    public LiveData<BridgeDamage> selectDamage(String damageId) {
        return damageRepository.selectDamage(damageId);
    }

    public LiveData<List<DamageImagesWithFullInfo>> getDamageImages(String roadId, String bridgeId, String damageId) {
        return damageImageRepository.getDamageImages(roadId, bridgeId, damageId);
    }

    public MutableLiveData<Boolean> onDamageSave() {
        return onDamageSave;
    }

    public void insertDamage(BridgeDamage damage) {
        trim(damage);
        disposableObserver = new DisposableCompletableObserver() {
            @Override
            public void onComplete() {
                onDamageSave.postValue(true);
            }

            @Override
            public void onError(Throwable e) {
                onDamageSave.postValue(false);
            }
        };

        Completable.fromAction(() -> damageRepository.insertDamage(damage))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(disposableObserver);
    }

    private void trim(BridgeDamage damage) {
        if (damage.description != null)
            damage.description = damage.description.trim();
    }

    public Completable insertDamageImage(DamageImages damageImage) {
        trim(damageImage);

        return Completable.fromAction(() -> damageImageRepository.insertImage(damageImage))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
    }

    private void trim(DamageImages damageImage) {
        if (damageImage.gpsX == -1)
            damageImage.gpsX = null;
        else
            damageImage.gpsX = Double.parseDouble(StringUtils.format("%.7f", damageImage.gpsX));

        if (damageImage.gpsY == -1)
            damageImage.gpsY = null;
        else
            damageImage.gpsY = Double.parseDouble(StringUtils.format("%.7f", damageImage.gpsY));

        if (damageImage.azimuth == -1)
            damageImage.azimuth = null;
        else
            damageImage.azimuth = Float.parseFloat(StringUtils.format("%.3f", damageImage.azimuth));
    }

    public void deleteDamage(String damageId) {
        damageRepository.removeDamage(damageId);
    }

    public void deleteDamageImage(String imageId) {
        damageImageRepository.removeImage(imageId);
    }
}