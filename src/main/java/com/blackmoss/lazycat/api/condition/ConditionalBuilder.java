package com.blackmoss.lazycat.api.condition;

import com.blackmoss.lazycat.api.LazyIdentifier;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.JsonOps;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.common.conditions.ICondition;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

@SuppressWarnings({"unused", "UnusedReturnValue"})
public interface ConditionalBuilder<T> {
    List<ICondition> conditionList();

    default T when(ICondition... conditions) {
        Objects.requireNonNull(conditions, "conditions");
        for (ICondition condition : conditions) {
            conditionList().add(Objects.requireNonNull(condition, "conditions contains null"));
        }
        return typedThis();
    }

    default T when(Collection<? extends ICondition> conditions) {
        Objects.requireNonNull(conditions, "conditions");
        for (ICondition condition : conditions) {
            when(condition);
        }
        return typedThis();
    }

    default T whenNot(ICondition condition) {
        return when(Conditionals.not(Objects.requireNonNull(condition, "condition")));
    }

    default T ifAny(ICondition... conditions) {
        Objects.requireNonNull(conditions, "conditions");
        return when(Conditionals.or(conditions));
    }

    default T ifModLoaded(String... modids) {
        Objects.requireNonNull(modids, "modids");
        for (String modid : modids) {
            when(Conditionals.modLoaded(Objects.requireNonNull(modid, "modids contains null")));
        }
        return typedThis();
    }

    default T unlessModLoaded(String... modids) {
        Objects.requireNonNull(modids, "modids");
        for (String modid : modids) {
            when(Conditionals.not(Conditionals.modLoaded(Objects.requireNonNull(modid, "modids contains null"))));
        }
        return typedThis();
    }

    default T ifItemExists(Object item) {
        return when(Conditionals.itemExists(Conditionals.itemId(item)));
    }

    default T ifItemMissing(Object item) {
        return when(Conditionals.not(Conditionals.itemExists(Conditionals.itemId(item))));
    }

    default T ifTagEmpty(String id) {
        return ifTagEmpty(LazyIdentifier.getItemTag(id));
    }

    default T ifTagEmpty(TagKey<Item> tag) {
        return when(Conditionals.tagEmpty(Objects.requireNonNull(tag, "tag")));
    }

    default T ifTagNotEmpty(String id) {
        return ifTagNotEmpty(LazyIdentifier.getItemTag(id));
    }

    default T ifTagNotEmpty(TagKey<Item> tag) {
        return when(Conditionals.not(Conditionals.tagEmpty(Objects.requireNonNull(tag, "tag"))));
    }

    default boolean hasConditions() {
        return !conditionList().isEmpty();
    }

    default List<ICondition> conditions() {
        return List.copyOf(conditionList());
    }

    default ICondition[] conditionsArray() {
        return conditionList().toArray(ICondition[]::new);
    }

    default void writeConditions(JsonObject json) {
        writeConditions(JsonOps.INSTANCE, json);
    }

    default void writeConditions(DynamicOps<JsonElement> ops, JsonObject json) {
        Objects.requireNonNull(ops, "ops");
        Objects.requireNonNull(json, "json");
        if (hasConditions()) {
            ICondition.writeConditions(ops, json, conditionList());
        }
    }

    @SuppressWarnings("unchecked")
    private T typedThis() {
        return (T) this;
    }
}
