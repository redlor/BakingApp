package it.redlor.bakingapp.di;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import it.redlor.bakingapp.ui.SingleStepFragment;
import it.redlor.bakingapp.ui.StepFragment;

/**
 * Module for the Fragments
 */

@Module
public abstract class FragmentModule {
    @ContributesAndroidInjector
    abstract SingleStepFragment bindSingleStepFragment();

    @ContributesAndroidInjector
    abstract StepFragment bindStepFragment();
}
