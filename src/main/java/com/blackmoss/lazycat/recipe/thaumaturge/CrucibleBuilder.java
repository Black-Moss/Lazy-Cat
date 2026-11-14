package com.blackmoss.lazycat.recipe.thaumaturge;

import com.blackmoss.lazycat.api.recipe.RecipeContext;
import com.blackmoss.lazycat.api.recipe.RecipeIngredient;
import com.blackmoss.lazycat.api.recipe.thaumaturge.AbstractThaumaturgeRecipeBuilder;
import com.leclowndu93150.thaumaturge.content.recipe.crucible.CrucibleRecipe;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeUnlockAdvancementBuilder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.ItemLike;

import java.util.Objects;

public final class CrucibleBuilder extends AbstractThaumaturgeRecipeBuilder<CrucibleBuilder> {
    private Ingredient catalyst;

    public CrucibleBuilder(RecipeContext context, ItemLike result, Object catalyst) {
        super(context, result);
        RecipeIngredient catalystIngredient = RecipeIngredient.from(Objects.requireNonNull(catalyst, "catalyst"));
        this.catalyst = catalystIngredient.resolve(context.items());
        defaultUnlock(catalystIngredient);
    }

    @Override
    protected CrucibleBuilder self() {
        return this;
    }

    public CrucibleBuilder catalyst(Object source) {
        this.catalyst = RecipeIngredient.from(source).resolve(context().items());
        return this;
    }

    @Override
    protected void validate() {
        super.validate();
        if (catalyst == null) {
            throw new IllegalStateException("Crucible recipe requires a catalyst");
        }
        requireAspects();
    }

    @Override
    protected void build(RecipeOutput output, ResourceKey<Recipe<?>> key) {
        CrucibleRecipe recipe = new CrucibleRecipe(catalyst, buildAspects(), result(), gate());
        RecipeUnlockAdvancementBuilder advancement = new RecipeUnlockAdvancementBuilder();
        applyCriteria(advancement::unlockedBy);
        output.accept(key, recipe, advancement.build(output, key, category()));
    }
}
