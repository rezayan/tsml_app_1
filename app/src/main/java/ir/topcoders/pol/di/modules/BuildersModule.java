package ir.topcoders.pol.di.modules;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import ir.topcoders.pol.view.activities.BackupActivity;
import ir.topcoders.pol.view.activities.BridgeDetailActivity;
import ir.topcoders.pol.view.activities.BridgeListActivity;
import ir.topcoders.pol.view.activities.CameraActivity;
import ir.topcoders.pol.view.activities.DamageDetailActivity;
import ir.topcoders.pol.view.activities.MapSelectActivity;
import ir.topcoders.pol.view.activities.RoadDetailActivity;
import ir.topcoders.pol.view.activities.RoadListActivity;

@Module
public abstract class BuildersModule {
    @ContributesAndroidInjector
    abstract RoadListActivity contributeRoadListActivity();

    @ContributesAndroidInjector
    abstract RoadDetailActivity contributeRoadDetailActivity();

    @ContributesAndroidInjector
    abstract BridgeListActivity contributeBridgeListActivity();

    @ContributesAndroidInjector
    abstract BridgeDetailActivity contributeBridgeDetailActivity();

    @ContributesAndroidInjector
    abstract MapSelectActivity contributeMapSelectActivity();

    @ContributesAndroidInjector
    abstract DamageDetailActivity contributeDamageDetailActivity();

    @ContributesAndroidInjector
    abstract CameraActivity contributeCameraActivity();

    @ContributesAndroidInjector
    abstract BackupActivity contributeBackupActivity();
}
