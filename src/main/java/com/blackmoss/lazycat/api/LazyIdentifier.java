package com.blackmoss.lazycat.api;

import com.blackmoss.lazycat.LazyCat;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;

import java.util.Objects;

@SuppressWarnings("unused")
public final class LazyIdentifier {
    public static ResourceLocation lazyCat(String path) {
        return ResourceLocation.fromNamespaceAndPath(LazyCat.MODID, path);
    }

    public static ResourceLocation getBlockIdentifier(Block block) {
        Objects.requireNonNull(block, () -> "Unregistered block: " + block);
        return BuiltInRegistries.BLOCK.getKey(block);
    }

    public static String getBlockId(Block block) {
        return getBlockIdentifier(block).toString();
    }

    public static String getBlockSpace(Block block) {
        return getBlockIdentifier(block).getNamespace();
    }

    public static String getBlockPath(Block block) {
        return getBlockIdentifier(block).getPath();
    }

    public static ResourceLocation getItemIdentifier(ItemLike item) {
        Objects.requireNonNull(item, () -> "Unregistered item: " + item);
        return BuiltInRegistries.ITEM.getKey(item.asItem());
    }

    public static String getItemId(ItemLike item) {
        return getItemIdentifier(item).toString();
    }

    public static String getItemSpace(ItemLike item) {
        return getItemIdentifier(item).getNamespace();
    }

    public static String getItemPath(ItemLike item) {
        return getItemIdentifier(item).getPath();
    }
}
