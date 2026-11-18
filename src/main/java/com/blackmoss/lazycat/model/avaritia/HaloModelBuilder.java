package com.blackmoss.lazycat.model.avaritia;

import com.blackmoss.lazycat.api.model.ModelContext;
import com.blackmoss.lazycat.api.model.avaritia.AbstractAvaritiaModelBuilder;
import com.google.gson.JsonObject;
import net.minecraft.world.item.Item;

@SuppressWarnings("unused")
public final class HaloModelBuilder extends AbstractAvaritiaModelBuilder<HaloModelBuilder> {
    public HaloModelBuilder(ModelContext context, Item item) {
        super(context, item);
    }

    @Override
    protected HaloModelBuilder self() {
        return this;
    }

    @Override
    protected void createModel(JsonObject json) {
        writeLoader(json, "halo");
        writeHalo(json);
    }
}
