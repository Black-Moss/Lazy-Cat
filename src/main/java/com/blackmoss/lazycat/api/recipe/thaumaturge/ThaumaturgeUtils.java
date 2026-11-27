package com.blackmoss.lazycat.api.recipe.thaumaturge;

import com.leclowndu93150.thaumaturge.api.aspect.AspectInstance;
import com.leclowndu93150.thaumaturge.api.aspect.IAspect;
import com.leclowndu93150.thaumaturge.registry.TTDataComponents;
import com.leclowndu93150.thaumaturge.registry.TTItems;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.common.crafting.DataComponentIngredient;

import java.util.Objects;

@SuppressWarnings("unused")
public final class ThaumaturgeUtils {
    public static Ingredient aspectItem(HolderLookup.Provider registries, ResourceKey<IAspect> aspect, ItemLike item) {
        return DataComponentIngredient.of(false,
                TTDataComponents.CRYSTAL_ASPECT.get(),
                new AspectInstance(registries.lookupOrThrow(IAspect.REGISTRY_KEY).getOrThrow(aspect), 1),
                item);
    }

    public static Ingredient crystal(HolderLookup.Provider registries, ResourceKey<IAspect> aspect) {
        return aspectItem(registries, aspect, TTItems.ESSENTIA_CRYSTAL.get());
    }

    public static Ingredient manaBean(HolderLookup.Provider registries, ResourceKey<IAspect> aspect) {
        return aspectItem(registries, aspect, TTItems.MANA_BEAN.get());
    }

    public static Ingredient phial(HolderLookup.Provider registries, ResourceKey<IAspect> aspect) {
        return aspectItem(registries, aspect, TTItems.PHIAL.get());
    }

    public static ItemStack aspectItem(Holder<IAspect> aspect, ItemLike item) {
        return aspectItem(aspect, item, 1);
    }

    public static ItemStack aspectItem(Holder<IAspect> aspect, ItemLike item, int count) {
        Objects.requireNonNull(aspect, "aspect");
        Objects.requireNonNull(item, "item");
        if (count < 1 || count > 64) {
            throw new IllegalArgumentException("count must be between 1 and 64");
        }
        ItemStack stack = new ItemStack(item, count);
        stack.set(TTDataComponents.CRYSTAL_ASPECT.get(), new AspectInstance(aspect, 1));
        return stack;
    }

    public static ItemStack crystal(Holder<IAspect> aspect) {
        return crystal(aspect, 1);
    }

    public static ItemStack crystal(Holder<IAspect> aspect, int count) {
        return aspectItem(aspect, TTItems.ESSENTIA_CRYSTAL.get(), count);
    }

    public static ItemStack manaBean(Holder<IAspect> aspect) {
        return manaBean(aspect, 1);
    }

    public static ItemStack manaBean(Holder<IAspect> aspect, int count) {
        return aspectItem(aspect, TTItems.MANA_BEAN.get(), count);
    }

    public static ItemStack phial(Holder<IAspect> aspect) {
        return phial(aspect, 1);
    }

    public static ItemStack phial(Holder<IAspect> aspect, int count) {
        return aspectItem(aspect, TTItems.PHIAL.get(), count);
    }
}