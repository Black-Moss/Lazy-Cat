package com.blackmoss.lazycat.recipe.mekanism;

import com.blackmoss.lazycat.api.recipe.RecipeContext;
import com.blackmoss.lazycat.api.recipe.mekanism.AbstractMekanismRecipeBuilder;
import com.blackmoss.lazycat.api.recipe.mekanism.ItemStackToChemicalType;
import mekanism.api.datagen.recipe.builder.ItemStackToChemicalRecipeBuilder;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;

import java.util.Objects;

@SuppressWarnings("unused")
public final class ItemStackToChemicalBuilder extends AbstractMekanismRecipeBuilder<ItemStackToChemicalBuilder> {
    private final ItemStackToChemicalType type;
    private Object input;
    private Object output;
    private int outputAmount = 1;

    public ItemStackToChemicalBuilder(RecipeContext context, ItemStackToChemicalType type) {
        super(context, null);
        this.type = Objects.requireNonNull(type, "type");
    }

    @Override
    protected ItemStackToChemicalBuilder self() {
        return this;
    }

    @Override
    protected String defaultFolder() {
        return folderOf(type);
    }

    @Override
    protected String defaultIdPath() {
        return sourcePath(this.output, "output");
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
        if (type == null) {
            throw new IllegalStateException("requires a type");
        }
    }

    @Override
    protected void build(RecipeOutput output, ResourceLocation id) {
        ItemStackToChemicalRecipeBuilder builder = switch (type) {
            case CHEMICAL_CONVERSION -> ItemStackToChemicalRecipeBuilder.chemicalConversion(
                    itemIngredient(input), chemicalStack(this.output, outputAmount));
            case OXIDIZING -> ItemStackToChemicalRecipeBuilder.oxidizing(
                    itemIngredient(input), chemicalStack(this.output, outputAmount));
            case PIGMENT_EXTRACTING -> ItemStackToChemicalRecipeBuilder.pigmentExtracting(
                    itemIngredient(input), chemicalStack(this.output, outputAmount));
        };
        builder.build(output, id);
    }
}