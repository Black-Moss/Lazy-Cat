package com.blackmoss.lazycat.model;

import com.blackmoss.lazycat.api.LazyIdentifier;
import com.blackmoss.lazycat.api.model.BlockModelBuilder;
import com.blackmoss.lazycat.api.model.ModelContext;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
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

    protected void blockCubeAll(ModelContext context, Block block) {
        new BlockModelBuilder(context, block).model(getBlockModelId(block)).save();
    }

    protected void blockCustom(ModelContext context, Block block) {
        new BlockModelBuilder(context, block).custom(getBlockModelId(block)).save();
    }

    protected void onlyBlockState(ModelContext context, Block block) {
        new BlockModelBuilder(context, block).custom(getBlockModelId(block)).onlyState().save();
    }

    protected void onlyItemModel(ModelContext context, Block block) {
        new BlockModelBuilder(context, block).onlyItem().save();
    }

    protected void blockObj(ModelContext context, Block block) {
        new BlockModelBuilder(context, block).custom(getBlockModelId(block)).save();
    }

    protected void blockFacing(ModelContext context, Block block) {
        new BlockModelBuilder(context, block).facing().custom(getBlockModelId(block)).save();
    }

    private ResourceLocation getBlockModelId(Block block) {
        return ResourceLocation.fromNamespaceAndPath(this.modid, "block/" + LazyIdentifier.getBlockPath(block));
    }
}
