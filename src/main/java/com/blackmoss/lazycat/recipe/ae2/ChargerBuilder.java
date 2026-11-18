package com.blackmoss.lazycat.recipe.ae2;

import appeng.recipes.handlers.ChargerRecipe;
import com.blackmoss.lazycat.api.LazyIngredient;
import com.blackmoss.lazycat.api.recipe.AbstractRecipeBuilder;
import com.blackmoss.lazycat.api.recipe.RecipeContext;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.Objects;

@SuppressWarnings("unused")
public final class ChargerBuilder extends AbstractRecipeBuilder<ChargerBuilder> {
    private Object input;

    public ChargerBuilder(RecipeContext context, Object result) {
        super(context, result);
    }

    @Override
    protected ChargerBuilder self() {
        return this;
    }

    @Override
    protected String defaultFolder() {
        return "charger";
    }

    public ChargerBuilder input(Object source) {
        this.input = Objects.requireNonNull(source, "input");
        return this;
    }

    @Override
    protected void validate() {
        if (input == null) {
            throw new IllegalStateException("Charger recipe requires an input");
        }
    }

    @Override
    protected void build(RecipeOutput output, ResourceLocation id) {
        Ingredient ingredient = LazyIngredient.from(input).resolve(context().items());
        ChargerRecipe recipe = new ChargerRecipe(ingredient, result());
        output.accept(id, recipe, null);
    }
}