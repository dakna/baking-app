package app.knapp.udacity.bakingapp.widget;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import app.knapp.udacity.bakingapp.R;
import app.knapp.udacity.bakingapp.RecipeActivity;
import app.knapp.udacity.bakingapp.model.Recipe;

public class RecipeWidgetProvider extends AppWidgetProvider {
    private static final String TAG = "RecipeWidgetProvider";

    public static void updateRecipeWidget(Context context, AppWidgetManager appWidgetManager, int recipeWidgetId) {
        Log.d(TAG, "updateRecipeWidget: ");
        Recipe recipe = Store.loadRecipe(context);
        if (recipe != null) {
            Log.d(TAG, "updateRecipeWidget: recipe " + recipe.getName());
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, new Intent(context, RecipeActivity.class), 0);
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.recipe_widget);

            views.setTextViewText(R.id.tv_recipe_widget_name, recipe.getName());
            views.setOnClickPendingIntent(R.id.tv_recipe_widget_name, pendingIntent);

            Intent intent = new Intent(context, RecipeWidgetService.class);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, recipeWidgetId);
            views.setRemoteAdapter(R.id.lv_recipe_widget, intent);

            appWidgetManager.updateAppWidget(recipeWidgetId, views);
            appWidgetManager.notifyAppWidgetViewDataChanged(recipeWidgetId, R.id.lv_recipe_widget);
        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] recipeWidgetIdArray) {
        Log.d(TAG, "onUpdate: ");
        for (int recipeWidgetId : recipeWidgetIdArray) {
            updateRecipeWidget(context, appWidgetManager, recipeWidgetId);
        }
    }

    public static void updateRecipeWidgets(Context context, AppWidgetManager appWidgetManager, int[] recipeWidgetIdArray) {
        Log.d(TAG, "updateRecipeWidgets: ");
        for (int recipeWidgetId : recipeWidgetIdArray) {
            updateRecipeWidget(context, appWidgetManager, recipeWidgetId);
        }
    }
}

