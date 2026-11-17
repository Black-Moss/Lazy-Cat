package com.blackmoss.lazycat.recipe.thaumaturge;

import com.blackmoss.lazycat.api.LazyIngredient;
import com.blackmoss.lazycat.api.recipe.RecipeContext;
import com.blackmoss.lazycat.api.recipe.thaumaturge.AbstractThaumaturgeRecipeBuilder;
import com.leclowndu93150.thaumaturge.api.aspect.IAspect;
import com.leclowndu93150.thaumaturge.content.recipe.workbench.ArcaneShapedCraftingRecipe;
import com.leclowndu93150.thaumaturge.content.recipe.workbench.ArcaneShapedRecipePattern;
import net.minecraft.core.Holder;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeUnlockAdvancementBuilder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;

import java.util.*;

@SuppressWarnings("unused")
public final class ArcaneShapedBuilder extends AbstractThaumaturgeRecipeBuilder<ArcaneShapedBuilder> {
    private final List<String> rows = new ArrayList<>();
    private final Map<Character, Ingredient> key = new LinkedHashMap<>();
    private final int vis;

    public ArcaneShapedBuilder(RecipeContext context, Object result, int vis) {
        super(context, result);
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

    @Override
    public ArcaneShapedBuilder aspect(Object source, int amount) {
        Holder<IAspect> holder = resolveAspect(aspects(), source, amount);
        if (!holder.value().isPrimal()) {
            throw new IllegalArgumentException("Arcane workbench aspects must be primal");
        }
        super.aspect(holder, amount);
        return this;
    }

    @SuppressWarnings("unused")
    public ArcaneShapedBuilder pattern(String row) {
        if (!rows.isEmpty() && row.length() != rows.getFirst().length()) {
            throw new IllegalArgumentException("Pattern must be the same width on every line");
        }
        rows.add(row);
        return this;
    }

    public ArcaneShapedBuilder define(Character symbol, Object source) {
        Objects.requireNonNull(symbol, "symbol");
        if (key.containsKey(symbol)) {
            throw new IllegalArgumentException("Symbol '%s' is already defined".formatted(symbol));
        }
        if (symbol == ' ') {
            throw new IllegalArgumentException("Symbol ' ' (whitespace) is reserved");
        }
        key.put(symbol, LazyIngredient.from(source).resolve(context().items()));
        defaultUnlock(LazyIngredient.from(source));
        return this;
    }

    @Override
    protected void validate() {
        super.validate();
        if (rows.isEmpty()) {
            throw new IllegalStateException("Arcane workbench shaped recipe requires at least one pattern row");
        }
        if (key.isEmpty()) {
            throw new IllegalStateException("Arcane workbench shaped recipe requires at least one defined symbol");
        }
        if (vis <= 0) {
            throw new IllegalStateException("Arcane workbench recipe requires vis > 0");
        }
    }

    @Override
    protected void build(RecipeOutput output, ResourceKey<Recipe<?>> key) {
        ArcaneShapedRecipePattern pattern = ArcaneShapedRecipePattern.of(this.key, rows);
        ArcaneShapedCraftingRecipe recipe = new ArcaneShapedCraftingRecipe(
                RecipeBuilder.createCraftingCommonInfo(true), vis, gate(), buildAspects(), pattern, result());
        RecipeUnlockAdvancementBuilder advancement = new RecipeUnlockAdvancementBuilder();
        applyCriteria(advancement::unlockedBy);
        output.accept(key, recipe, advancement.build(output, key, category()));
    }
}
