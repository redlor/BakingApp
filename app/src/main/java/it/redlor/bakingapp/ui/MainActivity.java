package it.redlor.bakingapp.ui;

import android.appwidget.AppWidgetManager;
import android.arch.lifecycle.ViewModelProviders;
import android.content.ComponentName;
import android.content.Intent;
import android.databinding.DataBindingUtil;
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
import it.redlor.bakingapp.ui.widget.BakingAppWidgetProvider;
import it.redlor.bakingapp.utils.ConnectivityUtils;
import it.redlor.bakingapp.viewmodels.RecipesViewModel;
import it.redlor.bakingapp.viewmodels.ViewModelFactory;

import static it.redlor.bakingapp.utils.Constants.CLICKED_RECIPE;

public class MainActivity extends AppCompatActivity implements RecipeClickCallback, HasSupportFragmentInjector  {


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


        if (ConnectivityUtils.internetAvailable(this)) {
            setOnlineUI();
            recipesViewModel.getRecipes().observe(MainActivity.this, recipesList -> {
                        processResponse(recipesList);
                    }
            );

        } else {
            setOfflineUI();
        }


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


    @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {
        return fragmentDispatchingAndroidInjector;
    }

    @Override
    public void onClick(Recipe recipe) {
        Intent intent = new Intent(this, DetailsActivity.class);
        intent.putExtra(CLICKED_RECIPE, recipe);
       Intent widget = new Intent(this, BakingAppWidgetProvider.class);
       widget.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        int[] ids = AppWidgetManager.getInstance(getApplication())
                .getAppWidgetIds(new ComponentName(getApplication(), BakingAppWidgetProvider.class));
        widget.putExtra(CLICKED_RECIPE, recipe);
        widget.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
        sendBroadcast(widget);

        startActivity(intent);
    }
}
