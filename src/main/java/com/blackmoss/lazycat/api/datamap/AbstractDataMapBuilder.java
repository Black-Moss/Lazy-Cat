package com.blackmoss.lazycat.api.datamap;

import net.neoforged.neoforge.common.data.DataMapProvider;
import net.neoforged.neoforge.registries.datamaps.DataMapType;

import java.util.Objects;
import java.util.function.BiConsumer;

@SuppressWarnings("unused")
public abstract class AbstractDataMapBuilder<R, V> {
    private final DataMapContext context;
    private final DataMapType<R, V> type;
    private final BiConsumer<DataMapProvider.Builder<V, R>, V> target;

    protected AbstractDataMapBuilder(
            DataMapContext context,
            DataMapType<R, V> type,
            BiConsumer<DataMapProvider.Builder<V, R>, V> target) {
        this.context = Objects.requireNonNull(context, "context");
        this.type = Objects.requireNonNull(type, "type");
        this.target = Objects.requireNonNull(target, "target");
    }

    protected final DataMapContext context() {
        return this.context;
    }

    protected final DataMapType<R, V> type() {
        return this.type;
    }

    public final void save() {
        this.target.accept(this.context.builder(this.type), Objects.requireNonNull(build(), "data map value"));
    }

    protected abstract V build();
}
