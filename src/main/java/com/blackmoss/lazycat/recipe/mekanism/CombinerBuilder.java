package com.blackmoss.lazycat.recipe.mekanism;

import com.blackmoss.lazycat.api.recipe.RecipeContext;
import com.blackmoss.lazycat.api.recipe.mekanism.AbstractMekanismRecipeBuilder;
import mekanism.api.datagen.recipe.builder.CombinerRecipeBuilder;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;

import java.util.Objects;

@SuppressWarnings("unused")
public final class CombinerBuilder extends AbstractMekanismRecipeBuilder<CombinerBuilder> {
    private Object mainItem;
    private Object extra;

    public CombinerBuilder(RecipeContext context, Object result) {
        super(context, result);
    }

    @Override
    protected CombinerBuilder self() {
        return this;
    }

    @Override
    protected String defaultFolder() {
        return "combining";
    }

    public CombinerBuilder mainItem(Object source) {
        this.mainItem = Objects.requireNonNull(source, "mainItem");
        return this;
    }

    public CombinerBuilder extra(Object source) {
        this.extra = Objects.requireNonNull(source, "extra");
        return this;
    }

    @Override
    protected void validate() {
        if (mainItem == null) {
            throw new IllegalStateException("requires a main item input");
        }
        if (extra == null) {
            throw new IllegalStateException("requires an extra item input");
        }
    }

    @Override
    protected void build(RecipeOutput output, ResourceLocation id) {
        CombinerRecipeBuilder.combining(itemIngredient(mainItem), itemIngredient(extra), result()).build(output, id);
    }
}