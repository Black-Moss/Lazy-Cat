package com.blackmoss.lazycat.model.avaritia;

import com.blackmoss.lazycat.api.model.ModelContext;
import com.blackmoss.lazycat.api.model.avaritia.AbstractEffectModelBuilder;
import net.minecraft.world.item.Item;

@SuppressWarnings("unused")
public final class EternalModelBuilder extends AbstractEffectModelBuilder<EternalModelBuilder> {
    public EternalModelBuilder(ModelContext context, Item item) {
        super(context, item, "eternal", "eternal");
    }

    @Override
    protected EternalModelBuilder self() {
        return this;
    }
}
