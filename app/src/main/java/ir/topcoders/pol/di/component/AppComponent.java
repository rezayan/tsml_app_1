package ir.topcoders.pol.di.component;

import javax.inject.Singleton;

import dagger.Component;
import dagger.android.AndroidInjectionModule;
import ir.topcoders.pol.MyApp;
import ir.topcoders.pol.di.modules.AppModule;
import ir.topcoders.pol.di.modules.BuildersModule;
import ir.topcoders.pol.di.modules.ViewModelModule;

@Singleton
@Component(modules = {AndroidInjectionModule.class, BuildersModule.class, AppModule.class, ViewModelModule.class})
public interface AppComponent {
    void inject(MyApp app);
}
