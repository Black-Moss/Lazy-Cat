package com.blackmoss.lazycat.recipe.mekanism;

import com.blackmoss.lazycat.api.recipe.RecipeContext;
import com.blackmoss.lazycat.api.recipe.mekanism.AbstractMekanismRecipeBuilder;
import com.blackmoss.lazycat.api.recipe.mekanism.RotaryType;
import mekanism.api.datagen.recipe.builder.RotaryRecipeBuilder;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;

import java.util.Objects;

@SuppressWarnings("unused")
public final class RotaryBuilder extends AbstractMekanismRecipeBuilder<RotaryBuilder> {
    private final RotaryType type;
    private Object fluidInput;
    private int fluidInputAmount = 1;
    private Object chemicalInput;
    private int chemicalInputAmount = 1;
    private Object chemicalOutput;
    private int chemicalOutputAmount = 1;
    private Object fluidOutput;
    private int fluidOutputAmount = 1;

    public RotaryBuilder(RecipeContext context, RotaryType type) {
        super(context, null);
        this.type = Objects.requireNonNull(type, "type");
    }

    @Override
    protected RotaryBuilder self() {
        return this;
    }

    @Override
    protected String defaultFolder() {
        return "rotary";
    }

    @Override
    protected String defaultIdPath() {
        return switch (type) {
            case FLUID_TO_CHEMICAL -> sourcePath(chemicalOutput, "chemicalOutput");
            case CHEMICAL_TO_FLUID -> sourcePath(fluidOutput, "fluidOutput");
        };
    }

    public RotaryBuilder fluidInput(Object source) {
        return fluidInput(source, fluidInputAmount);
    }

    public RotaryBuilder fluidInput(Object source, int amount) {
        this.fluidInput = Objects.requireNonNull(source, "fluidInput");
        this.fluidInputAmount = amount;
        return this;
    }

    public RotaryBuilder chemicalInput(Object source) {
        return chemicalInput(source, chemicalInputAmount);
    }

    public RotaryBuilder chemicalInput(Object source, int amount) {
        this.chemicalInput = Objects.requireNonNull(source, "chemicalInput");
        this.chemicalInputAmount = amount;
        return this;
    }

    public RotaryBuilder chemicalOutput(Object source) {
        return chemicalOutput(source, chemicalOutputAmount);
    }

    public RotaryBuilder chemicalOutput(Object source, int amount) {
        this.chemicalOutput = Objects.requireNonNull(source, "chemicalOutput");
        this.chemicalOutputAmount = amount;
        return this;
    }

    public RotaryBuilder fluidOutput(Object source) {
        return fluidOutput(source, fluidOutputAmount);
    }

    public RotaryBuilder fluidOutput(Object source, int amount) {
        this.fluidOutput = Objects.requireNonNull(source, "fluidOutput");
        this.fluidOutputAmount = amount;
        return this;
    }

    @Override
    protected void validate() {
        if (type == null) {
            throw new IllegalStateException("requires a type");
        }
        if (fluidInputAmount < 1) {
            throw new IllegalArgumentException("fluid input amount must be positive");
        }
        if (chemicalInputAmount < 1) {
            throw new IllegalArgumentException("chemical input amount must be positive");
        }
        if (type == RotaryType.FLUID_TO_CHEMICAL) {
            if (fluidInput == null) {
                throw new IllegalStateException("requires a fluid input");
            }
            if (chemicalOutput == null) {
                throw new IllegalStateException("requires a chemical output");
            }
        } else {
            if (chemicalInput == null) {
                throw new IllegalStateException("requires a chemical input");
            }
            if (fluidOutput == null) {
                throw new IllegalStateException("requires a fluid output");
            }
        }
    }

    @Override
    protected void build(RecipeOutput output, ResourceLocation id) {
        RotaryRecipeBuilder builder = switch (type) {
            case FLUID_TO_CHEMICAL -> RotaryRecipeBuilder.rotary(
                    fluidIngredient(fluidInput, fluidInputAmount),
                    chemicalStack(chemicalOutput, chemicalOutputAmount));
            case CHEMICAL_TO_FLUID -> RotaryRecipeBuilder.rotary(
                    chemicalIngredient(chemicalInput, chemicalInputAmount),
                    fluidStack(fluidOutput, fluidOutputAmount));
        };
        builder.build(output, id);
    }
}