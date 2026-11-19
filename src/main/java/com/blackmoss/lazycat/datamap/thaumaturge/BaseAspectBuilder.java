package com.blackmoss.lazycat.datamap.thaumaturge;

import com.blackmoss.lazycat.api.datamap.AbstractDataMapBuilder;
import com.blackmoss.lazycat.api.datamap.DataMapContext;
import com.blackmoss.lazycat.api.datamap.DataMapTargets;
import com.leclowndu93150.thaumaturge.api.aspect.AspectDataMaps;
import com.leclowndu93150.thaumaturge.api.aspect.AspectList;
import com.leclowndu93150.thaumaturge.api.aspect.IAspect;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.common.data.DataMapProvider;

import java.util.Objects;
import java.util.function.BiConsumer;

@SuppressWarnings("unused")
public final class BaseAspectBuilder extends AbstractDataMapBuilder<Item, AspectList> {
    private AspectList aspects = AspectList.EMPTY;

    public BaseAspectBuilder(DataMapContext context, Item item) {
        this(context, DataMapTargets.entry(BuiltInRegistries.ITEM, Objects.requireNonNull(item, "item")));
    }

    public BaseAspectBuilder(DataMapContext context, TagKey<Item> tag) {
        this(context, DataMapTargets.tag(Objects.requireNonNull(tag, "tag")));
    }

    private BaseAspectBuilder(
            DataMapContext context,
            BiConsumer<DataMapProvider.Builder<AspectList, Item>, AspectList> target) {
        super(context, AspectDataMaps.BASE_ASPECTS, target);
    }

    public BaseAspectBuilder aspect(ResourceKey<IAspect> aspect, int amount) {
        Objects.requireNonNull(aspect, "aspect");
        if (amount <= 0) {
            throw new IllegalArgumentException("aspect amount must be positive");
        }
        Holder<IAspect> holder = context().registries().lookupOrThrow(IAspect.REGISTRY_KEY).getOrThrow(aspect);
        this.aspects = this.aspects.add(holder, amount);
        return this;
    }

    @Override
    protected AspectList build() {
        if (this.aspects.isEmpty()) {
            throw new IllegalStateException("a base aspect entry needs at least one aspect");
        }
        return this.aspects;
    }
}
