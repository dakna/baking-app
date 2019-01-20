package app.knapp.udacity.bakingapp.ui;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import app.knapp.udacity.bakingapp.R;
import app.knapp.udacity.bakingapp.RecipeActivity;
import app.knapp.udacity.bakingapp.model.Recipe;
import app.knapp.udacity.bakingapp.widget.RecipeWidgetService;
import butterknife.BindView;
import butterknife.ButterKnife;


public class RecipeFragment extends Fragment {

    private static final String TAG = "RecipeFragment";
    private SharedViewModel viewModel;

    @BindView(R.id.rv_recipe)
    public RecyclerView rvRecipe;

    public static RecipeFragment newInstance() {
        return new RecipeFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.recipe_fragment, container, false);
        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        viewModel = ViewModelProviders.of(getActivity()).get(SharedViewModel.class);
        Log.d(TAG, "onActivityCreated: recipe name " + viewModel.getSelectedRecipe().getValue().getName());
        getActivity().setTitle(viewModel.getSelectedRecipe().getValue().getName());
        viewModel.getSelectedRecipe().observe(getActivity(), new Observer<Recipe>() {
            @Override
            public void onChanged(@Nullable Recipe recipe) {
                Log.d(TAG, "onChanged Fragment: size of recipe steps " + recipe.getSteps().size());
                rvRecipe.setAdapter(new RecipeAdapter(getContext(), viewModel.getSelectedRecipe().getValue(), (RecipeActivity) getActivity()));
            }
        });

        rvRecipe.setHasFixedSize(true);

        rvRecipe.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        rvRecipe.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));

        rvRecipe.addOnItemTouchListener(new RecyclerView.SimpleOnItemTouchListener());

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        Log.d(TAG, "onCreateOptionsMenu: ");

        menuInflater.inflate(R.menu.recipe, menu);
        super.onCreateOptionsMenu(menu, menuInflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_add_to_widget) {
            Log.d(TAG, "onOptionsItemSelected: adding recipe to widget");
            RecipeWidgetService.updateWidget(getActivity(), viewModel.getSelectedRecipe().getValue());
            return true;
        } else
            return super.onOptionsItemSelected(item);
    }

}
