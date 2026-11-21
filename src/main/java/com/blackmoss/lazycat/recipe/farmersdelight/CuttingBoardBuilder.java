package com.blackmoss.lazycat.recipe.farmersdelight;

import com.blackmoss.lazycat.api.LazyIngredient;
import com.blackmoss.lazycat.api.recipe.AbstractRecipeBuilder;
import com.blackmoss.lazycat.api.recipe.RecipeContext;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.level.ItemLike;
import vectorwing.farmersdelight.common.tag.CommonTags;
import vectorwing.farmersdelight.data.builder.CuttingBoardRecipeBuilder;
import vectorwing.farmersdelight.data.builder.CuttingRecipeFolder;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@SuppressWarnings("unused")
public final class CuttingBoardBuilder extends AbstractRecipeBuilder<CuttingBoardBuilder> {
    private final List<Extra> extras = new ArrayList<>();
    private final int primaryCount;
    private Object primarySource;
    private Object tool = CommonTags.Items.TOOLS_KNIFE;
    private SoundEvent sound;
    private CuttingRecipeFolder folder = CuttingRecipeFolder.CUTTING;

    public CuttingBoardBuilder(RecipeContext context, Object result) {
        this(context, result, 1);
    }

    public CuttingBoardBuilder(RecipeContext context, Object result, int count) {
        super(context, result);
        if (count < 1 || count > 64) {
            throw new IllegalArgumentException("count must be between 1 and 64");
        }
        this.primaryCount = count;
    }

    @Override
    protected CuttingBoardBuilder self() {
        return this;
    }

    @Override
    public CuttingBoardBuilder count(int count) {
        throw new UnsupportedOperationException(
                "Cutting Board results carry their own count, use CuttingBoardBuilder(context, result, count)");
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
        this.tool = Objects.requireNonNull(source, "tool");
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
        if (tool == null) {
            throw new IllegalStateException("Cutting board recipe requires a tool");
        }
    }

    @Override
    protected void build(RecipeOutput output, ResourceLocation id) {
        CuttingBoardRecipeBuilder builder = CuttingBoardRecipeBuilder.cuttingRecipe(
                LazyIngredient.from(primarySource).resolve(context().items()),
                LazyIngredient.from(tool).resolve(context().items()),
                resultItem(),
                this.primaryCount);
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