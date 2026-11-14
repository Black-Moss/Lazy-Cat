package com.blackmoss.lazycat.recipe.ae2;

import com.blackmoss.lazycat.api.recipe.AbstractRecipeBuilder;
import com.blackmoss.lazycat.api.recipe.RecipeContext;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.ItemLike;
import appeng.recipes.game.StorageCellUpgradeRecipe;

import java.util.Objects;

public final class StorageCellUpgradeBuilder extends AbstractRecipeBuilder<StorageCellUpgradeBuilder> {
    private final ItemLike inputCell;
    private ItemLike inputComponent;
    private ItemLike resultComponent;

    public StorageCellUpgradeBuilder(RecipeContext context, ItemLike result, ItemLike inputCell) {
        super(context, result);
        this.inputCell = Objects.requireNonNull(inputCell, "inputCell");
    }

    @Override
    protected StorageCellUpgradeBuilder self() {
        return this;
    }

    public StorageCellUpgradeBuilder component(ItemLike component) {
        this.inputComponent = Objects.requireNonNull(component, "component");
        return this;
    }

    public StorageCellUpgradeBuilder resultComponent(ItemLike component) {
        this.resultComponent = Objects.requireNonNull(component, "resultComponent");
        return this;
    }

    @Override
    protected void validate() {
        if (inputComponent == null) {
            throw new IllegalStateException("Storage cell upgrade recipe requires an input component");
        }
        if (resultComponent == null) {
            throw new IllegalStateException("Storage cell upgrade recipe requires a result component");
        }
    }

    @Override
    protected void build(RecipeOutput output, ResourceKey<Recipe<?>> key) {
        output.accept(key, new StorageCellUpgradeRecipe(
                inputCell.asItem(),
                inputComponent.asItem(),
                resultItem().asItem(),
                resultComponent.asItem()), null);
    }
}
