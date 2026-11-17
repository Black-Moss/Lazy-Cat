package com.blackmoss.lazycat.recipe.minecraft;

import com.blackmoss.lazycat.api.LazyIngredient;
import com.blackmoss.lazycat.api.recipe.AbstractRecipeBuilder;
import com.blackmoss.lazycat.api.recipe.RecipeContext;
import net.minecraft.data.recipes.CustomCraftingRecipeBuilder;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.crafting.Recipe;

import java.util.Objects;

public final class CustomCraftingBuilder extends AbstractRecipeBuilder<CustomCraftingBuilder> {
    private final CustomCraftingRecipeBuilder.Factory factory;

    public CustomCraftingBuilder(
            RecipeContext context,
            Object result,
            CustomCraftingRecipeBuilder.Factory factory) {
        super(context, result);
        this.factory = Objects.requireNonNull(factory, "factory");
        defaultUnlock(LazyIngredient.from(result));
    }

    @Override
    protected CustomCraftingBuilder self() {
        return this;
    }

    @Override
    protected void build(RecipeOutput output, ResourceKey<Recipe<?>> key) {
        CustomCraftingRecipeBuilder builder = CustomCraftingRecipeBuilder.customCrafting(
                category(), factory);
        builder.group(group());
        applyCriteria(builder::unlockedBy);
        builder.save(output, key);
    }
}
