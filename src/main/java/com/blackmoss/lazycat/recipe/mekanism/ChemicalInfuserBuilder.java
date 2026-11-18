package com.blackmoss.lazycat.recipe.mekanism;

import com.blackmoss.lazycat.api.recipe.RecipeContext;
import com.blackmoss.lazycat.api.recipe.mekanism.AbstractMekanismRecipeBuilder;
import com.blackmoss.lazycat.api.recipe.mekanism.ChemicalInfuserType;
import mekanism.api.datagen.recipe.builder.ChemicalChemicalToChemicalRecipeBuilder;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.crafting.Recipe;

import java.util.Objects;

@SuppressWarnings("unused")
public final class ChemicalInfuserBuilder extends AbstractMekanismRecipeBuilder<ChemicalInfuserBuilder> {
    private final ChemicalInfuserType type;
    private Object left;
    private Object right;
    private Object output;
    private int outputAmount = 1;

    public ChemicalInfuserBuilder(RecipeContext context, ChemicalInfuserType type) {
        super(context, null);
        this.type = Objects.requireNonNull(type, "type");
    }

    @Override
    protected ChemicalInfuserBuilder self() {
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
        if (type == null) {
            throw new IllegalStateException("requires a rotary mode");
        }
    }

    @Override
    protected void build(RecipeOutput output, ResourceKey<Recipe<?>> key) {
        ChemicalChemicalToChemicalRecipeBuilder builder = switch (type) {
            case CHEMICAL_INFUSING -> ChemicalChemicalToChemicalRecipeBuilder.chemicalInfusing(
                    chemicalIngredient(left, 1), chemicalIngredient(right, 1), chemicalTemplate(this.output, outputAmount));
            case PIGMENT_MIXING -> ChemicalChemicalToChemicalRecipeBuilder.pigmentMixing(
                    chemicalIngredient(left, 1), chemicalIngredient(right, 1), chemicalTemplate(this.output, outputAmount));
        };
        builder.save(output, key.identifier());
    }
}
