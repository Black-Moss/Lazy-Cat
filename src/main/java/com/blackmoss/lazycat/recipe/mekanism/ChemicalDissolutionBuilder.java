package com.blackmoss.lazycat.recipe.mekanism;

import com.blackmoss.lazycat.api.recipe.RecipeContext;
import com.blackmoss.lazycat.api.recipe.mekanism.AbstractMekanismRecipeBuilder;
import mekanism.api.datagen.recipe.builder.ChemicalDissolutionRecipeBuilder;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;

import java.util.Objects;

@SuppressWarnings("unused")
public final class ChemicalDissolutionBuilder extends AbstractMekanismRecipeBuilder<ChemicalDissolutionBuilder> {
    private Object itemInput;
    private Object chemicalInput;
    private int chemicalInputAmount = 1;
    private Object output;
    private int outputAmount = 1;
    private boolean perTick;

    public ChemicalDissolutionBuilder(RecipeContext context) {
        super(context, null);
    }

    @Override
    protected ChemicalDissolutionBuilder self() {
        return this;
    }

    @Override
    protected String defaultFolder() {
        return "processing";
    }

    @Override
    protected String defaultIdPath() {
        return sourcePath(output, "output");
    }

    public ChemicalDissolutionBuilder itemInput(Object source) {
        this.itemInput = Objects.requireNonNull(source, "itemInput");
        return this;
    }

    public ChemicalDissolutionBuilder chemicalInput(Object source) {
        return chemicalInput(source, chemicalInputAmount);
    }

    public ChemicalDissolutionBuilder chemicalInput(Object source, int amount) {
        this.chemicalInput = Objects.requireNonNull(source, "chemicalInput");
        this.chemicalInputAmount = amount;
        return this;
    }

    public ChemicalDissolutionBuilder output(Object source) {
        return output(source, outputAmount);
    }

    public ChemicalDissolutionBuilder output(Object source, int amount) {
        this.output = Objects.requireNonNull(source, "output");
        this.outputAmount = amount;
        return this;
    }

    public ChemicalDissolutionBuilder perTick(boolean value) {
        this.perTick = value;
        return this;
    }

    @Override
    protected void validate() {
        if (itemInput == null) {
            throw new IllegalStateException("requires an item input");
        }
        if (chemicalInput == null) {
            throw new IllegalStateException("requires a chemical input");
        }
        if (output == null) {
            throw new IllegalStateException("requires a chemical output");
        }
    }

    @Override
    protected void build(RecipeOutput output, ResourceLocation id) {
        ChemicalDissolutionRecipeBuilder.dissolution(
                        itemIngredient(itemInput),
                        chemicalIngredient(chemicalInput, chemicalInputAmount),
                        chemicalStack(output, outputAmount),
                        perTick)
                .build(output, id);
    }
}