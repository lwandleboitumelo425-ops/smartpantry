package com.BoitumeloModise.smartpantry;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

/** User preferences persisted with SharedPreferences. */
public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        setTitle("Settings");

        SharedPreferences prefs = getSharedPreferences("settings", MODE_PRIVATE);
        Switch swExpiry = findViewById(R.id.swExpiry);
        Switch swAlmost = findViewById(R.id.swAlmost);

        swExpiry.setChecked(prefs.getBoolean("expiry_alerts", true));
        swAlmost.setChecked(prefs.getBoolean("show_almost", true));

        swExpiry.setOnCheckedChangeListener((b, checked) ->
                prefs.edit().putBoolean("expiry_alerts", checked).apply());
        swAlmost.setOnCheckedChangeListener((b, checked) ->
                prefs.edit().putBoolean("show_almost", checked).apply());

        BottomNavigationView nav = findViewById(R.id.bottomNav);
        nav.setSelectedItemId(R.id.nav_settings);
        nav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_pantry) {
                finish();
            } else if (id == R.id.nav_recipes) {
                startActivity(new Intent(this, SuggestedRecipesActivity.class));
                finish();
            }
            return true;
        });
    }
}