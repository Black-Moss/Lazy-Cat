package com.blackmoss.lazycat.recipe.minecraft;

import com.blackmoss.lazycat.api.recipe.AbstractRecipeBuilder;
import com.blackmoss.lazycat.api.recipe.RecipeContext;
import com.blackmoss.lazycat.api.recipe.RecipeIngredient;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.ItemLike;

import java.util.ArrayList;
import java.util.List;

public final class ShapelessBuilder extends AbstractRecipeBuilder<ShapelessBuilder> {
    private static final int MAX_INGREDIENTS = 9;

    private final List<RecipeIngredient> recipeIngredients = new ArrayList<>();

    public ShapelessBuilder(RecipeContext context, ItemLike result) {
        super(context, result);
    }

    @Override
    protected ShapelessBuilder self() {
        return this;
    }

    public ShapelessBuilder requires(Object source) {
        return requires(source, 1);
    }

    public ShapelessBuilder requires(Object source, int count) {
        if (count < 1) {
            throw new IllegalArgumentException("count must be positive");
        }
        RecipeIngredient ingredient = RecipeIngredient.from(source);
        for (int i = 0; i < count; i++) {
            recipeIngredients.add(ingredient);
        }
        defaultUnlock(ingredient);
        return this;
    }

    @Override
    protected void validate() {
        if (recipeIngredients.isEmpty()) {
            throw new IllegalStateException("Shapeless recipe %s has no ingredients".formatted(defaultIdPath()));
        }
        if (recipeIngredients.size() > MAX_INGREDIENTS) {
            throw new IllegalStateException(
                    "Shapeless recipe %s has more than %d ingredients".formatted(defaultIdPath(), MAX_INGREDIENTS));
        }
    }

    @Override
    protected void build(RecipeOutput output, ResourceKey<Recipe<?>> key) {
        ShapelessRecipeBuilder builder = ShapelessRecipeBuilder.shapeless(
                context().items(), category(), result());
        for (RecipeIngredient ingredient : recipeIngredients) {
            builder.requires(ingredient.resolve(context().items()));
        }
        builder.group(group());
        applyCriteria(builder::unlockedBy);
        builder.save(output, key);
    }
}
