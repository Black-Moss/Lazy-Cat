package com.blackmoss.lazycat.recipe.avaritia;

import com.blackmoss.lazycat.api.LazyIngredient;
import com.blackmoss.lazycat.api.recipe.minecraft.AbstractRecipeBuilder;
import com.blackmoss.lazycat.api.recipe.RecipeContext;
import committee.nova.mods.avaritia.common.crafting.recipe.CompressorRecipe;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeUnlockAdvancementBuilder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.ItemLike;

import java.util.Objects;

public final class CompressorBuilder extends AbstractRecipeBuilder<CompressorBuilder> {
    private final int timeCost;
    private Ingredient input;
    private int inputCount = 1000;

    public CompressorBuilder(RecipeContext context, ItemLike result, Object input, int timeCost) {
        super(context, result);
        this.input = LazyIngredient.from(Objects.requireNonNull(input, "input")).resolve(context.items());
        this.timeCost = timeCost;
        defaultUnlock(LazyIngredient.from(input));
    }

    @Override
    protected CompressorBuilder self() {
        return this;
    }

    public CompressorBuilder input(Object source) {
        this.input = LazyIngredient.from(source).resolve(context().items());
        return this;
    }

    public CompressorBuilder inputCount(int inputCount) {
        this.inputCount = inputCount;
        return this;
    }

    @Override
    protected void validate() {
        if (input == null) {
            throw new IllegalStateException("Compressor recipe requires an input");
        }
        if (timeCost <= 0) {
            throw new IllegalStateException("Compressor recipe requires a positive timeCost");
        }
    }

    @Override
    protected void build(RecipeOutput output, ResourceKey<Recipe<?>> key) {
        CompressorRecipe recipe = new CompressorRecipe(input, result(), inputCount, timeCost);
        RecipeUnlockAdvancementBuilder advancement = new RecipeUnlockAdvancementBuilder();
        applyCriteria(advancement::unlockedBy);
        output.accept(key, recipe, advancement.build(output, key, category()));
    }
}
