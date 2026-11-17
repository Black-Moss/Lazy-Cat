package com.blackmoss.lazycat.recipe.mekanism;

import com.blackmoss.lazycat.api.recipe.RecipeContext;
import com.blackmoss.lazycat.api.recipe.mekanism.AbstractMekanismRecipeBuilder;
import mekanism.api.datagen.recipe.builder.NucleosynthesizingRecipeBuilder;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.ItemLike;

import java.util.Objects;

@SuppressWarnings("unused")
public final class NucleosynthesizingBuilder extends AbstractMekanismRecipeBuilder<NucleosynthesizingBuilder> {
    private Object input;
    private Object chemical;
    private int chemicalAmount = 1;
    private int duration = 100;
    private boolean perTick;

    public NucleosynthesizingBuilder(RecipeContext context, ItemLike result) {
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

    public NucleosynthesizingBuilder duration(int ticks) {
        this.duration = ticks;
        return this;
    }

    public NucleosynthesizingBuilder perTick(boolean value) {
        this.perTick = value;
        return this;
    }

    @Override
    protected void validate() {
        if (input == null) {
            throw new IllegalStateException("requires an input");
        }
        if (chemical == null) {
            throw new IllegalStateException("requires a chemical input");
        }
    }

    @Override
    protected void build(RecipeOutput output, ResourceKey<Recipe<?>> key) {
        NucleosynthesizingRecipeBuilder.nucleosynthesizing(
                        itemIngredient(input), chemicalIngredient(chemical, chemicalAmount), result(), duration, perTick)
                .save(output, key.identifier());
    }
}
