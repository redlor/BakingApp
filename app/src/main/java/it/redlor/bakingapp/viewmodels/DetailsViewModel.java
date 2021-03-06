package it.redlor.bakingapp.viewmodels;

import android.app.Application;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import java.util.ArrayList;

import javax.inject.Inject;

import it.redlor.bakingapp.pojos.Ingredient;
import it.redlor.bakingapp.pojos.Recipe;
import it.redlor.bakingapp.pojos.Step;

/**
 * ViewModel for the Details Activity
 */
public class DetailsViewModel extends ViewModel {

    MutableLiveData<Recipe> recipe;
    ArrayList<Ingredient> ingredientsList;
    ArrayList<Step> stepsList;
    StringBuilder stringBuilder;

    private Application application;

    @Inject
    public DetailsViewModel(Application application) {
        this.application = application;
        this.recipe = new MutableLiveData<>();
        this.ingredientsList = new ArrayList<>();
        this.stepsList = new ArrayList<>();
    }

    public Recipe getRecipe() {
        return recipe.getValue();
    }

    public void setRecipe(Recipe recipe) {
        this.recipe.setValue(recipe);
    }

    public StringBuilder getIngredients() {
        stringBuilder = new StringBuilder();
        try {
            ingredientsList = recipe.getValue().getIngredients();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        for (Ingredient ingredient : ingredientsList) {
            float quantity = ingredient.getQuantity();
            String measure = ingredient.getMeasure();
            String ingredientName = ingredient.getIngredient();

            stringBuilder.append("\n");
            stringBuilder.append("\u2022 ");
            stringBuilder.append(ingredientName);
            stringBuilder.append(" - ");
            stringBuilder.append(quantity);
            stringBuilder.append(" ");
            stringBuilder.append(measure);
        }
        return stringBuilder;
    }

    public ArrayList<Step> getSteps() {
        try {
            stepsList = recipe.getValue().getSteps();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return stepsList;
    }
}
