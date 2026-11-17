package com.blackmoss.lazycat.recipe.avaritia;

import com.blackmoss.lazycat.api.LazyIngredient;
import com.blackmoss.lazycat.api.recipe.AbstractRecipeBuilder;
import com.blackmoss.lazycat.api.recipe.RecipeContext;
import committee.nova.mods.avaritia.common.crafting.recipe.ExtremeSmithingRecipe;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeUnlockAdvancementBuilder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.common.crafting.CompoundIngredient;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public final class ExtremeSmithingBuilder extends AbstractRecipeBuilder<ExtremeSmithingBuilder> {
    private final List<Ingredient> additions = new ArrayList<>();
    private Ingredient template;
    private Ingredient base;

    public ExtremeSmithingBuilder(RecipeContext context, ItemLike result) {
        super(context, result);
    }

    @Override
    protected ExtremeSmithingBuilder self() {
        return this;
    }

    public ExtremeSmithingBuilder template(Object source) {
        this.template = LazyIngredient.from(source).resolve(context().items());
        defaultUnlock(LazyIngredient.from(source));
        return this;
    }

    public ExtremeSmithingBuilder base(Object source) {
        this.base = LazyIngredient.from(source).resolve(context().items());
        return this;
    }

    public ExtremeSmithingBuilder addition(Object source) {
        return addition(source, 1);
    }

    public ExtremeSmithingBuilder addition(Object source, int count) {
        for (int i = 0; i < count; i++) {
            additions.add(LazyIngredient.from(source).resolve(context().items()));
        }
        return this;
    }

    @Override
    protected void validate() {
        if (template == null) {
            throw new IllegalStateException("Extreme smithing recipe requires a template");
        }
        if (base == null) {
            throw new IllegalStateException("Extreme smithing recipe requires a base");
        }
        if (additions.isEmpty()) {
            throw new IllegalStateException("Extreme smithing recipe requires at least one addition");
        }
    }

    @Override
    protected void build(RecipeOutput output, ResourceKey<Recipe<?>> key) {
        Ingredient additions = this.additions.size() == 1
                ? this.additions.getFirst()
                : CompoundIngredient.of(this.additions.toArray(new Ingredient[0]));
        ExtremeSmithingRecipe recipe = new ExtremeSmithingRecipe(template, base, additions, result());
        RecipeUnlockAdvancementBuilder advancement = new RecipeUnlockAdvancementBuilder();
        applyCriteria(advancement::unlockedBy);
        output.accept(key, recipe, advancement.build(output, key, category()));
    }
}
