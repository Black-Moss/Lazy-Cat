package com.blackmoss.lazycat.recipe.mekanism;

import com.blackmoss.lazycat.api.recipe.RecipeContext;
import com.blackmoss.lazycat.api.recipe.mekanism.AbstractMekanismRecipeBuilder;
import mekanism.api.datagen.recipe.builder.ItemStackToEnergyRecipeBuilder;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.crafting.Recipe;

import java.util.Objects;

@SuppressWarnings("unused")
public final class ItemStackToEnergyBuilder extends AbstractMekanismRecipeBuilder<ItemStackToEnergyBuilder> {
    private Object input;
    private int energyOutput;

    public ItemStackToEnergyBuilder(RecipeContext context) {
        super(context, null);
    }

    @Override
    protected ItemStackToEnergyBuilder self() {
        return this;
    }

    @Override
    protected String defaultIdPath() {
        return sourcePath(input, "input");
    }

    @Override
    protected String defaultFolder() {
        return "energy_conversion";
    }

    public ItemStackToEnergyBuilder input(Object source) {
        this.input = Objects.requireNonNull(source, "input");
        return this;
    }

    public ItemStackToEnergyBuilder energyOutput(int value) {
        this.energyOutput = value;
        return this;
    }

    @Override
    protected void validate() {
        if (input == null) {
            throw new IllegalStateException("requires an item input");
        }
    }

    @Override
    protected void build(RecipeOutput output, ResourceKey<Recipe<?>> key) {
        ItemStackToEnergyRecipeBuilder.energyConversion(itemIngredient(input), energyOutput).save(output, key.identifier());
    }
}
