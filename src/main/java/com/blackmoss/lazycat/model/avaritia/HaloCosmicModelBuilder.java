package com.blackmoss.lazycat.model.avaritia;

import com.blackmoss.lazycat.api.model.ModelContext;
import com.blackmoss.lazycat.api.model.avaritia.AbstractAvaritiaModelBuilder;
import com.google.gson.JsonObject;
import net.minecraft.world.item.Item;

@SuppressWarnings("unused")
public final class HaloCosmicModelBuilder extends AbstractAvaritiaModelBuilder<HaloCosmicModelBuilder> {
    public HaloCosmicModelBuilder(ModelContext context, Item item) {
        super(context, item);
    }

    @Override
    protected HaloCosmicModelBuilder self() {
        return this;
    }

    @Override
    protected void createModel(JsonObject json) {
        writeLoader(json, "halo_cosmic");
        writeHalo(json);
        writeMasks(json, "cosmic");
    }
}
