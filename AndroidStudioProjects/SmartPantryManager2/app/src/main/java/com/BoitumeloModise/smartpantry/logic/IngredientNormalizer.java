package com.BoitumeloModise.smartpantry.logic;

import java.util.HashMap;
import java.util.Map;

/** Makes "Tomatoes", " tomato ", "TOMATO" compare equal and converts units to a base unit. */
public class IngredientNormalizer {

    private static final Map<String, String> SYNONYMS = new HashMap<>();
    static {
        SYNONYMS.put("eggs", "egg");
        SYNONYMS.put("tomatoes", "tomato");
        SYNONYMS.put("potatoes", "potato");
        SYNONYMS.put("chillies", "chili");
        SYNONYMS.put("chilli", "chili");
        SYNONYMS.put("vegetable oil", "oil");
        SYNONYMS.put("sunflower oil", "oil");
        SYNONYMS.put("cooking oil", "oil");
        SYNONYMS.put("olive oil", "oil");
        SYNONYMS.put("mealie meal", "maize meal");
        SYNONYMS.put("spaghetti", "pasta");
        SYNONYMS.put("macaroni", "pasta");
    }

    public static String normalizeName(String raw) {
        String s = raw.trim().toLowerCase().replaceAll("\\s+", " ");
        if (SYNONYMS.containsKey(s)) return SYNONYMS.get(s);
        // simple singularisation
        if (s.endsWith("ies") && s.length() > 4) s = s.substring(0, s.length() - 3) + "y";
        else if (s.endsWith("oes")) s = s.substring(0, s.length() - 2);
        else if (s.endsWith("ches") || s.endsWith("shes") || s.endsWith("xes") || s.endsWith("sses"))
            s = s.substring(0, s.length() - 2);
        else if (s.endsWith("s") && !s.endsWith("ss") && !s.equals("oats") && !s.equals("beans"))
            s = s.substring(0, s.length() - 1);
        return s;
    }

    /** Converts to base unit: g for mass, ml for volume, pcs for count. */
    public static double toBase(double qty, String unit) {
        switch (unit.trim().toLowerCase()) {
            case "kg":   return qty * 1000;
            case "l":    return qty * 1000;
            case "tsp":  return qty * 5;
            case "tbsp": return qty * 15;
            case "cup":  return qty * 250;
            default:     return qty;
        }
    }

    /** Unit family, so grams are never compared with pieces. */
    public static String family(String unit) {
        switch (unit.trim().toLowerCase()) {
            case "g": case "kg": return "mass";
            case "ml": case "l": case "tsp": case "tbsp": case "cup": return "volume";
            default: return "count";
        }
    }
}