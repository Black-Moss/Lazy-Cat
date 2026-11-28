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

import java.util.Objects;

@SuppressWarnings("unused")
public abstract class LazyCatModelProvider extends ModelProvider {
    protected final String modid;
    protected ModelContext context;
    protected BlockModelGenerators blockModels;
    protected ItemModelGenerators itemModels;

    public LazyCatModelProvider(PackOutput output, String modid) {
        super(output, Objects.requireNonNull(modid, "modid"));
        if (modid.isBlank()) {
            throw new IllegalArgumentException("modid must not be blank");
        }
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
        this.blockModels = blockModels;
        this.itemModels = itemModels;
        this.context = new ModelContext(itemModels, blockModels);
        addModels();
    }

    protected abstract void addModels();

    protected final ModelContext context() {
        return this.context;
    }

    protected void blockCubeAll(Block block) {
        var blockModels = this.blockModels;
        var itemModels = this.itemModels;

        Identifier modelId = getBlockModelId(block);
        ModelTemplates.CUBE_ALL.create(modelId, TextureMapping.cube(block), blockModels.modelOutput);
        blockModels.blockStateOutput.accept(MultiVariantGenerator.dispatch(block, BlockModelGenerators.plainVariant(modelId)));
        itemModels.itemModelOutput.accept(block.asItem(), ItemModelUtils.plainModel(modelId));
    }

    protected void blockCustom(Block block) {
        Identifier modelId = getBlockModelId(block);
        MultiVariant variant = new MultiVariant(WeightedList.of(new Variant(modelId)));
        this.blockModels.blockStateOutput.accept(MultiVariantGenerator.dispatch(block, variant));
        this.itemModels.itemModelOutput.accept(block.asItem(), ItemModelUtils.plainModel(modelId));
    }

    protected void blockBlockState(Block block) {
        this.blockModels.blockStateOutput.accept(BlockModelGenerators.createSimpleBlock(block, BlockModelGenerators.plainVariant(getBlockModelId(block))));
    }

    protected void blockItemModel(Block block) {
        this.itemModels.itemModelOutput.accept(block.asItem(), ItemModelUtils.plainModel(getBlockModelId(block)));
    }

    protected void blockObj(Block block) {
        Identifier modelId = getBlockModelId(block);
        this.blockModels.blockStateOutput.accept(BlockModelGenerators.createSimpleBlock(block, BlockModelGenerators.plainVariant(modelId)));
        this.itemModels.itemModelOutput.accept(block.asItem(), ItemModelUtils.plainModel(modelId));
    }

    protected void blockFacing(Block block) {
        Identifier modelId = getBlockModelId(block);
        MultiVariant variant = new MultiVariant(WeightedList.of(new Variant(modelId)));

        this.blockModels.blockStateOutput.accept(
                MultiVariantGenerator.dispatch(block, variant)
                        .with(BlockModelGenerators.ROTATION_FACING));
        this.itemModels.itemModelOutput.accept(block.asItem(), ItemModelUtils.plainModel(modelId));
    }

    protected void itemFlat(ItemLike item) {
        this.itemModels.generateFlatItem(item.asItem(), ModelTemplates.FLAT_ITEM);
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
