package com.blackmoss.lazycat.api.recipe.thaumaturge;

import com.leclowndu93150.thaumaturge.api.aspect.AspectInstance;
import com.leclowndu93150.thaumaturge.api.aspect.IAspect;
import com.leclowndu93150.thaumaturge.registry.TTDataComponents;
import com.leclowndu93150.thaumaturge.registry.TTItems;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.common.crafting.DataComponentIngredient;

import java.util.Objects;

@SuppressWarnings("unused")
public final class ThaumaturgeUtils {
    public static Ingredient aspectItem(HolderLookup.Provider registries, ResourceKey<IAspect> aspect, ItemLike item) {
        return DataComponentIngredient.of(
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

    public static ItemStackTemplate aspectItem(Holder<IAspect> aspect, ItemLike item) {
        Objects.requireNonNull(aspect, "aspect");
        Objects.requireNonNull(item, "item");
        return new ItemStackTemplate(
                item.asItem(),
                1,
                DataComponentPatch.builder()
                        .set(TTDataComponents.CRYSTAL_ASPECT.get(), new AspectInstance(aspect, 1))
                        .build());
    }

    public static ItemStackTemplate crystal(Holder<IAspect> aspect) {
        return aspectItem(aspect, TTItems.ESSENTIA_CRYSTAL.get());
    }

    public static ItemStackTemplate manaBean(Holder<IAspect> aspect) {
        return aspectItem(aspect, TTItems.MANA_BEAN.get());
    }

    public static ItemStackTemplate phial(Holder<IAspect> aspect) {
        return aspectItem(aspect, TTItems.PHIAL.get());
    }
}
