package com.blackmoss.lazycat.recipe.ae2;

import appeng.recipes.handlers.ChargerRecipe;
import com.blackmoss.lazycat.api.recipe.minecraft.AbstractRecipeBuilder;
import com.blackmoss.lazycat.api.recipe.RecipeContext;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.ItemLike;

import java.util.Objects;

@SuppressWarnings("unused")
public final class ChargerBuilder extends AbstractRecipeBuilder<ChargerBuilder> {
    private Object input;

    public ChargerBuilder(RecipeContext context, ItemLike result) {
        super(context, result);
    }

    @Override
    protected ChargerBuilder self() {
        return this;
    }

    public ChargerBuilder input(Object source) {
        this.input = Objects.requireNonNull(source, "ingredient");
        return this;
    }

    @Override
    protected void validate() {
        if (input == null) {
            throw new IllegalStateException("Charger recipe requires an ingredient");
        }
    }

    @Override
    protected void build(RecipeOutput output, ResourceKey<Recipe<?>> key) {
        output.accept(key, new ChargerRecipe(toIngredient(input), result()), null);
    }
}
