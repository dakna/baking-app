package app.knapp.udacity.bakingapp.widget;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import com.google.gson.Gson;

import app.knapp.udacity.bakingapp.model.Recipe;

public class Store {
    private static final String TAG = "Store";
    private static final String SHAREDPREFERENCES_NAME = "recipe_sharedpreferences";
    private static final String RECIPE_KEY = "recipe_key";

    public static void saveRecipe(Context context, Recipe recipe) {
        Log.d(TAG, "saveRecipe: ");
        SharedPreferences.Editor prefs = context.getSharedPreferences(SHAREDPREFERENCES_NAME, Context.MODE_PRIVATE).edit();
        Gson gson = new Gson();
        String jsonRecipe = gson.toJson(recipe);
        prefs.putString(RECIPE_KEY, jsonRecipe);
        prefs.apply();
    }

    public static Recipe loadRecipe(Context context) {
        Log.d(TAG, "loadRecipe: ");
        SharedPreferences prefs = context.getSharedPreferences(SHAREDPREFERENCES_NAME, Context.MODE_PRIVATE);
        String jsonRecipe = prefs.getString(RECIPE_KEY, "");
        Gson gson = new Gson();
        return "".equals(jsonRecipe) ? null :gson.fromJson(prefs.getString(RECIPE_KEY, ""), Recipe.class);
    }

}
