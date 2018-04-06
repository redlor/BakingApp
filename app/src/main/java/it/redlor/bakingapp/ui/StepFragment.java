package it.redlor.bakingapp.ui;

import android.content.res.Configuration;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import javax.inject.Inject;

import it.redlor.bakingapp.R;
import it.redlor.bakingapp.databinding.FragmentStepBinding;
import it.redlor.bakingapp.pojos.Step;
import it.redlor.bakingapp.ui.adapters.StepPagerAdapter;
import it.redlor.bakingapp.viewmodels.ViewModelFactory;

import static it.redlor.bakingapp.utils.Constants.STEP_ID;
import static it.redlor.bakingapp.utils.Constants.STEP_LIST;


public class StepFragment extends android.support.v4.app.Fragment {

    FragmentStepBinding fragmentStepBinding;
    int stepId;
    private StepPagerAdapter stepPagerAdapter;
    ArrayList<Step> steps;

    @Inject
    ViewModelFactory viewModelFactory;

    public StepFragment() {}

    public static StepFragment newInstance(int stepId) {
        Bundle arguments = new Bundle();
        arguments.putInt(STEP_ID, stepId);
        StepFragment stepFragment = new StepFragment();
        stepFragment.setArguments(arguments);
        return stepFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentStepBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_step, container, false);
        return fragmentStepBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        int orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            fragmentStepBinding.stepTabLayout.setVisibility(View.GONE);
        }
        Bundle bundle = new Bundle();
        bundle = getArguments();

        if (savedInstanceState != null) {
            stepId = savedInstanceState.getInt(STEP_ID);
            fragmentStepBinding.stepViewPager.setCurrentItem(stepId);
        } else {

            stepId = bundle.getInt(STEP_ID);

        }
        steps = bundle.getParcelableArrayList(STEP_LIST);
            stepPagerAdapter = new StepPagerAdapter(getActivity().getSupportFragmentManager(),
                    new ArrayList<>(0), getContext());
            stepPagerAdapter.setSteps(steps);
            fragmentStepBinding.stepViewPager.setAdapter(stepPagerAdapter);
            fragmentStepBinding.stepViewPager.setCurrentItem(stepId);
            fragmentStepBinding.stepViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    stepId = position;
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });

            fragmentStepBinding.stepTabLayout.setupWithViewPager(fragmentStepBinding.stepViewPager);
        }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(STEP_ID, stepId);
        super.onSaveInstanceState(outState);
    }
}
