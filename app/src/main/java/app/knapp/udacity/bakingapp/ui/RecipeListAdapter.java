package app.knapp.udacity.bakingapp.ui;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.List;
import com.squareup.picasso.Picasso;
import app.knapp.udacity.bakingapp.R;
import app.knapp.udacity.bakingapp.model.Recipe;
import butterknife.BindView;
import butterknife.ButterKnife;


public class RecipeListAdapter extends RecyclerView.Adapter<RecipeListAdapter.RecipeViewHolder> {
    private Context context;
    private List<Recipe> recipeList;
    private RecipeListAdapter.OnRecipeSelectedListener onRecipeSelectedListener;

    public RecipeListAdapter(Context context, List<Recipe> recipeList, RecipeListAdapter.OnRecipeSelectedListener onRecipeSelectedListener) {
        this.context = context;
        this.recipeList = recipeList;
        this.onRecipeSelectedListener = onRecipeSelectedListener;
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recipe_list_item, parent, false);

        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, final int position) {
        holder.tvRecipeName.setText(recipeList.get(position).getName());
        holder.tvServings.setText(context.getString(R.string.servings, recipeList.get(position).getServings()));
        String recipeImage = recipeList.get(position).getImage();
        if (!recipeImage.isEmpty()) {
           Picasso.get()
                    .load(recipeImage)
                    .placeholder(R.drawable.ic_cake)
                    .into(holder.ivRecipeImage);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onRecipeSelectedListener != null)
                    onRecipeSelectedListener.onRecipeSelected(recipeList.get(position));
            }
        });
    }

    public void setRecipeList(List<Recipe> recipeList) {
        this.recipeList.clear();
        this.recipeList.addAll(recipeList);
    }


    @Override
    public int getItemCount() {
        return recipeList.size();
    }


    public class RecipeViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_recipe_name)
        public TextView tvRecipeName;

        @BindView(R.id.tv_servings)
        public TextView tvServings;

        @BindView(R.id.iv_recipe_image)
        public AppCompatImageView ivRecipeImage;

        public RecipeViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface OnRecipeSelectedListener {
        void onRecipeSelected(Recipe recipe);
    }

}
