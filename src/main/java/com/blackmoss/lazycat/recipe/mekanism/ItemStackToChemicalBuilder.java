package com.blackmoss.lazycat.recipe.mekanism;

import com.blackmoss.lazycat.api.recipe.RecipeContext;
import com.blackmoss.lazycat.api.recipe.mekanism.AbstractMekanismRecipeBuilder;
import mekanism.api.datagen.recipe.builder.ItemStackToChemicalRecipeBuilder;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;

import java.util.Objects;

@SuppressWarnings("unused")
public final class ItemStackToChemicalBuilder extends AbstractMekanismRecipeBuilder<ItemStackToChemicalBuilder> {
    private Object input;
    private Object output;
    private int outputAmount = 1;
    private final Mode mode;

    public ItemStackToChemicalBuilder(RecipeContext context, Mode mode) {
        super(context, null);
        this.mode = Objects.requireNonNull(mode, "mode");
    }

    @Override
    protected ItemStackToChemicalBuilder self() {
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

    public ItemStackToChemicalBuilder input(Object source) {
        this.input = Objects.requireNonNull(source, "input");
        return this;
    }

    public ItemStackToChemicalBuilder output(Object source) {
        return output(source, outputAmount);
    }

    public ItemStackToChemicalBuilder output(Object source, int amount) {
        this.output = Objects.requireNonNull(source, "output");
        this.outputAmount = amount;
        return this;
    }

    @Override
    protected void validate() {
        if (input == null) {
            throw new IllegalStateException("requires an item input");
        }
        if (output == null) {
            throw new IllegalStateException("requires a chemical output");
        }
    }

    @Override
    protected void build(RecipeOutput output, ResourceLocation id) {
        ItemStackToChemicalRecipeBuilder builder = switch (mode) {
            case CHEMICAL_CONVERSION -> ItemStackToChemicalRecipeBuilder.chemicalConversion(
                    itemIngredient(input), chemicalStack(output, outputAmount));
            case OXIDIZING -> ItemStackToChemicalRecipeBuilder.oxidizing(
                    itemIngredient(input), chemicalStack(output, outputAmount));
            case PIGMENT_EXTRACTING -> ItemStackToChemicalRecipeBuilder.pigmentExtracting(
                    itemIngredient(input), chemicalStack(output, outputAmount));
        };
        builder.build(output, id);
    }

    public enum Mode {
        CHEMICAL_CONVERSION,
        OXIDIZING,
        PIGMENT_EXTRACTING
    }
}