package com.blackmoss.lazycat.recipe.thaumaturge;

import com.blackmoss.lazycat.api.recipe.RecipeContext;
import com.blackmoss.lazycat.api.recipe.thaumaturge.AbstractDustTriggerRecipeBuilder;
import com.leclowndu93150.thaumaturge.content.recipe.dust.DustTriggerTagRecipe;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeUnlockAdvancementBuilder;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

@SuppressWarnings("unused")
public final class DustTriggerTagBuilder extends AbstractDustTriggerRecipeBuilder<DustTriggerTagBuilder> {
    private @Nullable TagKey<Block> targetTag;

    public DustTriggerTagBuilder(RecipeContext context, Object result, Object targetTag) {
        super(context, result);
        this.targetTag = resolveTag(targetTag);
        defaultUnlock(resultIngredient());
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
                if (!Registries.BLOCK.equals(tag.registry())) {
                    throw new IllegalArgumentException("Not a block tag: " + tag.location());
                }
                yield (TagKey<Block>) tag;
            }
            case Identifier id -> TagKey.create(Registries.BLOCK, id);
            case String text -> TagKey.create(Registries.BLOCK, tagId(text));
            case ResourceKey<?> key -> TagKey.create(Registries.BLOCK, key.identifier());
            default -> throw new IllegalArgumentException(
                    "Unsupported dust trigger tag: " + source.getClass().getName());
        };
    }

    private Identifier tagId(String text) {
        return text.indexOf(':') >= 0
                ? Identifier.parse(text)
                : Identifier.fromNamespaceAndPath(context().modid(), text);
    }

    @Override
    protected void validate() {
        super.validate();
        if (targetTag == null) {
            throw new IllegalStateException("Dust trigger recipe requires a target tag");
        }
    }

    @Override
    protected void build(RecipeOutput output, ResourceKey<Recipe<?>> key) {
        DustTriggerTagRecipe recipe = new DustTriggerTagRecipe(targetTag, result(), gate());
        RecipeUnlockAdvancementBuilder advancement = new RecipeUnlockAdvancementBuilder();
        applyCriteria(advancement::unlockedBy);
        output.accept(key, recipe, advancement.build(output, key, category()));
    }
}
