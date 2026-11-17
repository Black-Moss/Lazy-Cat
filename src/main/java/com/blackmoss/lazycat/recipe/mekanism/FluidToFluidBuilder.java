package com.blackmoss.lazycat.recipe.mekanism;

import com.blackmoss.lazycat.api.recipe.RecipeContext;
import com.blackmoss.lazycat.api.recipe.mekanism.AbstractMekanismRecipeBuilder;
import mekanism.api.datagen.recipe.builder.FluidToFluidRecipeBuilder;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.crafting.Recipe;

import java.util.Objects;

@SuppressWarnings("unused")
public final class FluidToFluidBuilder extends AbstractMekanismRecipeBuilder<FluidToFluidBuilder> {
    private Object input;
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
    protected String defaultIdPath() {
        return sourcePath(output, "output");
    }

    @Override
    protected String defaultFolder() {
        return "evaporating";
    }

    public FluidToFluidBuilder input(Object source) {
        this.input = Objects.requireNonNull(source, "input");
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
    }

    @Override
    protected void build(RecipeOutput output, ResourceKey<Recipe<?>> key) {
        FluidToFluidRecipeBuilder.evaporating(fluidIngredient(input, 1), fluidTemplate(this.output, outputAmount))
                .save(output, key.identifier());
    }
}
