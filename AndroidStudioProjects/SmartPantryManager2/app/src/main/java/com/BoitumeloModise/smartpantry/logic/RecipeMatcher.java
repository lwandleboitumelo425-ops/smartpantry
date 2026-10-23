package com.BoitumeloModise.smartpantry.logic;

import com.BoitumeloModise.smartpantry.model.PantryItem;
import com.BoitumeloModise.smartpantry.model.Recipe;
import com.BoitumeloModise.smartpantry.model.RecipeIngredient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * STRICT MATCHING RULE: a recipe is suggested only if EVERY required ingredient
 * exists in the pantry in at least the required quantity.
 */
public class RecipeMatcher {

    public static class Result {
        public final List<Recipe> suggested = new ArrayList<>();
        public final List<Recipe> almostThere = new ArrayList<>(); // missing exactly 1
    }

    public static Result match(List<PantryItem> pantry, List<Recipe> recipes) {
        // key = normalisedName|unitFamily  ->  total quantity in base unit
        Map<String, Double> stock = new HashMap<>();
        for (PantryItem p : pantry) {
            String key = key(p.getName(), p.getUnit());
            double base = IngredientNormalizer.toBase(p.getQuantity(), p.getUnit());
            Double existing = stock.get(key);
            stock.put(key, existing == null ? base : existing + base);
        }

        Result result = new Result();
        for (Recipe r : recipes) {
            int missing = countMissing(r, stock);
            if (missing == 0) result.suggested.add(r);
            else if (missing == 1) result.almostThere.add(r);
        }
        return result;
    }

    /** Number of required ingredients the pantry cannot cover (name missing OR quantity too low). */
    static int countMissing(Recipe r, Map<String, Double> stock) {
        int missing = 0;
        for (RecipeIngredient ing : r.getIngredients()) {
            Double have = stock.get(key(ing.getName(), ing.getUnit()));
            double need = IngredientNormalizer.toBase(ing.getQuantity(), ing.getUnit());
            if (have == null || have < need) missing++;
        }
        return missing;
    }

    private static String key(String name, String unit) {
        return IngredientNormalizer.normalizeName(name) + "|" + IngredientNormalizer.family(unit);
    }
}