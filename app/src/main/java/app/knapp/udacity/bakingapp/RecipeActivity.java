package app.knapp.udacity.bakingapp;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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
import app.knapp.udacity.bakingapp.model.Step;
import app.knapp.udacity.bakingapp.ui.RecipeAdapter;
import app.knapp.udacity.bakingapp.ui.RecipeFragment;
import app.knapp.udacity.bakingapp.ui.RecipeListAdapter;
import app.knapp.udacity.bakingapp.ui.RecipeListFragment;
import app.knapp.udacity.bakingapp.ui.SharedViewModel;


public class RecipeActivity extends AppCompatActivity implements RecipeListAdapter.OnRecipeSelectedListener, RecipeAdapter.OnStepSelectedListener {
    private static final String TAG = "RecipeActivity";

    private List<Recipe> recipeList;
    public SharedViewModel viewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);
        initViewModel();
        initFragment();
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

    private void initFragment() {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.master_container, new RecipeListFragment(), "recipe_list")
                .addToBackStack(null)
                .commit();

        // change title when fragments are changed. every fragment after main entry list will have recipe name
        getSupportFragmentManager().addOnBackStackChangedListener(
                new FragmentManager.OnBackStackChangedListener() {
                    public void onBackStackChanged() {
                        Log.d(TAG, "onBackStackChanged: ");
                        if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
                            setTitle(viewModel.getSelectedRecipe().getValue().getName());
                        } else {
                            setTitle(getString(R.string.title_list));
                        }
                    }
                });        
    }

    private void showRecipe() {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

        // check here for displayWide
        fragmentTransaction.add(R.id.master_container, new RecipeFragment(), "recipe_list")
                .addToBackStack(null)
                .commit();
    }

    public void onRecipeSelected(Recipe recipe) {
        Log.d(TAG, "onRecipeSelected: recipe Name " + recipe.getName());
        viewModel.selectRecipe(recipe);
        showRecipe();
    }

    public void onStepSelected(Step step) {
        Log.d(TAG, "onStepSelected: step short description " + step.getShortDescription());
    }


}
