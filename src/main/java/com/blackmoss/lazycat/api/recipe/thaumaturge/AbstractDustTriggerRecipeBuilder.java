package com.blackmoss.lazycat.api.recipe.thaumaturge;

import com.blackmoss.lazycat.api.recipe.AbstractRecipeBuilder;
import com.blackmoss.lazycat.api.recipe.RecipeContext;
import com.leclowndu93150.thaumaturge.api.recipe.ResearchGate;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.Optional;

@SuppressWarnings("unused")
public abstract class AbstractDustTriggerRecipeBuilder<B extends AbstractDustTriggerRecipeBuilder<B>>
        extends AbstractRecipeBuilder<B> {
    private @Nullable ResearchGate gate;

    protected AbstractDustTriggerRecipeBuilder(RecipeContext context, Object result) {
        super(context, result);
    }

    protected final Block resolveBlock(Object source) {
        Objects.requireNonNull(source, "block");
        return switch (source) {
            case Block block -> block;
            case BlockState state -> state.getBlock();
            case ItemLike item -> {
                Block block = Block.byItem(item.asItem());
                if (block == Blocks.AIR) {
                    throw new IllegalArgumentException("Item is not a block: " + item.asItem());
                }
                yield block;
            }
            case ResourceLocation id -> blockById(id);
            case String text -> blockById(ResourceLocation.parse(text));
            case ResourceKey<?> key -> blockById(key.location());
            default -> throw new IllegalArgumentException(
                    "Unsupported dust trigger target: " + source.getClass().getName());
        };
    }

    private Block blockById(ResourceLocation id) {
        ResourceKey<Block> key = ResourceKey.create(Registries.BLOCK, id);
        return context().lookupProvide()
                .lookupOrThrow(Registries.BLOCK)
                .get(key)
                .map(Holder::value)
                .orElseThrow(() -> new IllegalArgumentException("Unknown block: " + id));
    }

    protected final Optional<ResearchGate> gate() {
        return Optional.ofNullable(gate);
    }

    @Override
    protected String defaultFolder() {
        return "dust_trigger";
    }

    public B gate(ResearchGate gate) {
        this.gate = Objects.requireNonNull(gate, "gate");
        return self();
    }

    public B gate(ResourceLocation entry) {
        return gate(new ResearchGate(entry, Optional.empty(), false));
    }

    public B gate(String entry) {
        return gate(ResourceLocation.parse(entry));
    }
}
