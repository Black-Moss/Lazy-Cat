package com.blackmoss.lazycat.recipe.mekanism;

import com.blackmoss.lazycat.api.recipe.RecipeContext;
import com.blackmoss.lazycat.api.recipe.mekanism.AbstractMekanismRecipeBuilder;
import mekanism.api.datagen.recipe.builder.ChemicalToChemicalRecipeBuilder;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.crafting.Recipe;

import java.util.Objects;

public final class ChemicalToChemicalBuilder extends AbstractMekanismRecipeBuilder<ChemicalToChemicalBuilder> {
    private Object input;
    private Object output;
    private int outputAmount = 1;
    private Mode mode = Mode.ACTIVATING;
    public ChemicalToChemicalBuilder(RecipeContext context) {
        super(context, null);
    }

    @Override
    protected ChemicalToChemicalBuilder self() {
        return this;
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

    public ChemicalToChemicalBuilder activating() {
        this.mode = Mode.ACTIVATING;
        return this;
    }

    public ChemicalToChemicalBuilder centrifuging() {
        this.mode = Mode.CENTRIFUGING;
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
    protected void build(RecipeOutput output, ResourceKey<Recipe<?>> key) {
        ChemicalToChemicalRecipeBuilder builder = switch (mode) {
            case ACTIVATING ->
                    ChemicalToChemicalRecipeBuilder.activating(chemicalIngredient(input, 1), chemicalTemplate(output, outputAmount));
            case CENTRIFUGING ->
                    ChemicalToChemicalRecipeBuilder.centrifuging(chemicalIngredient(input, 1), chemicalTemplate(output, outputAmount));
        };
        builder.save(output, key.identifier());
    }

    public enum Mode {
        ACTIVATING,
        CENTRIFUGING
    }
}
