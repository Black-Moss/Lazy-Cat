package com.blackmoss.lazycat.recipe.thaumaturge;

import com.blackmoss.lazycat.api.LazyIngredient;
import com.blackmoss.lazycat.api.recipe.RecipeContext;
import com.blackmoss.lazycat.api.recipe.thaumaturge.AbstractThaumaturgeRecipeBuilder;
import com.leclowndu93150.thaumaturge.api.aspect.IAspect;
import com.leclowndu93150.thaumaturge.content.recipe.workbench.ArcaneShapelessCraftingRecipe;
import net.minecraft.core.Holder;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeUnlockAdvancementBuilder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public final class ArcaneShapelessBuilder extends AbstractThaumaturgeRecipeBuilder<ArcaneShapelessBuilder> {
    private final List<Ingredient> ingredients = new ArrayList<>();
    private final int vis;

    public ArcaneShapelessBuilder(RecipeContext context, Object result, int vis) {
        super(context, result);
        this.vis = vis;
    }

    @Override
    protected ArcaneShapelessBuilder self() {
        return this;
    }

    @Override
    protected String defaultFolder() {
        return "arcane_workbench";
    }

    @Override
    public ArcaneShapelessBuilder aspect(Object source, int amount) {
        Holder<IAspect> holder = resolveAspect(aspects(), source, amount);
        if (!holder.value().isPrimal()) {
            throw new IllegalArgumentException("Arcane workbench aspects must be primal");
        }
        super.aspect(holder, amount);
        return this;
    }

    public ArcaneShapelessBuilder requires(Object source) {
        return requires(source, 1);
    }

    public ArcaneShapelessBuilder requires(Object source, int count) {
        for (int i = 0; i < count; i++) {
            ingredients.add(LazyIngredient.from(source).resolve(context().items()));
        }
        defaultUnlock(LazyIngredient.from(source));
        return this;
    }

    @Override
    protected void validate() {
        super.validate();
        if (ingredients.isEmpty()) {
            throw new IllegalStateException("Arcane workbench shapeless recipe requires at least one ingredient");
        }
        if (vis <= 0) {
            throw new IllegalStateException("Arcane workbench recipe requires vis > 0");
        }
    }

    @Override
    protected void build(RecipeOutput output, ResourceKey<Recipe<?>> key) {
        ArcaneShapelessCraftingRecipe recipe = new ArcaneShapelessCraftingRecipe(
                RecipeBuilder.createCraftingCommonInfo(true), vis, gate(), buildAspects(), result(), ingredients);
        RecipeUnlockAdvancementBuilder advancement = new RecipeUnlockAdvancementBuilder();
        applyCriteria(advancement::unlockedBy);
        output.accept(key, recipe, advancement.build(output, key, category()));
    }
}
