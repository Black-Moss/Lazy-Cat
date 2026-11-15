package com.blackmoss.lazycat.api.recipe;

import com.blackmoss.lazycat.api.LazyIngredient;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.criterion.ImpossibleTrigger;
import net.minecraft.advancements.criterion.InventoryChangeTrigger;
import net.minecraft.advancements.criterion.ItemPredicate;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

import java.util.Objects;

public final class RecipeContext {
    private final HolderLookup.Provider lookupProvide;
    private final HolderGetter<Item> items;
    private final RecipeOutput output;
    private final String space;

    private RecipeContext(
            HolderLookup.Provider registries,
            HolderGetter<Item> items,
            RecipeOutput output,
            String namespace) {
        lookupProvide = registries;
        this.items = items;
        this.output = output;
        space = namespace;
    }

    public static RecipeContext of(
            HolderLookup.Provider registries,
            RecipeOutput output,
            String namespace) {
        Objects.requireNonNull(registries, "lookupProvide");
        Objects.requireNonNull(output, "output");
        Objects.requireNonNull(namespace, "namespace");
        if (namespace.isBlank()) {
            throw new IllegalArgumentException("namespace must not be blank");
        }
        return new RecipeContext(
                registries,
                registries.lookupOrThrow(Registries.ITEM),
                output,
                namespace);
    }

    public HolderLookup.Provider lookupProvide() {
        return lookupProvide;
    }

    public HolderGetter<Item> items() {
        return items;
    }

    public RecipeOutput output() {
        return output;
    }

    public String namespace() {
        return space;
    }

    public LazyIngredient ingredient(Object source) {
        return LazyIngredient.from(source);
    }

    public String id(Object source) {
        return LazyIngredient.from(source).label();
    }

    public Criterion<?> has(Object source) {
        Objects.requireNonNull(source, "source");
        if (source instanceof TagKey<?> tag) {
            return tag.cast(Registries.ITEM)
                    .map(itemTag -> InventoryChangeTrigger.TriggerInstance.hasItems(
                            ItemPredicate.Builder.item().of(items, itemTag)))
                    .orElseThrow(() -> new IllegalArgumentException("Not an item tag: " + tag));
        }
        //noinspection deprecation
        var first = LazyIngredient.from(source).resolve(items).items().findFirst();
        if (first.isPresent()) {
            return InventoryChangeTrigger.TriggerInstance.hasItems(
                    ItemPredicate.Builder.item().of(items, first.get().value()));
        }
        return impossible();
    }

    public Criterion<?> impossible() {
        return CriteriaTriggers.IMPOSSIBLE.createCriterion(new ImpossibleTrigger.TriggerInstance());
    }
}
