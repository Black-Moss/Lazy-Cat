package com.blackmoss.lazycat.recipe.avaritia;

import com.blackmoss.lazycat.api.LazyIngredient;
import com.blackmoss.lazycat.api.recipe.AbstractRecipeBuilder;
import com.blackmoss.lazycat.api.recipe.RecipeContext;
import committee.nova.mods.avaritia.common.crafting.recipe.ShapelessTableCraftingRecipe;
import net.minecraft.core.NonNullList;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public final class ExtremeCraftingShapelessBuilder extends AbstractRecipeBuilder<ExtremeCraftingShapelessBuilder> {
    private final List<Ingredient> ingredients = new ArrayList<>();
    private int tier;

    public ExtremeCraftingShapelessBuilder(RecipeContext context, Object result) {
        super(context, result);
    }

    @Override
    protected ExtremeCraftingShapelessBuilder self() {
        return this;
    }

    public ExtremeCraftingShapelessBuilder requires(Object source) {
        ingredients.add(LazyIngredient.from(source).resolve(context().items()));
        return this;
    }

    public ExtremeCraftingShapelessBuilder tier(int tier) {
        this.tier = tier;
        return this;
    }

    @Override
    protected void validate() {
        if (ingredients.isEmpty()) {
            throw new IllegalStateException("Extreme crafting shapeless recipe requires at least one ingredient");
        }
    }

    @Override
    protected void build(RecipeOutput output, ResourceLocation id) {
        NonNullList<Ingredient> list = NonNullList.create();
        list.addAll(ingredients);
        ShapelessTableCraftingRecipe recipe = new ShapelessTableCraftingRecipe(list, result(), tier);
        output.accept(id, recipe, null);
    }
}