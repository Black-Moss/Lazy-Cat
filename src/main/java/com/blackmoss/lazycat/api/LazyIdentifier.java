package com.blackmoss.lazycat.api;

import com.blackmoss.lazycat.LazyCat;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;

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

    public static TagKey<Item> getItemTag(String id) {
        return getItemTag(Identifier.parse(id));
    }

    public static TagKey<Item> getItemTag(String namespace, String path) {
        return getItemTag(Identifier.fromNamespaceAndPath(namespace, path));
    }

    public static TagKey<Item> getCommonItemTag(String path) {
        return getItemTag("c", path);
    }

    public static TagKey<Item> getDefaultItemTag(String path) {
        return getItemTag(Identifier.withDefaultNamespace(path));
    }

    public static TagKey<Block> getBlockTag(Identifier id) {
        return getTag(Registries.BLOCK, id);
    }

    public static TagKey<Block> getBlockTag(String id) {
        return getBlockTag(Identifier.parse(id));
    }

    public static TagKey<Block> getBlockTag(String namespace, String path) {
        return getBlockTag(Identifier.fromNamespaceAndPath(namespace, path));
    }

    public static TagKey<Block> getCommonBlockTag(String path) {
        return getBlockTag("c", path);
    }

    public static TagKey<Block> getDefaultBlockTag(String path) {
        return getBlockTag(Identifier.withDefaultNamespace(path));
    }

    public static TagKey<EntityType<?>> getEntityTag(Identifier id) {
        return getTag(Registries.ENTITY_TYPE, id);
    }

    public static TagKey<EntityType<?>> getEntityTag(String id) {
        return getEntityTag(Identifier.parse(id));
    }

    public static TagKey<EntityType<?>> getEntityTag(String namespace, String path) {
        return getEntityTag(Identifier.fromNamespaceAndPath(namespace, path));
    }

    public static TagKey<EntityType<?>> getCommonEntityTag(String path) {
        return getEntityTag("c", path);
    }

    public static TagKey<EntityType<?>> getDefaultEntityTag(String path) {
        return getEntityTag(Identifier.withDefaultNamespace(path));
    }

    public static TagKey<Fluid> getFluidTag(Identifier id) {
        return getTag(Registries.FLUID, id);
    }

    public static TagKey<Fluid> getFluidTag(String id) {
        return getFluidTag(Identifier.parse(id));
    }

    public static TagKey<Fluid> getFluidTag(String namespace, String path) {
        return getFluidTag(Identifier.fromNamespaceAndPath(namespace, path));
    }

    public static TagKey<Fluid> getCommonFluidTag(String path) {
        return getFluidTag("c", path);
    }

    public static TagKey<Fluid> getDefaultFluidTag(String path) {
        return getFluidTag(Identifier.withDefaultNamespace(path));
    }

    public static TagKey<MobEffect> getEffectTag(Identifier id) {
        return getTag(Registries.MOB_EFFECT, id);
    }

    public static TagKey<MobEffect> getEffectTag(String id) {
        return getEffectTag(Identifier.parse(id));
    }

    public static TagKey<MobEffect> getEffectTag(String namespace, String path) {
        return getEffectTag(Identifier.fromNamespaceAndPath(namespace, path));
    }

    public static TagKey<MobEffect> getCommonEffectTag(String path) {
        return getEffectTag("c", path);
    }

    public static TagKey<MobEffect> getDefaultEffectTag(String path) {
        return getEffectTag(Identifier.withDefaultNamespace(path));
    }
}
