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

@SuppressWarnings("unused")
public final class CookingPotBuilder extends AbstractRecipeBuilder<CookingPotBuilder> {
    private final List<Object> ingredients = new ArrayList<>();
    private Float experience;
    private Integer time;
    private ItemLike container;
    private CookingPotRecipeBookTab recipeBookTab;

    public CookingPotBuilder(RecipeContext context, Object result) {
        super(context, result);
    }

    @Override
    protected CookingPotBuilder self() {
        return this;
    }

    public CookingPotBuilder exp(float experience) {
        this.experience = experience;
        return this;
    }

    public CookingPotBuilder time(int time) {
        this.time = time;
        return this;
    }

    @Override
    protected String defaultFolder() {
        return "cooking";
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
        if (experience != null && experience < 0.0f) {
            throw new IllegalArgumentException("experience must not be negative");
        }
        if (time != null && time <= 0) {
            throw new IllegalArgumentException("time must be positive");
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
                grp, recipeBookTab, inputs, resultIngredient().stack(context().items()),
                containerStack, experience, time);
        RecipeUnlockAdvancementBuilder advancement = new RecipeUnlockAdvancementBuilder();
        applyCriteria(advancement::unlockedBy);
        output.accept(key, recipe, advancement.build(output, key, category()));
    }
}
