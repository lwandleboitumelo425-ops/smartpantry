package com.BoitumeloModise.smartpantry.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.BoitumeloModise.smartpantry.model.PantryItem;
import com.BoitumeloModise.smartpantry.model.Recipe;
import com.BoitumeloModise.smartpantry.model.RecipeIngredient;

import java.util.ArrayList;
import java.util.List;

/** SQLite database: pantry items (full CRUD) and recipes (read, seeded on first run). */
public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "smart_pantry.db";
    private static final int DB_VERSION = 1;

    public static final String T_PANTRY = "pantry_items";
    public static final String T_RECIPES = "recipes";
    public static final String T_RECIPE_ING = "recipe_ingredients";

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + T_PANTRY + " (" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT NOT NULL, quantity REAL NOT NULL, " +
                "unit TEXT NOT NULL, expiry_date TEXT)");
        db.execSQL("CREATE TABLE " + T_RECIPES + " (" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT NOT NULL, steps TEXT NOT NULL)");
        db.execSQL("CREATE TABLE " + T_RECIPE_ING + " (" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "recipe_id INTEGER NOT NULL, name TEXT NOT NULL, " +
                "quantity REAL NOT NULL, unit TEXT NOT NULL, " +
                "FOREIGN KEY(recipe_id) REFERENCES " + T_RECIPES + "(_id))");
        RecipeSeeder.seed(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldV, int newV) {
        db.execSQL("DROP TABLE IF EXISTS " + T_RECIPE_ING);
        db.execSQL("DROP TABLE IF EXISTS " + T_RECIPES);
        db.execSQL("DROP TABLE IF EXISTS " + T_PANTRY);
        onCreate(db);
    }

    // ---------------- PANTRY CRUD ----------------

    public long addPantryItem(PantryItem item) {
        return getWritableDatabase().insert(T_PANTRY, null, toValues(item));
    }

    public int updatePantryItem(PantryItem item) {
        return getWritableDatabase().update(T_PANTRY, toValues(item),
                "_id=?", new String[]{String.valueOf(item.getId())});
    }

    public int deletePantryItem(long id) {
        return getWritableDatabase().delete(T_PANTRY, "_id=?",
                new String[]{String.valueOf(id)});
    }

    public PantryItem getPantryItem(long id) {
        Cursor c = getReadableDatabase().query(T_PANTRY, null, "_id=?",
                new String[]{String.valueOf(id)}, null, null, null);
        PantryItem item = null;
        if (c.moveToFirst()) item = fromCursor(c);
        c.close();
        return item;
    }

    public List<PantryItem> getAllPantryItems() {
        List<PantryItem> list = new ArrayList<>();
        Cursor c = getReadableDatabase().query(T_PANTRY, null, null, null,
                null, null, "name ASC");
        while (c.moveToNext()) list.add(fromCursor(c));
        c.close();
        return list;
    }

    private ContentValues toValues(PantryItem item) {
        ContentValues cv = new ContentValues();
        cv.put("name", item.getName());
        cv.put("quantity", item.getQuantity());
        cv.put("unit", item.getUnit());
        cv.put("expiry_date", item.getExpiryDate());
        return cv;
    }

    private PantryItem fromCursor(Cursor c) {
        return new PantryItem(
                c.getLong(c.getColumnIndexOrThrow("_id")),
                c.getString(c.getColumnIndexOrThrow("name")),
                c.getDouble(c.getColumnIndexOrThrow("quantity")),
                c.getString(c.getColumnIndexOrThrow("unit")),
                c.getString(c.getColumnIndexOrThrow("expiry_date")));
    }

    // ---------------- RECIPES (read only) ----------------

    public List<Recipe> getAllRecipes() {
        List<Recipe> list = new ArrayList<>();
        Cursor c = getReadableDatabase().query(T_RECIPES, null, null, null, null, null, "name ASC");
        while (c.moveToNext()) {
            Recipe r = new Recipe(c.getLong(0), c.getString(1), c.getString(2));
            r.setIngredients(getIngredientsFor(r.getId()));
            list.add(r);
        }
        c.close();
        return list;
    }

    public Recipe getRecipe(long id) {
        Cursor c = getReadableDatabase().query(T_RECIPES, null, "_id=?",
                new String[]{String.valueOf(id)}, null, null, null);
        Recipe r = null;
        if (c.moveToFirst()) {
            r = new Recipe(c.getLong(0), c.getString(1), c.getString(2));
            r.setIngredients(getIngredientsFor(id));
        }
        c.close();
        return r;
    }

    private List<RecipeIngredient> getIngredientsFor(long recipeId) {
        List<RecipeIngredient> list = new ArrayList<>();
        Cursor c = getReadableDatabase().query(T_RECIPE_ING, null, "recipe_id=?",
                new String[]{String.valueOf(recipeId)}, null, null, null);
        while (c.moveToNext()) {
            list.add(new RecipeIngredient(
                    c.getString(c.getColumnIndexOrThrow("name")),
                    c.getDouble(c.getColumnIndexOrThrow("quantity")),
                    c.getString(c.getColumnIndexOrThrow("unit"))));
        }
        c.close();
        return list;
    }
}