package com.blackmoss.lazycat.recipe.mekanism;

import com.blackmoss.lazycat.api.recipe.RecipeContext;
import com.blackmoss.lazycat.api.recipe.mekanism.AbstractMekanismRecipeBuilder;
import mekanism.api.datagen.recipe.builder.PressurizedReactionRecipeBuilder;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;

import java.util.Objects;

@SuppressWarnings("unused")
public final class PressurizedReactionBuilder extends AbstractMekanismRecipeBuilder<PressurizedReactionBuilder> {
    private Object input;
    private Object fluidInput;
    private Object chemicalInput;
    private int chemicalInputAmount = 1;
    private int duration = 10;
    private Object outputChem;

    public PressurizedReactionBuilder(RecipeContext context, Object result) {
        super(context, result);
    }

    @Override
    protected PressurizedReactionBuilder self() {
        return this;
    }

    @Override
    protected String defaultFolder() {
        return "reaction";
    }

    public PressurizedReactionBuilder input(Object source) {
        this.input = Objects.requireNonNull(source, "input");
        return this;
    }

    public PressurizedReactionBuilder fluidInput(Object source) {
        this.fluidInput = Objects.requireNonNull(source, "fluidInput");
        return this;
    }

    public PressurizedReactionBuilder chemicalInput(Object source) {
        return chemicalInput(source, chemicalInputAmount);
    }

    public PressurizedReactionBuilder chemicalInput(Object source, int amount) {
        this.chemicalInput = Objects.requireNonNull(source, "chemicalInput");
        this.chemicalInputAmount = amount;
        return this;
    }

    public PressurizedReactionBuilder duration(int duration) {
        this.duration = duration;
        return this;
    }

    public PressurizedReactionBuilder outputChem(Object source) {
        this.outputChem = Objects.requireNonNull(source, "outputChem");
        return this;
    }

    @Override
    protected void validate() {
        if (input == null) {
            throw new IllegalStateException("requires an item input");
        }
        if (fluidInput == null) {
            throw new IllegalStateException("requires a fluid input");
        }
        if (chemicalInput == null) {
            throw new IllegalStateException("requires a chemical input");
        }
        if (duration <= 0) {
            throw new IllegalArgumentException("duration must be positive");
        }
    }

    @Override
    protected void build(RecipeOutput output, ResourceLocation id) {
        PressurizedReactionRecipeBuilder builder = outputChem == null
                ? PressurizedReactionRecipeBuilder.reaction(
                itemIngredient(input),
                fluidIngredient(fluidInput, 1),
                chemicalIngredient(chemicalInput, chemicalInputAmount),
                duration,
                result())
                : PressurizedReactionRecipeBuilder.reaction(
                itemIngredient(input),
                fluidIngredient(fluidInput, 1),
                chemicalIngredient(chemicalInput, chemicalInputAmount),
                duration,
                result(),
                chemicalStack(outputChem, 1));
        builder.build(output, id);
    }
}