package com.blackmoss.lazycat.model.avaritia;

import com.blackmoss.lazycat.api.model.ModelContext;
import com.blackmoss.lazycat.api.model.avaritia.AbstractEffectModelBuilder;
import committee.nova.mods.avaritia.client.model.loader.AvaritiaItemModelLoaders;
import net.minecraft.world.item.Item;

public final class CosmicArcModelBuilder extends AbstractEffectModelBuilder<CosmicArcModelBuilder> {
    public CosmicArcModelBuilder(ModelContext context, Item item) {
        super(context, item, AvaritiaItemModelLoaders.CosmicArc::new);
    }

    @Override
    protected CosmicArcModelBuilder self() {
        return this;
    }
}
