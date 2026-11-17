package com.blackmoss.lazycat.api.model;

import com.blackmoss.lazycat.api.LazyIdentifier;
import net.minecraft.client.data.models.model.ItemModelUtils;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.client.renderer.item.ItemModel;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;

import java.util.Objects;

public final class ItemModelBuilder extends AbstractModelBuilder<ItemModelBuilder> {
    private final Item item;
    private Identifier model;
    private ItemModel.Unbaked unbaked;

    public ItemModelBuilder(ModelContext context, Item item) {
        super(context);
        this.item = Objects.requireNonNull(item, "item");
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

    public void save() {
        if (this.unbaked == null) {
            ModelTemplates.FLAT_ITEM.create(
                    this.model, TextureMapping.layer0(this.item), this.context().itemModels().modelOutput);
            this.unbaked = ItemModelUtils.plainModel(this.model);
        }
        this.context().itemModels().itemModelOutput.accept(this.item, this.unbaked);
    }
}
