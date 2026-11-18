package com.blackmoss.lazycat.recipe.mekanism;

import com.blackmoss.lazycat.api.recipe.RecipeContext;
import com.blackmoss.lazycat.api.recipe.mekanism.AbstractMekanismRecipeBuilder;
import mekanism.api.datagen.recipe.builder.ChemicalChemicalToChemicalRecipeBuilder;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;

import java.util.Objects;

@SuppressWarnings("unused")
public final class ChemicalInfuserBuilder extends AbstractMekanismRecipeBuilder<ChemicalInfuserBuilder> {
    private Object left;
    private Object right;
    private Object output;
    private int outputAmount = 1;
    private final Mode mode;

    public ChemicalInfuserBuilder(RecipeContext context, Mode mode) {
        super(context, null);
        this.mode = Objects.requireNonNull(mode, "mode");
    }

    @Override
    protected ChemicalInfuserBuilder self() {
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

    public ChemicalInfuserBuilder left(Object source) {
        this.left = Objects.requireNonNull(source, "left");
        return this;
    }

    public ChemicalInfuserBuilder right(Object source) {
        this.right = Objects.requireNonNull(source, "right");
        return this;
    }

    public ChemicalInfuserBuilder output(Object source) {
        return output(source, outputAmount);
    }

    public ChemicalInfuserBuilder output(Object source, int amount) {
        this.output = Objects.requireNonNull(source, "output");
        this.outputAmount = amount;
        return this;
    }

    @Override
    protected void validate() {
        if (left == null) {
            throw new IllegalStateException("requires a left chemical input");
        }
        if (right == null) {
            throw new IllegalStateException("requires a right chemical input");
        }
        if (output == null) {
            throw new IllegalStateException("requires a chemical output");
        }
    }

    @Override
    protected void build(RecipeOutput output, ResourceLocation id) {
        ChemicalChemicalToChemicalRecipeBuilder builder = switch (mode) {
            case CHEMICAL_INFUSING -> ChemicalChemicalToChemicalRecipeBuilder.chemicalInfusing(
                    chemicalIngredient(left, 1), chemicalIngredient(right, 1), chemicalStack(output, outputAmount));
            case PIGMENT_MIXING -> ChemicalChemicalToChemicalRecipeBuilder.pigmentMixing(
                    chemicalIngredient(left, 1), chemicalIngredient(right, 1), chemicalStack(output, outputAmount));
        };
        builder.build(output, id);
    }

    public enum Mode {
        CHEMICAL_INFUSING,
        PIGMENT_MIXING
    }
}