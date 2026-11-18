package com.blackmoss.lazycat.api.thaumaturge.blueprint;

import com.blackmoss.lazycat.thaumaturge.blueprint.BlueprintBuilder;
import com.google.gson.JsonObject;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;

import java.util.*;
import java.util.concurrent.CompletableFuture;

@SuppressWarnings("unused")
public final class BlueprintContext {
    private static final String DIRECTORY = "thaumaturge/blueprint";

    private final PackOutput.PathProvider blueprints;
    private final Map<ResourceLocation, JsonObject> data = new LinkedHashMap<>();
    private final String namespace;

    private BlueprintContext(PackOutput output, String namespace) {
        this.blueprints = output.createPathProvider(PackOutput.Target.DATA_PACK, DIRECTORY);
        this.namespace = namespace;
    }

    public static BlueprintContext of(PackOutput output, String namespace) {
        Objects.requireNonNull(output, "output");
        Objects.requireNonNull(namespace, "namespace");
        return new BlueprintContext(output, namespace);
    }

    public String namespace() {
        return this.namespace;
    }

    public BlueprintBuilder blueprint(ResourceLocation id) {
        Objects.requireNonNull(id, "id");
        return new BlueprintBuilder(this, id);
    }

    public BlueprintBuilder blueprint(String path) {
        Objects.requireNonNull(path, "path");
        return blueprint(ResourceLocation.fromNamespaceAndPath(this.namespace, path));
    }

    public void submit(ResourceLocation id, JsonObject json) {
        Objects.requireNonNull(id, "id");
        Objects.requireNonNull(json, "json");
        if (this.data.putIfAbsent(id, json) != null) {
            throw new IllegalStateException("Duplicate thaumaturge blueprint: " + id);
        }
    }

    public CompletableFuture<?> save(CachedOutput cache) {
        Objects.requireNonNull(cache, "cache");
        List<CompletableFuture<?>> tasks = new ArrayList<>(this.data.size());
        this.data.forEach((id, json) -> tasks.add(DataProvider.saveStable(cache, json, this.blueprints.json(id))));
        return CompletableFuture.allOf(tasks.toArray(CompletableFuture[]::new));
    }
}
