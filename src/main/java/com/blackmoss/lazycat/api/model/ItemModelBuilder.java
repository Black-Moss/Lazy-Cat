package com.blackmoss.lazycat.api.model;

import com.blackmoss.lazycat.api.LazyIdentifier;
import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.util.Objects;

@SuppressWarnings("unused")
public final class ItemModelBuilder extends AbstractModelBuilder<ItemModelBuilder> {
    private final Item item;
    private ResourceLocation model;
    private ResourceLocation parent = ResourceLocation.withDefaultNamespace("item/generated");
    private ResourceLocation texture;
    private ResourceLocation base;
    private ResourceLocation overlay;
    private boolean layered;
    private boolean plain;

    public ItemModelBuilder(ModelContext context, Item item) {
        super(context);
        this.item = Objects.requireNonNull(item, "item");
        this.model = defaultModel(item);
        this.texture = defaultTexture(item);
    }

    public ItemModelBuilder(ModelContext context, Block block) {
        this(context, Objects.requireNonNull(block, "block").asItem());
    }

    private static ResourceLocation defaultModel(Item item) {
        return ResourceLocation.fromNamespaceAndPath(LazyIdentifier.getItemSpace(item), "item/" + LazyIdentifier.getItemPath(item));
    }

    private static ResourceLocation defaultTexture(Item item) {
        return defaultModel(item);
    }

    @Override
    protected ItemModelBuilder self() {
        return this;
    }

    public ItemModelBuilder model(ResourceLocation model) {
        this.model = Objects.requireNonNull(model, "model");
        return this;
    }

    public ItemModelBuilder parent(ResourceLocation parent) {
        this.parent = Objects.requireNonNull(parent, "parent");
        return this;
    }

    public ItemModelBuilder texture(ResourceLocation layer0) {
        this.texture = Objects.requireNonNull(layer0, "layer0");
        return this;
    }

    public ItemModelBuilder twoLayered() {
        return twoLayered(
                texture("item/" + LazyIdentifier.getItemPath(this.item)),
                texture("item/" + LazyIdentifier.getItemPath(this.item) + "_overlay"));
    }

    public ItemModelBuilder twoLayered(ResourceLocation base, ResourceLocation overlay) {
        this.parent = ResourceLocation.withDefaultNamespace("item/generated");
        this.base = Objects.requireNonNull(base, "base");
        this.overlay = Objects.requireNonNull(overlay, "overlay");
        this.layered = true;
        this.plain = false;
        return this;
    }

    public ItemModelBuilder plainModel() {
        return plainModel(defaultModel(this.item));
    }

    public ItemModelBuilder plainModel(ResourceLocation model) {
        this.parent = Objects.requireNonNull(model, "model");
        this.plain = true;
        this.layered = false;
        return this;
    }

    private ResourceLocation texture(String path) {
        return ResourceLocation.fromNamespaceAndPath(LazyIdentifier.getItemSpace(this.item), path);
    }

    public void save() {
        JsonObject json = new JsonObject();
        json.addProperty("parent", this.parent.toString());
        if (!this.plain) {
            JsonObject textures = new JsonObject();
            if (this.layered) {
                textures.addProperty("layer0", this.base.toString());
                textures.addProperty("layer1", this.overlay.toString());
            } else {
                textures.addProperty("layer0", this.texture.toString());
            }
            json.add("textures", textures);
        }
        this.context().submitItem(this.model, json);
    }
}
