package com.blackmoss.lazycat.recipe.mekanism;

import com.blackmoss.lazycat.api.recipe.RecipeContext;
import com.blackmoss.lazycat.api.recipe.mekanism.AbstractMekanismRecipeBuilder;
import com.blackmoss.lazycat.api.recipe.mekanism.ItemStackChemicalToItemStackType;
import mekanism.api.datagen.recipe.builder.ItemStackChemicalToItemStackRecipeBuilder;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.crafting.Recipe;

import java.util.Objects;

@SuppressWarnings("unused")
public final class ItemStackChemicalToItemStackBuilder extends AbstractMekanismRecipeBuilder<ItemStackChemicalToItemStackBuilder> {
    private final ItemStackChemicalToItemStackType type;
    private Object mainItem;
    private Object chemical;
    private int chemicalAmount = 1;
    private boolean perTick;

    public ItemStackChemicalToItemStackBuilder(RecipeContext context, Object result, ItemStackChemicalToItemStackType type) {
        super(context, result);
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
            throw new IllegalStateException("requires a rotary mode");
        }
    }

    @Override
    protected void build(RecipeOutput output, ResourceKey<Recipe<?>> key) {
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
        builder.save(output, key.identifier());
    }
}
