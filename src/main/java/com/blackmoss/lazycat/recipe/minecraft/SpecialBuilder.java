package com.blackmoss.lazycat.recipe.minecraft;

import com.blackmoss.lazycat.api.recipe.AbstractRecipeBuilder;
import com.blackmoss.lazycat.api.recipe.RecipeContext;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.SpecialRecipeBuilder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.crafting.Recipe;

import java.util.Objects;
import java.util.function.Supplier;

public final class SpecialBuilder extends AbstractRecipeBuilder<SpecialBuilder> {
    private final Supplier<Recipe<?>> factory;

    public SpecialBuilder(RecipeContext context, Supplier<Recipe<?>> factory) {
        super(context, null);
        this.factory = Objects.requireNonNull(factory, "factory");
        allowEmptyAdvancement(true);
    }

    @Override
    protected SpecialBuilder self() {
        return this;
    }

    @Override
    protected String defaultIdPath() {
        throw new IllegalStateException("Special recipe requires an explicit id() or save(String)");
    }

    @Override
    protected void build(RecipeOutput output, ResourceKey<Recipe<?>> key) {
        SpecialRecipeBuilder builder = SpecialRecipeBuilder.special(factory);
        applyCriteria(builder::unlockedBy);
        builder.save(output, key);
    }
}
