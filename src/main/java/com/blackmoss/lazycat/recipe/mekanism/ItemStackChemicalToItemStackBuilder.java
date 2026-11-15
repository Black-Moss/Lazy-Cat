package com.blackmoss.lazycat.recipe.mekanism;

import com.blackmoss.lazycat.api.recipe.RecipeContext;
import com.blackmoss.lazycat.api.recipe.mekanism.AbstractMekanismRecipeBuilder;
import mekanism.api.datagen.recipe.builder.ItemStackChemicalToItemStackRecipeBuilder;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.ItemLike;

import java.util.Objects;

public final class ItemStackChemicalToItemStackBuilder extends AbstractMekanismRecipeBuilder<ItemStackChemicalToItemStackBuilder> {
    private Object mainItem;
    private Object chemical;
    private int chemicalAmount = 1;
    private boolean perTick;
    private Mode mode;
    public ItemStackChemicalToItemStackBuilder(RecipeContext context, ItemLike result) {
        super(context, result);
    }

    @Override
    protected ItemStackChemicalToItemStackBuilder self() {
        return this;
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

    public ItemStackChemicalToItemStackBuilder metallurgicInfusing() {
        this.mode = Mode.METALLURGIC_INFUSING;
        return this;
    }

    public ItemStackChemicalToItemStackBuilder compressing() {
        this.mode = Mode.COMPRESSING;
        return this;
    }

    public ItemStackChemicalToItemStackBuilder purifying() {
        this.mode = Mode.PURIFYING;
        return this;
    }

    public ItemStackChemicalToItemStackBuilder injecting() {
        this.mode = Mode.INJECTING;
        return this;
    }

    public ItemStackChemicalToItemStackBuilder painting() {
        this.mode = Mode.PAINTING;
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
    protected void build(RecipeOutput output, ResourceKey<Recipe<?>> key) {
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
        builder.save(output, key.identifier());
    }

    public enum Mode {
        METALLURGIC_INFUSING,
        COMPRESSING,
        PURIFYING,
        INJECTING,
        PAINTING
    }
}
