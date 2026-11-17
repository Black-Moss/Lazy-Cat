package com.blackmoss.lazycat.recipe.mekanism;

import com.blackmoss.lazycat.api.recipe.RecipeContext;
import com.blackmoss.lazycat.api.recipe.mekanism.AbstractMekanismRecipeBuilder;
import mekanism.api.datagen.recipe.builder.ItemStackToChemicalRecipeBuilder;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.crafting.Recipe;

import java.util.Objects;

@SuppressWarnings("unused")
public final class ItemStackToChemicalBuilder extends AbstractMekanismRecipeBuilder<ItemStackToChemicalBuilder> {
    private Object input;
    private Object output;
    private int outputAmount = 1;
    private final Mode mode;

    public ItemStackToChemicalBuilder(RecipeContext context, Mode mode) {
        super(context, null);
        this.mode = mode;
    }

    @Override
    protected ItemStackToChemicalBuilder self() {
        return this;
    }

    @Override
    protected String defaultFolder() {
        return folderOf(mode);
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
    protected void build(RecipeOutput output, ResourceKey<Recipe<?>> key) {
        ItemStackToChemicalRecipeBuilder builder = switch (mode) {
            case CHEMICAL_CONVERSION ->
                    ItemStackToChemicalRecipeBuilder.chemicalConversion(itemIngredient(input), chemicalTemplate(output, outputAmount));
            case OXIDIZING ->
                    ItemStackToChemicalRecipeBuilder.oxidizing(itemIngredient(input), chemicalTemplate(output, outputAmount));
            case PIGMENT_EXTRACTING ->
                    ItemStackToChemicalRecipeBuilder.pigmentExtracting(itemIngredient(input), chemicalTemplate(output, outputAmount));
        };
        builder.save(output, key.identifier());
    }

    public enum Mode {
        CHEMICAL_CONVERSION,
        OXIDIZING,
        PIGMENT_EXTRACTING
    }
}
