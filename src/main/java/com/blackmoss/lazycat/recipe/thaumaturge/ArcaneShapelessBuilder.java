package com.blackmoss.lazycat.recipe.thaumaturge;

import com.blackmoss.lazycat.api.LazyIngredient;
import com.blackmoss.lazycat.api.recipe.RecipeContext;
import com.blackmoss.lazycat.api.recipe.thaumaturge.AbstractThaumaturgeRecipeBuilder;
import com.leclowndu93150.thaumaturge.content.recipe.workbench.ArcaneShapelessCraftingRecipe;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public final class ArcaneShapelessBuilder extends AbstractThaumaturgeRecipeBuilder<ArcaneShapelessBuilder> {
    private final List<Ingredient> ingredients = new ArrayList<>();
    private final int vis;

    public ArcaneShapelessBuilder(RecipeContext context, Object result, int vis) {
        super(context, result);
        if (vis <= 0) {
            throw new IllegalArgumentException("vis must be positive");
        }
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
        if (!isPrimalAspect(source)) {
            throw new IllegalArgumentException("Arcane workbench aspects must be primal");
        }
        super.aspect(source, amount);
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
        if (ingredients.isEmpty()) {
            throw new IllegalStateException("Arcane workbench shapeless recipe requires at least one ingredient");
        }
    }

    @Override
    protected void build(RecipeOutput output, ResourceLocation id) {
        ArcaneShapelessCraftingRecipe recipe = new ArcaneShapelessCraftingRecipe(
                group() != null ? group() : "", vis, gate(), buildAspects(), result(), ingredients);
        output.accept(id, recipe, null);
    }
}