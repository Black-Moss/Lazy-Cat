package com.blackmoss.lazycat.api.recipe;

import com.blackmoss.lazycat.api.LazyIngredient;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.ImpossibleTrigger;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.Objects;

public final class RecipeContext {
    private final HolderLookup.Provider registries;
    private final HolderGetter<Item> items;
    private final RecipeOutput output;
    private final String modid;

    private RecipeContext(
            HolderLookup.Provider registries,
            HolderGetter<Item> items,
            RecipeOutput output,
            String modid) {
        this.registries = registries;
        this.items = items;
        this.output = output;
        this.modid = modid;
    }

    public static RecipeContext of(
            HolderLookup.Provider registries,
            RecipeOutput output,
            String modid) {
        Objects.requireNonNull(registries, "registries");
        Objects.requireNonNull(output, "output");
        Objects.requireNonNull(modid, "modid");
        if (modid.isBlank()) {
            throw new IllegalArgumentException("modid must not be blank");
        }
        return new RecipeContext(
                registries,
                registries.lookupOrThrow(Registries.ITEM),
                output,
                modid);
    }

    public HolderLookup.Provider lookupProvide() {
        return this.registries;
    }

    public HolderGetter<Item> items() {
        return items;
    }

    public RecipeOutput output() {
        return output;
    }

    public String modid() {
        return modid;
    }

    public LazyIngredient ingredient(Object source) {
        return LazyIngredient.from(source);
    }

    public String id(Object source) {
        return LazyIngredient.from(source).label();
    }

    public Criterion<?> has(Object source) {
        Objects.requireNonNull(source, "source");
        LazyIngredient ingredient = LazyIngredient.from(source);
        if (ingredient.tag().isPresent()) {
            return InventoryChangeTrigger.TriggerInstance.hasItems(
                    ItemPredicate.Builder.item().of(ingredient.tag().get()));
        }
        ItemStack[] stacks = ingredient.resolve(items).getItems();
        if (stacks.length > 0 && !stacks[0].isEmpty()) {
            return InventoryChangeTrigger.TriggerInstance.hasItems(
                    ItemPredicate.Builder.item().of(stacks[0].getItem()));
        }
        return impossible();
    }

    public Criterion<?> impossible() {
        return CriteriaTriggers.IMPOSSIBLE.createCriterion(new ImpossibleTrigger.TriggerInstance());
    }
}
