package ir.topcoders.pol.di.modules;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

import ir.topcoders.pol.viewmodel.BackupViewModel;
import ir.topcoders.pol.viewmodel.BridgeDetailViewModel;
import ir.topcoders.pol.viewmodel.BridgeListViewModel;
import ir.topcoders.pol.viewmodel.DamageDetailViewModel;
import ir.topcoders.pol.viewmodel.RoadDetailViewModel;
import ir.topcoders.pol.viewmodel.RoadListViewModel;
import ir.topcoders.pol.viewmodel.ViewModelFactory;

@Module
public abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(RoadListViewModel.class)
    @SuppressWarnings("unused")
    abstract ViewModel bindsRoadListViewModel(RoadListViewModel roadListViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(RoadDetailViewModel.class)
    @SuppressWarnings("unused")
    abstract ViewModel bindsRoadDetailViewModel(RoadDetailViewModel roadDetailViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(BridgeListViewModel.class)
    @SuppressWarnings("unused")
    abstract ViewModel bindsBridgeListViewModel(BridgeListViewModel bridgeListViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(BridgeDetailViewModel.class)
    @SuppressWarnings("unused")
    abstract ViewModel bindsBridgeDetailViewModel(BridgeDetailViewModel bridgeDetailViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(DamageDetailViewModel.class)
    @SuppressWarnings("unused")
    abstract ViewModel bindsDamageDetailViewModel(DamageDetailViewModel damageDetailViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(BackupViewModel.class)
    @SuppressWarnings("unused")
    abstract ViewModel bindsBackupViewModel(BackupViewModel backupViewModel);

    @Binds
    @SuppressWarnings("unused")
    abstract ViewModelProvider.Factory bindsViewModelFactory(ViewModelFactory viewModelFactory);
}
