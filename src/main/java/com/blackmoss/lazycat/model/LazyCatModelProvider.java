package com.blackmoss.lazycat.model;

import com.blackmoss.lazycat.api.LazyIdentifier;
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

public abstract class LazyCatModelProvider extends ModelProvider {
    protected final String MODID;

    public LazyCatModelProvider(PackOutput output, String modid) {
        super(output, modid);
        MODID = modid;
    }

    protected void blockCubeAll(@NonNull BlockModelGenerators blockModels, @NonNull ItemModelGenerators itemModels, @NonNull Block block) {
        Identifier modelId = getBlockModelId(block);
        ModelTemplates.CUBE_ALL.create(modelId, TextureMapping.cube(block), blockModels.modelOutput);
        blockModels.blockStateOutput.accept(MultiVariantGenerator.dispatch(block, BlockModelGenerators.plainVariant(modelId)));
        itemModels.itemModelOutput.accept(block.asItem(), ItemModelUtils.plainModel(modelId));
    }

    protected void blockCustom(@NonNull BlockModelGenerators blockModels, @NonNull ItemModelGenerators itemModels, @NonNull Block block) {
        Identifier modelId = getBlockModelId(block);
        MultiVariant variant = new MultiVariant(WeightedList.of(new Variant(modelId)));
        blockModels.blockStateOutput.accept(MultiVariantGenerator.dispatch(block, variant));
        itemModels.itemModelOutput.accept(block.asItem(), ItemModelUtils.plainModel(modelId));
    }

    protected void onlyBlockState(@NonNull BlockModelGenerators blockModels, @NonNull Block block) {
        blockModels.blockStateOutput.accept(BlockModelGenerators.createSimpleBlock(block, BlockModelGenerators.plainVariant(getBlockModelId(block))));
    }

    protected void onlyItemModel(@NonNull ItemModelGenerators itemModels, @NonNull Block block) {
        itemModels.itemModelOutput.accept(block.asItem(), ItemModelUtils.plainModel(getBlockModelId(block)));
    }

    protected void itemFlat(@NonNull ItemModelGenerators itemModels, @NonNull Item item) {
        Identifier modelId = getItemModelId(item);
        ModelTemplates.FLAT_ITEM.create(modelId, TextureMapping.layer0(item), itemModels.modelOutput);
        itemModels.itemModelOutput.accept(item, ItemModelUtils.plainModel(modelId));
    }

    protected void blockObj(@NonNull BlockModelGenerators blockModels, @NonNull ItemModelGenerators itemModels, @NonNull Block block) {
        Identifier modelId = getBlockModelId(block);
        blockModels.blockStateOutput.accept(BlockModelGenerators.createSimpleBlock(block, BlockModelGenerators.plainVariant(modelId)));
        itemModels.itemModelOutput.accept(block.asItem(), ItemModelUtils.plainModel(modelId));
    }

    protected void blockFacing(@NonNull BlockModelGenerators blockModels, @NonNull ItemModelGenerators itemModels, @NonNull Block block) {
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
