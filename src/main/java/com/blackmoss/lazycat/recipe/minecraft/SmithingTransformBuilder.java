package com.blackmoss.lazycat.recipe.minecraft;

import com.blackmoss.lazycat.api.LazyIngredient;
import com.blackmoss.lazycat.api.recipe.AbstractRecipeBuilder;
import com.blackmoss.lazycat.api.recipe.RecipeContext;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeUnlockAdvancementBuilder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.SmithingTransformRecipe;

import java.util.Optional;

@SuppressWarnings("unused")
public final class SmithingTransformBuilder extends AbstractRecipeBuilder<SmithingTransformBuilder> {
    private LazyIngredient template;
    private LazyIngredient base;
    private LazyIngredient addition;

    public SmithingTransformBuilder(RecipeContext context, Object result) {
        super(context, result);
    }

    @Override
    protected SmithingTransformBuilder self() {
        return this;
    }

    @Override
    protected String defaultFolder() {
        return "smithing";
    }

    public SmithingTransformBuilder template(Object source) {
        template = LazyIngredient.from(source);
        return this;
    }

    public SmithingTransformBuilder noTemplate() {
        template = null;
        return this;
    }

    public SmithingTransformBuilder base(Object source) {
        base = LazyIngredient.from(source);
        defaultUnlock(base);
        return this;
    }

    public SmithingTransformBuilder addition(Object source) {
        addition = LazyIngredient.from(source);
        return this;
    }

    public SmithingTransformBuilder noAddition() {
        addition = null;
        return this;
    }

    @Override
    protected void validate() {
        if (base == null) {
            throw new IllegalStateException("Smithing transform recipe %s has no base".formatted(defaultIdPath()));
        }
    }

    @Override
    protected void build(RecipeOutput output, ResourceKey<Recipe<?>> key) {
        Ingredient base = this.base.resolve(context().items());
        Optional<Ingredient> template = this.template != null
                ? Optional.of(this.template.resolve(context().items()))
                : Optional.empty();
        Optional<Ingredient> addition = this.addition != null
                ? Optional.of(this.addition.resolve(context().items()))
                : Optional.empty();

        SmithingTransformRecipe recipe = new SmithingTransformRecipe(
                new Recipe.CommonInfo(true), template, base, addition, result());

        RecipeUnlockAdvancementBuilder advancementBuilder = new RecipeUnlockAdvancementBuilder();
        applyCriteria(advancementBuilder::unlockedBy);
        output.accept(key, recipe, advancementBuilder.build(output, key, category()));
    }
}
