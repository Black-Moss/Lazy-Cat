package com.blackmoss.lazycat.datamap.neoforge;

import com.blackmoss.lazycat.api.LazyIdentifier;
import com.blackmoss.lazycat.api.datamap.AbstractDataMapBuilder;
import com.blackmoss.lazycat.api.datamap.DataMapContext;
import com.blackmoss.lazycat.api.datamap.DataMapTargets;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.DataMapProvider;
import net.neoforged.neoforge.registries.datamaps.builtin.NeoForgeDataMaps;
import net.neoforged.neoforge.registries.datamaps.builtin.Strippable;

import java.util.Objects;
import java.util.function.BiConsumer;

@SuppressWarnings("unused")
public final class StrippableBuilder extends AbstractDataMapBuilder<Block, Strippable> {
    private Block strippedBlock;

    public StrippableBuilder(DataMapContext context, Block block) {
        this(context, DataMapTargets.entry(LazyIdentifier.getBlockIdentifier(block)));
    }

    public StrippableBuilder(DataMapContext context, TagKey<Block> tag) {
        this(context, DataMapTargets.tag(Objects.requireNonNull(tag, "tag")));
    }

    private StrippableBuilder(
            DataMapContext context,
            BiConsumer<DataMapProvider.Builder<Strippable, Block>, Strippable> target) {
        super(context, NeoForgeDataMaps.STRIPPABLES, target);
    }

    public StrippableBuilder stripped(Block block) {
        this.strippedBlock = Objects.requireNonNull(block, "block");
        return this;
    }

    @Override
    protected Strippable build() {
        if (this.strippedBlock == null) {
            throw new IllegalStateException("strippable needs a stripped block");
        }
        return new Strippable(this.strippedBlock);
    }
}
