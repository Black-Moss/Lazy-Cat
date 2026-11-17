package com.blackmoss.lazycat.model.avaritia;

import com.blackmoss.lazycat.api.model.ModelContext;
import com.blackmoss.lazycat.api.model.avaritia.AbstractAvaritiaModelBuilder;
import committee.nova.mods.avaritia.client.model.loader.AvaritiaItemModelLoaders;
import net.minecraft.client.renderer.item.ItemModel;
import net.minecraft.world.item.Item;

public final class HaloCosmicModelBuilder extends AbstractAvaritiaModelBuilder<HaloCosmicModelBuilder> {
    public HaloCosmicModelBuilder(ModelContext context, Item item) {
        super(context, item);
    }

    @Override
    protected HaloCosmicModelBuilder self() {
        return this;
    }

    @Override
    protected ItemModel.Unbaked createModel() {
        return new AvaritiaItemModelLoaders.HaloCosmic(
                this.model, maskList(), this.texture, haloColor(), this.size, this.pulse, tintList());
    }
}
