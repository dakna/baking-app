package app.knapp.udacity.bakingapp;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import app.knapp.udacity.bakingapp.ui.RecipeStepFragment;
import app.knapp.udacity.bakingapp.ui.SharedViewModel;


public class RecipeActivity extends AppCompatActivity implements RecipeListAdapter.OnRecipeSelectedListener, RecipeAdapter.OnStepSelectedListener {
    private static final String TAG = "RecipeActivity";
    public static final String RECIPE = "recipe";
    public static final String STEP = "step";
    public static final String RECIPE_LIST = "recipe_list";

    private List<Recipe> recipeList;
    public SharedViewModel viewModel;
    private boolean displayWide;
    private int stackIdentifier;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        displayWide = getResources().getBoolean(R.bool.displayWide);
        initViewModel();
        if (savedInstanceState == null ) {
            initFragment();
        }
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
        fragmentTransaction.add(R.id.master_container, new RecipeListFragment(), RECIPE_LIST)
                .addToBackStack(null)
                .commit();

        // change title when fragments are changed. every fragment after main entry list will have recipe name
        getSupportFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
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

        if (!displayWide) {
            fragmentTransaction.replace(R.id.master_container, new RecipeFragment(), RECIPE)
                    .addToBackStack(null);
            stackIdentifier = fragmentTransaction.commit();

        } else {
            Fragment fragment = getSupportFragmentManager().findFragmentByTag(RECIPE_LIST);
            fragmentTransaction.remove(fragment);
            fragmentTransaction.replace(R.id.left_container, new RecipeFragment(), RECIPE)
                    .addToBackStack(null);

            fragmentTransaction.replace(R.id.right_container, new RecipeStepFragment(), RECIPE)
                    .addToBackStack(null);

            fragmentTransaction.commit();

        }
    }

    private void showStep() {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

        if (!displayWide) {
            fragmentTransaction.replace(R.id.master_container, new RecipeStepFragment(), STEP)
                    .addToBackStack(null)
                    .commit();
        } else {
            fragmentTransaction.replace(R.id.right_container, new RecipeStepFragment(), RECIPE)
                    .addToBackStack(null)
                    .commit();
        }

    }

    @Override
    public void onBackPressed(){
        FragmentManager fm = getSupportFragmentManager();

        // make sure we get back in tablet mode to the recipe list with one back
        if (fm.getBackStackEntryCount() > 2) {
            Log.i(TAG, "popping backstack");
            fm.popBackStack(stackIdentifier,0);
        } else {
            Log.i("MainActivity", "not more than 3 on backstack");
            super.onBackPressed();
        }
    }

    public void onRecipeSelected(Recipe recipe) {
        Log.d(TAG, "onRecipeSelected: recipe Name " + recipe.getName());
        viewModel.selectRecipe(recipe);
        viewModel.selectStepIndex(0);
        showRecipe();
    }

    public void onStepSelected(Integer index) {
        Log.d(TAG, "onStepSelected: step index " + index);
        viewModel.selectStepIndex(index);
        showStep();
    }


}
