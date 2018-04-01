package it.redlor.bakingapp.di;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;
import it.redlor.bakingapp.viewmodels.RecipesViewModel;
import it.redlor.bakingapp.viewmodels.ViewModelFactory;
import it.redlor.bakingapp.viewmodels.ViewModelKey;

/**
 * Module that provides ViewModels
 */

@Module(includes = {ApiClientModule.class})
public abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(RecipesViewModel.class)
    abstract ViewModel bindRecipesVM(RecipesViewModel recipesViewModel);

    @Binds
    abstract ViewModelProvider.Factory bindVMFactory(ViewModelFactory viewModelFactory);

}
