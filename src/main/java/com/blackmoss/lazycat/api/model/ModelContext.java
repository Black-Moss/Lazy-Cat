package com.blackmoss.lazycat.api.model;

import com.google.gson.JsonObject;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;

import java.util.*;
import java.util.concurrent.CompletableFuture;

@SuppressWarnings("unused")
public final class ModelContext {
    private static final String ITEM_DIRECTORY = "models/item";
    private static final String BLOCK_DIRECTORY = "models/block";
    private static final String BLOCK_STATE_DIRECTORY = "blockstates";

    private final PackOutput.PathProvider itemModels;
    private final PackOutput.PathProvider blockModels;
    private final PackOutput.PathProvider blockStates;
    private final Map<ResourceLocation, JsonObject> itemData = new LinkedHashMap<>();
    private final Map<ResourceLocation, JsonObject> blockData = new LinkedHashMap<>();
    private final Map<ResourceLocation, JsonObject> blockStateData = new LinkedHashMap<>();
    private final String namespace;

    private ModelContext(PackOutput output, String namespace) {
        this.itemModels = output.createPathProvider(PackOutput.Target.RESOURCE_PACK, ITEM_DIRECTORY);
        this.blockModels = output.createPathProvider(PackOutput.Target.RESOURCE_PACK, BLOCK_DIRECTORY);
        this.blockStates = output.createPathProvider(PackOutput.Target.RESOURCE_PACK, BLOCK_STATE_DIRECTORY);
        this.namespace = namespace;
    }

    public static ModelContext of(PackOutput output, String namespace) {
        Objects.requireNonNull(output, "output");
        Objects.requireNonNull(namespace, "namespace");
        return new ModelContext(output, namespace);
    }

    private static void submit(Map<ResourceLocation, JsonObject> target, ResourceLocation id, JsonObject json, String type) {
        Objects.requireNonNull(json, "json");
        if (target.putIfAbsent(id, json) != null) {
            throw new IllegalStateException("Duplicate lazycat %s model: %s".formatted(type, id));
        }
    }

    public String namespace() {
        return this.namespace;
    }

    public void submitItem(ResourceLocation id, JsonObject json) {
        Objects.requireNonNull(id, "id");
        submit(this.itemData, id, json, "item");
    }

    public void submitBlock(ResourceLocation id, JsonObject json) {
        Objects.requireNonNull(id, "id");
        submit(this.blockData, id, json, "block");
    }

    public void submitBlockState(ResourceLocation id, JsonObject json) {
        Objects.requireNonNull(id, "id");
        submit(this.blockStateData, id, json, "blockstate");
    }

    private static ResourceLocation relative(ResourceLocation id, String folder) {
        String path = id.getPath();
        if (path.startsWith(folder + "/")) {
            path = path.substring(folder.length() + 1);
        }
        return ResourceLocation.fromNamespaceAndPath(id.getNamespace(), path);
    }

    public CompletableFuture<?> save(CachedOutput cache) {
        Objects.requireNonNull(cache, "cache");
        List<CompletableFuture<?>> tasks = new ArrayList<>(
                this.itemData.size() + this.blockData.size() + this.blockStateData.size());
        this.itemData.forEach((id, json) ->
                tasks.add(DataProvider.saveStable(cache, json, this.itemModels.json(relative(id, "item")))));
        this.blockData.forEach((id, json) ->
                tasks.add(DataProvider.saveStable(cache, json, this.blockModels.json(relative(id, "block")))));
        this.blockStateData.forEach((id, json) ->
                tasks.add(DataProvider.saveStable(cache, json, this.blockStates.json(id))));
        return CompletableFuture.allOf(tasks.toArray(CompletableFuture[]::new));
    }
}
