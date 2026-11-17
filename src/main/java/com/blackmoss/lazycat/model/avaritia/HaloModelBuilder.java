package com.blackmoss.lazycat.model.avaritia;

import com.blackmoss.lazycat.api.model.ModelContext;
import com.blackmoss.lazycat.api.model.avaritia.AbstractAvaritiaModelBuilder;
import committee.nova.mods.avaritia.client.model.loader.AvaritiaItemModelLoaders;
import net.minecraft.client.renderer.item.ItemModel;
import net.minecraft.world.item.Item;

public final class HaloModelBuilder extends AbstractAvaritiaModelBuilder<HaloModelBuilder> {
    public HaloModelBuilder(ModelContext context, Item item) {
        super(context, item);
    }

    @Override
    protected HaloModelBuilder self() {
        return this;
    }

    @Override
    protected ItemModel.Unbaked createModel() {
        return new AvaritiaItemModelLoaders.Halo(
                this.model, this.texture, haloColor(), this.size, this.pulse, tintList());
    }
}
