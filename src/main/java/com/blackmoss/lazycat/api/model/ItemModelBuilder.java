package com.blackmoss.lazycat.api.model;

import com.blackmoss.lazycat.api.LazyIdentifier;
import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

import java.util.Objects;

@SuppressWarnings("unused")
public final class ItemModelBuilder extends AbstractModelBuilder<ItemModelBuilder> {
    private ResourceLocation model;
    private ResourceLocation parent = ResourceLocation.withDefaultNamespace("item/generated");
    private ResourceLocation texture;

    public ItemModelBuilder(ModelContext context, Item item) {
        super(context);
        Item item1 = Objects.requireNonNull(item, "item");
        this.model = defaultModel(item);
        this.texture = defaultTexture(item);
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

    public void save() {
        JsonObject json = new JsonObject();
        json.addProperty("parent", this.parent.toString());
        JsonObject textures = new JsonObject();
        textures.addProperty("layer0", this.texture.toString());
        json.add("textures", textures);
        this.context().submitItem(this.model, json);
    }
}
