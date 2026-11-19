package com.blackmoss.lazycat.datamap.neoforge;

import com.blackmoss.lazycat.api.LazyIdentifier;
import com.blackmoss.lazycat.api.datamap.AbstractDataMapBuilder;
import com.blackmoss.lazycat.api.datamap.DataMapContext;
import com.blackmoss.lazycat.api.datamap.DataMapTargets;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.common.data.DataMapProvider;
import net.neoforged.neoforge.registries.datamaps.builtin.FurnaceFuel;
import net.neoforged.neoforge.registries.datamaps.builtin.NeoForgeDataMaps;

import java.util.Objects;
import java.util.function.BiConsumer;

@SuppressWarnings("unused")
public final class FurnaceFuelBuilder extends AbstractDataMapBuilder<Item, FurnaceFuel> {
    private Integer burnTime;

    public FurnaceFuelBuilder(DataMapContext context, Item item) {
        this(context, DataMapTargets.entry(LazyIdentifier.getItemIdentifier(item)));
    }

    public FurnaceFuelBuilder(DataMapContext context, TagKey<Item> tag) {
        this(context, DataMapTargets.tag(Objects.requireNonNull(tag, "tag")));
    }

    private FurnaceFuelBuilder(
            DataMapContext context,
            BiConsumer<DataMapProvider.Builder<FurnaceFuel, Item>, FurnaceFuel> target) {
        super(context, NeoForgeDataMaps.FURNACE_FUELS, target);
    }

    public FurnaceFuelBuilder burnTime(int burnTime) {
        if (burnTime <= 0) {
            throw new IllegalArgumentException("burn time must be positive");
        }
        this.burnTime = burnTime;
        return this;
    }

    @Override
    protected FurnaceFuel build() {
        if (this.burnTime == null) {
            throw new IllegalStateException("furnace fuel needs a burn time");
        }
        return new FurnaceFuel(this.burnTime);
    }
}
