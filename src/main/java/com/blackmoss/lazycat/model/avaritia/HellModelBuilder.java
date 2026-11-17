package com.blackmoss.lazycat.model.avaritia;

import com.blackmoss.lazycat.api.model.ModelContext;
import com.blackmoss.lazycat.api.model.avaritia.AbstractEffectModelBuilder;
import committee.nova.mods.avaritia.client.model.loader.AvaritiaItemModelLoaders;
import net.minecraft.world.item.Item;

public final class HellModelBuilder extends AbstractEffectModelBuilder<HellModelBuilder> {
    public HellModelBuilder(ModelContext context, Item item) {
        super(context, item, AvaritiaItemModelLoaders.Hell::new);
    }

    @Override
    protected HellModelBuilder self() {
        return this;
    }
}
