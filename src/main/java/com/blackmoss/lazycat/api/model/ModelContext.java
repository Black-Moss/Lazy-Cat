package com.blackmoss.lazycat.api.model;

import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;

import java.util.Objects;

public record ModelContext(ItemModelGenerators itemModels, BlockModelGenerators blockModels) {
    public ModelContext(ItemModelGenerators itemModels, BlockModelGenerators blockModels) {
        this.itemModels = Objects.requireNonNull(itemModels, "itemModels");
        this.blockModels = Objects.requireNonNull(blockModels, "blockModels");
    }
}
