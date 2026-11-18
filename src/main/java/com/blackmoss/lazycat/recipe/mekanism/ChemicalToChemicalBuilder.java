package com.blackmoss.lazycat.recipe.mekanism;

import com.blackmoss.lazycat.api.recipe.RecipeContext;
import com.blackmoss.lazycat.api.recipe.mekanism.AbstractMekanismRecipeBuilder;
import mekanism.api.datagen.recipe.builder.ChemicalToChemicalRecipeBuilder;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;

import java.util.Objects;

@SuppressWarnings("unused")
public final class ChemicalToChemicalBuilder extends AbstractMekanismRecipeBuilder<ChemicalToChemicalBuilder> {
    private Object input;
    private Object output;
    private int outputAmount = 1;
    private final Mode mode;

    public ChemicalToChemicalBuilder(RecipeContext context, Mode mode) {
        super(context, null);
        this.mode = Objects.requireNonNull(mode, "mode");
    }

    @Override
    protected ChemicalToChemicalBuilder self() {
        return this;
    }

    @Override
    protected String defaultFolder() {
        return folderOf(mode);
    }

    @Override
    protected String defaultIdPath() {
        return sourcePath(output, "output");
    }

    public ChemicalToChemicalBuilder input(Object source) {
        this.input = Objects.requireNonNull(source, "input");
        return this;
    }

    public ChemicalToChemicalBuilder output(Object source) {
        return output(source, outputAmount);
    }

    public ChemicalToChemicalBuilder output(Object source, int amount) {
        this.output = Objects.requireNonNull(source, "output");
        this.outputAmount = amount;
        return this;
    }

    @Override
    protected void validate() {
        if (input == null) {
            throw new IllegalStateException("requires a chemical input");
        }
        if (output == null) {
            throw new IllegalStateException("requires a chemical output");
        }
    }

    @Override
    protected void build(RecipeOutput output, ResourceLocation id) {
        ChemicalToChemicalRecipeBuilder builder = switch (mode) {
            case ACTIVATING -> ChemicalToChemicalRecipeBuilder.activating(
                    chemicalIngredient(input, 1), chemicalStack(output, outputAmount));
            case CENTRIFUGING -> ChemicalToChemicalRecipeBuilder.centrifuging(
                    chemicalIngredient(input, 1), chemicalStack(output, outputAmount));
        };
        builder.build(output, id);
    }

    public enum Mode {
        ACTIVATING,
        CENTRIFUGING
    }
}