package com.BoitumeloModise.smartpantry;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.BoitumeloModise.smartpantry.adapter.RecipeAdapter;
import com.BoitumeloModise.smartpantry.data.DatabaseHelper;
import com.BoitumeloModise.smartpantry.logic.RecipeMatcher;
import com.google.android.material.bottomnavigation.BottomNavigationView;

/** Runs the strict-matching rule against the pantry and shows only recipes that fully qualify. */
public class SuggestedRecipesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggested_recipes);
        setTitle("Suggested Recipes");

        DatabaseHelper db = new DatabaseHelper(this);
        RecipeMatcher.Result res = RecipeMatcher.match(db.getAllPantryItems(), db.getAllRecipes());

        RecipeAdapter.Listener open = r -> {
            Intent i = new Intent(this, RecipeDetailActivity.class);
            i.putExtra(RecipeDetailActivity.EXTRA_RECIPE_ID, r.getId());
            startActivity(i);
        };

        RecyclerView rv = findViewById(R.id.rvSuggested);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(new RecipeAdapter(res.suggested, open));
        findViewById(R.id.tvEmpty).setVisibility(res.suggested.isEmpty() ? View.VISIBLE : View.GONE);

        SharedPreferences prefs = getSharedPreferences("settings", MODE_PRIVATE);
        boolean showAlmost = prefs.getBoolean("show_almost", true);

        RecyclerView rvA = findViewById(R.id.rvAlmost);
        rvA.setLayoutManager(new LinearLayoutManager(this));
        rvA.setAdapter(new RecipeAdapter(res.almostThere, open));

        boolean almostVisible = showAlmost && !res.almostThere.isEmpty();
        findViewById(R.id.tvAlmostHeader).setVisibility(almostVisible ? View.VISIBLE : View.GONE);
        rvA.setVisibility(almostVisible ? View.VISIBLE : View.GONE);

        BottomNavigationView nav = findViewById(R.id.bottomNav);
        nav.setSelectedItemId(R.id.nav_recipes);
        nav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_pantry) {
                finish();
            } else if (id == R.id.nav_settings) {
                startActivity(new Intent(this, SettingsActivity.class));
                finish();
            }
            return true;
        });
    }
}