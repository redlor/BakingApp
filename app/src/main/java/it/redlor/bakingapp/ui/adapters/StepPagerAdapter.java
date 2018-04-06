package it.redlor.bakingapp.ui.adapters;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

import io.reactivex.annotations.NonNull;
import it.redlor.bakingapp.R;
import it.redlor.bakingapp.pojos.Step;
import it.redlor.bakingapp.ui.SingleStepFragment;

public class StepPagerAdapter extends FragmentPagerAdapter {

    private List<Step> steps;
    private final String title;

    public StepPagerAdapter(FragmentManager fragmentManager, List<Step> steps, Context context) {
        super(fragmentManager);
        setSteps(steps);
        title = context.getResources().getString(R.string.step);

    }

    public void setSteps(@NonNull List<Step> steps) {
        this.steps = steps;
        notifyDataSetChanged();
    }

    @Override
    public Fragment getItem(int position) {
        return SingleStepFragment.newInstance(
                steps.get(position).getDescription(),
                steps.get(position).getVideoURL(),
                steps.get(position).getThumbnailURL()
        );
    }

    @Override
    public int getCount() {
        return steps.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return title + " " + position;
    }
}
