package com.blackmoss.lazycat.recipe.mekanism;

import com.blackmoss.lazycat.api.recipe.RecipeContext;
import com.blackmoss.lazycat.api.recipe.mekanism.AbstractMekanismRecipeBuilder;
import com.blackmoss.lazycat.api.recipe.mekanism.ItemStackToItemStackType;
import mekanism.api.datagen.recipe.builder.ItemStackToItemStackRecipeBuilder;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.crafting.Recipe;

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

    public ItemStackToItemStackBuilder input(Object source) {
        this.input = Objects.requireNonNull(source, "input");
        return this;
    }

    @Override
    protected void validate() {
        if (input == null) {
            throw new IllegalStateException("ItemStack to ItemStack recipe requires an input");
        }
        if (type == null) {
            throw new IllegalStateException("requires a rotary mode");
        }
    }

    @Override
    protected void build(RecipeOutput output, ResourceKey<Recipe<?>> key) {
        ItemStackToItemStackRecipeBuilder builder = switch (type) {
            case CRUSHING -> ItemStackToItemStackRecipeBuilder.crushing(itemIngredient(input), result());
            case ENRICHING -> ItemStackToItemStackRecipeBuilder.enriching(itemIngredient(input), result());
            case SMELTING -> ItemStackToItemStackRecipeBuilder.smelting(itemIngredient(input), result());
        };
        builder.save(output, key.identifier());
    }
}
