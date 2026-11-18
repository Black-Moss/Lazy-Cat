package com.blackmoss.lazycat.api.model;

import com.blackmoss.lazycat.api.LazyIdentifier;
import net.minecraft.client.data.models.model.ItemModelUtils;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.client.renderer.item.ItemModel;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.util.Objects;

@SuppressWarnings("unused")
public final class ItemModelBuilder extends AbstractModelBuilder<ItemModelBuilder> {
    private final Item item;
    private Identifier model;
    private ItemModel.Unbaked unbaked;

    public ItemModelBuilder(ModelContext context, Item item) {
        super(context);
        this.item = Objects.requireNonNull(item, "item");
        this.model = defaultModel(item);
    }

    public ItemModelBuilder(ModelContext context, Block block) {
        super(context);
        this.item = Objects.requireNonNull(block.asItem(), "item");
        this.model = defaultModel(item);
    }

    private static Identifier defaultModel(Item item) {
        return Identifier.fromNamespaceAndPath(LazyIdentifier.getItemSpace(item), "item/" + LazyIdentifier.getItemPath(item));
    }

    @Override
    protected ItemModelBuilder self() {
        return this;
    }

    public ItemModelBuilder model(Identifier model) {
        this.model = Objects.requireNonNull(model, "model");
        return this;
    }

    public ItemModelBuilder unbaked(ItemModel.Unbaked unbaked) {
        this.unbaked = Objects.requireNonNull(unbaked, "unbaked");
        return this;
    }

    public ItemModelBuilder plainModel() {
        this.unbaked = ItemModelUtils.plainModel(this.model);
        return this;
    }

    public ItemModelBuilder plainModel(Identifier model) {
        return model(model).plainModel();
    }

    public ItemModelBuilder twoLayered() {
        return twoLayered(
                texture("item/" + LazyIdentifier.getItemPath(this.item)),
                texture("item/" + LazyIdentifier.getItemPath(this.item) + "_overlay"));
    }

    public ItemModelBuilder twoLayered(Identifier base, Identifier overlay) {
        return twoLayered(new Material(base), new Material(overlay));
    }

    public ItemModelBuilder twoLayered(Material base, Material overlay) {
        ModelTemplates.TWO_LAYERED_ITEM.create(
                this.model, TextureMapping.layered(base, overlay), this.context().itemModels().modelOutput);
        this.unbaked = ItemModelUtils.plainModel(this.model);
        return this;
    }

    private Identifier texture(String path) {
        return Identifier.fromNamespaceAndPath(LazyIdentifier.getItemSpace(this.item), path);
    }

    public void save() {
        if (this.unbaked == null) {
            ModelTemplates.FLAT_ITEM.create(
                    this.model, TextureMapping.layer0(this.item), this.context().itemModels().modelOutput);
            this.unbaked = ItemModelUtils.plainModel(this.model);
        }
        this.context().itemModels().itemModelOutput.accept(this.item, this.unbaked);
    }
}
