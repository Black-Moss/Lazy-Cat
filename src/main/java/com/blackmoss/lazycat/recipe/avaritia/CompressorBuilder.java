package com.blackmoss.lazycat.recipe.avaritia;

import com.blackmoss.lazycat.api.LazyIngredient;
import com.blackmoss.lazycat.api.recipe.AbstractRecipeBuilder;
import com.blackmoss.lazycat.api.recipe.RecipeContext;
import committee.nova.mods.avaritia.common.crafting.recipe.CompressorRecipe;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;

@SuppressWarnings("unused")
public final class CompressorBuilder extends AbstractRecipeBuilder<CompressorBuilder> {
    private Ingredient input;
    private int inputCount = 1;
    private int timeCost;

    public CompressorBuilder(RecipeContext context, Object result, Object input, int timeCost) {
        super(context, result);
        this.input = LazyIngredient.from(input).resolve(context().items());
        this.timeCost = timeCost;
    }

    @Override
    protected CompressorBuilder self() {
        return this;
    }

    public CompressorBuilder input(Object source) {
        input = LazyIngredient.from(source).resolve(context().items());
        return this;
    }

    public CompressorBuilder inputCount(int inputCount) {
        if (inputCount < 1) {
            throw new IllegalArgumentException("input count must be positive");
        }
        this.inputCount = inputCount;
        return this;
    }

    public CompressorBuilder timeCost(int timeCost) {
        if (timeCost < 1) {
            throw new IllegalArgumentException("time cost must be positive");
        }
        this.timeCost = timeCost;
        return this;
    }

    @Override
    protected void build(RecipeOutput output, ResourceLocation id) {
        CompressorRecipe recipe = new CompressorRecipe(input, result(), inputCount, timeCost);
        output.accept(id, recipe, null);
    }
}