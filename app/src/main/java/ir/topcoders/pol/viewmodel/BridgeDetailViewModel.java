package ir.topcoders.pol.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import java.util.List;
import java.util.UUID;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableCompletableObserver;
import io.reactivex.schedulers.Schedulers;
import ir.topcoders.pol.utils.StringUtils;
import ir.topcoders.pol.data.LocalSettings;
import ir.topcoders.pol.data.model.Bridge;
import ir.topcoders.pol.data.model.BridgeDamageWithImage;
import ir.topcoders.pol.data.source.BridgeRepository;
import ir.topcoders.pol.data.source.DamageRepository;
import ir.topcoders.pol.utils.Utils;

public class BridgeDetailViewModel extends ViewModel {

    private final BridgeRepository bridgeRepository;
    private final DamageRepository damageRepository;
    private DisposableCompletableObserver disposableObserver;
    private MutableLiveData<Integer> onBridgeSave = new MutableLiveData<>();
    private Bridge mBridge;
    private LocalSettings localSettings;

    public static final int SAVE_RESULT_OK = 0;
    public static final int SAVE_RESULT_UNIQUE = 1;
    public static final int SAVE_RESULT_ERROR = 2;

    @Inject
    BridgeDetailViewModel(BridgeRepository bridgeRepository, DamageRepository damageRepository, LocalSettings localSettings) {
        this.bridgeRepository = bridgeRepository;
        this.damageRepository = damageRepository;
        this.localSettings = localSettings;
    }

    public LiveData<Bridge> selectBridge(String bridgeId) {
        return bridgeRepository.selectBridge(bridgeId);
    }

    public LiveData<List<BridgeDamageWithImage>> getBridgeDamages(String roadId, String bridgeId) {
        return damageRepository.getBridgeDamagesWithImage(roadId, bridgeId);
    }

    public MutableLiveData<Integer> onBridgeSave() {
        return onBridgeSave;
    }

    public void insertBridge(Bridge bridge) {
        trim(bridge);
        disposableObserver = new DisposableCompletableObserver() {
            @Override
            public void onComplete() {
                onBridgeSave.postValue(SAVE_RESULT_OK);
            }

            @Override
            public void onError(Throwable e) {
                if (e.getMessage().startsWith("UNIQUE"))
                    onBridgeSave.postValue(SAVE_RESULT_UNIQUE);
                else
                    onBridgeSave.postValue(SAVE_RESULT_ERROR);
            }
        };

        Completable.fromAction(() -> {
            if (!bridgeRepository.insertBridge(bridge))
                throw new Exception("error in insert!");
        }).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(disposableObserver);
    }

    private void trim(Bridge bridge) {
        bridge.registrationDate = bridge.registrationDate.trim();
        if (bridge.bridgeCode != null && bridge.bridgeCode.trim().length() == 0)
            bridge.bridgeCode = null;
        if (bridge.bridgeCode != null)
            bridge.bridgeCode = bridge.bridgeCode.trim();
        if (bridge.bridgeName != null)
            bridge.bridgeName = bridge.bridgeName.trim();
        bridge.constructionYear = bridge.constructionYear.trim();
        bridge.gpsX = Double.parseDouble(StringUtils.format("%.7f", bridge.gpsX));
        bridge.gpsY = Double.parseDouble(StringUtils.format("%.7f", bridge.gpsY));
    }

    public void setBridge(Bridge bridge) {
        this.mBridge = bridge;
    }

    public void newBridge(String roadId) {
        mBridge = new Bridge();
        mBridge.isNew = true;
        mBridge.bridgeId = UUID.randomUUID().toString();
        mBridge.roadId = roadId;
        mBridge.registrationDate = Utils.getPersianDateAndTimeForRecord();
        mBridge.operatorName = localSettings.getOperatorName();
        mBridge.operatorCellNumber = localSettings.getOperatorCellNumber();
    }

    public Bridge getBridge() {
        return mBridge;
    }

    public void deleteCurrentBridge() {
        bridgeRepository.removeBridge(mBridge.bridgeId);
    }
}