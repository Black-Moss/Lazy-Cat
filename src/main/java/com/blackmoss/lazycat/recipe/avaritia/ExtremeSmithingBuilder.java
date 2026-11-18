package com.blackmoss.lazycat.recipe.avaritia;

import com.blackmoss.lazycat.api.LazyIngredient;
import com.blackmoss.lazycat.api.recipe.AbstractRecipeBuilder;
import com.blackmoss.lazycat.api.recipe.RecipeContext;
import committee.nova.mods.avaritia.common.crafting.recipe.ExtremeSmithingRecipe;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;

@SuppressWarnings("unused")
public final class ExtremeSmithingBuilder extends AbstractRecipeBuilder<ExtremeSmithingBuilder> {
    private Ingredient template;
    private Ingredient base;
    private Ingredient addition;

    public ExtremeSmithingBuilder(RecipeContext context, Object result) {
        super(context, result);
    }

    @Override
    protected ExtremeSmithingBuilder self() {
        return this;
    }

    public ExtremeSmithingBuilder template(Object source) {
        template = LazyIngredient.from(source).resolve(context().items());
        return this;
    }

    public ExtremeSmithingBuilder base(Object source) {
        base = LazyIngredient.from(source).resolve(context().items());
        return this;
    }

    public ExtremeSmithingBuilder addition(Object source) {
        addition = LazyIngredient.from(source).resolve(context().items());
        return this;
    }

    @Override
    protected void validate() {
        if (base == null) {
            throw new IllegalStateException("Extreme smithing recipe requires a base");
        }
    }

    @Override
    protected void build(RecipeOutput output, ResourceLocation id) {
        ExtremeSmithingRecipe recipe = new ExtremeSmithingRecipe(
                template, base, addition, result());
        output.accept(id, recipe, null);
    }
}