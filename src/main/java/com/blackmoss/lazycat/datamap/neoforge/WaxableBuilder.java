package com.blackmoss.lazycat.datamap.neoforge;

import com.blackmoss.lazycat.api.LazyIdentifier;
import com.blackmoss.lazycat.api.datamap.AbstractDataMapBuilder;
import com.blackmoss.lazycat.api.datamap.DataMapContext;
import com.blackmoss.lazycat.api.datamap.DataMapTargets;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.DataMapProvider;
import net.neoforged.neoforge.registries.datamaps.builtin.NeoForgeDataMaps;
import net.neoforged.neoforge.registries.datamaps.builtin.Waxable;

import java.util.Objects;
import java.util.function.BiConsumer;

@SuppressWarnings("unused")
public final class WaxableBuilder extends AbstractDataMapBuilder<Block, Waxable> {
    private Block waxed;

    public WaxableBuilder(DataMapContext context, Block block) {
        this(context, DataMapTargets.entry(LazyIdentifier.getBlockIdentifier(block)));
    }

    public WaxableBuilder(DataMapContext context, TagKey<Block> tag) {
        this(context, DataMapTargets.tag(Objects.requireNonNull(tag, "tag")));
    }

    private WaxableBuilder(
            DataMapContext context,
            BiConsumer<DataMapProvider.Builder<Waxable, Block>, Waxable> target) {
        super(context, NeoForgeDataMaps.WAXABLES, target);
    }

    public WaxableBuilder waxed(Block block) {
        this.waxed = Objects.requireNonNull(block, "block");
        return this;
    }

    @Override
    protected Waxable build() {
        if (this.waxed == null) {
            throw new IllegalStateException("waxable needs a waxed block");
        }
        return new Waxable(this.waxed);
    }
}
