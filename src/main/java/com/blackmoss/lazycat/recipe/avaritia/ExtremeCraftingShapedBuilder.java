package com.blackmoss.lazycat.recipe.avaritia;

import com.blackmoss.lazycat.api.LazyIngredient;
import com.blackmoss.lazycat.api.recipe.AbstractRecipeBuilder;
import com.blackmoss.lazycat.api.recipe.RecipeContext;
import committee.nova.mods.avaritia.common.crafting.recipe.ShapedTableCraftingRecipe;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeUnlockAdvancementBuilder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.ShapedRecipePattern;
import net.minecraft.world.level.ItemLike;

import java.util.*;

@SuppressWarnings("unused")
public final class ExtremeCraftingShapedBuilder extends AbstractRecipeBuilder<ExtremeCraftingShapedBuilder> {
    private final Map<Character, LazyIngredient> definitions = new LinkedHashMap<>();
    private final List<String> patterns = new ArrayList<>();
    private int tier;
    private boolean compatible;

    public ExtremeCraftingShapedBuilder(RecipeContext context, ItemLike result) {
        super(context, result);
    }

    @Override
    protected ExtremeCraftingShapedBuilder self() {
        return this;
    }

    public ExtremeCraftingShapedBuilder define(Character symbol, Object source) {
        var ingredient = LazyIngredient.from(source);
        if (symbol == ' ') {
            throw new IllegalArgumentException("Symbol ' ' is reserved");
        }
        if (definitions.containsKey(symbol)) {
            throw new IllegalArgumentException("Symbol '%s' is already defined".formatted(symbol));
        }
        definitions.put(symbol, Objects.requireNonNull(ingredient, "ingredient"));
        defaultUnlock(ingredient);
        return this;
    }

    public ExtremeCraftingShapedBuilder pattern(String... rows) {
        Objects.requireNonNull(rows, "rows");
        for (String row : rows) {
            patterns.add(Objects.requireNonNull(row, "rows contains null"));
        }
        return this;
    }

    public ExtremeCraftingShapedBuilder tier(int tier) {
        this.tier = tier;
        return this;
    }

    public ExtremeCraftingShapedBuilder compatible(boolean compatible) {
        this.compatible = compatible;
        return this;
    }

    @Override
    protected void validate() {
        if (patterns.isEmpty()) {
            throw new IllegalStateException("Extreme shaped recipe requires at least one pattern row");
        }
        int width = patterns.getFirst().length();
        for (String row : patterns) {
            if (row.length() != width) {
                throw new IllegalStateException("Extreme shaped recipe has rows of different width");
            }
            for (char symbol : row.toCharArray()) {
                if (symbol != ' ' && !definitions.containsKey(symbol)) {
                    throw new IllegalStateException("Extreme shaped recipe uses undefined symbol '%s'".formatted(symbol));
                }
            }
        }
    }

    @Override
    protected void build(RecipeOutput output, ResourceKey<Recipe<?>> key) {
        Map<Character, Ingredient> ingredientKey = new LinkedHashMap<>();
        for (Map.Entry<Character, LazyIngredient> entry : definitions.entrySet()) {
            ingredientKey.put(entry.getKey(), entry.getValue().resolve(context().items()));
        }
        ShapedRecipePattern pattern = ShapedRecipePattern.of(ingredientKey, patterns);
        ShapedTableCraftingRecipe recipe = new ShapedTableCraftingRecipe(pattern, result(), tier, compatible);
        RecipeUnlockAdvancementBuilder advancement = new RecipeUnlockAdvancementBuilder();
        applyCriteria(advancement::unlockedBy);
        output.accept(key, recipe, advancement.build(output, key, category()));
    }
}
