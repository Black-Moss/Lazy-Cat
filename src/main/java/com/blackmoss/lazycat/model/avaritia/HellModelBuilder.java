package com.blackmoss.lazycat.model.avaritia;

import com.blackmoss.lazycat.api.model.ModelContext;
import com.blackmoss.lazycat.api.model.avaritia.AbstractEffectModelBuilder;
import net.minecraft.world.item.Item;

@SuppressWarnings("unused")
public final class HellModelBuilder extends AbstractEffectModelBuilder<HellModelBuilder> {
    public HellModelBuilder(ModelContext context, Item item) {
        super(context, item, "hell", "hell");
    }

    @Override
    protected HellModelBuilder self() {
        return this;
    }
}
