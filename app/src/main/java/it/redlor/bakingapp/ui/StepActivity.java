package it.redlor.bakingapp.ui;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import java.util.ArrayList;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;
import it.redlor.bakingapp.R;
import it.redlor.bakingapp.databinding.ActivityStepBinding;
import it.redlor.bakingapp.pojos.Step;

import static it.redlor.bakingapp.utils.Constants.CLICKED_STEP;
import static it.redlor.bakingapp.utils.Constants.STEP_ID;
import static it.redlor.bakingapp.utils.Constants.STEP_LIST;

/**
 * Activity with Step Fragment
 */

public class StepActivity extends AppCompatActivity implements HasSupportFragmentInjector {

    @Inject
    DispatchingAndroidInjector<Fragment> fragmentDispatchingAndroidInjector;

    ActivityStepBinding activityStepBinding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityStepBinding = DataBindingUtil.setContentView(this, R.layout.activity_step);

        if (savedInstanceState == null) {
            Intent intent = getIntent();
            Step step = intent.getParcelableExtra(CLICKED_STEP);
            ArrayList<Step> stepArrayList = intent.getParcelableArrayListExtra(STEP_LIST);
            int stepId = step.getId();
            Bundle bundle = new Bundle();
            bundle.putInt(STEP_ID, stepId);
            bundle.putParcelable(CLICKED_STEP, step);
            bundle.putParcelableArrayList(STEP_LIST, stepArrayList);
            StepFragment stepFragment = StepFragment.newInstance(stepId);
            stepFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.step_container, stepFragment)
                    .commit();

        }
    }

    @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {
        return fragmentDispatchingAndroidInjector;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
