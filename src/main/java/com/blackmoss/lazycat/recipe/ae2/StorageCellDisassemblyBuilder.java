package com.blackmoss.lazycat.recipe.ae2;

import com.blackmoss.lazycat.api.recipe.AbstractRecipeBuilder;
import com.blackmoss.lazycat.api.recipe.RecipeContext;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.ItemLike;
import appeng.recipes.game.StorageCellDisassemblyRecipe;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class StorageCellDisassemblyBuilder extends AbstractRecipeBuilder<StorageCellDisassemblyBuilder> {
    private final Item storageCell;
    private final List<ItemStackTemplate> drops = new ArrayList<>();

    public StorageCellDisassemblyBuilder(RecipeContext context, ItemLike result, Item storageCell) {
        super(context, result);
        this.storageCell = Objects.requireNonNull(storageCell, "storageCell");
        drops.add(result());
    }

    @Override
    protected StorageCellDisassemblyBuilder self() {
        return this;
    }

    public StorageCellDisassemblyBuilder addDrop(ItemLike item, int count) {
        drops.add(new ItemStackTemplate(item.asItem(), count));
        return this;
    }

    @Override
    protected void build(RecipeOutput output, ResourceKey<Recipe<?>> key) {
        output.accept(key, new StorageCellDisassemblyRecipe(storageCell, drops), null);
    }
}
