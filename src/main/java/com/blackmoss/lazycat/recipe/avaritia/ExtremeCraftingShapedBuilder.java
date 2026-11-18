package com.blackmoss.lazycat.recipe.avaritia;

import com.blackmoss.lazycat.api.LazyIngredient;
import com.blackmoss.lazycat.api.recipe.AbstractRecipeBuilder;
import com.blackmoss.lazycat.api.recipe.RecipeContext;
import committee.nova.mods.avaritia.common.crafting.recipe.ShapedTableCraftingRecipe;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.ShapedRecipePattern;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

@SuppressWarnings("unused")
public final class ExtremeCraftingShapedBuilder extends AbstractRecipeBuilder<ExtremeCraftingShapedBuilder> {
    private final Map<Character, Ingredient> definitions = new LinkedHashMap<>();
    private String[] rows = new String[0];
    private int tier;

    public ExtremeCraftingShapedBuilder(RecipeContext context, Object result) {
        super(context, result);
    }

    @Override
    protected ExtremeCraftingShapedBuilder self() {
        return this;
    }

    public ExtremeCraftingShapedBuilder define(char symbol, Object source) {
        Objects.requireNonNull(source, "source");
        if (symbol == ' ') {
            throw new IllegalArgumentException("Symbol ' ' is reserved");
        }
        if (definitions.containsKey(symbol)) {
            throw new IllegalArgumentException("Symbol '" + symbol + "' is already defined");
        }
        definitions.put(symbol, LazyIngredient.from(source).resolve(context().items()));
        return this;
    }

    public ExtremeCraftingShapedBuilder pattern(String... rows) {
        Objects.requireNonNull(rows, "rows");
        this.rows = rows.clone();
        return this;
    }

    public ExtremeCraftingShapedBuilder tier(int tier) {
        this.tier = tier;
        return this;
    }

    @Override
    protected void validate() {
        if (rows.length == 0) {
            throw new IllegalStateException("Extreme crafting shaped recipe requires a pattern");
        }
        int width = rows[0].length();
        for (String row : rows) {
            if (row.length() != width) {
                throw new IllegalStateException("Extreme crafting shaped recipe has rows of different width");
            }
            for (char symbol : row.toCharArray()) {
                if (symbol != ' ' && !definitions.containsKey(symbol)) {
                    throw new IllegalStateException(
                            "Extreme crafting shaped recipe uses undefined symbol '" + symbol + "'");
                }
            }
        }
    }

    @Override
    protected void build(RecipeOutput output, ResourceLocation id) {
        ShapedRecipePattern pattern = ShapedRecipePattern.of(definitions, rows);
        ShapedTableCraftingRecipe recipe = new ShapedTableCraftingRecipe(pattern, result(), tier, true);
        output.accept(id, recipe, null);
    }
}