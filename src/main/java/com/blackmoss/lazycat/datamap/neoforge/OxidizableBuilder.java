package com.blackmoss.lazycat.datamap.neoforge;

import com.blackmoss.lazycat.api.datamap.AbstractDataMapBuilder;
import com.blackmoss.lazycat.api.datamap.DataMapContext;
import com.blackmoss.lazycat.api.datamap.DataMapTargets;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.DataMapProvider;
import net.neoforged.neoforge.registries.datamaps.builtin.NeoForgeDataMaps;
import net.neoforged.neoforge.registries.datamaps.builtin.Oxidizable;

import java.util.Objects;
import java.util.function.BiConsumer;

@SuppressWarnings("unused")
public final class OxidizableBuilder extends AbstractDataMapBuilder<Block, Oxidizable> {
    private Block nextStage;

    public OxidizableBuilder(DataMapContext context, Block block) {
        this(context, DataMapTargets.entry(BuiltInRegistries.BLOCK, Objects.requireNonNull(block, "block")));
    }

    public OxidizableBuilder(DataMapContext context, TagKey<Block> tag) {
        this(context, DataMapTargets.tag(Objects.requireNonNull(tag, "tag")));
    }

    private OxidizableBuilder(
            DataMapContext context,
            BiConsumer<DataMapProvider.Builder<Oxidizable, Block>, Oxidizable> target) {
        super(context, NeoForgeDataMaps.OXIDIZABLES, target);
    }

    public OxidizableBuilder nextStage(Block block) {
        this.nextStage = Objects.requireNonNull(block, "block");
        return this;
    }

    @Override
    protected Oxidizable build() {
        if (this.nextStage == null) {
            throw new IllegalStateException("oxidizable needs a next oxidation stage");
        }
        return new Oxidizable(this.nextStage);
    }
}
