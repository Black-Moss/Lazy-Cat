package com.blackmoss.lazycat.api.model.avaritia;

import com.blackmoss.lazycat.api.LazyIdentifier;
import com.blackmoss.lazycat.api.model.AbstractModelBuilder;
import com.blackmoss.lazycat.api.model.ModelContext;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import committee.nova.mods.avaritia.Const;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@SuppressWarnings("unused")
public abstract class AbstractAvaritiaModelBuilder<B extends AbstractAvaritiaModelBuilder<B>>
        extends AbstractModelBuilder<B> {
    protected final Item item;
    protected final List<ResourceLocation> mask = new ArrayList<>();
    protected final List<Integer> tints = new ArrayList<>();
    protected ResourceLocation model;
    protected ResourceLocation parent = ResourceLocation.withDefaultNamespace("item/generated");
    protected ResourceLocation texture = Const.rl("misc/halo");
    protected int color;
    protected int size;
    protected boolean pulse;

    protected AbstractAvaritiaModelBuilder(ModelContext context, Item item) {
        super(context);
        this.item = Objects.requireNonNull(item, "item");
        this.model = defaultModel(item);
    }

    private static ResourceLocation defaultModel(Item item) {
        return ResourceLocation.fromNamespaceAndPath(LazyIdentifier.getItemSpace(item), "item/" + LazyIdentifier.getItemPath(item));
    }

    public B model(ResourceLocation model) {
        this.model = Objects.requireNonNull(model, "model");
        return self();
    }

    public B parent(ResourceLocation parent) {
        this.parent = Objects.requireNonNull(parent, "parent");
        return self();
    }

    public B mask(ResourceLocation... mask) {
        Objects.requireNonNull(mask, "mask");
        for (ResourceLocation id : mask) {
            this.mask.add(Objects.requireNonNull(id, "mask contains null"));
        }
        return self();
    }

    public B tint(int... colors) {
        Objects.requireNonNull(colors, "colors");
        for (int value : colors) {
            this.tints.add(value);
        }
        return self();
    }

    public B texture(ResourceLocation texture) {
        this.texture = Objects.requireNonNull(texture, "texture");
        return self();
    }

    public B color(int color) {
        this.color = color;
        return self();
    }

    public B size(int size) {
        this.size = size;
        return self();
    }

    public B pulse(boolean pulse) {
        this.pulse = pulse;
        return self();
    }

    public void save() {
        JsonObject json = new JsonObject();
        json.addProperty("parent", this.parent.toString());
        createModel(json);
        JsonObject textures = new JsonObject();
        textures.addProperty("layer0", defaultModel(this.item).toString());
        if (json.has("halo")) {
            textures.addProperty("halo", this.texture.toString());
        }
        json.add("textures", textures);
        this.context().submitItem(this.model, json);
    }

    protected abstract void createModel(JsonObject json);

    protected final List<ResourceLocation> maskList() {
        return List.copyOf(this.mask);
    }

    protected final List<Integer> tintList() {
        return List.copyOf(this.tints);
    }

    protected final int haloColor() {
        return -this.color;
    }

    protected final void writeLoader(JsonObject json, String type) {
        json.addProperty("loader", "avaritia:" + type);
    }

    protected final void writeMasks(JsonObject json, String key) {
        if (this.mask.isEmpty()) {
            return;
        }
        JsonObject holder = new JsonObject();
        if (this.mask.size() == 1) {
            holder.addProperty("mask", this.mask.getFirst().toString());
        } else {
            JsonArray array = new JsonArray();
            this.mask.forEach(id -> array.add(id.toString()));
            holder.add("mask", array);
        }
        json.add(key, holder);
    }

    protected final void writeHalo(JsonObject json) {
        JsonObject halo = new JsonObject();
        halo.addProperty("texture", "#halo");
        halo.addProperty("color", haloColor());
        halo.addProperty("size", this.size);
        halo.addProperty("pulse", this.pulse);
        if (!this.tints.isEmpty()) {
            JsonArray layerColors = new JsonArray();
            this.tints.forEach(layerColors::add);
            halo.add("layerColors", layerColors);
        }
        json.add("halo", halo);
    }
}
