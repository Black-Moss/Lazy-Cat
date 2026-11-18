package com.blackmoss.lazycat.recipe.mekanism;

import com.blackmoss.lazycat.api.recipe.RecipeContext;
import com.blackmoss.lazycat.api.recipe.mekanism.AbstractMekanismRecipeBuilder;
import mekanism.api.datagen.recipe.builder.ItemStackToItemStackRecipeBuilder;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;

import java.util.Objects;

@SuppressWarnings("unused")
public final class ItemStackToItemStackBuilder extends AbstractMekanismRecipeBuilder<ItemStackToItemStackBuilder> {
    private Object input;
    private final Mode mode;

    public ItemStackToItemStackBuilder(RecipeContext context, Object result, Mode mode) {
        super(context, result);
        this.mode = Objects.requireNonNull(mode, "mode");
    }

    @Override
    protected ItemStackToItemStackBuilder self() {
        return this;
    }

    @Override
    protected String defaultFolder() {
        return folderOf(mode);
    }

    @Override
    protected String defaultIdPath() {
        return sourcePath(input, "input");
    }

    public ItemStackToItemStackBuilder input(Object source) {
        this.input = Objects.requireNonNull(source, "input");
        return this;
    }

    @Override
    protected void validate() {
        if (input == null) {
            throw new IllegalStateException("requires an item input");
        }
        if (mode == null) {
            throw new IllegalStateException("requires a mode");
        }
    }

    @Override
    protected void build(RecipeOutput output, ResourceLocation id) {
        ItemStackToItemStackRecipeBuilder builder = switch (mode) {
            case CRUSHING -> ItemStackToItemStackRecipeBuilder.crushing(itemIngredient(input), result());
            case ENRICHING -> ItemStackToItemStackRecipeBuilder.enriching(itemIngredient(input), result());
            case SMELTING -> ItemStackToItemStackRecipeBuilder.smelting(itemIngredient(input), result());
        };
        builder.build(output, id);
    }

    public enum Mode {
        CRUSHING,
        ENRICHING,
        SMELTING
    }
}