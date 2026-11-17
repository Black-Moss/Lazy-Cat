package com.blackmoss.lazycat.api.model;

import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;

import java.util.Objects;

public final class ModelContext {
    private final ItemModelGenerators itemModels;
    private final BlockModelGenerators blockModels;

    public ModelContext(ItemModelGenerators itemModels, BlockModelGenerators blockModels) {
        this.itemModels = Objects.requireNonNull(itemModels, "itemModels");
        this.blockModels = Objects.requireNonNull(blockModels, "blockModels");
    }

    public ItemModelGenerators itemModels() {
        return this.itemModels;
    }

    public BlockModelGenerators blockModels() {
        return this.blockModels;
    }
}
