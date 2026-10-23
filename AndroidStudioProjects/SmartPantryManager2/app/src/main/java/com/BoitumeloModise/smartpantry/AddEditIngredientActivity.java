package com.BoitumeloModise.smartpantry;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.BoitumeloModise.smartpantry.data.DatabaseHelper;
import com.BoitumeloModise.smartpantry.model.PantryItem;

import java.util.Arrays;

/** Add a new pantry item, or edit an existing one if an ID is passed in the Intent. */
public class AddEditIngredientActivity extends AppCompatActivity {

    public static final String EXTRA_ID = "extra_id";
    private static final String[] UNITS = {"pcs", "g", "kg", "ml", "l", "tsp", "tbsp", "cup"};

    private EditText etName, etQty, etExpiry;
    private Spinner spUnit;
    private DatabaseHelper db;
    private long editId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_ingredient);

        db = new DatabaseHelper(this);
        etName = findViewById(R.id.etName);
        etQty = findViewById(R.id.etQty);
        etExpiry = findViewById(R.id.etExpiry);
        spUnit = findViewById(R.id.spUnit);
        spUnit.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, UNITS));

        editId = getIntent().getLongExtra(EXTRA_ID, -1);
        if (editId != -1) {
            setTitle("Edit Ingredient");
            PantryItem item = db.getPantryItem(editId);
            if (item != null) {
                etName.setText(item.getName());
                etQty.setText(String.valueOf(item.getQuantity()));
                etExpiry.setText(item.getExpiryDate());
                int idx = Arrays.asList(UNITS).indexOf(item.getUnit());
                spUnit.setSelection(Math.max(idx, 0));
            }
        } else {
            setTitle("Add Ingredient");
        }

        findViewById(R.id.btnSave).setOnClickListener(v -> save());
    }

    private void save() {
        String name = etName.getText().toString().trim();
        String qtyStr = etQty.getText().toString().trim();
        String expiry = etExpiry.getText().toString().trim();

        // ---- Input validation ----
        if (name.isEmpty()) { etName.setError("Name is required"); etName.requestFocus(); return; }
        if (qtyStr.isEmpty()) { etQty.setError("Quantity is required"); etQty.requestFocus(); return; }
        double qty;
        try { qty = Double.parseDouble(qtyStr); }
        catch (NumberFormatException e) { etQty.setError("Enter a valid number"); return; }
        if (qty <= 0) { etQty.setError("Quantity must be greater than 0"); return; }
        if (!expiry.isEmpty() && !expiry.matches("\\d{4}-\\d{2}-\\d{2}")) {
            etExpiry.setError("Use format yyyy-MM-dd"); return;
        }

        PantryItem item = new PantryItem(editId, name, qty,
                spUnit.getSelectedItem().toString(), expiry.isEmpty() ? null : expiry);

        if (editId == -1) db.addPantryItem(item);
        else db.updatePantryItem(item);

        Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
        finish(); // back to MainActivity, whose onResume() reloads the list
    }
}