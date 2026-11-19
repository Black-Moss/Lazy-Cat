package com.blackmoss.lazycat.api.thaumaturge.blueprint;

import com.leclowndu93150.thaumaturge.api.recipe.BlueprintSource;
import com.leclowndu93150.thaumaturge.api.recipe.BlueprintTarget;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Objects;

@SuppressWarnings("unused")
public final class Blueprints {
    private Blueprints() {
    }

    public static BlueprintSource source(Block block) {
        return new BlueprintSource.BlockSource(Objects.requireNonNull(block, "block"));
    }

    public static BlueprintSource source(BlockState state) {
        return new BlueprintSource.StateSource(Objects.requireNonNull(state, "state"));
    }

    public static BlueprintSource source(TagKey<Block> tag) {
        return new BlueprintSource.TagSource(Objects.requireNonNull(tag, "tag"));
    }

    public static BlueprintTarget targetKeep() {
        return BlueprintTarget.Keep.INSTANCE;
    }

    public static BlueprintTarget targetAir() {
        return BlueprintTarget.Air.INSTANCE;
    }

    public static BlueprintTarget target(Block block) {
        return new BlueprintTarget.BlockTarget(Objects.requireNonNull(block, "block"), false, false);
    }

    public static BlueprintTarget target(Block block, boolean applyPlayerFacing, boolean opposite) {
        return new BlueprintTarget.BlockTarget(Objects.requireNonNull(block, "block"), applyPlayerFacing, opposite);
    }

    public static BlueprintTarget target(BlockState state) {
        return new BlueprintTarget.StateTarget(Objects.requireNonNull(state, "state"));
    }

    public static BlueprintTarget target(ItemStack stack) {
        return new BlueprintTarget.StackTarget(Objects.requireNonNull(stack, "stack"));
    }

    @SuppressWarnings("unchecked")
    public static BlueprintSource sourceOf(Object source) {
        Objects.requireNonNull(source, "source");
        return switch (source) {
            case BlueprintSource blueprintSource -> blueprintSource;
            case Block block -> source(block);
            case BlockState state -> source(state);
            case TagKey<?> tag -> source((TagKey<Block>) tag);
            default -> throw new IllegalArgumentException(
                    "Unsupported blueprint source: " + source.getClass().getName());
        };
    }

    public static BlueprintTarget targetOf(Object target) {
        Objects.requireNonNull(target, "target");
        return switch (target) {
            case BlueprintTarget blueprintTarget -> blueprintTarget;
            case Block block -> target(block);
            case BlockState state -> target(state);
            case ItemStack stack -> target(stack);
            default -> throw new IllegalArgumentException(
                    "Unsupported blueprint target: " + target.getClass().getName());
        };
    }
}
