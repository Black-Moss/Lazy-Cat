package com.blackmoss.lazycat.recipe.mekanism;

import com.blackmoss.lazycat.api.recipe.RecipeContext;
import com.blackmoss.lazycat.api.recipe.mekanism.AbstractMekanismRecipeBuilder;
import mekanism.api.datagen.recipe.builder.ChemicalCrystallizerRecipeBuilder;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;

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

    public ChemicalCrystallizerBuilder chemical(Object source) {
        return chemical(source, chemicalAmount);
    }

    public ChemicalCrystallizerBuilder chemical(Object source, int amount) {
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
    protected void build(RecipeOutput output, ResourceLocation id) {
        ChemicalCrystallizerRecipeBuilder.crystallizing(
                chemicalIngredient(chemical, chemicalAmount), result()).build(output, id);
    }
}