package it.redlor.bakingapp.di;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import it.redlor.bakingapp.ui.DetailsActivity;
import it.redlor.bakingapp.ui.MainActivity;
import it.redlor.bakingapp.ui.SplashScreenActivity;
import it.redlor.bakingapp.ui.StepActivity;

/**
 * Module for the activities
 */

@Module
public abstract class ActivityModule {

    @ContributesAndroidInjector
    abstract SplashScreenActivity bindSplashActivity();

    @ContributesAndroidInjector
    abstract MainActivity bindMainActivity();

    @ContributesAndroidInjector
    abstract DetailsActivity bindDetailsActivity();

    @ContributesAndroidInjector
    abstract StepActivity bindStepActivity();
}
