package com.blackmoss.lazycat.recipe.farmersdelight;

import com.blackmoss.lazycat.api.LazyIngredient;
import com.blackmoss.lazycat.api.recipe.AbstractRecipeBuilder;
import com.blackmoss.lazycat.api.recipe.RecipeContext;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.level.ItemLike;
import vectorwing.farmersdelight.data.builder.CuttingBoardRecipeBuilder;
import vectorwing.farmersdelight.data.builder.CuttingRecipeFolder;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@SuppressWarnings("unused")
public final class CuttingBoardBuilder extends AbstractRecipeBuilder<CuttingBoardBuilder> {
    private Object primarySource;
    private Object toolSource;
    private final List<Extra> extras = new ArrayList<>();
    private SoundEvent sound;
    private CuttingRecipeFolder folder = CuttingRecipeFolder.CUTTING;

    public CuttingBoardBuilder(RecipeContext context, Object result) {
        super(context, result);
    }

    @Override
    protected CuttingBoardBuilder self() {
        return this;
    }

    @Override
    protected String defaultFolder() {
        return folder.getSerializedName();
    }

    public CuttingBoardBuilder input(Object source) {
        this.primarySource = Objects.requireNonNull(source, "ingredient");
        return this;
    }

    public CuttingBoardBuilder tool(Object source) {
        this.toolSource = Objects.requireNonNull(source, "tool");
        return this;
    }

    public CuttingBoardBuilder addResult(Object source) {
        return addResult(source, 1);
    }

    public CuttingBoardBuilder addResult(Object source, int count) {
        return addResultWithChance(source, 1f, count);
    }

    public CuttingBoardBuilder addResultWithChance(Object source, float chance) {
        return addResultWithChance(source, chance, 1);
    }

    public CuttingBoardBuilder addResultWithChance(Object source, float chance, int count) {
        ItemLike item = LazyIngredient.from(source).stack(context().items()).getItem();
        extras.add(new Extra(item, count, chance));
        return this;
    }

    public CuttingBoardBuilder sound(SoundEvent sound) {
        this.sound = sound;
        return this;
    }

    public CuttingBoardBuilder salvaging() {
        this.folder = CuttingRecipeFolder.SALVAGING;
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
    protected void build(RecipeOutput output, ResourceLocation id) {
        CuttingBoardRecipeBuilder builder = CuttingBoardRecipeBuilder.cuttingRecipe(
                LazyIngredient.from(primarySource).resolve(context().items()),
                LazyIngredient.from(toolSource).resolve(context().items()),
                resultItem());
        for (Extra extra : extras) {
            builder.addResultWithChance(extra.item(), extra.chance(), extra.count());
        }
        if (sound != null) {
            builder.addSound(sound);
        }
        builder.group(group());
        applyCriteria(builder::unlockedBy);
        builder.save(output, id);
    }

    private record Extra(ItemLike item, int count, float chance) {
    }
}