package com.blackmoss.lazycat.recipe.mekanism;

import com.blackmoss.lazycat.api.recipe.RecipeContext;
import com.blackmoss.lazycat.api.recipe.mekanism.AbstractMekanismRecipeBuilder;
import mekanism.api.datagen.recipe.builder.RotaryRecipeBuilder;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;

import java.util.Objects;

@SuppressWarnings("unused")
public final class RotaryBuilder extends AbstractMekanismRecipeBuilder<RotaryBuilder> {
    private Object fluidInput;
    private Object chemicalInput;
    private Object chemicalOutput;
    private int chemicalOutputAmount = 1;
    private Object fluidOutput;
    private int fluidOutputAmount = 1;
    private final Mode mode;

    public RotaryBuilder(RecipeContext context, Mode mode) {
        super(context, null);
        this.mode = Objects.requireNonNull(mode, "mode");
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
        return switch (mode) {
            case FLUID_TO_CHEMICAL -> sourcePath(chemicalOutput, "chemicalOutput");
            case CHEMICAL_TO_FLUID -> sourcePath(fluidOutput, "fluidOutput");
        };
    }

    public RotaryBuilder fluidInput(Object source) {
        this.fluidInput = Objects.requireNonNull(source, "fluidInput");
        return this;
    }

    public RotaryBuilder chemicalInput(Object source) {
        this.chemicalInput = Objects.requireNonNull(source, "chemicalInput");
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
        if (mode == Mode.FLUID_TO_CHEMICAL) {
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
        RotaryRecipeBuilder builder = switch (mode) {
            case FLUID_TO_CHEMICAL -> RotaryRecipeBuilder.rotary(
                    fluidIngredient(fluidInput, 1), chemicalStack(chemicalOutput, chemicalOutputAmount));
            case CHEMICAL_TO_FLUID -> RotaryRecipeBuilder.rotary(
                    chemicalIngredient(chemicalInput, 1), fluidStack(fluidOutput, fluidOutputAmount));
        };
        builder.build(output, id);
    }

    public enum Mode {
        FLUID_TO_CHEMICAL,
        CHEMICAL_TO_FLUID
    }
}