package com.blackmoss.lazycat.recipe.mekanism;

import com.blackmoss.lazycat.api.recipe.RecipeContext;
import com.blackmoss.lazycat.api.recipe.mekanism.AbstractMekanismRecipeBuilder;
import mekanism.api.datagen.recipe.builder.FluidToFluidRecipeBuilder;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;

import java.util.Objects;

@SuppressWarnings("unused")
public final class FluidToFluidBuilder extends AbstractMekanismRecipeBuilder<FluidToFluidBuilder> {
    private Object input;
    private int inputAmount = 1;
    private Object output;
    private int outputAmount = 1;

    public FluidToFluidBuilder(RecipeContext context) {
        super(context, null);
    }

    @Override
    protected FluidToFluidBuilder self() {
        return this;
    }

    @Override
    protected String defaultFolder() {
        return "evaporating";
    }

    @Override
    protected String defaultIdPath() {
        return sourcePath(this.output, "output");
    }

    public FluidToFluidBuilder input(Object source) {
        return input(source, inputAmount);
    }

    public FluidToFluidBuilder input(Object source, int amount) {
        this.input = Objects.requireNonNull(source, "input");
        this.inputAmount = amount;
        return this;
    }

    public FluidToFluidBuilder output(Object source) {
        return output(source, outputAmount);
    }

    public FluidToFluidBuilder output(Object source, int amount) {
        this.output = Objects.requireNonNull(source, "output");
        this.outputAmount = amount;
        return this;
    }

    @Override
    protected void validate() {
        if (input == null) {
            throw new IllegalStateException("requires a fluid input");
        }
        if (output == null) {
            throw new IllegalStateException("requires a fluid output");
        }
        if (inputAmount < 1) {
            throw new IllegalArgumentException("fluid input amount must be positive");
        }
        if (outputAmount < 1) {
            throw new IllegalArgumentException("fluid output amount must be positive");
        }
    }

    @Override
    protected void build(RecipeOutput output, ResourceLocation id) {
        FluidToFluidRecipeBuilder.evaporating(
                fluidIngredient(input, inputAmount), fluidStack(this.output, outputAmount)).build(output, id);
    }
}