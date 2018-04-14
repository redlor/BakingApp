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
import it.redlor.bakingapp.databinding.ListItemBinding;
import it.redlor.bakingapp.pojos.Recipe;
import it.redlor.bakingapp.ui.callbacks.RecipeClickCallback;
import it.redlor.bakingapp.viewmodels.RecipesViewModel;

/**
 * RecyclerView Adapter for the list of recipes
 */


public class RecipeRecyclerAdapter extends RecyclerView.Adapter<RecipeRecyclerAdapter.RecipeViewHolder> {

    private RecipesViewModel recipesViewModel;
    private List<Recipe> recipes;
    private RecipeClickCallback recipeClickCallback;

    public RecipeRecyclerAdapter(List<Recipe> list, RecipesViewModel viewModel, RecipeClickCallback callback) {
        recipes = new ArrayList<>();
        this.recipes = list;
        this.recipesViewModel = viewModel;
        this.recipeClickCallback = callback;
    }

    public void setData(List<Recipe> recipeList) {
        this.recipes = recipeList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        return RecipeViewHolder.create(layoutInflater, parent, recipeClickCallback);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {
        holder.bind(recipes.get(position), recipeClickCallback);
    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }


    static class RecipeViewHolder extends RecyclerView.ViewHolder {

        private final ViewDataBinding viewDataBinding;

        public RecipeViewHolder(final ListItemBinding listItemBinding, final RecipeClickCallback callback) {
            super(listItemBinding.getRoot());
            this.viewDataBinding = listItemBinding;
            listItemBinding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    callback.onClick(listItemBinding.getRecipeViewModel());
                }
            });
        }

        public static RecipeViewHolder create(LayoutInflater inflater, ViewGroup parent, RecipeClickCallback callback) {
            ListItemBinding itemBinding = ListItemBinding.inflate(inflater, parent, false);
            return new RecipeViewHolder(itemBinding, callback);
        }

        public void bind(Object viewModel, RecipeClickCallback callback) {
            viewDataBinding.setVariable(BR.recipeViewModel, viewModel);
            viewDataBinding.setVariable(BR.callback, callback);
            viewDataBinding.executePendingBindings();
        }
    }
}
