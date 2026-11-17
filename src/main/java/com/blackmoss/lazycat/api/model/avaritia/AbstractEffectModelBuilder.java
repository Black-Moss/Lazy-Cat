package com.blackmoss.lazycat.api.model.avaritia;

import com.blackmoss.lazycat.api.model.ModelContext;
import net.minecraft.client.renderer.item.ItemModel;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;

import java.util.List;
import java.util.Objects;
import java.util.function.BiFunction;

public abstract class AbstractEffectModelBuilder<B extends AbstractEffectModelBuilder<B>>
        extends AbstractAvaritiaModelBuilder<B> {
    private final BiFunction<Identifier, List<Identifier>, ItemModel.Unbaked> factory;

    protected AbstractEffectModelBuilder(
            ModelContext context,
            Item item,
            BiFunction<Identifier, List<Identifier>, ItemModel.Unbaked> factory) {
        super(context, item);
        this.factory = Objects.requireNonNull(factory, "factory");
    }

    @Override
    protected final ItemModel.Unbaked createModel() {
        return this.factory.apply(this.model, maskList());
    }
}
