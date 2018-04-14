package it.redlor.bakingapp.di;

import android.app.Application;

import dagger.Module;
import dagger.Provides;
import dagger.android.AndroidInjectionModule;
import it.redlor.bakingapp.BakingApp;

/**
 * Module to provide the Context
 */

@Module(includes = {AndroidInjectionModule.class, ViewModelModule.class})
public class AppModule {

    @Provides
    Application provideContext(BakingApp application) {
        return application;
    }

}
