package com.blackmoss.lazycat.recipe.mekanism;

import com.blackmoss.lazycat.api.recipe.RecipeContext;
import com.blackmoss.lazycat.api.recipe.mekanism.AbstractMekanismRecipeBuilder;
import com.blackmoss.lazycat.api.recipe.mekanism.ChemicalToChemicalType;
import mekanism.api.datagen.recipe.builder.ChemicalToChemicalRecipeBuilder;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.crafting.Recipe;

import java.util.Objects;

@SuppressWarnings("unused")
public final class ChemicalToChemicalBuilder extends AbstractMekanismRecipeBuilder<ChemicalToChemicalBuilder> {
    private final ChemicalToChemicalType type;
    private Object input;
    private Object output;
    private int outputAmount = 1;

    public ChemicalToChemicalBuilder(RecipeContext context, ChemicalToChemicalType type) {
        super(context, null);
        this.type = Objects.requireNonNull(type, "type");
    }

    @Override
    protected ChemicalToChemicalBuilder self() {
        return this;
    }

    @Override
    protected String defaultIdPath() {
        return sourcePath(output, "output");
    }

    @Override
    protected String defaultFolder() {
        return folderOf(type);
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
        if (type == null) {
            throw new IllegalStateException("requires a rotary mode");
        }
    }

    @Override
    protected void build(RecipeOutput output, ResourceKey<Recipe<?>> key) {
        ChemicalToChemicalRecipeBuilder builder = switch (type) {
            case ACTIVATING ->
                    ChemicalToChemicalRecipeBuilder.activating(chemicalIngredient(input, 1), chemicalTemplate(this.output, outputAmount));
            case CENTRIFUGING ->
                    ChemicalToChemicalRecipeBuilder.centrifuging(chemicalIngredient(input, 1), chemicalTemplate(this.output, outputAmount));
        };
        builder.save(output, key.identifier());
    }
}
