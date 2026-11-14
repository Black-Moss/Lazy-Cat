package com.blackmoss.lazycat.api.recipe;

@FunctionalInterface
public interface RecipeContributor {
    void addRecipes(RecipeContext context);
}
