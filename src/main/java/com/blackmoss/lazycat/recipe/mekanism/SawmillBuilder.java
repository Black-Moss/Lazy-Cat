package com.blackmoss.lazycat.recipe.mekanism;

import com.blackmoss.lazycat.api.LazyIngredient;
import com.blackmoss.lazycat.api.recipe.RecipeContext;
import com.blackmoss.lazycat.api.recipe.mekanism.AbstractMekanismRecipeBuilder;
import mekanism.api.datagen.recipe.builder.SawmillRecipeBuilder;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;

import java.util.Objects;

@SuppressWarnings("unused")
public final class SawmillBuilder extends AbstractMekanismRecipeBuilder<SawmillBuilder> {
    private Object input;
    private Object secondary;
    private double secondaryChance;

    public SawmillBuilder(RecipeContext context, Object result) {
        super(context, result);
    }

    @Override
    protected SawmillBuilder self() {
        return this;
    }

    @Override
    protected String defaultFolder() {
        return "sawing";
    }

    public SawmillBuilder input(Object source) {
        this.input = Objects.requireNonNull(source, "input");
        return this;
    }

    public SawmillBuilder secondary(Object source, double chance) {
        this.secondary = Objects.requireNonNull(source, "secondary");
        this.secondaryChance = chance;
        return this;
    }

    @Override
    protected void validate() {
        if (input == null) {
            throw new IllegalStateException("requires an item input");
        }
        if (secondaryChance < 0.0 || secondaryChance > 1.0) {
            throw new IllegalArgumentException("secondary chance must be between 0 and 1");
        }
    }

    @Override
    protected void build(RecipeOutput output, ResourceLocation id) {
        SawmillRecipeBuilder builder = secondary == null
                ? SawmillRecipeBuilder.sawing(itemIngredient(input), result())
                : SawmillRecipeBuilder.sawing(itemIngredient(input), result(), LazyIngredient.from(secondary).stack(context().items()), secondaryChance);
        builder.build(output, id);
    }
}