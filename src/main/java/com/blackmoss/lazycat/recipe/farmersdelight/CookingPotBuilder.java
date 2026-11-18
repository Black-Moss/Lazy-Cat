package com.blackmoss.lazycat.recipe.farmersdelight;

import com.blackmoss.lazycat.api.LazyIngredient;
import com.blackmoss.lazycat.api.recipe.AbstractRecipeBuilder;
import com.blackmoss.lazycat.api.recipe.RecipeContext;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;
import vectorwing.farmersdelight.client.recipebook.CookingPotRecipeBookTab;
import vectorwing.farmersdelight.data.builder.CookingPotRecipeBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@SuppressWarnings("unused")
public final class CookingPotBuilder extends AbstractRecipeBuilder<CookingPotBuilder> {
    private final int cookingTime;
    private final float experience;
    private final List<Ingredient> ingredients = new ArrayList<>();
    private Object container;
    private CookingPotRecipeBookTab tab;

    public CookingPotBuilder(RecipeContext context, Object result, int cookingTime, float experience) {
        super(context, result);
        this.cookingTime = cookingTime;
        this.experience = experience;
    }

    @Override
    protected CookingPotBuilder self() {
        return this;
    }

    @Override
    protected String defaultFolder() {
        return "cooking";
    }

    public CookingPotBuilder ingredient(Object source) {
        ingredients.add(LazyIngredient.from(source).resolve(context().items()));
        return this;
    }

    public CookingPotBuilder container(Object source) {
        this.container = Objects.requireNonNull(source, "container");
        return this;
    }

    public CookingPotBuilder tab(CookingPotRecipeBookTab tab) {
        this.tab = Objects.requireNonNull(tab, "tab");
        return this;
    }

    @Override
    protected void validate() {
        if (ingredients.isEmpty()) {
            throw new IllegalStateException("Cooking pot recipe requires at least one ingredient");
        }
    }

    @Override
    protected void build(RecipeOutput output, ResourceLocation id) {
        CookingPotRecipeBuilder builder = container == null
                ? CookingPotRecipeBuilder.cookingPotRecipe(resultItem(), 1, cookingTime, experience)
                : CookingPotRecipeBuilder.cookingPotRecipe(resultItem(), 1, cookingTime, experience, containerItem());
        for (Ingredient ingredient : ingredients) {
            builder.addIngredient(ingredient);
        }
        builder.setRecipeBookTab(tab);
        builder.group(group());
        applyCriteria(builder::unlockedBy);
        builder.save(output, id);
    }

    private net.minecraft.world.level.ItemLike containerItem() {
        return LazyIngredient.from(container).stack(context().items()).getItem();
    }
}