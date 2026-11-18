package com.blackmoss.lazycat.recipe.mekanism;

import com.blackmoss.lazycat.api.recipe.RecipeContext;
import com.blackmoss.lazycat.api.recipe.mekanism.AbstractMekanismRecipeBuilder;
import mekanism.api.datagen.recipe.builder.NucleosynthesizingRecipeBuilder;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;

import java.util.Objects;

@SuppressWarnings("unused")
public final class NucleosynthesizingBuilder extends AbstractMekanismRecipeBuilder<NucleosynthesizingBuilder> {
    private Object input;
    private Object chemical;
    private int chemicalAmount = 1;
    private int duration = 10;
    private boolean perTick;

    public NucleosynthesizingBuilder(RecipeContext context, Object result) {
        super(context, result);
    }

    @Override
    protected NucleosynthesizingBuilder self() {
        return this;
    }

    @Override
    protected String defaultFolder() {
        return "nucleosynthesizing";
    }

    public NucleosynthesizingBuilder input(Object source) {
        this.input = Objects.requireNonNull(source, "input");
        return this;
    }

    public NucleosynthesizingBuilder chemical(Object source) {
        return chemical(source, chemicalAmount);
    }

    public NucleosynthesizingBuilder chemical(Object source, int amount) {
        this.chemical = Objects.requireNonNull(source, "chemical");
        this.chemicalAmount = amount;
        return this;
    }

    public NucleosynthesizingBuilder duration(int duration) {
        this.duration = duration;
        return this;
    }

    public NucleosynthesizingBuilder perTick(boolean value) {
        this.perTick = value;
        return this;
    }

    @Override
    protected void validate() {
        if (input == null) {
            throw new IllegalStateException("requires an item input");
        }
        if (chemical == null) {
            throw new IllegalStateException("requires a chemical input");
        }
        if (duration <= 0) {
            throw new IllegalArgumentException("duration must be positive");
        }
    }

    @Override
    protected void build(RecipeOutput output, ResourceLocation id) {
        NucleosynthesizingRecipeBuilder.nucleosynthesizing(
                        itemIngredient(input),
                        chemicalIngredient(chemical, chemicalAmount),
                        result(),
                        duration,
                        perTick)
                .build(output, id);
    }
}