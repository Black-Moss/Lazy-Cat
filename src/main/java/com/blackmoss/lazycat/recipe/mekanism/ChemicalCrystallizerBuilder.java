package com.blackmoss.lazycat.recipe.mekanism;

import com.blackmoss.lazycat.api.recipe.RecipeContext;
import com.blackmoss.lazycat.api.recipe.mekanism.AbstractMekanismRecipeBuilder;
import mekanism.api.datagen.recipe.builder.ChemicalCrystallizerRecipeBuilder;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.crafting.Recipe;

import java.util.Objects;

@SuppressWarnings("unused")
public final class ChemicalCrystallizerBuilder extends AbstractMekanismRecipeBuilder<ChemicalCrystallizerBuilder> {
    private Object chemical;
    private int chemicalAmount = 1;

    public ChemicalCrystallizerBuilder(RecipeContext context, Object result) {
        super(context, result);
    }

    @Override
    protected ChemicalCrystallizerBuilder self() {
        return this;
    }

    @Override
    protected String defaultFolder() {
        return "crystallizing";
    }

    public ChemicalCrystallizerBuilder input(Object source) {
        return input(source, chemicalAmount);
    }

    public ChemicalCrystallizerBuilder input(Object source, int amount) {
        this.chemical = Objects.requireNonNull(source, "chemical");
        this.chemicalAmount = amount;
        return this;
    }

    @Override
    protected void validate() {
        if (chemical == null) {
            throw new IllegalStateException("requires a chemical input");
        }
    }

    @Override
    protected void build(RecipeOutput output, ResourceKey<Recipe<?>> key) {
        ChemicalCrystallizerRecipeBuilder.crystallizing(chemicalIngredient(chemical, chemicalAmount), result())
                .save(output, key.identifier());
    }
}
