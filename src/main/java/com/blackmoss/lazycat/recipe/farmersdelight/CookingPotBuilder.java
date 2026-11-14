package com.blackmoss.lazycat.recipe.farmersdelight;

import com.blackmoss.lazycat.api.recipe.AbstractRecipeBuilder;
import com.blackmoss.lazycat.api.recipe.RecipeContext;
import net.minecraft.core.NonNullList;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeUnlockAdvancementBuilder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.ItemLike;
import vectorwing.farmersdelight.client.recipebook.CookingPotRecipeBookTab;
import vectorwing.farmersdelight.common.crafting.CookingPotRecipe;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class CookingPotBuilder extends AbstractRecipeBuilder<CookingPotBuilder> {
    private final int cookingTime;
    private final float experience;
    private ItemLike container;
    private CookingPotRecipeBookTab recipeBookTab;
    private final List<Object> ingredients = new ArrayList<>();

    public CookingPotBuilder(RecipeContext context, ItemLike result, int cookingTime, float experience) {
        super(context, result);
        this.cookingTime = cookingTime;
        this.experience = experience;
    }

    @Override
    protected CookingPotBuilder self() {
        return this;
    }

    public CookingPotBuilder container(ItemLike container) {
        this.container = container;
        return this;
    }

    public CookingPotBuilder recipeBookTab(CookingPotRecipeBookTab tab) {
        this.recipeBookTab = tab;
        return this;
    }

    public CookingPotBuilder addIngredient(Object source) {
        return addIngredient(source, 1);
    }

    public CookingPotBuilder addIngredient(Object source, int quantity) {
        Objects.requireNonNull(source, "ingredient");
        for (int i = 0; i < quantity; ++i) {
            ingredients.add(source);
        }
        return this;
    }

    @Override
    protected void validate() {
        if (ingredients.isEmpty()) {
            throw new IllegalStateException("Cooking pot recipe requires at least one ingredient");
        }
    }

    @Override
    protected void build(RecipeOutput output, ResourceKey<Recipe<?>> key) {
        NonNullList<Ingredient> inputs = NonNullList.create();
        for (Object source : ingredients) {
            inputs.add(toIngredient(source));
        }
        ItemStack containerStack = container != null ? new ItemStack(container.asItem()) : ItemStack.EMPTY;
        String grp = group() != null ? group() : "";
        CookingPotRecipe recipe = new CookingPotRecipe(
                grp, recipeBookTab, inputs, new ItemStack(resultItem(), count()), containerStack, experience, cookingTime);
        RecipeUnlockAdvancementBuilder advancement = new RecipeUnlockAdvancementBuilder();
        applyCriteria(advancement::unlockedBy);
        output.accept(key, recipe, advancement.build(output, key, category()));
    }
}
