package com.blackmoss.lazycat.recipe.mekanism;

import com.blackmoss.lazycat.api.recipe.RecipeContext;
import com.blackmoss.lazycat.api.recipe.mekanism.AbstractMekanismRecipeBuilder;
import mekanism.api.datagen.recipe.builder.SawmillRecipeBuilder;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.ItemLike;

import java.util.Objects;

public final class SawmillBuilder extends AbstractMekanismRecipeBuilder<SawmillBuilder> {
    private Object input;
    private Object secondary;
    private double secondaryChance = 1.0;

    public SawmillBuilder(RecipeContext context, ItemLike result) {
        super(context, result);
    }

    @Override
    protected SawmillBuilder self() {
        return this;
    }

    @Override
    protected String defaultFolder() {
        return "sawing";
    }

    public SawmillBuilder input(Object source) {
        this.input = Objects.requireNonNull(source, "input");
        return this;
    }

    public SawmillBuilder secondary(Object source, double chance) {
        this.secondary = Objects.requireNonNull(source, "secondary");
        this.secondaryChance = chance;
        return this;
    }

    @Override
    protected void validate() {
        if (input == null) {
            throw new IllegalStateException("requires an input");
        }
    }

    @Override
    protected void build(RecipeOutput output, ResourceKey<Recipe<?>> key) {
        if (secondary == null) {
            SawmillRecipeBuilder.sawing(itemIngredient(input), result()).save(output, key.identifier());
        } else {
            SawmillRecipeBuilder.sawing(itemIngredient(input), result(), itemTemplate(secondary, 1), secondaryChance)
                    .save(output, key.identifier());
        }
    }
}
