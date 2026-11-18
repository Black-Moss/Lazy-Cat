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
    private final Mode mode;

    public ItemStackChemicalToItemStackBuilder(RecipeContext context, Mode mode) {
        super(context, null);
        this.mode = Objects.requireNonNull(mode, "mode");
    }

    @Override
    protected ItemStackChemicalToItemStackBuilder self() {
        return this;
    }

    @Override
    protected String defaultFolder() {
        return folderOf(mode);
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
    }

    @Override
    protected void build(RecipeOutput output, ResourceLocation id) {
        ItemStackChemicalToItemStackRecipeBuilder builder = switch (mode) {
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

    public enum Mode {
        METALLURGIC_INFUSING,
        COMPRESSING,
        PURIFYING,
        INJECTING,
        PAINTING
    }
}