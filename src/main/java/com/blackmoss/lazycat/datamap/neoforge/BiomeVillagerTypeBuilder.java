package com.blackmoss.lazycat.datamap.neoforge;

import com.blackmoss.lazycat.api.datamap.AbstractDataMapBuilder;
import com.blackmoss.lazycat.api.datamap.DataMapContext;
import com.blackmoss.lazycat.api.datamap.DataMapTargets;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.npc.VillagerType;
import net.minecraft.world.level.biome.Biome;
import net.neoforged.neoforge.common.data.DataMapProvider;
import net.neoforged.neoforge.registries.datamaps.builtin.BiomeVillagerType;
import net.neoforged.neoforge.registries.datamaps.builtin.NeoForgeDataMaps;

import java.util.Objects;
import java.util.function.BiConsumer;

@SuppressWarnings("unused")
public final class BiomeVillagerTypeBuilder extends AbstractDataMapBuilder<Biome, BiomeVillagerType> {
    private VillagerType villagerType;

    public BiomeVillagerTypeBuilder(DataMapContext context, ResourceKey<Biome> biome) {
        this(context, DataMapTargets.entry(Objects.requireNonNull(biome, "biome")));
    }

    public BiomeVillagerTypeBuilder(DataMapContext context, TagKey<Biome> tag) {
        this(context, DataMapTargets.tag(Objects.requireNonNull(tag, "tag")));
    }

    private BiomeVillagerTypeBuilder(
            DataMapContext context,
            BiConsumer<DataMapProvider.Builder<BiomeVillagerType, Biome>, BiomeVillagerType> target) {
        super(context, NeoForgeDataMaps.VILLAGER_TYPES, target);
    }

    public BiomeVillagerTypeBuilder villagerType(VillagerType villagerType) {
        this.villagerType = Objects.requireNonNull(villagerType, "villagerType");
        return this;
    }

    public BiomeVillagerTypeBuilder villagerType(ResourceKey<VillagerType> villagerType) {
        Objects.requireNonNull(villagerType, "villagerType");
        return villagerType(BuiltInRegistries.VILLAGER_TYPE.getOrThrow(villagerType));
    }

    @Override
    protected BiomeVillagerType build() {
        if (this.villagerType == null) {
            throw new IllegalStateException("biome villager type needs a villager type");
        }
        return new BiomeVillagerType(this.villagerType);
    }
}
