package com.BoitumeloModise.smartpantry.data;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

/** Inserts the built-in recipe collection the first time the database is created. */
public class RecipeSeeder {

    private static long addRecipe(SQLiteDatabase db, String name, String steps) {
        ContentValues cv = new ContentValues();
        cv.put("name", name);
        cv.put("steps", steps);
        return db.insert(DatabaseHelper.T_RECIPES, null, cv);
    }

    private static void addIng(SQLiteDatabase db, long recipeId, String name, double qty, String unit) {
        ContentValues cv = new ContentValues();
        cv.put("recipe_id", recipeId);
        cv.put("name", name);
        cv.put("quantity", qty);
        cv.put("unit", unit);
        db.insert(DatabaseHelper.T_RECIPE_ING, null, cv);
    }

    public static void seed(SQLiteDatabase db) {
        long r;

        r = addRecipe(db, "Scrambled Eggs", "1. Whisk eggs with salt.\n2. Melt butter in a pan.\n3. Cook on low heat, stirring, until just set.");
        addIng(db, r, "egg", 2, "pcs"); addIng(db, r, "butter", 10, "g"); addIng(db, r, "salt", 1, "g");

        r = addRecipe(db, "Boiled Eggs", "1. Place eggs in cold water with salt.\n2. Bring to boil.\n3. Simmer 8 minutes, cool in cold water.");
        addIng(db, r, "egg", 2, "pcs"); addIng(db, r, "salt", 1, "g");

        r = addRecipe(db, "Cheese Omelette", "1. Beat eggs with salt.\n2. Heat oil, pour in eggs.\n3. Add cheese, fold and serve.");
        addIng(db, r, "egg", 3, "pcs"); addIng(db, r, "cheese", 30, "g"); addIng(db, r, "salt", 1, "g"); addIng(db, r, "oil", 10, "ml");

        r = addRecipe(db, "Vegetable Omelette", "1. Fry onion and tomato in oil.\n2. Pour in beaten eggs.\n3. Cook until set.");
        addIng(db, r, "egg", 3, "pcs"); addIng(db, r, "onion", 1, "pcs"); addIng(db, r, "tomato", 1, "pcs"); addIng(db, r, "oil", 10, "ml");

        r = addRecipe(db, "Tomato Pasta", "1. Boil pasta.\n2. Fry garlic in oil, add chopped tomatoes.\n3. Simmer 10 min, toss with pasta.");
        addIng(db, r, "pasta", 200, "g"); addIng(db, r, "tomato", 3, "pcs"); addIng(db, r, "garlic", 2, "pcs"); addIng(db, r, "oil", 15, "ml");

        r = addRecipe(db, "Cheese Toastie", "1. Butter bread.\n2. Add cheese between slices.\n3. Toast in a pan until golden.");
        addIng(db, r, "bread", 2, "pcs"); addIng(db, r, "cheese", 50, "g"); addIng(db, r, "butter", 10, "g");

        r = addRecipe(db, "Grilled Cheese and Tomato", "1. Butter bread.\n2. Layer cheese and sliced tomato.\n3. Grill until cheese melts.");
        addIng(db, r, "bread", 2, "pcs"); addIng(db, r, "cheese", 50, "g"); addIng(db, r, "tomato", 1, "pcs"); addIng(db, r, "butter", 10, "g");

        r = addRecipe(db, "Garlic Bread", "1. Mix soft butter with crushed garlic.\n2. Spread on bread.\n3. Grill 5 minutes.");
        addIng(db, r, "bread", 2, "pcs"); addIng(db, r, "butter", 20, "g"); addIng(db, r, "garlic", 1, "pcs");

        r = addRecipe(db, "French Toast", "1. Beat egg with milk.\n2. Dip bread.\n3. Fry in butter until golden on both sides.");
        addIng(db, r, "bread", 2, "pcs"); addIng(db, r, "egg", 1, "pcs"); addIng(db, r, "milk", 50, "ml"); addIng(db, r, "butter", 10, "g");

        r = addRecipe(db, "Peanut Butter Sandwich", "1. Spread peanut butter on bread.\n2. Close and slice.");
        addIng(db, r, "bread", 2, "pcs"); addIng(db, r, "peanut butter", 30, "g");

        r = addRecipe(db, "Banana Smoothie", "1. Peel banana.\n2. Blend with milk and honey until smooth.");
        addIng(db, r, "banana", 1, "pcs"); addIng(db, r, "milk", 250, "ml"); addIng(db, r, "honey", 10, "ml");

        r = addRecipe(db, "Oats Porridge", "1. Combine oats and milk in a pot.\n2. Simmer 5 min, stirring.\n3. Sweeten with honey.");
        addIng(db, r, "oats", 50, "g"); addIng(db, r, "milk", 250, "ml"); addIng(db, r, "honey", 10, "ml");

        r = addRecipe(db, "Pancakes", "1. Whisk flour, milk, egg and sugar.\n2. Fry ladles of batter in a hot pan.\n3. Flip when bubbles form.");
        addIng(db, r, "flour", 150, "g"); addIng(db, r, "milk", 250, "ml"); addIng(db, r, "egg", 1, "pcs"); addIng(db, r, "sugar", 20, "g");

        r = addRecipe(db, "Egg Fried Rice", "1. Heat oil, scramble egg.\n2. Add cold rice and soy sauce.\n3. Stir-fry 5 minutes.");
        addIng(db, r, "rice", 200, "g"); addIng(db, r, "egg", 1, "pcs"); addIng(db, r, "soy sauce", 15, "ml"); addIng(db, r, "oil", 15, "ml");

        r = addRecipe(db, "Rice and Beans", "1. Fry onion in oil.\n2. Add cooked rice, beans and salt.\n3. Heat through.");
        addIng(db, r, "rice", 200, "g"); addIng(db, r, "beans", 200, "g"); addIng(db, r, "onion", 1, "pcs"); addIng(db, r, "salt", 2, "g"); addIng(db, r, "oil", 10, "ml");

        r = addRecipe(db, "Tomato Soup", "1. Fry onion and garlic.\n2. Add chopped tomatoes and salt, simmer 15 min.\n3. Blend until smooth.");
        addIng(db, r, "tomato", 4, "pcs"); addIng(db, r, "onion", 1, "pcs"); addIng(db, r, "garlic", 1, "pcs"); addIng(db, r, "salt", 2, "g"); addIng(db, r, "oil", 10, "ml");

        r = addRecipe(db, "Potato Wedges", "1. Cut potatoes into wedges.\n2. Toss with oil and salt.\n3. Bake at 200°C for 30 min.");
        addIng(db, r, "potato", 3, "pcs"); addIng(db, r, "oil", 20, "ml"); addIng(db, r, "salt", 2, "g");

        r = addRecipe(db, "Mashed Potato", "1. Boil peeled potatoes until soft.\n2. Mash with butter, milk and salt.");
        addIng(db, r, "potato", 4, "pcs"); addIng(db, r, "butter", 20, "g"); addIng(db, r, "milk", 50, "ml"); addIng(db, r, "salt", 2, "g");

        r = addRecipe(db, "Chicken Stir-fry", "1. Slice chicken, fry in oil.\n2. Add onion and soy sauce.\n3. Stir-fry until cooked through.");
        addIng(db, r, "chicken", 300, "g"); addIng(db, r, "onion", 1, "pcs"); addIng(db, r, "soy sauce", 20, "ml"); addIng(db, r, "oil", 15, "ml");

        r = addRecipe(db, "Pap", "1. Boil water with salt.\n2. Stir in maize meal.\n3. Cover and simmer 20 min, stirring occasionally.");
        addIng(db, r, "maize meal", 250, "g"); addIng(db, r, "salt", 2, "g");
    }
}