package com.blackmoss.lazycat.datamap.neoforge;

import com.blackmoss.lazycat.api.LazyIdentifier;
import com.blackmoss.lazycat.api.datamap.AbstractDataMapBuilder;
import com.blackmoss.lazycat.api.datamap.DataMapContext;
import com.blackmoss.lazycat.api.datamap.DataMapTargets;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.common.data.DataMapProvider;
import net.neoforged.neoforge.registries.datamaps.builtin.Compostable;
import net.neoforged.neoforge.registries.datamaps.builtin.NeoForgeDataMaps;

import java.util.Objects;
import java.util.function.BiConsumer;

@SuppressWarnings("unused")
public final class CompostableBuilder extends AbstractDataMapBuilder<Item, Compostable> {
    private Float chance;
    private boolean villagerCompost;

    public CompostableBuilder(DataMapContext context, Item item) {
        this(context, DataMapTargets.entry(LazyIdentifier.getItemIdentifier(item)));
    }

    public CompostableBuilder(DataMapContext context, TagKey<Item> tag) {
        this(context, DataMapTargets.tag(Objects.requireNonNull(tag, "tag")));
    }

    private CompostableBuilder(
            DataMapContext context,
            BiConsumer<DataMapProvider.Builder<Compostable, Item>, Compostable> target) {
        super(context, NeoForgeDataMaps.COMPOSTABLES, target);
    }

    public CompostableBuilder chance(float chance) {
        if (chance < 0.0f || chance > 1.0f) {
            throw new IllegalArgumentException("chance must be between 0 and 1");
        }
        this.chance = chance;
        return this;
    }

    public CompostableBuilder villagerCompost(boolean villagerCompost) {
        this.villagerCompost = villagerCompost;
        return this;
    }

    @Override
    protected Compostable build() {
        if (this.chance == null) {
            throw new IllegalStateException("compostable needs a chance");
        }
        return this.villagerCompost ? new Compostable(this.chance, true) : new Compostable(this.chance);
    }
}
