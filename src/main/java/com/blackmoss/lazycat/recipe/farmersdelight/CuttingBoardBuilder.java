package com.blackmoss.lazycat.recipe.farmersdelight;

import com.blackmoss.lazycat.api.recipe.AbstractRecipeBuilder;
import com.blackmoss.lazycat.api.recipe.RecipeContext;
import net.minecraft.core.NonNullList;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.ItemLike;
import vectorwing.farmersdelight.common.crafting.CuttingBoardRecipe;
import vectorwing.farmersdelight.common.crafting.ingredient.ChanceResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public final class CuttingBoardBuilder extends AbstractRecipeBuilder<CuttingBoardBuilder> {
    private Object primarySource;
    private Object toolSource;
    private SoundEvent sound;
    private final List<Extra> extras = new ArrayList<>();

    public CuttingBoardBuilder(RecipeContext context, ItemLike result) {
        super(context, result);
    }

    @Override
    protected CuttingBoardBuilder self() {
        return this;
    }

    public CuttingBoardBuilder input(Object source) {
        this.primarySource = Objects.requireNonNull(source, "ingredient");
        return this;
    }

    public CuttingBoardBuilder tool(Object source) {
        this.toolSource = Objects.requireNonNull(source, "tool");
        return this;
    }

    public CuttingBoardBuilder addResult(ItemLike result) {
        return addResult(result, 1);
    }

    public CuttingBoardBuilder addResult(ItemLike result, int count) {
        extras.add(new Extra(result, count, 1f));
        return this;
    }

    public CuttingBoardBuilder addResultWithChance(ItemLike result, float chance) {
        return addResultWithChance(result, chance, 1);
    }

    public CuttingBoardBuilder addResultWithChance(ItemLike result, float chance, int count) {
        extras.add(new Extra(result, count, chance));
        return this;
    }

    public CuttingBoardBuilder sound(SoundEvent sound) {
        this.sound = sound;
        return this;
    }

    @Override
    protected void validate() {
        if (primarySource == null) {
            throw new IllegalStateException("Cutting board recipe requires an ingredient");
        }
        if (toolSource == null) {
            throw new IllegalStateException("Cutting board recipe requires a tool");
        }
    }

    @Override
    protected void build(RecipeOutput output, ResourceKey<Recipe<?>> key) {
        NonNullList<ChanceResult> results = NonNullList.create();
        results.add(new ChanceResult(new ItemStack(resultItem(), count()), 1f));
        for (Extra extra : extras) {
            results.add(new ChanceResult(new ItemStack(extra.item(), extra.count()), extra.chance()));
        }
        Optional<SoundEvent> soundEvent = sound != null ? Optional.of(sound) : Optional.empty();
        String grp = group() != null ? group() : "";
        CuttingBoardRecipe recipe = new CuttingBoardRecipe(
                grp, toIngredient(primarySource), toIngredient(toolSource), results, soundEvent);
        output.accept(key, recipe, null);
    }

    private record Extra(ItemLike item, int count, float chance) {
    }
}
