package it.redlor.bakingapp.ui.adapters;

import android.databinding.ViewDataBinding;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import it.redlor.bakingapp.BR;
import it.redlor.bakingapp.databinding.StepItemBinding;
import it.redlor.bakingapp.pojos.Step;
import it.redlor.bakingapp.ui.callbacks.StepClickCallback;

public class StepsAdapter extends RecyclerView.Adapter<StepsAdapter.StepViewHolder> {

    private StepClickCallback stepClickCallback;
    private List<Step> stepList;

    public StepsAdapter(List<Step> stepsList, StepClickCallback stepClickCallback) {
        stepList = new ArrayList<>();
        this.stepList = stepsList;
        this.stepClickCallback = stepClickCallback;
    }

    @NonNull
    @Override
    public StepViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        return StepViewHolder.create(layoutInflater, parent, stepClickCallback);
    }

    @Override
    public void onBindViewHolder(@NonNull StepViewHolder holder, int position) {
            holder.bind(stepList.get(position), stepClickCallback);
    }

    @Override
    public int getItemCount() {
        return stepList.size();
    }

    static class StepViewHolder extends RecyclerView.ViewHolder {

        private final ViewDataBinding viewDataBinding;

        public StepViewHolder(final StepItemBinding stepItemBinding, final StepClickCallback stepClickCallback) {
            super(stepItemBinding.getRoot());
            this.viewDataBinding = stepItemBinding;
            stepItemBinding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    stepClickCallback.onClick(stepItemBinding.getStep());
                }
            });
        }

        public static StepViewHolder create(LayoutInflater inflater, ViewGroup parent, StepClickCallback stepClickCallback) {
            StepItemBinding stepItemBinding = StepItemBinding.inflate(inflater, parent, false);
            return new StepViewHolder(stepItemBinding, stepClickCallback);
        }

        void bind(final Step step, final StepClickCallback stepClickCallback) {
            viewDataBinding.setVariable(BR.step, step);
            viewDataBinding.setVariable(BR.stepCallback, stepClickCallback);
            viewDataBinding.executePendingBindings();
        }
    }
}
