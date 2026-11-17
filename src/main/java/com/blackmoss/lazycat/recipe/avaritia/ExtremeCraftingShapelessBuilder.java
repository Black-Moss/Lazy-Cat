package com.blackmoss.lazycat.recipe.avaritia;

import com.blackmoss.lazycat.api.LazyIngredient;
import com.blackmoss.lazycat.api.recipe.AbstractRecipeBuilder;
import com.blackmoss.lazycat.api.recipe.RecipeContext;
import committee.nova.mods.avaritia.common.crafting.recipe.ShapelessTableCraftingRecipe;
import net.minecraft.core.NonNullList;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeUnlockAdvancementBuilder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;

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

    @Override
    protected String defaultFolder() {
        return "extreme_crafting";
    }

    public ExtremeCraftingShapelessBuilder requires(Object source) {
        return requires(source, 1);
    }

    public ExtremeCraftingShapelessBuilder requires(Object source, int count) {
        for (int i = 0; i < count; i++) {
            ingredients.add(LazyIngredient.from(source).resolve(context().items()));
        }
        defaultUnlock(LazyIngredient.from(source));
        return this;
    }

    public ExtremeCraftingShapelessBuilder tier(int tier) {
        this.tier = tier;
        return this;
    }

    @Override
    protected void validate() {
        if (ingredients.isEmpty()) {
            throw new IllegalStateException("Extreme shapeless recipe requires at least one ingredient");
        }
    }

    @Override
    protected void build(RecipeOutput output, ResourceKey<Recipe<?>> key) {
        NonNullList<Ingredient> ingredients = NonNullList.create();
        ingredients.addAll(this.ingredients);
        ShapelessTableCraftingRecipe recipe = new ShapelessTableCraftingRecipe(ingredients, result(), tier);
        RecipeUnlockAdvancementBuilder advancement = new RecipeUnlockAdvancementBuilder();
        applyCriteria(advancement::unlockedBy);
        output.accept(key, recipe, advancement.build(output, key, category()));
    }
}
