package com.blackmoss.lazycat.api.recipe.mekanism;

import com.blackmoss.lazycat.api.recipe.AbstractRecipeBuilder;
import com.blackmoss.lazycat.api.recipe.RecipeContext;
import mekanism.api.MekanismAPI;
import mekanism.api.chemical.Chemical;
import mekanism.api.recipes.ingredients.ChemicalStackIngredient;
import mekanism.api.recipes.ingredients.ItemStackIngredient;
import mekanism.api.recipes.ingredients.creator.IngredientCreatorAccess;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;

import java.util.Objects;

public abstract class AbstractMekanismRecipeBuilder<B extends AbstractMekanismRecipeBuilder<B>>
        extends AbstractRecipeBuilder<B> {

    protected AbstractMekanismRecipeBuilder(RecipeContext context, ItemLike result) {
        super(context, result);
    }

    protected ItemStackIngredient itemIngredient(Object source) {
        Objects.requireNonNull(source, "item source");
        HolderGetter<Item> items = context().lookupProvide().lookupOrThrow(Registries.ITEM);
        switch (source) {
            case ItemLike item -> {
                return IngredientCreatorAccess.item().from(item);
            }
            case TagKey<?> tag -> {
                //noinspection unchecked
                return IngredientCreatorAccess.item().from(items, (TagKey<Item>) tag);
            }
            case String s -> {
                ResourceKey<Item> key = ResourceKey.create(Registries.ITEM, Identifier.parse(s));
                return IngredientCreatorAccess.item().from(items.getOrThrow(key).value());
            }
            default -> {
            }
        }
        throw new IllegalArgumentException("Unsupported Mekanism item source: " + source.getClass().getName());
    }

    @SuppressWarnings("unchecked")
    protected ChemicalStackIngredient chemicalIngredient(Object source, int amount) {
        Objects.requireNonNull(source, "chemical source");
        if (amount < 1) {
            throw new IllegalArgumentException("chemical amount must be positive, was " + amount);
        }
        HolderGetter<Chemical> chemicals = context().lookupProvide().lookupOrThrow(MekanismAPI.CHEMICAL_REGISTRY_NAME);
        return switch (source) {
            case TagKey<?> tag ->
                    IngredientCreatorAccess.chemicalStack().from(chemicals, (TagKey<Chemical>) tag, amount);
            case Holder<?> h -> IngredientCreatorAccess.chemicalStack().fromHolder((Holder<Chemical>) h, amount);
            case ResourceKey<?> k ->
                    IngredientCreatorAccess.chemicalStack().fromHolder(chemicals.getOrThrow((ResourceKey<Chemical>) k), amount);
            case String s -> IngredientCreatorAccess.chemicalStack().fromHolder(
                    chemicals.getOrThrow(ResourceKey.create(MekanismAPI.CHEMICAL_REGISTRY_NAME, Identifier.parse(s))), amount);
            default ->
                    throw new IllegalArgumentException("Unsupported Mekanism chemical source: " + source.getClass().getName());
        };
    }
}
