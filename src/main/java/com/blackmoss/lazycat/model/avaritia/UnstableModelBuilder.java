package com.blackmoss.lazycat.model.avaritia;

import com.blackmoss.lazycat.api.model.ModelContext;
import com.blackmoss.lazycat.api.model.avaritia.AbstractEffectModelBuilder;
import net.minecraft.world.item.Item;

@SuppressWarnings("unused")
public final class UnstableModelBuilder extends AbstractEffectModelBuilder<UnstableModelBuilder> {
    public UnstableModelBuilder(ModelContext context, Item item) {
        super(context, item, "unstable", "unstable");
    }

    @Override
    protected UnstableModelBuilder self() {
        return this;
    }
}
