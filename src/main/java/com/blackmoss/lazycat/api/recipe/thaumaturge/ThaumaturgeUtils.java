package com.blackmoss.lazycat.api.recipe.thaumaturge;

import com.leclowndu93150.thaumaturge.api.aspect.AspectInstance;
import com.leclowndu93150.thaumaturge.api.aspect.IAspect;
import com.leclowndu93150.thaumaturge.registry.TTDataComponents;
import com.leclowndu93150.thaumaturge.registry.TTItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.common.crafting.DataComponentIngredient;

public class ThaumaturgeUtils {
    public static Ingredient aspectItem(HolderLookup.Provider registries, ResourceKey<IAspect> aspect, ItemLike item) {
        return DataComponentIngredient.of(
                TTDataComponents.CRYSTAL_ASPECT.get(),
                new AspectInstance(registries.lookupOrThrow(IAspect.REGISTRY_KEY).getOrThrow(aspect), 1),
                item);
    }

    public static Ingredient manaBean(HolderLookup.Provider registries, ResourceKey<IAspect> aspect) {
        return aspectItem(registries, aspect, TTItems.MANA_BEAN.get());
    }

    public static Ingredient phial(HolderLookup.Provider registries, ResourceKey<IAspect> aspect) {
        return aspectItem(registries, aspect, TTItems.PHIAL.get());
    }
}
