package com.blackmoss.lazycat.recipe.farmersdelight;

import com.blackmoss.lazycat.api.LazyIngredient;
import com.blackmoss.lazycat.api.recipe.AbstractRecipeBuilder;
import com.blackmoss.lazycat.api.recipe.RecipeContext;
import net.minecraft.core.NonNullList;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.crafting.Recipe;
import vectorwing.farmersdelight.common.crafting.CuttingBoardRecipe;
import vectorwing.farmersdelight.common.crafting.ingredient.ChanceResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@SuppressWarnings("unused")
public final class CuttingBoardBuilder extends AbstractRecipeBuilder<CuttingBoardBuilder> {
    private final List<Extra> extras = new ArrayList<>();
    private Object primarySource;
    private Object toolSource;
    private SoundEvent sound;

    public CuttingBoardBuilder(RecipeContext context, Object result) {
        super(context, result);
    }

    @Override
    protected CuttingBoardBuilder self() {
        return this;
    }

    @Override
    protected String defaultFolder() {
        return "cutting";
    }

    public CuttingBoardBuilder input(Object source) {
        this.primarySource = Objects.requireNonNull(source, "ingredient");
        return this;
    }

    public CuttingBoardBuilder tool(Object source) {
        this.toolSource = Objects.requireNonNull(source, "tool");
        return this;
    }

    public CuttingBoardBuilder addResult(Object result) {
        return addResult(result, 1);
    }

    public CuttingBoardBuilder addResult(Object result, int count) {
        extras.add(new Extra(LazyIngredient.from(result), count, 1f));
        return this;
    }

    public CuttingBoardBuilder addResultWithChance(Object result, float chance) {
        return addResultWithChance(result, chance, 1);
    }

    public CuttingBoardBuilder addResultWithChance(Object result, float chance, int count) {
        extras.add(new Extra(LazyIngredient.from(result), count, chance));
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
        results.add(new ChanceResult(resultIngredient().stack(context().items()), 1f));
        for (Extra extra : extras) {
            results.add(new ChanceResult(
                    extra.item().count(extra.count()).stack(context().items()), extra.chance()));
        }
        Optional<SoundEvent> soundEvent = sound != null ? Optional.of(sound) : Optional.empty();
        String grp = group() != null ? group() : "";
        CuttingBoardRecipe recipe = new CuttingBoardRecipe(
                grp, toIngredient(primarySource), toIngredient(toolSource), results, soundEvent);
        output.accept(key, recipe, null);
    }

    private record Extra(com.blackmoss.lazycat.api.LazyIngredient item, int count, float chance) {
    }
}
