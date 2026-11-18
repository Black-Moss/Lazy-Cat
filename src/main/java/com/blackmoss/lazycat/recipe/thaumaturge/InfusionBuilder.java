package com.blackmoss.lazycat.recipe.thaumaturge;

import com.blackmoss.lazycat.api.LazyIngredient;
import com.blackmoss.lazycat.api.recipe.RecipeContext;
import com.blackmoss.lazycat.api.thaumaturge.AbstractThaumaturgeRecipeBuilder;
import com.leclowndu93150.thaumaturge.content.infusion.InfusionRecipe;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@SuppressWarnings("unused")
public final class InfusionBuilder extends AbstractThaumaturgeRecipeBuilder<InfusionBuilder> {
    private final List<Ingredient> components = new ArrayList<>();
    private Ingredient catalyst;
    private int instability;

    public InfusionBuilder(RecipeContext context, Object result, Object catalyst) {
        super(context, result);
        LazyIngredient ingredient = LazyIngredient.from(Objects.requireNonNull(catalyst, "catalyst"));
        this.catalyst = ingredient.resolve(context.items());
        defaultUnlock(ingredient);
    }

    @Override
    protected InfusionBuilder self() {
        return this;
    }

    @Override
    protected String defaultFolder() {
        return "infusion";
    }

    public InfusionBuilder catalyst(Object source) {
        this.catalyst = LazyIngredient.from(source).resolve(context().items());
        return this;
    }

    public InfusionBuilder component(Object source) {
        return component(source, 1);
    }

    public InfusionBuilder component(Object source, int count) {
        for (int i = 0; i < count; i++) {
            components.add(LazyIngredient.from(source).resolve(context().items()));
        }
        return this;
    }

    public InfusionBuilder instability(int instability) {
        if (instability < 0) {
            throw new IllegalArgumentException("instability must not be negative");
        }
        this.instability = instability;
        return this;
    }

    @Override
    protected void validate() {
        if (catalyst == null) {
            throw new IllegalStateException("Infusion recipe requires a catalyst");
        }
        if (components.isEmpty()) {
            throw new IllegalStateException("Infusion recipe requires at least one component");
        }
        requireAspects();
    }

    @Override
    protected void build(RecipeOutput output, ResourceLocation id) {
        InfusionRecipe recipe = new InfusionRecipe(
                catalyst, components, buildAspects(), instability, result(), gate());
        output.accept(id, recipe, null);
    }
}
