package it.redlor.bakingapp.ui.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RemoteViews;

import java.util.ArrayList;

import it.redlor.bakingapp.R;
import it.redlor.bakingapp.pojos.Ingredient;
import it.redlor.bakingapp.pojos.Recipe;
import it.redlor.bakingapp.ui.DetailsActivity;

import static it.redlor.bakingapp.utils.Constants.CLICKED_RECIPE;

/**
 * Implementation of App Widget functionality.
 */
public class BakingAppWidgetProvider extends AppWidgetProvider {

    public static Recipe recipe;

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.baking_app_widget);

        // populate the widget's TextViews
            if (recipe != null) {
                views.setTextViewText(R.id.widget_title_text_view, recipe.getName());
                views.setTextViewText(R.id.widget_ingredients_text_view, getIngredients(recipe));
            }
        // Set the intent when the widget is clicked
             Intent intent = new Intent(context, DetailsActivity.class);
            intent.putExtra(CLICKED_RECIPE, recipe);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
           views.setOnClickPendingIntent(R.id.widget_layout, pendingIntent);


        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    // Receive the last clicked recipe
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent != null) {
            try {
                Bundle bundle = intent.getExtras();
                recipe = bundle.getParcelable(CLICKED_RECIPE);
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }
        super.onReceive(context, intent);
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    // This method formats the list of ingredients
    public static StringBuilder getIngredients(Recipe recipe) {
        StringBuilder stringBuilder = new StringBuilder();
        ArrayList<Ingredient> ingredientsList = recipe.getIngredients();
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

}

