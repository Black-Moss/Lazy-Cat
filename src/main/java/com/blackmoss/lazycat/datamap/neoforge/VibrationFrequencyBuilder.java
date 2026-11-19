package com.blackmoss.lazycat.datamap.neoforge;

import com.blackmoss.lazycat.api.LazyIdentifier;
import com.blackmoss.lazycat.api.datamap.AbstractDataMapBuilder;
import com.blackmoss.lazycat.api.datamap.DataMapContext;
import com.blackmoss.lazycat.api.datamap.DataMapTargets;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.gameevent.GameEvent;
import net.neoforged.neoforge.common.data.DataMapProvider;
import net.neoforged.neoforge.registries.datamaps.builtin.NeoForgeDataMaps;
import net.neoforged.neoforge.registries.datamaps.builtin.VibrationFrequency;

import java.util.Objects;
import java.util.function.BiConsumer;

@SuppressWarnings("unused")
public final class VibrationFrequencyBuilder extends AbstractDataMapBuilder<GameEvent, VibrationFrequency> {
    private Integer frequency;

    public VibrationFrequencyBuilder(DataMapContext context, GameEvent gameEvent) {
        this(context, DataMapTargets.entry(LazyIdentifier.getIdentifier(BuiltInRegistries.GAME_EVENT, gameEvent)));
    }

    public VibrationFrequencyBuilder(DataMapContext context, Holder<GameEvent> gameEvent) {
        this(context, DataMapTargets.entry(Objects.requireNonNull(gameEvent, "gameEvent").unwrapKey()
                .orElseThrow(() -> new IllegalArgumentException("game event is not bound to a key"))));
    }

    public VibrationFrequencyBuilder(DataMapContext context, TagKey<GameEvent> tag) {
        this(context, DataMapTargets.tag(Objects.requireNonNull(tag, "tag")));
    }

    private VibrationFrequencyBuilder(
            DataMapContext context,
            BiConsumer<DataMapProvider.Builder<VibrationFrequency, GameEvent>, VibrationFrequency> target) {
        super(context, NeoForgeDataMaps.VIBRATION_FREQUENCIES, target);
    }

    public VibrationFrequencyBuilder frequency(int frequency) {
        if (frequency < 1 || frequency > 15) {
            throw new IllegalArgumentException("frequency must be between 1 and 15");
        }
        this.frequency = frequency;
        return this;
    }

    @Override
    protected VibrationFrequency build() {
        if (this.frequency == null) {
            throw new IllegalStateException("vibration frequency needs a frequency");
        }
        return new VibrationFrequency(this.frequency);
    }
}
