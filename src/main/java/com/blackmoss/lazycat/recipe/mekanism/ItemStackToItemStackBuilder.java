package com.blackmoss.lazycat.recipe.mekanism;

import com.blackmoss.lazycat.api.recipe.RecipeContext;
import com.blackmoss.lazycat.api.recipe.mekanism.AbstractMekanismRecipeBuilder;
import mekanism.api.datagen.recipe.builder.ItemStackToItemStackRecipeBuilder;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;

import java.util.Objects;

@SuppressWarnings("unused")
public final class ItemStackToItemStackBuilder extends AbstractMekanismRecipeBuilder<ItemStackToItemStackBuilder> {
    private final ItemStackToItemStackType type;
    private Object input;

    public ItemStackToItemStackBuilder(RecipeContext context, Object result, ItemStackToItemStackType type) {
        super(context, result);
        this.type = Objects.requireNonNull(type, "type");
    }

    @Override
    protected ItemStackToItemStackBuilder self() {
        return this;
    }

    @Override
    protected String defaultFolder() {
        return folderOf(type);
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
        if (type == null) {
            throw new IllegalStateException("requires a type");
        }
    }

    @Override
    protected void build(RecipeOutput output, ResourceLocation id) {
        ItemStackToItemStackRecipeBuilder builder = switch (type) {
            case CRUSHING -> ItemStackToItemStackRecipeBuilder.crushing(itemIngredient(input), result());
            case ENRICHING -> ItemStackToItemStackRecipeBuilder.enriching(itemIngredient(input), result());
            case SMELTING -> ItemStackToItemStackRecipeBuilder.smelting(itemIngredient(input), result());
        };
        builder.build(output, id);
    }

    public enum ItemStackToItemStackType {
        CRUSHING,
        ENRICHING,
        SMELTING
    }
}