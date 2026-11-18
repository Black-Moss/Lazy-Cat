package com.blackmoss.lazycat.api.model.avaritia;

import com.blackmoss.lazycat.api.model.ModelContext;
import com.google.gson.JsonObject;
import committee.nova.mods.avaritia.Const;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

import java.util.Objects;

public abstract class AbstractEffectModelBuilder<B extends AbstractEffectModelBuilder<B>>
        extends AbstractAvaritiaModelBuilder<B> {
    private final ResourceLocation loader;
    private final String maskKey;

    protected AbstractEffectModelBuilder(ModelContext context, Item item, String loader, String maskKey) {
        super(context, item);
        this.loader = Objects.requireNonNull(Const.rl(loader), "loader");
        this.maskKey = Objects.requireNonNull(maskKey, "maskKey");
    }

    @Override
    protected final void createModel(JsonObject json) {
        json.addProperty("loader", this.loader.toString());
        writeMasks(json, this.maskKey);
    }
}
