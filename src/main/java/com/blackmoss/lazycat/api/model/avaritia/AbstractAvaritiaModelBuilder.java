package com.blackmoss.lazycat.api.model.avaritia;

import com.blackmoss.lazycat.api.LazyIdentifier;
import com.blackmoss.lazycat.api.model.AbstractModelBuilder;
import com.blackmoss.lazycat.api.model.ModelContext;
import committee.nova.mods.avaritia.Const;
import net.minecraft.client.color.item.ItemTintSource;
import net.minecraft.client.renderer.item.ItemModel;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@SuppressWarnings("unused")
public abstract class AbstractAvaritiaModelBuilder<B extends AbstractAvaritiaModelBuilder<B>>
        extends AbstractModelBuilder<B> {
    protected final Item item;
    protected final List<Identifier> mask = new ArrayList<>();
    protected final List<ItemTintSource> tints = new ArrayList<>();
    protected Identifier model;
    protected Identifier texture = Const.rl("misc/halo");
    protected int color;
    protected int size;
    protected boolean pulse;

    protected AbstractAvaritiaModelBuilder(ModelContext context, Item item) {
        super(context);
        this.item = Objects.requireNonNull(item, "item");
        this.model = defaultModel(item);
    }

    private static Identifier defaultModel(Item item) {
        return Identifier.fromNamespaceAndPath(LazyIdentifier.getItemSpace(item), "item/" + LazyIdentifier.getItemPath(item));
    }

    public B model(Identifier model) {
        this.model = Objects.requireNonNull(model, "model");
        return self();
    }

    public B mask(Identifier... mask) {
        Objects.requireNonNull(mask, "mask");
        for (Identifier id : mask) {
            this.mask.add(Objects.requireNonNull(id, "mask contains null"));
        }
        return self();
    }

    public B tint(ItemTintSource... tints) {
        Objects.requireNonNull(tints, "tints");
        for (ItemTintSource tint : tints) {
            this.tints.add(Objects.requireNonNull(tint, "tints contains null"));
        }
        return self();
    }

    public B texture(Identifier texture) {
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
        this.context().itemModels().itemModelOutput.accept(this.item, createModel());
    }

    protected abstract ItemModel.Unbaked createModel();

    protected final List<Identifier> maskList() {
        return List.copyOf(this.mask);
    }

    protected final List<ItemTintSource> tintList() {
        return List.copyOf(this.tints);
    }

    protected final int haloColor() {
        return -this.color;
    }
}
