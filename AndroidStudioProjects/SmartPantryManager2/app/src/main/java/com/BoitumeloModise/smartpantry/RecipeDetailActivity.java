package com.BoitumeloModise.smartpantry;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.BoitumeloModise.smartpantry.data.DatabaseHelper;
import com.BoitumeloModise.smartpantry.model.Recipe;
import com.BoitumeloModise.smartpantry.model.RecipeIngredient;

/** Shows full ingredient list and method for the recipe whose ID was passed in the Intent. */
public class RecipeDetailActivity extends AppCompatActivity {

    public static final String EXTRA_RECIPE_ID = "recipe_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        long id = getIntent().getLongExtra(EXTRA_RECIPE_ID, -1);
        Recipe r = new DatabaseHelper(this).getRecipe(id);
        if (r == null) { finish(); return; }

        setTitle(r.getName());
        ((TextView) findViewById(R.id.tvTitle)).setText(r.getName());

        StringBuilder sb = new StringBuilder();
        for (RecipeIngredient i : r.getIngredients()) {
            sb.append("• ").append(i.getQuantity()).append(" ").append(i.getUnit())
                    .append(" ").append(i.getName()).append("\n");
        }
        ((TextView) findViewById(R.id.tvIngredients)).setText(sb.toString().trim());
        ((TextView) findViewById(R.id.tvSteps)).setText(r.getSteps());
    }
}