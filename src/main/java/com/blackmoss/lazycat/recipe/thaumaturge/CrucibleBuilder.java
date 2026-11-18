package com.blackmoss.lazycat.recipe.thaumaturge;

import com.blackmoss.lazycat.api.LazyIngredient;
import com.blackmoss.lazycat.api.recipe.RecipeContext;
import com.blackmoss.lazycat.api.thaumaturge.AbstractThaumaturgeRecipeBuilder;
import com.leclowndu93150.thaumaturge.content.recipe.crucible.CrucibleRecipe;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.Objects;

@SuppressWarnings("unused")
public final class CrucibleBuilder extends AbstractThaumaturgeRecipeBuilder<CrucibleBuilder> {
    private Ingredient catalyst;

    public CrucibleBuilder(RecipeContext context, Object result, Object catalyst) {
        super(context, result);
        LazyIngredient ingredient = LazyIngredient.from(Objects.requireNonNull(catalyst, "catalyst"));
        this.catalyst = ingredient.resolve(context.items());
        defaultUnlock(ingredient);
    }

    @Override
    protected CrucibleBuilder self() {
        return this;
    }

    @Override
    protected String defaultFolder() {
        return "crucible";
    }

    public CrucibleBuilder catalyst(Object source) {
        this.catalyst = LazyIngredient.from(source).resolve(context().items());
        return this;
    }

    @Override
    protected void validate() {
        if (catalyst == null) {
            throw new IllegalStateException("Crucible recipe requires a catalyst");
        }
        requireAspects();
    }

    @Override
    protected void build(RecipeOutput output, ResourceLocation id) {
        CrucibleRecipe recipe = new CrucibleRecipe(catalyst, buildAspects(), result(), gate());
        output.accept(id, recipe, null);
    }
}