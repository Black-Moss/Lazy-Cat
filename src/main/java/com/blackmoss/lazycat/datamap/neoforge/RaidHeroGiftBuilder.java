package com.blackmoss.lazycat.datamap.neoforge;

import com.blackmoss.lazycat.api.LazyIdentifier;
import com.blackmoss.lazycat.api.datamap.AbstractDataMapBuilder;
import com.blackmoss.lazycat.api.datamap.DataMapContext;
import com.blackmoss.lazycat.api.datamap.DataMapTargets;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.npc.villager.VillagerProfession;
import net.minecraft.world.level.storage.loot.LootTable;
import net.neoforged.neoforge.common.data.DataMapProvider;
import net.neoforged.neoforge.registries.datamaps.builtin.NeoForgeDataMaps;
import net.neoforged.neoforge.registries.datamaps.builtin.RaidHeroGift;

import java.util.Objects;
import java.util.function.BiConsumer;

@SuppressWarnings("unused")
public final class RaidHeroGiftBuilder extends AbstractDataMapBuilder<VillagerProfession, RaidHeroGift> {
    private ResourceKey<LootTable> lootTable;

    public RaidHeroGiftBuilder(DataMapContext context, VillagerProfession profession) {
        this(context, DataMapTargets.entry(LazyIdentifier.getIdentifier(BuiltInRegistries.VILLAGER_PROFESSION, profession)));
    }

    public RaidHeroGiftBuilder(DataMapContext context, TagKey<VillagerProfession> tag) {
        this(context, DataMapTargets.tag(Objects.requireNonNull(tag, "tag")));
    }

    private RaidHeroGiftBuilder(
            DataMapContext context,
            BiConsumer<DataMapProvider.Builder<RaidHeroGift, VillagerProfession>, RaidHeroGift> target) {
        super(context, NeoForgeDataMaps.RAID_HERO_GIFTS, target);
    }

    public RaidHeroGiftBuilder lootTable(ResourceKey<LootTable> lootTable) {
        this.lootTable = Objects.requireNonNull(lootTable, "lootTable");
        return this;
    }

    @Override
    protected RaidHeroGift build() {
        if (this.lootTable == null) {
            throw new IllegalStateException("raid hero gift needs a loot table");
        }
        return new RaidHeroGift(this.lootTable);
    }
}
