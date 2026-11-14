package com.blackmoss.lazycat.recipe.ae2;

import com.blackmoss.lazycat.api.recipe.AbstractRecipeBuilder;
import com.blackmoss.lazycat.api.recipe.RecipeContext;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.ItemLike;
import appeng.recipes.transform.TransformCircumstance;
import appeng.recipes.transform.TransformRecipe;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class TransformBuilder extends AbstractRecipeBuilder<TransformBuilder> {
    private final List<Object> inputs = new ArrayList<>();
    private TransformCircumstance circumstance;

    public TransformBuilder(RecipeContext context, ItemLike result, TransformCircumstance circumstance) {
        super(context, result);
        this.circumstance = Objects.requireNonNull(circumstance, "circumstance");
    }

    @Override
    protected TransformBuilder self() {
        return this;
    }

    public TransformBuilder circumstance(TransformCircumstance circumstance) {
        this.circumstance = Objects.requireNonNull(circumstance, "circumstance");
        return this;
    }

    public TransformBuilder addIngredient(Object source) {
        inputs.add(Objects.requireNonNull(source, "ingredient"));
        return this;
    }

    @Override
    protected void validate() {
        if (inputs.isEmpty()) {
            throw new IllegalStateException("Transform recipe requires at least one ingredient");
        }
    }

    @Override
    protected void build(RecipeOutput output, ResourceKey<Recipe<?>> key) {
        List<Ingredient> ingredients = new ArrayList<>(inputs.size());
        for (Object source : inputs) {
            ingredients.add(toIngredient(source));
        }
        output.accept(key, new TransformRecipe(ingredients, result(), circumstance), null);
    }
}
