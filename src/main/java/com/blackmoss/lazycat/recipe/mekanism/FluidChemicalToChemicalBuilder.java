package com.blackmoss.lazycat.recipe.mekanism;

import com.blackmoss.lazycat.api.recipe.RecipeContext;
import com.blackmoss.lazycat.api.recipe.mekanism.AbstractMekanismRecipeBuilder;
import mekanism.api.datagen.recipe.builder.FluidChemicalToChemicalRecipeBuilder;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.crafting.Recipe;

import java.util.Objects;

@SuppressWarnings("unused")
public final class FluidChemicalToChemicalBuilder extends AbstractMekanismRecipeBuilder<FluidChemicalToChemicalBuilder> {
    private Object fluidInput;
    private Object chemicalInput;
    private Object output;
    private int outputAmount = 1;

    public FluidChemicalToChemicalBuilder(RecipeContext context) {
        super(context, null);
    }

    @Override
    protected FluidChemicalToChemicalBuilder self() {
        return this;
    }

    @Override
    protected String defaultFolder() {
        return "processing";
    }

    public FluidChemicalToChemicalBuilder fluidInput(Object source) {
        this.fluidInput = Objects.requireNonNull(source, "fluidInput");
        return this;
    }

    public FluidChemicalToChemicalBuilder chemicalInput(Object source) {
        this.chemicalInput = Objects.requireNonNull(source, "chemicalInput");
        return this;
    }

    public FluidChemicalToChemicalBuilder output(Object source) {
        return output(source, outputAmount);
    }

    public FluidChemicalToChemicalBuilder output(Object source, int amount) {
        this.output = Objects.requireNonNull(source, "output");
        this.outputAmount = amount;
        return this;
    }

    @Override
    protected void validate() {
        if (fluidInput == null) {
            throw new IllegalStateException("requires a fluid input");
        }
        if (chemicalInput == null) {
            throw new IllegalStateException("requires a chemical input");
        }
        if (output == null) {
            throw new IllegalStateException("requires a chemical output");
        }
    }

    @Override
    protected void build(RecipeOutput output, ResourceKey<Recipe<?>> key) {
        FluidChemicalToChemicalRecipeBuilder.washing(
                        fluidIngredient(fluidInput, 1), chemicalIngredient(chemicalInput, 1), chemicalTemplate(output, outputAmount))
                .save(output, key.identifier());
    }
}
