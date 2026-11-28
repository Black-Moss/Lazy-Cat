package com.blackmoss.lazycat.recipe.mekanism;

import com.blackmoss.lazycat.api.recipe.RecipeContext;
import com.blackmoss.lazycat.api.recipe.mekanism.AbstractMekanismRecipeBuilder;
import mekanism.api.datagen.recipe.builder.ElectrolysisRecipeBuilder;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;

import java.util.Objects;

@SuppressWarnings("unused")
public final class ElectrolysisBuilder extends AbstractMekanismRecipeBuilder<ElectrolysisBuilder> {
    private Object fluidInput;
    private int fluidInputAmount = 1;
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
    protected String defaultFolder() {
        return "separator";
    }

    @Override
    protected String defaultIdPath() {
        return sourcePath(fluidInput, "fluidInput");
    }

    public ElectrolysisBuilder fluidInput(Object source) {
        return fluidInput(source, fluidInputAmount);
    }

    public ElectrolysisBuilder fluidInput(Object source, int amount) {
        this.fluidInput = Objects.requireNonNull(source, "fluidInput");
        this.fluidInputAmount = amount;
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
        if (fluidInputAmount < 1) {
            throw new IllegalArgumentException("fluid input amount must be positive");
        }
        if (leftAmount < 1) {
            throw new IllegalArgumentException("left output amount must be positive");
        }
        if (rightAmount < 1) {
            throw new IllegalArgumentException("right output amount must be positive");
        }
    }

    @Override
    protected void build(RecipeOutput output, ResourceLocation id) {
        ElectrolysisRecipeBuilder.separating(
                        fluidIngredient(fluidInput, fluidInputAmount),
                        chemicalStack(leftOutput, leftAmount),
                        chemicalStack(rightOutput, rightAmount))
                .build(output, id);
    }
}