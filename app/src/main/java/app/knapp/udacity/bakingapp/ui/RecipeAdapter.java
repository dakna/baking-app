package app.knapp.udacity.bakingapp.ui;

import android.content.Context;
import android.support.annotation.NonNull;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Locale;

import app.knapp.udacity.bakingapp.R;
import app.knapp.udacity.bakingapp.model.Ingredient;
import app.knapp.udacity.bakingapp.model.Recipe;
import app.knapp.udacity.bakingapp.model.Step;
import butterknife.BindView;
import butterknife.ButterKnife;


public class RecipeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int INGREDIENT_ITEMS = 1;
    private Context context;
    private Recipe recipe;
    private RecipeAdapter.OnStepSelectedListener onStepSelectedListener;

    public RecipeAdapter(Context context, Recipe recipe, RecipeAdapter.OnStepSelectedListener onStepSelectedListener) {
        this.context = context;
        this.recipe = recipe;
        this.onStepSelectedListener = onStepSelectedListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == 0) { // Ingredients
            return new IngredientsViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recipe_ingredient_list_item, parent, false));
        } else { // Steps
            return new StepViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recipe_step_list_item, parent, false));
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof IngredientsViewHolder) {
            IngredientsViewHolder viewHolder = (IngredientsViewHolder) holder;
            StringBuilder ingredientText = new StringBuilder();
            for (int i = 0; i < recipe.getIngredients().size(); i++) {
                Ingredient ingredient = recipe.getIngredients().get(i);
                //ingredientText.append(String.format(Locale.getDefault(), "• %s (%d %s)", ingredient.getIngredient(), ingredient.getQuantity(), ingredient.getMeasure()));
                ingredientText.append(String.format(Locale.getDefault(), "• %s (%f %s)", ingredient.getIngredient(), ingredient.getQuantity(), ingredient.getMeasure()));
                if (i != recipe.getIngredients().size() - 1)
                    ingredientText.append("\n");
            }
            viewHolder.tvIngredients.setText(ingredientText.toString());

        } else if (holder instanceof StepViewHolder) {
            StepViewHolder viewHolder = (StepViewHolder) holder;
            viewHolder.tvStepOrder.setText(String.valueOf(position - INGREDIENT_ITEMS) + ".");
            viewHolder.tvStepName.setText(recipe.getSteps().get(position - 1).getShortDescription());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onStepSelectedListener != null)
                        onStepSelectedListener.onStepSelected(recipe.getSteps().get(position - INGREDIENT_ITEMS));
                }
            });
        }
    }

    @Override
    public int getItemViewType(int position) {
        // ingredients first
        if (position == 0)
            return 0;
        else
            return 1;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }


    @Override
    public int getItemCount() {
        return INGREDIENT_ITEMS + recipe.getSteps().size();
    }

    public class IngredientsViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_ingredients)
        public TextView tvIngredients;

        public IngredientsViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);

        }

    }

    public class StepViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_step_order)
        public TextView tvStepOrder;

        @BindView(R.id.tv_step_name)
        public TextView tvStepName;

        public StepViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }

    }


    public interface OnStepSelectedListener {
        void onStepSelected(Step step);
    }

}
