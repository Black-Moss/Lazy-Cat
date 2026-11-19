package com.blackmoss.lazycat.api.datamap;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.neoforged.neoforge.common.data.DataMapProvider;

import java.util.Objects;
import java.util.function.BiConsumer;

@SuppressWarnings("unused")
public final class DataMapTargets {
    private DataMapTargets() {
    }

    public static <R, V> BiConsumer<DataMapProvider.Builder<V, R>, V> entry(Registry<R> registry, R value) {
        Objects.requireNonNull(registry, "registry");
        ResourceLocation id = Objects.requireNonNull(registry.getKey(Objects.requireNonNull(value, "value")), "data map key");
        return (builder, mapValue) -> builder.add(id, mapValue, false);
    }

    public static <R, V> BiConsumer<DataMapProvider.Builder<V, R>, V> entry(ResourceKey<R> key) {
        ResourceLocation id = Objects.requireNonNull(key, "key").location();
        return (builder, mapValue) -> builder.add(id, mapValue, false);
    }

    public static <R, V> BiConsumer<DataMapProvider.Builder<V, R>, V> tag(TagKey<R> tag) {
        TagKey<R> key = Objects.requireNonNull(tag, "tag");
        return (builder, mapValue) -> builder.add(key, mapValue, false);
    }
}
