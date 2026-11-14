package com.blackmoss.lazycat.api.recipe;

import net.minecraft.data.recipes.RecipeCategory;

public enum CookingType {
    SMELTING("smelting", RecipeCategory.MISC, 0.1f, 200),
    BLASTING("blasting", RecipeCategory.MISC, 0.1f, 100),
    SMOKING("smoking", RecipeCategory.MISC, 0.1f, 100),
    CAMPFIRE("campfire_cooking", RecipeCategory.FOOD, 0.1f, 600);

    private final String folder;
    private final RecipeCategory defaultCategory;
    private final float defaultExperience;
    private final int defaultTime;

    CookingType(String folder, RecipeCategory defaultCategory, float defaultExperience, int defaultTime) {
        this.folder = folder;
        this.defaultCategory = defaultCategory;
        this.defaultExperience = defaultExperience;
        this.defaultTime = defaultTime;
    }

    public String folder() {
        return folder;
    }

    public RecipeCategory defaultCategory() {
        return defaultCategory;
    }

    public float defaultExperience() {
        return defaultExperience;
    }

    public int defaultTime() {
        return defaultTime;
    }
}
