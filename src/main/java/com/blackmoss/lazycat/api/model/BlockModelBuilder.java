package com.blackmoss.lazycat.api.model;

import com.blackmoss.lazycat.api.LazyIdentifier;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.MultiVariant;
import net.minecraft.client.data.models.blockstates.MultiVariantGenerator;
import net.minecraft.client.data.models.blockstates.PropertyDispatch;
import net.minecraft.client.data.models.model.ItemModelUtils;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.client.renderer.block.dispatch.VariantMutator;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Block;

import java.util.Objects;

@SuppressWarnings("unused")
public final class BlockModelBuilder extends AbstractModelBuilder<BlockModelBuilder> {
    private final Block block;
    private Identifier model;
    private MultiVariant variant;
    private PropertyDispatch<VariantMutator> mutator;
    private boolean writeModel = true;
    private boolean writeState = true;
    private boolean writeItem = true;

    public BlockModelBuilder(ModelContext context, Block block) {
        super(context);
        this.block = Objects.requireNonNull(block, "block");
        this.model = defaultModel(block);
    }

    private static Identifier defaultModel(Block block) {
        return Identifier.fromNamespaceAndPath(LazyIdentifier.getBlockSpace(block), "block/" + LazyIdentifier.getBlockPath(block));
    }

    @Override
    protected BlockModelBuilder self() {
        return this;
    }

    public BlockModelBuilder model(Identifier model) {
        this.model = Objects.requireNonNull(model, "model");
        return this;
    }

    public BlockModelBuilder cubeAll() {
        return this;
    }

    public BlockModelBuilder custom(MultiVariant variant) {
        this.variant = Objects.requireNonNull(variant, "variant");
        return this;
    }

    public BlockModelBuilder facing() {
        this.mutator = BlockModelGenerators.ROTATION_FACING;
        return this;
    }

    public BlockModelBuilder onlyState() {
        this.writeModel = false;
        this.writeItem = false;
        return this;
    }

    public BlockModelBuilder onlyItem() {
        this.writeModel = false;
        this.writeState = false;
        return this;
    }

    public void save() {
        if (this.writeModel) {
            ModelTemplates.CUBE_ALL.create(
                    this.model, TextureMapping.cube(this.block), this.context().blockModels().modelOutput);
        }
        if (this.writeState) {
            MultiVariant variant = this.variant != null ? this.variant : BlockModelGenerators.plainVariant(this.model);
            MultiVariantGenerator generator = MultiVariantGenerator.dispatch(this.block, variant);
            if (this.mutator != null) {
                generator = generator.with(this.mutator);
            }
            this.context().blockModels().blockStateOutput.accept(generator);
        }
        if (this.writeItem) {
            this.context().blockModels().itemModelOutput.accept(
                    this.block.asItem(), ItemModelUtils.plainModel(this.model));
        }
    }
}
