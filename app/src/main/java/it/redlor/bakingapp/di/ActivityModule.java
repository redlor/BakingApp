package it.redlor.bakingapp.di;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import it.redlor.bakingapp.ui.DetailsActivity;
import it.redlor.bakingapp.ui.MainActivity;

/**
 * Module for the activities
 */

@Module
public abstract class ActivityModule {

    @ContributesAndroidInjector
    abstract MainActivity bindMainActivity();

    @ContributesAndroidInjector
    abstract DetailsActivity bindDetailsActivity();
}
