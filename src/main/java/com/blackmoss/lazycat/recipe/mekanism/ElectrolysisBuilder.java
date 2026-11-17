package com.blackmoss.lazycat.recipe.mekanism;

import com.blackmoss.lazycat.api.recipe.RecipeContext;
import com.blackmoss.lazycat.api.recipe.mekanism.AbstractMekanismRecipeBuilder;
import mekanism.api.datagen.recipe.builder.ElectrolysisRecipeBuilder;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.crafting.Recipe;

import java.util.Objects;

@SuppressWarnings("unused")
public final class ElectrolysisBuilder extends AbstractMekanismRecipeBuilder<ElectrolysisBuilder> {
    private Object fluidInput;
    private Object leftOutput;
    private int leftAmount = 1;
    private Object rightOutput;
    private int rightAmount = 1;

    public ElectrolysisBuilder(RecipeContext context) {
        super(context, null);
    }

    @Override
    protected ElectrolysisBuilder self() {
        return this;
    }

    @Override
    protected String defaultIdPath() {
        return sourcePath(fluidInput, "fluidInput");
    }

    @Override
    protected String defaultFolder() {
        return "separator";
    }

    public ElectrolysisBuilder fluidInput(Object source) {
        this.fluidInput = Objects.requireNonNull(source, "fluidInput");
        return this;
    }

    public ElectrolysisBuilder leftOutput(Object source) {
        return leftOutput(source, leftAmount);
    }

    public ElectrolysisBuilder leftOutput(Object source, int amount) {
        this.leftOutput = Objects.requireNonNull(source, "leftOutput");
        this.leftAmount = amount;
        return this;
    }

    public ElectrolysisBuilder rightOutput(Object source) {
        return rightOutput(source, rightAmount);
    }

    public ElectrolysisBuilder rightOutput(Object source, int amount) {
        this.rightOutput = Objects.requireNonNull(source, "rightOutput");
        this.rightAmount = amount;
        return this;
    }

    @Override
    protected void validate() {
        if (fluidInput == null) {
            throw new IllegalStateException("requires a fluid input");
        }
        if (leftOutput == null) {
            throw new IllegalStateException("requires a left chemical output");
        }
        if (rightOutput == null) {
            throw new IllegalStateException("requires a right chemical output");
        }
    }

    @Override
    protected void build(RecipeOutput output, ResourceKey<Recipe<?>> key) {
        ElectrolysisRecipeBuilder.separating(
                        fluidIngredient(fluidInput, 1), chemicalTemplate(leftOutput, leftAmount), chemicalTemplate(rightOutput, rightAmount))
                .save(output, key.identifier());
    }
}
