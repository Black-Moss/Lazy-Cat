package com.blackmoss.lazycat.api;

import com.blackmoss.lazycat.LazyCat;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;

import java.util.Objects;

@SuppressWarnings("unused")
public final class LazyIdentifier {
    public static Identifier lazyCat(String path) {
        return Identifier.fromNamespaceAndPath(LazyCat.MODID, path);
    }

    public static Identifier getBlockIdentifier(Block block) {
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

    public static Identifier getItemIdentifier(ItemLike item) {
        Objects.requireNonNull(item, () -> "Unregistered item: " + item);
        return BuiltInRegistries.ITEM.getKey(item.asItem());
    }

    public static String getItemId(ItemLike item) {
        return getItemIdentifier(item.asItem()).toString();
    }

    public static String getItemSpace(ItemLike item) {
        return getItemIdentifier(item).getNamespace();
    }

    public static String getItemPath(ItemLike item) {
        return getItemIdentifier(item).getPath();
    }

    public static <R> Identifier getIdentifier(Registry<R> registry, R value) {
        Objects.requireNonNull(registry, "registry");
        Objects.requireNonNull(value, "value");
        return Objects.requireNonNull(registry.getKey(value), () -> "Unregistered value: " + value);
    }

    public static <R> TagKey<R> getTag(ResourceKey<Registry<R>> registry, Identifier id) {
        return TagKey.create(Objects.requireNonNull(registry, "registry"), Objects.requireNonNull(id, "id"));
    }

    public static <R> TagKey<R> getTag(ResourceKey<Registry<R>> registry, String namespace, String path) {
        return getTag(registry, Identifier.fromNamespaceAndPath(namespace, path));
    }

    public static TagKey<Item> getItemTag(Identifier id) {
        return getTag(Registries.ITEM, id);
    }

    public static TagKey<Item> getItemTag(String namespace, String path) {
        return getItemTag(Identifier.fromNamespaceAndPath(namespace, path));
    }

    public static TagKey<Item> getItemTag(String path) {
        return getItemTag("c", path);
    }

    public static TagKey<Block> getBlockTag(Identifier id) {
        return getTag(Registries.BLOCK, id);
    }

    public static TagKey<Block> getBlockTag(String namespace, String path) {
        return getBlockTag(Identifier.fromNamespaceAndPath(namespace, path));
    }

    public static TagKey<Block> getBlockTag(String path) {
        return getBlockTag("c", path);
    }
}
