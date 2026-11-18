package com.blackmoss.lazycat.recipe.mekanism;

import com.blackmoss.lazycat.api.recipe.RecipeContext;
import com.blackmoss.lazycat.api.recipe.mekanism.AbstractMekanismRecipeBuilder;
import mekanism.api.datagen.recipe.builder.ItemStackChemicalToItemStackRecipeBuilder;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;

import java.util.Objects;

@SuppressWarnings("unused")
public final class ItemStackChemicalToItemStackBuilder
        extends AbstractMekanismRecipeBuilder<ItemStackChemicalToItemStackBuilder> {
    private Object mainItem;
    private Object chemical;
    private int chemicalAmount = 1;
    private boolean perTick;
    private final ItemStackChemicalToItemStackType type;

    public ItemStackChemicalToItemStackBuilder(RecipeContext context, ItemStackChemicalToItemStackType type) {
        super(context, null);
        this.type = Objects.requireNonNull(type, "type");
    }

    @Override
    protected ItemStackChemicalToItemStackBuilder self() {
        return this;
    }

    @Override
    protected String defaultFolder() {
        return folderOf(type);
    }

    @Override
    protected String defaultIdPath() {
        return sourcePath(mainItem, "mainItem");
    }

    public ItemStackChemicalToItemStackBuilder mainItem(Object source) {
        this.mainItem = Objects.requireNonNull(source, "mainItem");
        return this;
    }

    public ItemStackChemicalToItemStackBuilder chemical(Object source) {
        return chemical(source, chemicalAmount);
    }

    public ItemStackChemicalToItemStackBuilder chemical(Object source, int amount) {
        this.chemical = Objects.requireNonNull(source, "chemical");
        this.chemicalAmount = amount;
        return this;
    }

    public ItemStackChemicalToItemStackBuilder perTick(boolean value) {
        this.perTick = value;
        return this;
    }

    @Override
    protected void validate() {
        if (mainItem == null) {
            throw new IllegalStateException("requires a main item input");
        }
        if (chemical == null) {
            throw new IllegalStateException("requires a chemical input");
        }
        if (type == null) {
            throw new IllegalStateException("requires a type");
        }
    }

    @Override
    protected void build(RecipeOutput output, ResourceLocation id) {
        ItemStackChemicalToItemStackRecipeBuilder builder = switch (type) {
            case METALLURGIC_INFUSING -> ItemStackChemicalToItemStackRecipeBuilder.metallurgicInfusing(
                    itemIngredient(mainItem), chemicalIngredient(chemical, chemicalAmount), result(), perTick);
            case COMPRESSING -> ItemStackChemicalToItemStackRecipeBuilder.compressing(
                    itemIngredient(mainItem), chemicalIngredient(chemical, chemicalAmount), result(), perTick);
            case PURIFYING -> ItemStackChemicalToItemStackRecipeBuilder.purifying(
                    itemIngredient(mainItem), chemicalIngredient(chemical, chemicalAmount), result(), perTick);
            case INJECTING -> ItemStackChemicalToItemStackRecipeBuilder.injecting(
                    itemIngredient(mainItem), chemicalIngredient(chemical, chemicalAmount), result(), perTick);
            case PAINTING -> ItemStackChemicalToItemStackRecipeBuilder.painting(
                    itemIngredient(mainItem), chemicalIngredient(chemical, chemicalAmount), result(), perTick);
        };
        builder.build(output, id);
    }

    public enum ItemStackChemicalToItemStackType {
        METALLURGIC_INFUSING,
        COMPRESSING,
        PURIFYING,
        INJECTING,
        PAINTING
    }
}