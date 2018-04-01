package it.redlor.bakingapp.viewmodels;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import java.util.ArrayList;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import it.redlor.bakingapp.pojos.Recipe;
import it.redlor.bakingapp.utils.RecipesApiInterface;

public class RecipesViewModel extends ViewModel {

    private Application application;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private MutableLiveData<ArrayList<Recipe>> recipesList;
    private RecipesApiInterface recipesApiInterface;

    @Inject
    public RecipesViewModel(RecipesApiInterface recipesApiInterface, Application application) {
        this.recipesApiInterface = recipesApiInterface;
        this.application = application;
        recipesList = new MutableLiveData<>();
    }

    public void setRecipesList(MutableLiveData<ArrayList<Recipe>> recipesList) {
        this.recipesList = recipesList;
    }

    public LiveData<ArrayList<Recipe>> getRecipes() {
        loadRecipes();
        return recipesList;
    }

    private void loadRecipes() {
        compositeDisposable.add((Disposable)recipesApiInterface.getRecipes()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Recipe>() {
                    @Override
                    public void accept(Recipe recipe) throws Exception {
                      /*  recipesList.setValue(new ArrayList<Recipe>());
                        recipesList.getValue().add(recipe);*/
                    }
                }));
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.clear();
    }
}
