package com.blackmoss.lazycat.recipe.mekanism;

import com.blackmoss.lazycat.api.recipe.RecipeContext;
import com.blackmoss.lazycat.api.recipe.mekanism.AbstractMekanismRecipeBuilder;
import mekanism.api.datagen.recipe.builder.ItemStackChemicalToItemStackRecipeBuilder;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.ItemLike;

import java.util.Objects;

public final class MetallurgicInfusingBuilder extends AbstractMekanismRecipeBuilder<MetallurgicInfusingBuilder> {
    private Object mainItem;
    private Object chemical;
    private int chemicalAmount = 1;
    private boolean perTick;

    public MetallurgicInfusingBuilder(RecipeContext context, ItemLike result) {
        super(context, result);
    }

    @Override
    protected MetallurgicInfusingBuilder self() {
        return this;
    }

    public MetallurgicInfusingBuilder mainItem(Object source) {
        this.mainItem = Objects.requireNonNull(source, "mainItem");
        return this;
    }

    public MetallurgicInfusingBuilder chemical(Object source) {
        return chemical(source, chemicalAmount);
    }

    public MetallurgicInfusingBuilder chemical(Object source, int amount) {
        this.chemical = Objects.requireNonNull(source, "chemical");
        this.chemicalAmount = amount;
        return this;
    }

    public MetallurgicInfusingBuilder perTick(boolean perTick) {
        this.perTick = perTick;
        return this;
    }

    @Override
    protected void validate() {
        if (mainItem == null) {
            throw new IllegalStateException("Metallurgic infusing requires a mainItem item input");
        }
        if (chemical == null) {
            throw new IllegalStateException("Metallurgic infusing requires a chemical input");
        }
    }

    @Override
    protected void build(RecipeOutput output, ResourceKey<Recipe<?>> key) {
        ItemStackChemicalToItemStackRecipeBuilder.metallurgicInfusing(
                        itemIngredient(mainItem), chemicalIngredient(chemical, chemicalAmount), result(), perTick)
                .save(output, key.identifier());
    }
}
