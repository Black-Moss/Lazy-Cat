package com.blackmoss.lazycat.recipe.mekanism;

import com.blackmoss.lazycat.api.recipe.RecipeContext;
import com.blackmoss.lazycat.api.recipe.mekanism.AbstractMekanismRecipeBuilder;
import mekanism.api.datagen.recipe.builder.ItemStackToEnergyRecipeBuilder;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;

import java.util.Objects;

@SuppressWarnings("unused")
public final class ItemStackToEnergyBuilder extends AbstractMekanismRecipeBuilder<ItemStackToEnergyBuilder> {
    private Object input;
    private long energyOutput;

    public ItemStackToEnergyBuilder(RecipeContext context) {
        super(context, null);
    }

    @Override
    protected ItemStackToEnergyBuilder self() {
        return this;
    }

    @Override
    protected String defaultFolder() {
        return "energy_conversion";
    }

    @Override
    protected String defaultIdPath() {
        return sourcePath(input, "input");
    }

    public ItemStackToEnergyBuilder input(Object source) {
        this.input = Objects.requireNonNull(source, "input");
        return this;
    }

    public ItemStackToEnergyBuilder energyOutput(long energyOutput) {
        this.energyOutput = energyOutput;
        return this;
    }

    @Override
    protected void validate() {
        if (input == null) {
            throw new IllegalStateException("requires an item input");
        }
        if (energyOutput <= 0) {
            throw new IllegalStateException("requires a positive energy output");
        }
    }

    @Override
    protected void build(RecipeOutput output, ResourceLocation id) {
        ItemStackToEnergyRecipeBuilder.energyConversion(itemIngredient(input), energyOutput).build(output, id);
    }
}