package udacity.knapp.app.bakingapp;

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

import udacity.knapp.app.bakingapp.model.Recipe;

public class RecipeActivity extends AppCompatActivity {
    private static final String TAG = "RecipeActivity";

    List<Recipe> recipeList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);
        initRecipeList();
    }

    private void initRecipeList(){
        InputStream in = getResources().openRawResource(R.raw.baking);
        InputStreamReader reader = new InputStreamReader(in);
        Gson gson = new GsonBuilder().create();
        Type collectionType = new TypeToken<Collection<Recipe>>(){}.getType();
        recipeList = gson.fromJson(reader, collectionType);
        for (Recipe recipe : recipeList) {
            Log.d(TAG, "initRecipeList: found recipe for " + recipe.getName());
        }

    }
}
