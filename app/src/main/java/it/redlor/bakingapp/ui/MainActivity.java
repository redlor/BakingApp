package it.redlor.bakingapp.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import java.util.List;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;
import it.redlor.bakingapp.R;
import it.redlor.bakingapp.databinding.ActivityMainBinding;
import it.redlor.bakingapp.pojos.Recipe;
import it.redlor.bakingapp.ui.adapters.RecipeRecyclerAdapter;
import it.redlor.bakingapp.ui.callbacks.RecipeClickCallback;
import it.redlor.bakingapp.viewmodels.RecipesViewModel;
import it.redlor.bakingapp.viewmodels.ViewModelFactory;

public class MainActivity extends AppCompatActivity implements RecipeClickCallback, HasSupportFragmentInjector  {

    private static final String CLICKED_RECIPE = "clicked_recipe";

    RecipeRecyclerAdapter recipeRecyclerAdapter;
    private LinearLayoutManager linearLayoutManager;
    RecipesViewModel recipesViewModel;
    ActivityMainBinding activityMainBinding;

    @Inject
    DispatchingAndroidInjector<Fragment> fragmentDispatchingAndroidInjector;

    @Inject
    ViewModelFactory viewModelFactory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        recipesViewModel = ViewModelProviders.of(this, viewModelFactory).get(RecipesViewModel.class);

        if (internetAvailable()) {
            setOnlineUI();

        } else {
            setOfflineUI();
        }

        recipesViewModel.getRecipes().observe(MainActivity.this, recipesList -> {
            System.out.println(recipesList);
            processResponse(recipesList);

                }

        );
    }

    private void processResponse(List<Recipe> recipeList) {
        linearLayoutManager = new LinearLayoutManager(this);
        activityMainBinding.recipeRv.setLayoutManager(linearLayoutManager);
        recipeRecyclerAdapter = new RecipeRecyclerAdapter(recipeList, recipesViewModel, this);
        activityMainBinding.recipeRv.setAdapter(recipeRecyclerAdapter);
    }

    private void setOnlineUI() {
        activityMainBinding.recipeRv.setVisibility(View.VISIBLE);
        activityMainBinding.noInternetImage.setVisibility(View.GONE);
        activityMainBinding.noInternetText.setVisibility(View.GONE);
    }

    private void setOfflineUI() {
        activityMainBinding.recipeRv.setVisibility(View.GONE);
        activityMainBinding.noInternetImage.setVisibility(View.VISIBLE);
        activityMainBinding.noInternetText.setVisibility(View.VISIBLE);
    }

    public boolean internetAvailable() {
        // Get a reference to the ConnectivityManager to check state of network connectivity
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        // Get details on the currently active default data network
        NetworkInfo networkInfo = null;
        if (connectivityManager != null) {
            networkInfo = connectivityManager.getActiveNetworkInfo();
        }
        return networkInfo != null && networkInfo.isConnectedOrConnecting();
    }

    @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {
        return fragmentDispatchingAndroidInjector;
    }

    @Override
    public void onClick(Recipe recipe) {
        Intent intent = new Intent(this, DetailsActivity.class);
        intent.putExtra(CLICKED_RECIPE, recipe);
        startActivity(intent);
    }
}
