package com.BoitumeloModise.smartpantry;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.BoitumeloModise.smartpantry.adapter.PantryAdapter;
import com.BoitumeloModise.smartpantry.data.DatabaseHelper;
import com.BoitumeloModise.smartpantry.model.PantryItem;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

/** Pantry List screen – the app's home. Shows all ingredients from SQLite in a RecyclerView. */
public class MainActivity extends AppCompatActivity implements PantryAdapter.Listener {

    private DatabaseHelper db;
    private PantryAdapter adapter;
    private TextView tvEmpty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("My Pantry");

        db = new DatabaseHelper(this);
        tvEmpty = findViewById(R.id.tvEmpty);

        RecyclerView rv = findViewById(R.id.rvPantry);
        rv.setLayoutManager(new LinearLayoutManager(this));
        adapter = new PantryAdapter(db.getAllPantryItems(), this);
        rv.setAdapter(adapter);

        FloatingActionButton fab = findViewById(R.id.fabAdd);
        fab.setOnClickListener(v -> startActivity(new Intent(this, AddEditIngredientActivity.class)));

        BottomNavigationView nav = findViewById(R.id.bottomNav);
        nav.setSelectedItemId(R.id.nav_pantry);
        nav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_recipes) startActivity(new Intent(this, SuggestedRecipesActivity.class));
            else if (id == R.id.nav_settings) startActivity(new Intent(this, SettingsActivity.class));
            return true;
        });
    }

    /** onResume runs every time we return to this screen, so the list is always fresh. */
    @Override
    protected void onResume() {
        super.onResume();
        refresh();
        ((BottomNavigationView) findViewById(R.id.bottomNav)).setSelectedItemId(R.id.nav_pantry);
    }

    private void refresh() {
        List<PantryItem> items = db.getAllPantryItems();
        SharedPreferences prefs = getSharedPreferences("settings", MODE_PRIVATE);
        adapter.setItems(items, prefs.getBoolean("expiry_alerts", true));
        tvEmpty.setVisibility(items.isEmpty() ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onItemClick(PantryItem item) {
        Intent i = new Intent(this, AddEditIngredientActivity.class);
        i.putExtra(AddEditIngredientActivity.EXTRA_ID, item.getId());
        startActivity(i);
    }

    @Override
    public void onDeleteClick(PantryItem item) {
        new AlertDialog.Builder(this)
                .setTitle("Delete " + item.getName() + "?")
                .setMessage("This will remove it from your pantry.")
                .setPositiveButton("Delete", (d, w) -> { db.deletePantryItem(item.getId()); refresh(); })
                .setNegativeButton("Cancel", null)
                .show();
    }
}