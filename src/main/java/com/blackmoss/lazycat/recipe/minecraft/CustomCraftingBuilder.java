package com.blackmoss.lazycat.recipe.minecraft;

import com.blackmoss.lazycat.api.LazyIngredient;
import com.blackmoss.lazycat.api.recipe.minecraft.AbstractRecipeBuilder;
import com.blackmoss.lazycat.api.recipe.RecipeContext;
import net.minecraft.data.recipes.CustomCraftingRecipeBuilder;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.ItemLike;

import java.util.Objects;

public final class CustomCraftingBuilder extends AbstractRecipeBuilder<CustomCraftingBuilder> {
    private final CustomCraftingRecipeBuilder.Factory factory;

    public CustomCraftingBuilder(
            RecipeContext context,
            ItemLike result,
            CustomCraftingRecipeBuilder.Factory factory) {
        super(context, result);
        this.factory = Objects.requireNonNull(factory, "factory");
        defaultUnlock(LazyIngredient.of(result));
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
