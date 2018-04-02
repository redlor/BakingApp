package it.redlor.bakingapp.di;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import it.redlor.bakingapp.ui.DetailsFragment;

/**
 * Module for the Fragments
 */

@Module
public abstract class FragmentModule {
    @ContributesAndroidInjector
    abstract DetailsFragment bindDetailsFragment();
}
