package app.knapp.udacity.bakingapp.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import app.knapp.udacity.bakingapp.R;
import butterknife.BindView;


public class RecipeFragment extends Fragment {
    private SharedViewModel viewModel;

    @BindView(R.id.rv_recipe_list)
    public RecyclerView rvRecipeList;

    public static RecipeFragment newInstance() {
        return new RecipeFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.recipe_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = ViewModelProviders.of(this).get(SharedViewModel.class);
    }

    private void setupRecyclerView() {

        rvRecipeList.setHasFixedSize(true);

        boolean displayWide = getResources().getBoolean(R.bool.displayWide);
        if (displayWide) {
            rvRecipeList.setLayoutManager(new GridLayoutManager(getActivity().getApplicationContext(), 3));
            // Needs item spacing for gridlayoutmanager
        } else {
            rvRecipeList.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext(), LinearLayoutManager.VERTICAL, false));
            rvRecipeList.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        }

        rvRecipeList.addOnItemTouchListener(new RecyclerView.SimpleOnItemTouchListener());
    }

}
