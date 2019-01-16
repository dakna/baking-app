package app.knapp.udacity.bakingapp.ui;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import java.util.List;

import app.knapp.udacity.bakingapp.model.Recipe;

public class SharedViewModel extends ViewModel {
    private final MutableLiveData<List> recipeList  = new MutableLiveData<>();
    private final MutableLiveData<Recipe> selectedRecipe = new MutableLiveData<>();

    public void selectRecipe(Recipe recipe) {
        selectedRecipe.setValue(recipe);
    }

    public LiveData<Recipe> getSelectedRecipe() {
        return selectedRecipe;
    }


    public void init(List<Recipe> recipeList) {
        this.recipeList.setValue(recipeList);
    }
}
