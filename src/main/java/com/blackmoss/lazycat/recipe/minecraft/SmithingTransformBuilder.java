package com.blackmoss.lazycat.recipe.minecraft;

import com.blackmoss.lazycat.api.LazyIngredient;
import com.blackmoss.lazycat.api.recipe.AbstractRecipeBuilder;
import com.blackmoss.lazycat.api.recipe.RecipeContext;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.SmithingTransformRecipe;

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
    protected void build(RecipeOutput output, ResourceLocation id) {
        Ingredient baseIngredient = base.resolve(context().items());
        Ingredient templateIngredient = template != null
                ? template.resolve(context().items())
                : Ingredient.EMPTY;
        Ingredient additionIngredient = addition != null
                ? addition.resolve(context().items())
                : Ingredient.EMPTY;
        ItemStack resultStack = result();

        SmithingTransformRecipe recipe = new SmithingTransformRecipe(
                templateIngredient, baseIngredient, additionIngredient, resultStack);
        AdvancementHolder advancement = advancement(output, id);
        output.accept(id, recipe, advancement);
    }
}
