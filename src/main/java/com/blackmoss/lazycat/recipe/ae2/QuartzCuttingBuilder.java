package com.blackmoss.lazycat.recipe.ae2;

import com.blackmoss.lazycat.api.recipe.AbstractRecipeBuilder;
import com.blackmoss.lazycat.api.recipe.RecipeContext;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.ItemLike;
import appeng.recipes.quartzcutting.QuartzCuttingRecipe;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class QuartzCuttingBuilder extends AbstractRecipeBuilder<QuartzCuttingBuilder> {
    private final List<Object> inputs = new ArrayList<>();

    public QuartzCuttingBuilder(RecipeContext context, ItemLike result) {
        super(context, result);
    }

    @Override
    protected QuartzCuttingBuilder self() {
        return this;
    }

    public QuartzCuttingBuilder addIngredient(Object source) {
        inputs.add(Objects.requireNonNull(source, "ingredient"));
        return this;
    }

    @Override
    protected void validate() {
        if (inputs.isEmpty()) {
            throw new IllegalStateException("Quartz cutting recipe requires at least one ingredient");
        }
    }

    @Override
    protected void build(RecipeOutput output, ResourceKey<Recipe<?>> key) {
        List<Ingredient> ingredients = new ArrayList<>(inputs.size());
        for (Object source : inputs) {
            ingredients.add(toIngredient(source));
        }
        String group = group() != null ? group() : "";
        output.accept(key, new QuartzCuttingRecipe(
                new Recipe.CommonInfo(false),
                new CraftingRecipe.CraftingBookInfo(CraftingBookCategory.MISC, group),
                result(),
                ingredients), null);
    }
}
