package app.knapp.udacity.bakingapp.ui;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import java.util.List;

import app.knapp.udacity.bakingapp.model.Recipe;
import app.knapp.udacity.bakingapp.model.Step;

public class SharedViewModel extends ViewModel {
    private static final String TAG = "SharedViewModel";

    private final MutableLiveData<List<Recipe>> recipeList  = new MutableLiveData<>();
    private final MutableLiveData<Recipe> selectedRecipe = new MutableLiveData<>();
    private final MutableLiveData<Integer> selectedStepIndex = new MutableLiveData<>();

    public void init(List<Recipe> recipeList) {
        Log.d(TAG, "init: ");
        this.recipeList.setValue(recipeList);
    }

    public void selectRecipe(Recipe recipe) {
        Log.d(TAG, "selectRecipe: recipe ID " + recipe.getId());
        selectedRecipe.setValue(recipe);
    }
    public LiveData<Recipe> getSelectedRecipe() {
        Log.d(TAG, "getSelectedRecipe: ");
        return selectedRecipe;
    }

    public void selectStepIndex(Integer index) {
        selectedStepIndex.setValue(index);
    }

    public LiveData<Integer> getSelectedStepIndex() {
        Log.d(TAG, "getSelectedStepIndex: ");
        return selectedStepIndex;
    }


    public LiveData<List<Recipe>> getRecipeList() {
        return recipeList;
    }

}
