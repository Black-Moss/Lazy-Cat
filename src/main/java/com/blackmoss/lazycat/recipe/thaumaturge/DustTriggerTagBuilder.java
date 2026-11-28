package com.blackmoss.lazycat.recipe.thaumaturge;

import com.blackmoss.lazycat.api.recipe.RecipeContext;
import com.blackmoss.lazycat.api.recipe.thaumaturge.AbstractDustTriggerRecipeBuilder;
import com.leclowndu93150.thaumaturge.content.recipe.dust.DustTriggerTagRecipe;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

@SuppressWarnings("unused")
public final class DustTriggerTagBuilder extends AbstractDustTriggerRecipeBuilder<DustTriggerTagBuilder> {
    private @Nullable TagKey<Block> targetTag;

    public DustTriggerTagBuilder(RecipeContext context, Object result, Object targetTag) {
        super(context, result);
        this.targetTag = resolveTag(targetTag);
    }

    @Override
    protected DustTriggerTagBuilder self() {
        return this;
    }

    public DustTriggerTagBuilder targetTag(Object source) {
        this.targetTag = resolveTag(source);
        return this;
    }

    public DustTriggerTagBuilder targetTag(String tagId) {
        return targetTag((Object) tagId);
    }

    @SuppressWarnings("unchecked")
    private TagKey<Block> resolveTag(Object source) {
        Objects.requireNonNull(source, "tag");
        return switch (source) {
            case TagKey<?> tag -> {
                if (!tag.isFor(Registries.BLOCK)) {
                    throw new IllegalArgumentException("Not a block tag: " + tag.location());
                }
                yield (TagKey<Block>) tag;
            }
            case ResourceLocation id -> TagKey.create(Registries.BLOCK, id);
            case String text -> TagKey.create(Registries.BLOCK, tagId(text));
            case ResourceKey<?> key -> TagKey.create(Registries.BLOCK, key.location());
            default -> throw new IllegalArgumentException(
                    "Unsupported dust trigger tag: " + source.getClass().getName());
        };
    }

    private ResourceLocation tagId(String text) {
        return text.indexOf(':') >= 0
                ? ResourceLocation.parse(text)
                : ResourceLocation.fromNamespaceAndPath(context().modid(), text);
    }

    @Override
    protected void validate() {
        if (targetTag == null) {
            throw new IllegalStateException("Dust trigger recipe requires a target tag");
        }
    }

    @Override
    protected void build(RecipeOutput output, ResourceLocation id) {
        DustTriggerTagRecipe recipe = new DustTriggerTagRecipe(targetTag, result(), gate());
        output.accept(id, recipe, null);
    }
}
