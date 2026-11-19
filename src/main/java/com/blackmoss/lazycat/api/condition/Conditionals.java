package com.blackmoss.lazycat.api.condition;

import com.blackmoss.lazycat.api.LazyIdentifier;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.common.conditions.ICondition;
import net.neoforged.neoforge.common.conditions.IConditionBuilder;

import java.util.Objects;

final class Conditionals {
    private static final IConditionBuilder HELPERS = new IConditionBuilder() {
    };

    static ICondition modLoaded(String modid) {
        Objects.requireNonNull(modid, "modid");
        if (modid.isBlank()) {
            throw new IllegalArgumentException("modid must not be blank");
        }
        return HELPERS.modLoaded(modid);
    }

    static ICondition not(ICondition condition) {
        return HELPERS.not(Objects.requireNonNull(condition, "condition"));
    }

    static ICondition or(ICondition... conditions) {
        Objects.requireNonNull(conditions, "conditions");
        if (conditions.length == 0) {
            throw new IllegalArgumentException("At least one condition is required");
        }
        for (ICondition condition : conditions) {
            Objects.requireNonNull(condition, "conditions contains null");
        }
        return HELPERS.or(conditions);
    }

    static ICondition itemExists(ResourceLocation id) {
        Objects.requireNonNull(id, "item");
        return HELPERS.itemExists(id.getNamespace(), id.getPath());
    }

    static ICondition tagEmpty(TagKey<Item> tag) {
        return HELPERS.tagEmpty(Objects.requireNonNull(tag, "tag"));
    }

    static ResourceLocation itemId(Object source) {
        Objects.requireNonNull(source, "item");
        return switch (source) {
            case ItemLike item -> itemId(item);
            case ResourceLocation id -> id;
            case ResourceKey<?> key -> key.location();
            case Holder<?> holder -> itemId(holder);
            case String text -> text.contains(":")
                    ? ResourceLocation.parse(text)
                    : ResourceLocation.withDefaultNamespace(text);
            default -> throw new IllegalArgumentException("Unsupported item source: " + source.getClass().getName());
        };
    }

    private static ResourceLocation itemId(ItemLike item) {
        return LazyIdentifier.getItemIdentifier(item);
    }

    private static ResourceLocation itemId(Holder<?> holder) {
        return holder.unwrapKey()
                .map(ResourceKey::location)
                .orElseThrow(() -> new IllegalStateException("Item holder has no registry key"));
    }
}
