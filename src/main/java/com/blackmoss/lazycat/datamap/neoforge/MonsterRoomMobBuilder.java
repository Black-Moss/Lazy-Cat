package com.blackmoss.lazycat.datamap.neoforge;

import com.blackmoss.lazycat.api.LazyIdentifier;
import com.blackmoss.lazycat.api.datamap.AbstractDataMapBuilder;
import com.blackmoss.lazycat.api.datamap.DataMapContext;
import com.blackmoss.lazycat.api.datamap.DataMapTargets;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.neoforged.neoforge.common.data.DataMapProvider;
import net.neoforged.neoforge.registries.datamaps.builtin.MonsterRoomMob;
import net.neoforged.neoforge.registries.datamaps.builtin.NeoForgeDataMaps;

import java.util.Objects;
import java.util.function.BiConsumer;

@SuppressWarnings({"unused", "NullableProblems"})
public final class MonsterRoomMobBuilder extends AbstractDataMapBuilder<EntityType<?>, MonsterRoomMob> {
    private Integer weight;

    public MonsterRoomMobBuilder(DataMapContext context, EntityType<?> entityType) {
        this(context, DataMapTargets.entry(LazyIdentifier.getIdentifier(BuiltInRegistries.ENTITY_TYPE, entityType)));
    }

    public MonsterRoomMobBuilder(DataMapContext context, TagKey<EntityType<?>> tag) {
        this(context, DataMapTargets.tag(Objects.requireNonNull(tag, "tag")));
    }

    private MonsterRoomMobBuilder(
            DataMapContext context,
            BiConsumer<DataMapProvider.Builder<MonsterRoomMob, EntityType<?>>, MonsterRoomMob> target) {
        super(context, NeoForgeDataMaps.MONSTER_ROOM_MOBS, target);
    }

    public MonsterRoomMobBuilder weight(int weight) {
        if (weight < 0) {
            throw new IllegalArgumentException("weight must not be negative");
        }
        this.weight = weight;
        return this;
    }

    @Override
    protected MonsterRoomMob build() {
        if (this.weight == null) {
            throw new IllegalStateException("monster room mob needs a weight");
        }
        return new MonsterRoomMob(this.weight);
    }
}
