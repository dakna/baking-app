package app.knapp.udacity.bakingapp;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;

import app.knapp.udacity.bakingapp.model.Recipe;
import app.knapp.udacity.bakingapp.ui.RecipeListAdapter;
import app.knapp.udacity.bakingapp.ui.SharedViewModel;


public class RecipeActivity extends AppCompatActivity implements RecipeListAdapter.OnRecipeSelectedListener {
    private static final String TAG = "RecipeActivity";

    private List<Recipe> recipeList;
    public SharedViewModel viewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);
        initViewModel();
    }

    private void initViewModel(){
        InputStream in = getResources().openRawResource(R.raw.baking);
        InputStreamReader reader = new InputStreamReader(in);
        Gson gson = new GsonBuilder().create();
        Type collectionType = new TypeToken<Collection<Recipe>>(){}.getType();
        recipeList = gson.fromJson(reader, collectionType);

        viewModel = ViewModelProviders.of(this).get(SharedViewModel.class);
        viewModel.getRecipeList().observe(this, new Observer<List<Recipe>>() {
            @Override
            public void onChanged(@Nullable List<Recipe> recipeList) {
                Log.d(TAG, "onChanged Activity: size of list " + recipeList.size());
            }
        });

        viewModel.init(recipeList);
        for (Recipe recipe : recipeList) {
            Log.d(TAG, "initRecipeList: found recipe for " + recipe.getName());
        }

    }

    public void onRecipeSelected(Recipe recipe) {
        Log.d(TAG, "onRecipeSelected: recipe Name " + recipe.getName());
        viewModel.selectRecipe(recipe);
    }

}
