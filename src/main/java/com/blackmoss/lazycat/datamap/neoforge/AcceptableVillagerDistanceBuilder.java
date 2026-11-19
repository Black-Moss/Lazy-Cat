package com.blackmoss.lazycat.datamap.neoforge;

import com.blackmoss.lazycat.api.datamap.AbstractDataMapBuilder;
import com.blackmoss.lazycat.api.datamap.DataMapContext;
import com.blackmoss.lazycat.api.datamap.DataMapTargets;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.neoforged.neoforge.common.data.DataMapProvider;
import net.neoforged.neoforge.registries.datamaps.builtin.AcceptableVillagerDistance;
import net.neoforged.neoforge.registries.datamaps.builtin.NeoForgeDataMaps;

import java.util.Objects;
import java.util.function.BiConsumer;

@SuppressWarnings({"unused", "NullableProblems"})
public final class AcceptableVillagerDistanceBuilder extends AbstractDataMapBuilder<EntityType<?>, AcceptableVillagerDistance> {
    private Float distance;

    public AcceptableVillagerDistanceBuilder(DataMapContext context, EntityType<?> entityType) {
        this(context, DataMapTargets.entry(BuiltInRegistries.ENTITY_TYPE, Objects.requireNonNull(entityType, "entityType")));
    }

    public AcceptableVillagerDistanceBuilder(DataMapContext context, TagKey<EntityType<?>> tag) {
        this(context, DataMapTargets.tag(Objects.requireNonNull(tag, "tag")));
    }

    private AcceptableVillagerDistanceBuilder(
            DataMapContext context,
            BiConsumer<DataMapProvider.Builder<AcceptableVillagerDistance, EntityType<?>>, AcceptableVillagerDistance> target) {
        super(context, NeoForgeDataMaps.ACCEPTABLE_VILLAGER_DISTANCES, target);
    }

    public AcceptableVillagerDistanceBuilder distance(float distance) {
        if (distance <= 0.0f) {
            throw new IllegalArgumentException("distance must be positive");
        }
        this.distance = distance;
        return this;
    }

    @Override
    protected AcceptableVillagerDistance build() {
        if (this.distance == null) {
            throw new IllegalStateException("acceptable villager distance needs a distance");
        }
        return new AcceptableVillagerDistance(this.distance);
    }
}
