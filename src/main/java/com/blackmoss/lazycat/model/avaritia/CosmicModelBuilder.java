package com.blackmoss.lazycat.model.avaritia;

import com.blackmoss.lazycat.api.model.ModelContext;
import com.blackmoss.lazycat.api.model.avaritia.AbstractEffectModelBuilder;
import committee.nova.mods.avaritia.client.model.loader.AvaritiaItemModelLoaders;
import net.minecraft.world.item.Item;

public final class CosmicModelBuilder extends AbstractEffectModelBuilder<CosmicModelBuilder> {
    public CosmicModelBuilder(ModelContext context, Item item) {
        super(context, item, AvaritiaItemModelLoaders.Cosmic::new);
    }

    @Override
    protected CosmicModelBuilder self() {
        return this;
    }
}
