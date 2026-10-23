package com.BoitumeloModise.smartpantry.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.BoitumeloModise.smartpantry.R;
import com.BoitumeloModise.smartpantry.model.Recipe;

import java.util.List;

/** Binds a list of recipes to item_recipe.xml rows. */
public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.VH> {

    public interface Listener { void onRecipeClick(Recipe recipe); }

    private final List<Recipe> recipes;
    private final Listener listener;

    public RecipeAdapter(List<Recipe> recipes, Listener listener) {
        this.recipes = recipes;
        this.listener = listener;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_recipe, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int position) {
        Recipe r = recipes.get(position);
        h.tvName.setText(r.getName());
        h.tvCount.setText(r.getIngredients().size() + " ingredients");
        h.itemView.setOnClickListener(v -> listener.onRecipeClick(r));
    }

    @Override
    public int getItemCount() { return recipes.size(); }

    static class VH extends RecyclerView.ViewHolder {
        TextView tvName, tvCount;
        VH(View v) {
            super(v);
            tvName = v.findViewById(R.id.tvRecipeName);
            tvCount = v.findViewById(R.id.tvIngCount);
        }
    }
}