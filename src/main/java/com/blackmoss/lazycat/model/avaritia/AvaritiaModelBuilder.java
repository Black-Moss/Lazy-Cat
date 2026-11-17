package com.blackmoss.lazycat.model.avaritia;

import com.blackmoss.lazycat.api.model.ModelContext;
import net.minecraft.world.item.Item;

import java.util.Objects;

public final class AvaritiaModelBuilder {
    private final ModelContext context;

    public AvaritiaModelBuilder(ModelContext context) {
        this.context = Objects.requireNonNull(context, "context");
    }

    public CosmicModelBuilder cosmic(Item item) {
        return new CosmicModelBuilder(this.context, item);
    }

    public CosmicArcModelBuilder cosmicArc(Item item) {
        return new CosmicArcModelBuilder(this.context, item);
    }

    public HellModelBuilder hell(Item item) {
        return new HellModelBuilder(this.context, item);
    }

    public EternalModelBuilder eternal(Item item) {
        return new EternalModelBuilder(this.context, item);
    }

    public UnstableModelBuilder unstable(Item item) {
        return new UnstableModelBuilder(this.context, item);
    }

    public HaloModelBuilder halo(Item item) {
        return new HaloModelBuilder(this.context, item);
    }

    public HaloCosmicModelBuilder haloCosmic(Item item) {
        return new HaloCosmicModelBuilder(this.context, item);
    }

    public HaloEternalModelBuilder haloEternal(Item item) {
        return new HaloEternalModelBuilder(this.context, item);
    }
}
