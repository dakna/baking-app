package app.knapp.udacity.bakingapp.ui;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import java.util.List;

import app.knapp.udacity.bakingapp.model.Recipe;

public class SharedViewModel extends ViewModel {
    private static final String TAG = "SharedViewModel";

    private final MutableLiveData<List<Recipe>> recipeList  = new MutableLiveData<>();
    private final MutableLiveData<Recipe> selectedRecipe = new MutableLiveData<>();

    public void selectRecipe(Recipe recipe) {
        Log.d(TAG, "selectRecipe: recipe ID " + recipe.getId());
        selectedRecipe.setValue(recipe);
    }
    public LiveData<Recipe> getSelectedRecipe() {

        return selectedRecipe;
    }

    public LiveData<List<Recipe>> getRecipeList() {
        return recipeList;
    }

    public void init(List<Recipe> recipeList) {
        Log.d(TAG, "init: ");
        this.recipeList.setValue(recipeList);
    }
}
