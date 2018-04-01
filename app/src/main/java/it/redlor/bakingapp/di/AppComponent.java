package it.redlor.bakingapp.di;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.support.AndroidSupportInjectionModule;
import it.redlor.bakingapp.BakingApp;

/**
 * Dagger Component for the application
 */

@Singleton
@Component(modules = {AppModule.class,
        AndroidSupportInjectionModule.class,
        ActivityModule.class, FragmentModule.class})
public interface AppComponent {
    void inject(BakingApp bakingApp);

    @Component.Builder
    interface Builder {
        @BindsInstance
        Builder application(BakingApp bakingApp);

        AppComponent build();
    }
}
