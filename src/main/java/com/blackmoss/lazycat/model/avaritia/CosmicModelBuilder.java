package com.blackmoss.lazycat.model.avaritia;

import com.blackmoss.lazycat.api.model.ModelContext;
import com.blackmoss.lazycat.api.model.avaritia.AbstractEffectModelBuilder;
import net.minecraft.world.item.Item;

@SuppressWarnings("unused")
public final class CosmicModelBuilder extends AbstractEffectModelBuilder<CosmicModelBuilder> {
    public CosmicModelBuilder(ModelContext context, Item item) {
        super(context, item, "cosmic", "cosmic");
    }

    @Override
    protected CosmicModelBuilder self() {
        return this;
    }
}
