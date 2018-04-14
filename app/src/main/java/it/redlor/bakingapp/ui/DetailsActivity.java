package it.redlor.bakingapp.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;

import java.util.List;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;
import it.redlor.bakingapp.BR;
import it.redlor.bakingapp.R;
import it.redlor.bakingapp.databinding.ActivityDetailsBinding;
import it.redlor.bakingapp.pojos.Recipe;
import it.redlor.bakingapp.pojos.Step;
import it.redlor.bakingapp.ui.adapters.StepsAdapter;
import it.redlor.bakingapp.ui.callbacks.StepClickCallback;
import it.redlor.bakingapp.utils.SimpleDividerItemDecoration;
import it.redlor.bakingapp.viewmodels.DetailsViewModel;
import it.redlor.bakingapp.viewmodels.ViewModelFactory;

import static it.redlor.bakingapp.utils.Constants.CLICKED_RECIPE;
import static it.redlor.bakingapp.utils.Constants.CLICKED_STEP;
import static it.redlor.bakingapp.utils.Constants.STEP_ID;
import static it.redlor.bakingapp.utils.Constants.STEP_LIST;

public class DetailsActivity extends AppCompatActivity implements StepClickCallback, HasSupportFragmentInjector {

    // Declare a variable to check if in Dual Pane mode
    public static boolean mTwoPane;
    ActivityDetailsBinding activityDetailsBinding;
    DetailsViewModel detailsViewModel;
    @Inject
    ViewModelFactory viewModelFactory;
    @Inject
    DispatchingAndroidInjector<Fragment> fragmentDispatchingAndroidInjector;
    private LinearLayoutManager linearLayoutManager;
    private StepsAdapter stepsAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set the layout
        activityDetailsBinding = DataBindingUtil.setContentView(this, R.layout.activity_details);

        // Check if on a tablet
        if (activityDetailsBinding.stepsLinearLayout != null) {
            mTwoPane = true;
        }

        // Initialize the ViewModel
        detailsViewModel = ViewModelProviders.of(this, viewModelFactory).get(DetailsViewModel.class);

        // Get the clicked Recipe
        Intent intent = getIntent();
        Recipe recipe = intent.getParcelableExtra(CLICKED_RECIPE);

        // Set the clicked recipe in the ViewModel
        detailsViewModel.setRecipe(recipe);
        activityDetailsBinding.setVariable(BR.detailsViewModel, detailsViewModel);
        activityDetailsBinding.ingredientsTv.setText(detailsViewModel.getIngredients());

        // Set Steps RecyclerView
        setSteps(detailsViewModel.getSteps());

        // Set title
        setTitle(recipe.getName());

    }

    // Steps RecyclerView
    private void setSteps(List<Step> list) {
        linearLayoutManager = new LinearLayoutManager(this);
        activityDetailsBinding.stepsRv.setLayoutManager(linearLayoutManager);
        activityDetailsBinding.stepsRv.addItemDecoration(new SimpleDividerItemDecoration(this));
        stepsAdapter = new StepsAdapter(list, this);
        activityDetailsBinding.stepsRv.setAdapter(stepsAdapter);
    }


    @Override
    public void onClick(Step step) {

        // If on tablet add the fragment
        if (mTwoPane) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            StepFragment stepFragment = new StepFragment();
            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList(STEP_LIST, detailsViewModel.getSteps());
            bundle.putParcelable(CLICKED_STEP, step);
            bundle.putInt(STEP_ID, step.getId());
            stepFragment.setArguments(bundle);

            fragmentManager.beginTransaction()
                    .replace(R.id.step_container, stepFragment)
                    .commit();
            // if not on tablet, launch a new activity
        } else {
            Intent intent = new Intent(this, StepActivity.class);
            intent.putExtra(CLICKED_STEP, step);
            intent.putExtra(STEP_LIST, detailsViewModel.getSteps());
            startActivity(intent);
        }
    }

    @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {
        return fragmentDispatchingAndroidInjector;
    }


}
