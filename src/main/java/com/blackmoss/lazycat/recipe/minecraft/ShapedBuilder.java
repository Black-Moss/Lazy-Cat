package com.blackmoss.lazycat.recipe.minecraft;

import com.blackmoss.lazycat.api.recipe.AbstractRecipeBuilder;
import com.blackmoss.lazycat.api.recipe.RecipeContext;
import com.blackmoss.lazycat.api.recipe.RecipeIngredient;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.ItemLike;

import java.util.*;

public final class ShapedBuilder extends AbstractRecipeBuilder<ShapedBuilder> {
    private final Map<Character, RecipeIngredient> definitions = new LinkedHashMap<>();
    private final List<String> patterns = new ArrayList<>();
    private boolean showNotification = true;

    public ShapedBuilder(RecipeContext context, ItemLike result) {
        super(context, result);
    }

    @Override
    protected ShapedBuilder self() {
        return this;
    }

    public ShapedBuilder define(char symbol, Object source) {
        var ingredient = RecipeIngredient.from(source);
        if (symbol == ' ') {
            throw new IllegalArgumentException("Symbol ' ' is reserved");
        }
        if (definitions.containsKey(symbol)) {
            throw new IllegalArgumentException("Symbol '" + symbol + "' is already defined");
        }
        definitions.put(symbol, Objects.requireNonNull(ingredient, "ingredient"));
        defaultUnlock(ingredient);
        return this;
    }

    public ShapedBuilder pattern(String... rows) {
        Objects.requireNonNull(rows, "rows");
        for (String row : rows) {
            patterns.add(Objects.requireNonNull(row, "rows contains null"));
        }
        return this;
    }

    public ShapedBuilder showNotification(boolean show) {
        showNotification = show;
        return this;
    }

    @Override
    protected void validate() {
        if (patterns.isEmpty()) {
            throw new IllegalStateException("Shaped recipe %s has no pattern".formatted(defaultIdPath()));
        }
        int width = patterns.getFirst().length();
        for (String row : patterns) {
            if (row.length() != width) {
                throw new IllegalStateException("Shaped recipe %s has rows of different width".formatted(defaultIdPath()));
            }
            for (char symbol : row.toCharArray()) {
                if (symbol != ' ' && !definitions.containsKey(symbol)) {
                    throw new IllegalStateException(
                            "Shaped recipe %s uses undefined symbol '%s'".formatted(defaultIdPath(), symbol));
                }
            }
        }
    }

    @Override
    protected void build(RecipeOutput output, ResourceKey<Recipe<?>> key) {
        ShapedRecipeBuilder builder = ShapedRecipeBuilder.shaped(context().items(), category(), result());
        for (Map.Entry<Character, RecipeIngredient> entry : definitions.entrySet()) {
            builder.define(entry.getKey(), entry.getValue().resolve(context().items()));
        }
        for (String row : patterns) {
            builder.pattern(row);
        }
        builder.group(group());
        builder.showNotification(showNotification);
        applyCriteria(builder::unlockedBy);
        builder.save(output, key);
    }
}
