package com.blackmoss.lazycat.recipe.thaumaturge;

import com.blackmoss.lazycat.api.LazyIngredient;
import com.blackmoss.lazycat.api.recipe.RecipeContext;
import com.blackmoss.lazycat.api.recipe.thaumaturge.AbstractThaumaturgeRecipeBuilder;
import com.leclowndu93150.thaumaturge.content.recipe.workbench.ArcaneShapedCraftingRecipe;
import com.leclowndu93150.thaumaturge.content.recipe.workbench.ArcaneShapedRecipePattern;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.*;

@SuppressWarnings("unused")
public final class ArcaneShapedBuilder extends AbstractThaumaturgeRecipeBuilder<ArcaneShapedBuilder> {
    private final Map<Character, Ingredient> definitions = new LinkedHashMap<>();
    private final List<String> patterns = new ArrayList<>();
    private final int vis;

    public ArcaneShapedBuilder(RecipeContext context, Object result, int vis) {
        super(context, result);
        if (vis <= 0) {
            throw new IllegalArgumentException("vis must be positive");
        }
        this.vis = vis;
    }

    @Override
    protected ArcaneShapedBuilder self() {
        return this;
    }

    @Override
    protected String defaultFolder() {
        return "arcane_workbench";
    }

    public ArcaneShapedBuilder define(char symbol, Object source) {
        Objects.requireNonNull(source, "source");
        if (symbol == ' ') {
            throw new IllegalArgumentException("Symbol ' ' is reserved");
        }
        if (definitions.containsKey(symbol)) {
            throw new IllegalArgumentException("Symbol '" + symbol + "' is already defined");
        }
        definitions.put(symbol, LazyIngredient.from(source).resolve(context().items()));
        defaultUnlock(LazyIngredient.from(source));
        return this;
    }

    public ArcaneShapedBuilder pattern(String... rows) {
        Objects.requireNonNull(rows, "rows");
        for (String row : rows) {
            patterns.add(Objects.requireNonNull(row, "rows contains null"));
        }
        return this;
    }

    @Override
    protected void validate() {
        if (patterns.isEmpty()) {
            throw new IllegalStateException("Arcane workbench shaped recipe requires a pattern");
        }
        int width = patterns.getFirst().length();
        for (String row : patterns) {
            if (row.length() != width) {
                throw new IllegalStateException("Arcane workbench shaped recipe has rows of different width");
            }
            for (char symbol : row.toCharArray()) {
                if (symbol != ' ' && !definitions.containsKey(symbol)) {
                    throw new IllegalStateException(
                            "Arcane workbench shaped recipe uses undefined symbol '" + symbol + "'");
                }
            }
        }
    }

    @Override
    protected void build(RecipeOutput output, ResourceLocation id) {
        ArcaneShapedRecipePattern pattern = ArcaneShapedRecipePattern.of(definitions, patterns);
        ArcaneShapedCraftingRecipe recipe = new ArcaneShapedCraftingRecipe(
                group() != null ? group() : "", vis, gate(), buildAspects(), pattern, result());
        output.accept(id, recipe, null);
    }
}