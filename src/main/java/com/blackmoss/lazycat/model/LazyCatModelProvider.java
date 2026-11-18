package com.blackmoss.lazycat.model;

import com.blackmoss.lazycat.api.LazyIdentifier;
import com.blackmoss.lazycat.api.model.ItemModelBuilder;
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
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import org.jspecify.annotations.NonNull;

@SuppressWarnings("unused")
public abstract class LazyCatModelProvider extends ModelProvider {
    protected final String modid;
    private ModelContext context;

    public LazyCatModelProvider(PackOutput output, String modid) {
        super(output, modid);
        this.modid = modid;
    }

    private static Identifier defaultModel(ItemLike item) {
        if (item.asItem() instanceof BlockItem blockItem) {
            Block block = blockItem.getBlock();
            return Identifier.fromNamespaceAndPath(
                    LazyIdentifier.getBlockSpace(block), "block/" + LazyIdentifier.getBlockPath(block));
        }
        return Identifier.fromNamespaceAndPath(
                LazyIdentifier.getItemSpace(item), "item/" + LazyIdentifier.getItemPath(item));
    }

    @Override
    public @NonNull String getName() {
        return modid + " Model by Lazy Cat";
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

    protected void blockCubeAll(ModelContext context, Block block) {
        var blockModels = this.context.blockModels();
        var itemModels = this.context.itemModels();

        Identifier modelId = getBlockModelId(block);
        ModelTemplates.CUBE_ALL.create(modelId, TextureMapping.cube(block), blockModels.modelOutput);
        blockModels.blockStateOutput.accept(MultiVariantGenerator.dispatch(block, BlockModelGenerators.plainVariant(modelId)));
        itemModels.itemModelOutput.accept(block.asItem(), ItemModelUtils.plainModel(modelId));
    }

    protected void blockCustom(Block block) {
        Identifier modelId = getBlockModelId(block);
        MultiVariant variant = new MultiVariant(WeightedList.of(new Variant(modelId)));
        context.blockModels().blockStateOutput.accept(MultiVariantGenerator.dispatch(block, variant));
        context.itemModels().itemModelOutput.accept(block.asItem(), ItemModelUtils.plainModel(modelId));
    }

    protected void blockBlockState(Block block) {
        context.blockModels().blockStateOutput.accept(BlockModelGenerators.createSimpleBlock(block, BlockModelGenerators.plainVariant(getBlockModelId(block))));
    }

    protected void blockItemModel(Block block) {
        context.itemModels().itemModelOutput.accept(block.asItem(), ItemModelUtils.plainModel(getBlockModelId(block)));
    }

    protected void blockObj(Block block) {
        Identifier modelId = getBlockModelId(block);
        context.blockModels().blockStateOutput.accept(BlockModelGenerators.createSimpleBlock(block, BlockModelGenerators.plainVariant(modelId)));
        context.itemModels().itemModelOutput.accept(block.asItem(), ItemModelUtils.plainModel(modelId));
    }

    protected void blockFacing(Block block) {
        Identifier modelId = getBlockModelId(block);
        MultiVariant variant = new MultiVariant(WeightedList.of(new Variant(modelId)));

        context.blockModels().blockStateOutput.accept(
                MultiVariantGenerator.dispatch(block, variant)
                        .with(BlockModelGenerators.ROTATION_FACING));
        context.itemModels().itemModelOutput.accept(block.asItem(), ItemModelUtils.plainModel(modelId));
    }

    protected void itemFlat(ItemLike item) {
        context.itemModels().generateFlatItem(item.asItem(), ModelTemplates.FLAT_ITEM);
    }

    protected void itemTwoLayered(ItemLike item) {
        new ItemModelBuilder(this.context, item.asItem()).twoLayered().save();
    }

    protected void itemTwoLayered(ItemLike item, Identifier base, Identifier overlay) {
        new ItemModelBuilder(this.context, item.asItem()).twoLayered(base, overlay).save();
    }

    protected void plainModel(ItemLike item) {
        new ItemModelBuilder(this.context, item.asItem()).plainModel(defaultModel(item)).save();
    }

    protected void plainModel(ItemLike item, Identifier model) {
        new ItemModelBuilder(this.context, item.asItem()).plainModel(model).save();
    }

    protected Identifier getBlockModelId(Block block) {
        return Identifier.fromNamespaceAndPath(this.modid, "block/" + LazyIdentifier.getBlockPath(block));
    }

    protected Identifier getItemModelId(Item item) {
        return Identifier.fromNamespaceAndPath(this.modid, "item/" + LazyIdentifier.getItemPath(item));
    }
}
