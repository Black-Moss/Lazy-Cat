package com.blackmoss.lazycat.model;

import com.blackmoss.lazycat.api.LazyIdentifier;
import com.blackmoss.lazycat.api.model.ModelContext;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.client.data.models.MultiVariant;
import net.minecraft.client.data.models.blockstates.MultiVariantGenerator;
import net.minecraft.client.data.models.model.ItemModelUtils;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.client.renderer.block.dispatch.Variant;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.util.random.WeightedList;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import org.jspecify.annotations.NonNull;

@SuppressWarnings("unused")
public abstract class LazyCatModelProvider extends ModelProvider {
    protected final String MODID;
    private ModelContext context;

    public LazyCatModelProvider(PackOutput output, String modid) {
        super(output, modid);
        this.MODID = modid;
    }

    @Override
    protected final void registerModels(@NonNull BlockModelGenerators blockModels, @NonNull ItemModelGenerators itemModels) {
        this.context = new ModelContext(itemModels, blockModels);
        addModels(this.context);
    }

    protected abstract void addModels(ModelContext context);

    protected final ModelContext context() {
        return this.context;
    }

    protected void blockCubeAll(BlockModelGenerators blockModels, ItemModelGenerators itemModels, Block block) {
        Identifier modelId = getBlockModelId(block);
        ModelTemplates.CUBE_ALL.create(modelId, TextureMapping.cube(block), blockModels.modelOutput);
        blockModels.blockStateOutput.accept(MultiVariantGenerator.dispatch(block, BlockModelGenerators.plainVariant(modelId)));
        itemModels.itemModelOutput.accept(block.asItem(), ItemModelUtils.plainModel(modelId));
    }

    protected void blockCustom(BlockModelGenerators blockModels, ItemModelGenerators itemModels, Block block) {
        Identifier modelId = getBlockModelId(block);
        MultiVariant variant = new MultiVariant(WeightedList.of(new Variant(modelId)));
        blockModels.blockStateOutput.accept(MultiVariantGenerator.dispatch(block, variant));
        itemModels.itemModelOutput.accept(block.asItem(), ItemModelUtils.plainModel(modelId));
    }

    protected void onlyBlockState(BlockModelGenerators blockModels, Block block) {
        blockModels.blockStateOutput.accept(BlockModelGenerators.createSimpleBlock(block, BlockModelGenerators.plainVariant(getBlockModelId(block))));
    }

    protected void onlyItemModel(ItemModelGenerators itemModels, Block block) {
        itemModels.itemModelOutput.accept(block.asItem(), ItemModelUtils.plainModel(getBlockModelId(block)));
    }

    protected void blockObj(BlockModelGenerators blockModels, ItemModelGenerators itemModels, Block block) {
        Identifier modelId = getBlockModelId(block);
        blockModels.blockStateOutput.accept(BlockModelGenerators.createSimpleBlock(block, BlockModelGenerators.plainVariant(modelId)));
        itemModels.itemModelOutput.accept(block.asItem(), ItemModelUtils.plainModel(modelId));
    }

    protected void blockFacing(BlockModelGenerators blockModels, ItemModelGenerators itemModels, Block block) {
        Identifier modelId = getBlockModelId(block);
        MultiVariant variant = new MultiVariant(WeightedList.of(new Variant(modelId)));

        blockModels.blockStateOutput.accept(
                MultiVariantGenerator.dispatch(block, variant)
                        .with(BlockModelGenerators.ROTATION_FACING));
        itemModels.itemModelOutput.accept(block.asItem(), ItemModelUtils.plainModel(modelId));
    }

    private Identifier getBlockModelId(Block block) {
        return Identifier.fromNamespaceAndPath(this.MODID, "block/" + LazyIdentifier.getBlockIdentifier(block));
    }

    private Identifier getItemModelId(Item item) {
        return Identifier.fromNamespaceAndPath(this.MODID, "item/" + LazyIdentifier.getItemIdentifier(item));
    }
}
