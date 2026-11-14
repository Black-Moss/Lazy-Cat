package com.blackmoss.lazycat.recipe.ae2;

import com.blackmoss.lazycat.api.recipe.AbstractRecipeBuilder;
import com.blackmoss.lazycat.api.recipe.RecipeContext;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import appeng.recipes.game.CraftingUnitTransformRecipe;

import java.util.Objects;

public final class CraftingUnitTransformBuilder extends AbstractRecipeBuilder<CraftingUnitTransformBuilder> {
    private final Block upgradedBlock;
    private ItemLike upgradeItem;

    public CraftingUnitTransformBuilder(RecipeContext context, ItemLike result, Block upgradedBlock) {
        super(context, result);
        this.upgradedBlock = Objects.requireNonNull(upgradedBlock, "upgradedBlock");
    }

    @Override
    protected CraftingUnitTransformBuilder self() {
        return this;
    }

    public CraftingUnitTransformBuilder upgrade(ItemLike upgradeItem) {
        this.upgradeItem = Objects.requireNonNull(upgradeItem, "upgradeItem");
        return this;
    }

    @Override
    protected void validate() {
        if (upgradeItem == null) {
            throw new IllegalStateException("Crafting unit transform recipe requires an upgrade item");
        }
    }

    @Override
    protected void build(RecipeOutput output, ResourceKey<Recipe<?>> key) {
        output.accept(key, new CraftingUnitTransformRecipe(upgradedBlock, upgradeItem.asItem()), null);
    }
}
