package com.blackmoss.lazycat.model.avaritia;

import com.blackmoss.lazycat.api.model.ModelContext;
import com.blackmoss.lazycat.api.model.avaritia.AbstractEffectModelBuilder;
import net.minecraft.world.item.Item;

@SuppressWarnings("unused")
public final class CosmicArcModelBuilder extends AbstractEffectModelBuilder<CosmicArcModelBuilder> {
    public CosmicArcModelBuilder(ModelContext context, Item item) {
        super(context, item, "cosmic_arc", "cosmic");
    }

    @Override
    protected CosmicArcModelBuilder self() {
        return this;
    }
}
