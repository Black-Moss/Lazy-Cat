package com.blackmoss.lazycat.api.condition;

import com.blackmoss.lazycat.api.LazyIdentifier;
import net.minecraft.core.Holder;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.common.conditions.ICondition;
import net.neoforged.neoforge.common.conditions.NeoForgeConditions;

import java.util.Objects;

final class Conditionals {
    static ICondition modLoaded(String modid) {
        Objects.requireNonNull(modid, "modid");
        if (modid.isBlank()) {
            throw new IllegalArgumentException("modid must not be blank");
        }
        return NeoForgeConditions.modLoaded(modid);
    }

    static ICondition not(ICondition condition) {
        return NeoForgeConditions.not(Objects.requireNonNull(condition, "condition"));
    }

    static ICondition or(ICondition... conditions) {
        Objects.requireNonNull(conditions, "conditions");
        if (conditions.length == 0) {
            throw new IllegalArgumentException("At least one condition is required");
        }
        for (ICondition condition : conditions) {
            Objects.requireNonNull(condition, "conditions contains null");
        }
        return NeoForgeConditions.or(conditions);
    }

    static ICondition itemExists(Identifier id) {
        Objects.requireNonNull(id, "item");
        return NeoForgeConditions.itemRegistered(id.getNamespace(), id.getPath());
    }

    static ICondition tagEmpty(TagKey<Item> tag) {
        return NeoForgeConditions.tagEmpty(Objects.requireNonNull(tag, "tag"));
    }

    static Identifier itemId(Object source) {
        Objects.requireNonNull(source, "item");
        return switch (source) {
            case ItemLike item -> itemId(item);
            case Identifier id -> id;
            case ResourceKey<?> key -> key.identifier();
            case Holder<?> holder -> itemId(holder);
            case String text -> text.contains(":")
                    ? Identifier.parse(text)
                    : Identifier.withDefaultNamespace(text);
            default -> throw new IllegalArgumentException("Unsupported item source: " + source.getClass().getName());
        };
    }

    private static Identifier itemId(ItemLike item) {
        return LazyIdentifier.getItemIdentifier(item);
    }

    private static Identifier itemId(Holder<?> holder) {
        return holder.unwrapKey()
                .map(ResourceKey::identifier)
                .orElseThrow(() -> new IllegalStateException("Item holder has no registry key"));
    }
}
