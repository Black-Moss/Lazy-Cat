package com.blackmoss.lazycat.recipe.minecraft;

import com.blackmoss.lazycat.api.LazyIngredient;
import com.blackmoss.lazycat.api.recipe.minecraft.AbstractRecipeBuilder;
import com.blackmoss.lazycat.api.recipe.RecipeContext;
import net.minecraft.advancements.criterion.MinMaxBounds;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.TransmuteRecipeBuilder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.ItemLike;

import java.util.Objects;

@SuppressWarnings("unused")
public final class TransmuteBuilder extends AbstractRecipeBuilder<TransmuteBuilder> {
    private LazyIngredient input;
    private LazyIngredient material;
    private MinMaxBounds.Ints materialCount;
    private boolean addMaterialCountToOutput;

    public TransmuteBuilder(RecipeContext context, ItemLike result) {
        super(context, result);
    }

    @Override
    protected TransmuteBuilder self() {
        return this;
    }

    public TransmuteBuilder input(Object source) {
        input = LazyIngredient.from(source);
        defaultUnlock(input);
        return this;
    }

    public TransmuteBuilder material(Object source) {
        material = LazyIngredient.from(source);
        return this;
    }

    public TransmuteBuilder materialCount(MinMaxBounds.Ints count) {
        materialCount = Objects.requireNonNull(count, "count");
        return this;
    }

    public TransmuteBuilder addMaterialCountToOutput() {
        addMaterialCountToOutput = true;
        return this;
    }

    @Override
    protected void validate() {
        if (input == null || material == null) {
            throw new IllegalStateException("Transmute recipe %s requires input and material".formatted(defaultIdPath()));
        }
    }

    @Override
    protected void build(RecipeOutput output, ResourceKey<Recipe<?>> key) {
        TransmuteRecipeBuilder builder = TransmuteRecipeBuilder.transmute(
                category(),
                input.resolve(context().items()),
                material.resolve(context().items()),
                result());
        builder.group(group());
        if (materialCount != null) {
            builder.setMaterialCount(materialCount);
        }
        if (addMaterialCountToOutput) {
            builder.addMaterialCountToOutput();
        }
        applyCriteria(builder::unlockedBy);
        builder.save(output, key);
    }
}
