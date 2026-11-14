package com.blackmoss.lazycat.recipe.mekanism;

import com.blackmoss.lazycat.api.recipe.RecipeContext;
import com.blackmoss.lazycat.api.recipe.mekanism.AbstractMekanismRecipeBuilder;
import mekanism.api.datagen.recipe.builder.ItemStackToItemStackRecipeBuilder;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.ItemLike;

import java.util.Objects;

public final class ItemStackToItemStackBuilder extends AbstractMekanismRecipeBuilder<ItemStackToItemStackBuilder> {
    private Object input;
    private Mode mode;

    public ItemStackToItemStackBuilder(RecipeContext context, ItemLike result) {
        super(context, result);
    }

    @Override
    protected ItemStackToItemStackBuilder self() {
        return this;
    }

    public ItemStackToItemStackBuilder input(Object source) {
        this.input = Objects.requireNonNull(source, "input");
        return this;
    }

    public ItemStackToItemStackBuilder crushing() {
        this.mode = Mode.CRUSHING;
        return this;
    }

    public ItemStackToItemStackBuilder enriching() {
        this.mode = Mode.ENRICHING;
        return this;
    }

    public ItemStackToItemStackBuilder smelting() {
        this.mode = Mode.SMELTING;
        return this;
    }

    @Override
    protected void validate() {
        if (input == null) {
            throw new IllegalStateException("ItemStack to ItemStack recipe requires an input");
        }
    }

    @Override
    protected void build(RecipeOutput output, ResourceKey<Recipe<?>> key) {
        ItemStackToItemStackRecipeBuilder builder = switch (mode) {
            case CRUSHING -> ItemStackToItemStackRecipeBuilder.crushing(itemIngredient(input), result());
            case ENRICHING -> ItemStackToItemStackRecipeBuilder.enriching(itemIngredient(input), result());
            case SMELTING -> ItemStackToItemStackRecipeBuilder.smelting(itemIngredient(input), result());
        };
        builder.save(output, key.identifier());
    }

    public enum Mode {
        CRUSHING,
        ENRICHING,
        SMELTING
    }
}
