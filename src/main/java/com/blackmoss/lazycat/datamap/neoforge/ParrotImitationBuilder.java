package com.blackmoss.lazycat.datamap.neoforge;

import com.blackmoss.lazycat.api.datamap.AbstractDataMapBuilder;
import com.blackmoss.lazycat.api.datamap.DataMapContext;
import com.blackmoss.lazycat.api.datamap.DataMapTargets;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.neoforged.neoforge.common.data.DataMapProvider;
import net.neoforged.neoforge.registries.datamaps.builtin.NeoForgeDataMaps;
import net.neoforged.neoforge.registries.datamaps.builtin.ParrotImitation;

import java.util.Objects;
import java.util.function.BiConsumer;

@SuppressWarnings({"unused", "NullableProblems"})
public final class ParrotImitationBuilder extends AbstractDataMapBuilder<EntityType<?>, ParrotImitation> {
    private SoundEvent sound;

    public ParrotImitationBuilder(DataMapContext context, EntityType<?> entityType) {
        this(context, DataMapTargets.entry(BuiltInRegistries.ENTITY_TYPE, Objects.requireNonNull(entityType, "entityType")));
    }

    public ParrotImitationBuilder(DataMapContext context, TagKey<EntityType<?>> tag) {
        this(context, DataMapTargets.tag(Objects.requireNonNull(tag, "tag")));
    }

    private ParrotImitationBuilder(
            DataMapContext context,
            BiConsumer<DataMapProvider.Builder<ParrotImitation, EntityType<?>>, ParrotImitation> target) {
        super(context, NeoForgeDataMaps.PARROT_IMITATIONS, target);
    }

    public ParrotImitationBuilder sound(SoundEvent sound) {
        this.sound = Objects.requireNonNull(sound, "sound");
        return this;
    }

    @Override
    protected ParrotImitation build() {
        if (this.sound == null) {
            throw new IllegalStateException("parrot imitation needs a sound");
        }
        return new ParrotImitation(this.sound);
    }
}
