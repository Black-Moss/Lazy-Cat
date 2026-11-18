package com.blackmoss.lazycat.api.model;

import com.blackmoss.lazycat.api.LazyIdentifier;
import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;

import java.util.Objects;

@SuppressWarnings("unused")
public final class BlockModelBuilder extends AbstractModelBuilder<BlockModelBuilder> {
    private final ResourceLocation blockId;
    private ResourceLocation model;
    private ResourceLocation stateModel;
    private ResourceLocation texture;
    private boolean facing;
    private boolean writeModel = true;
    private boolean writeState = true;
    private boolean writeItem = true;

    public BlockModelBuilder(ModelContext context, Block block) {
        super(context);
        Block block1 = Objects.requireNonNull(block, "block");
        this.blockId = LazyIdentifier.getBlockIdentifier(block);
        this.model = defaultModel(block);
        this.texture = defaultModel(block);
    }

    private static ResourceLocation defaultModel(Block block) {
        return ResourceLocation.fromNamespaceAndPath(LazyIdentifier.getBlockSpace(block), "block/" + LazyIdentifier.getBlockPath(block));
    }

    @Override
    protected BlockModelBuilder self() {
        return this;
    }

    public BlockModelBuilder model(ResourceLocation model) {
        this.model = Objects.requireNonNull(model, "model");
        return this;
    }

    public BlockModelBuilder texture(ResourceLocation all) {
        this.texture = Objects.requireNonNull(all, "all");
        return this;
    }

    public BlockModelBuilder cubeAll() {
        return this;
    }

    public BlockModelBuilder custom(ResourceLocation model) {
        this.stateModel = Objects.requireNonNull(model, "model");
        this.writeModel = false;
        return this;
    }

    public BlockModelBuilder facing() {
        this.facing = true;
        return this;
    }

    public BlockModelBuilder onlyState() {
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
            JsonObject modelJson = new JsonObject();
            modelJson.addProperty("parent", "minecraft:block/cube_all");
            JsonObject textures = new JsonObject();
            textures.addProperty("all", this.texture.toString());
            modelJson.add("textures", textures);
            this.context().submitBlock(this.model, modelJson);
        }
        ResourceLocation stateModelId = this.stateModel != null ? this.stateModel : this.model;
        if (this.writeState) {
            JsonObject stateJson = new JsonObject();
            JsonObject variants = new JsonObject();
            if (this.facing) {
                addVariant(variants, "facing=north", stateModelId, 0);
                addVariant(variants, "facing=east", stateModelId, 90);
                addVariant(variants, "facing=south", stateModelId, 180);
                addVariant(variants, "facing=west", stateModelId, 270);
            } else {
                addVariant(variants, "", stateModelId, 0);
            }
            stateJson.add("variants", variants);
            this.context().submitBlockState(this.blockId, stateJson);
        }
        if (this.writeItem) {
            JsonObject itemJson = new JsonObject();
            itemJson.addProperty("parent", stateModelId.toString());
            this.context().submitItem(this.blockId, itemJson);
        }
    }

    private static void addVariant(JsonObject variants, String key, ResourceLocation model, int y) {
        JsonObject variant = new JsonObject();
        variant.addProperty("model", model.toString());
        if (y != 0) {
            variant.addProperty("y", y);
        }
        variants.add(key, variant);
    }
}
