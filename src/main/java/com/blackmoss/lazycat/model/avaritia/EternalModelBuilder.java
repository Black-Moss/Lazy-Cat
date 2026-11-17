package com.blackmoss.lazycat.model.avaritia;

import com.blackmoss.lazycat.api.model.ModelContext;
import com.blackmoss.lazycat.api.model.avaritia.AbstractEffectModelBuilder;
import committee.nova.mods.avaritia.client.model.loader.AvaritiaItemModelLoaders;
import net.minecraft.world.item.Item;

public final class EternalModelBuilder extends AbstractEffectModelBuilder<EternalModelBuilder> {
    public EternalModelBuilder(ModelContext context, Item item) {
        super(context, item, AvaritiaItemModelLoaders.Eternal::new);
    }

    @Override
    protected EternalModelBuilder self() {
        return this;
    }
}
