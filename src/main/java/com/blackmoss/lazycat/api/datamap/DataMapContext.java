package com.blackmoss.lazycat.api.datamap;

import net.minecraft.core.HolderLookup;
import net.neoforged.neoforge.common.data.DataMapProvider;
import net.neoforged.neoforge.registries.datamaps.DataMapType;

import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Objects;

@SuppressWarnings("unused")
public final class DataMapContext {
    private final HolderLookup.Provider registries;
    private final DataMapProvider provider;
    private final Map<DataMapType<?, ?>, DataMapProvider.Builder<?, ?>> builders = new IdentityHashMap<>();

    public DataMapContext(HolderLookup.Provider registries, DataMapProvider provider) {
        this.registries = Objects.requireNonNull(registries, "registries");
        this.provider = Objects.requireNonNull(provider, "provider");
    }

    public HolderLookup.Provider registries() {
        return this.registries;
    }

    public <R, V> DataMapProvider.Builder<V, R> builder(DataMapType<R, V> type) {
        Objects.requireNonNull(type, "type");
        //noinspection unchecked
        DataMapProvider.Builder<V, R> builder = (DataMapProvider.Builder<V, R>) this.builders.get(type);
        if (builder == null) {
            builder = this.provider.builder(type);
            this.builders.put(type, builder);
        }
        return builder;
    }
}
