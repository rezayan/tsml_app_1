package ir.topcoders.pol.di.modules;

import android.app.Application;
import android.arch.persistence.room.Room;
import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import ir.topcoders.pol.data.LocalSettings;
import ir.topcoders.pol.data.source.RoadRepository;
import ir.topcoders.pol.data.source.local.BridgeDamageDao;
import ir.topcoders.pol.data.source.local.BridgeDao;
import ir.topcoders.pol.data.source.local.DamageImageDao;
import ir.topcoders.pol.data.source.local.MyDatabase;
import ir.topcoders.pol.data.source.local.RawDao;
import ir.topcoders.pol.data.source.local.RoadDao;
import ir.topcoders.pol.utils.AppConstants;
import ir.topcoders.pol.utils.BackupUtils;
import ir.topcoders.pol.utils.Utils;

@Module
public class AppModule {

    private final Application app;

    public AppModule(Application app) {
        this.app = app;
    }

    @Provides
    @Singleton
    Application provideApplication() {
        return app;
    }

    @Provides
    @Singleton
    Context provideContext() {
        return app.getApplicationContext();
    }

    @Singleton
    @Provides
    MyDatabase provideDatabase(Application application) {
        return Room.databaseBuilder(application, MyDatabase.class, AppConstants.getDatabaseFilePath(application)).build();
    }

    @Singleton
    @Provides
    RoadDao provideRoadDao(MyDatabase myDatabase) {
        return myDatabase.roadDao();
    }

    @Singleton
    @Provides
    BridgeDao provideBridgeDao(MyDatabase myDatabase) {
        return myDatabase.bridgeDao();
    }

    @Singleton
    @Provides
    BridgeDamageDao provideBridgeDamageDao(MyDatabase myDatabase) {
        return myDatabase.bridgeDamageDao();
    }

    @Singleton
    @Provides
    DamageImageDao provideDamageImageDao(MyDatabase myDatabase) {
        return myDatabase.damageImageDao();
    }

    @Singleton
    @Provides
    RawDao provideRawDao(MyDatabase myDatabase) {
        return myDatabase.rawDao();
    }

    @Singleton
    @Provides
    Utils provideUtils() {
        return new Utils(app);
    }

    @Singleton
    @Provides
    LocalSettings provideLocalSettings() {
        return new LocalSettings(app);
    }
}
