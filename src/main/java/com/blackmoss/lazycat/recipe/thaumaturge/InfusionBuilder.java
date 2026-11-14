package com.blackmoss.lazycat.recipe.thaumaturge;

import com.blackmoss.lazycat.api.recipe.RecipeContext;
import com.blackmoss.lazycat.api.recipe.RecipeIngredient;
import com.blackmoss.lazycat.api.recipe.thaumaturge.AbstractThaumaturgeRecipeBuilder;
import com.blackmoss.lazycat.api.recipe.thaumaturge.InstabilityType;
import com.leclowndu93150.thaumaturge.content.infusion.InfusionRecipe;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeUnlockAdvancementBuilder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.ItemLike;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class InfusionBuilder extends AbstractThaumaturgeRecipeBuilder<InfusionBuilder> {
    private final List<Ingredient> components = new ArrayList<>();
    private Ingredient catalyst;
    private int instability;

    public InfusionBuilder(RecipeContext context, ItemLike result, Object catalyst) {
        super(context, result);
        RecipeIngredient catalystIngredient = RecipeIngredient.from(Objects.requireNonNull(catalyst, "catalyst"));
        this.catalyst = catalystIngredient.resolve(context.items());
        defaultUnlock(catalystIngredient);
    }

    @Override
    protected InfusionBuilder self() {
        return this;
    }

    public InfusionBuilder catalyst(Object source) {
        this.catalyst = RecipeIngredient.from(source).resolve(context().items());
        return this;
    }

    public InfusionBuilder component(Object source) {
        return component(source, 1);
    }

    public InfusionBuilder component(Object source, int count) {
        for (int i = 0; i < count; i++) {
            components.add(RecipeIngredient.from(source).resolve(context().items()));
        }
        return this;
    }

    public InfusionBuilder instability(int instability) {
        this.instability = instability;
        return this;
    }

    public InfusionBuilder instability(InstabilityType type) {
        return instability(type.get());
    }

    @Override
    protected void validate() {
        super.validate();
        if (catalyst == null) {
            throw new IllegalStateException("Infusion recipe requires a catalyst");
        }
        if (components.isEmpty()) {
            throw new IllegalStateException("Infusion recipe requires at least one component");
        }
        requireAspects();
    }

    @Override
    protected void build(RecipeOutput output, ResourceKey<Recipe<?>> key) {
        InfusionRecipe recipe = new InfusionRecipe(
                catalyst, components, buildAspects(), instability, result(), gate());
        RecipeUnlockAdvancementBuilder advancement = new RecipeUnlockAdvancementBuilder();
        applyCriteria(advancement::unlockedBy);
        output.accept(key, recipe, advancement.build(output, key, category()));
    }
}
