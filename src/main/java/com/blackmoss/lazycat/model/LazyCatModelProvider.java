package com.blackmoss.lazycat.model;

import com.blackmoss.lazycat.api.LazyIdentifier;
import com.blackmoss.lazycat.api.model.BlockModelBuilder;
import com.blackmoss.lazycat.api.model.ItemModelBuilder;
import com.blackmoss.lazycat.api.model.ModelContext;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import org.jspecify.annotations.NonNull;

import java.util.concurrent.CompletableFuture;

@SuppressWarnings("unused")
public abstract class LazyCatModelProvider implements DataProvider {
    private final String modid;
    private final ModelContext context;

    public LazyCatModelProvider(PackOutput output, String modid) {
        this.modid = modid;
        this.context = ModelContext.of(output, modid);
    }

    private static ResourceLocation defaultModel(ItemLike item) {
        if (item.asItem() instanceof BlockItem blockItem) {
            Block block = blockItem.getBlock();
            return ResourceLocation.fromNamespaceAndPath(
                    LazyIdentifier.getBlockSpace(block), "block/" + LazyIdentifier.getBlockPath(block));
        }
        return ResourceLocation.fromNamespaceAndPath(
                LazyIdentifier.getItemSpace(item), "item/" + LazyIdentifier.getItemPath(item));
    }

    protected abstract void addModels(ModelContext context);

    protected final ModelContext context() {
        return this.context;
    }

    @Override
    public @NonNull CompletableFuture<?> run(@NonNull CachedOutput cache) {
        addModels(this.context);
        return this.context.save(cache);
    }

    @Override
    public @NonNull String getName() {
        return modid + " Model by Lazy Cat";
    }

    protected void blockCubeAll(Block block) {
        new BlockModelBuilder(this.context, block).model(getBlockModelId(block)).save();
    }

    protected void blockCustom(Block block) {
        new BlockModelBuilder(this.context, block).custom(getBlockModelId(block)).save();
    }

    protected void blockBlockState(Block block) {
        new BlockModelBuilder(this.context, block).model(getBlockModelId(block)).onlyState().save();
    }

    protected void blockItemModel(Block block) {
        new BlockModelBuilder(this.context, block).model(getBlockModelId(block)).onlyItem().save();
    }

    protected void blockFacing(Block block) {
        new BlockModelBuilder(this.context, block).facing().custom(getBlockModelId(block)).save();
    }

    protected void itemFlat(ItemLike item) {
        new ItemModelBuilder(this.context, item.asItem()).save();
    }

    protected void itemTwoLayered(ItemLike item) {
        new ItemModelBuilder(this.context, item.asItem()).twoLayered().save();
    }

    protected void itemTwoLayered(ItemLike item, ResourceLocation base, ResourceLocation overlay) {
        new ItemModelBuilder(this.context, item.asItem()).twoLayered(base, overlay).save();
    }

    protected void plainModel(ItemLike item) {
        new ItemModelBuilder(this.context, item.asItem()).plainModel(defaultModel(item)).save();
    }

    protected void plainModel(ItemLike item, ResourceLocation model) {
        new ItemModelBuilder(this.context, item.asItem()).plainModel(model).save();
    }

    protected ResourceLocation getBlockModelId(Block block) {
        return ResourceLocation.fromNamespaceAndPath(this.modid, "block/" + LazyIdentifier.getBlockPath(block));
    }

    protected ResourceLocation getItemModelId(Item item) {
        return ResourceLocation.fromNamespaceAndPath(this.modid, "item/" + LazyIdentifier.getItemPath(item));
    }
}
