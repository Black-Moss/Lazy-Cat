package com.blackmoss.lazycat.api.recipe.mekanism;

import com.blackmoss.lazycat.api.LazyIngredient;
import com.blackmoss.lazycat.api.recipe.AbstractRecipeBuilder;
import com.blackmoss.lazycat.api.recipe.RecipeContext;
import mekanism.api.MekanismAPI;
import mekanism.api.chemical.Chemical;
import mekanism.api.chemical.ChemicalStackTemplate;
import mekanism.api.recipes.ingredients.ChemicalStackIngredient;
import mekanism.api.recipes.ingredients.FluidStackIngredient;
import mekanism.api.recipes.ingredients.ItemStackIngredient;
import mekanism.api.recipes.ingredients.creator.IngredientCreatorAccess;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.FluidStackTemplate;

import java.util.Objects;

@SuppressWarnings("unchecked")
public abstract class AbstractMekanismRecipeBuilder<B extends AbstractMekanismRecipeBuilder<B>>
        extends AbstractRecipeBuilder<B> {

    protected AbstractMekanismRecipeBuilder(RecipeContext context, Object result) {
        super(context, result == null ? null : com.blackmoss.lazycat.api.LazyIngredient.from(result));
    }

    protected final String sourcePath(Object source, String field) {
        Objects.requireNonNull(source, field);
        String path = switch (source) {
            case String text -> sanitize(text);
            case Identifier identifier -> identifier.getPath();
            case ResourceKey<?> key -> sanitize(key.identifier().toString());
            case TagKey<?> tag -> sanitize(tag.location().toString());
            case Holder<?> holder -> holder.unwrapKey()
                    .map(key -> sanitize(key.identifier().toString()))
                    .orElseThrow(() -> new IllegalStateException(field + " holder has no registry key"));
            case net.minecraft.world.level.ItemLike item -> LazyIngredient.from(item).label();
            default -> throw new IllegalArgumentException(
                    "Unsupported " + field + " source: " + source.getClass().getName());
        };
        if (path.isBlank()) {
            throw new IllegalStateException(field + " has an empty id path");
        }
        return path;
    }

    protected ItemStackIngredient itemIngredient(Object source) {
        Objects.requireNonNull(source, "item source");
        return IngredientCreatorAccess.item().from(LazyIngredient.from(source).resolve(context().items()));
    }

    protected ChemicalStackIngredient chemicalIngredient(Object source, int amount) {
        Objects.requireNonNull(source, "chemical source");
        if (amount < 1) {
            throw new IllegalArgumentException("chemical amount must be positive, was " + amount);
        }
        HolderGetter<Chemical> chemicals = context().registries().lookupOrThrow(MekanismAPI.CHEMICAL_REGISTRY_NAME);
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

    protected FluidStackIngredient fluidIngredient(Object source, int amount) {
        Objects.requireNonNull(source, "fluid source");
        if (amount < 1) {
            throw new IllegalArgumentException("fluid amount must be positive, was " + amount);
        }
        HolderGetter<Fluid> fluids = context().registries().lookupOrThrow(Registries.FLUID);
        return switch (source) {
            case TagKey<?> tag -> IngredientCreatorAccess.fluid().from(fluids, (TagKey<Fluid>) tag, amount);
            case Holder<?> h -> IngredientCreatorAccess.fluid().fromHolder((Holder<Fluid>) h, amount);
            case ResourceKey<?> k ->
                    IngredientCreatorAccess.fluid().fromHolder(fluids.getOrThrow((ResourceKey<Fluid>) k), amount);
            case String s -> IngredientCreatorAccess.fluid().fromHolder(
                    fluids.getOrThrow(ResourceKey.create(Registries.FLUID, Identifier.parse(s))), amount);
            default ->
                    throw new IllegalArgumentException("Unsupported Mekanism fluid source: " + source.getClass().getName());
        };
    }

    protected ItemStackTemplate itemTemplate(Object source, int count) {
        return com.blackmoss.lazycat.api.LazyIngredient.from(source)
                .count(count)
                .template(context().registries().lookupOrThrow(Registries.ITEM));
    }

    protected ChemicalStackTemplate chemicalTemplate(Object source, int amount) {
        Objects.requireNonNull(source, "chemical source");
        if (amount < 1) {
            throw new IllegalArgumentException("chemical amount must be positive, was " + amount);
        }
        HolderGetter<Chemical> chemicals = context().registries().lookupOrThrow(MekanismAPI.CHEMICAL_REGISTRY_NAME);
        Holder<Chemical> holder = switch (source) {
            case Holder<?> h -> (Holder<Chemical>) h;
            case ResourceKey<?> k -> chemicals.getOrThrow((ResourceKey<Chemical>) k);
            case String s ->
                    chemicals.getOrThrow(ResourceKey.create(MekanismAPI.CHEMICAL_REGISTRY_NAME, Identifier.parse(s)));
            default ->
                    throw new IllegalArgumentException("Unsupported Mekanism chemical source: " + source.getClass().getName());
        };
        return new ChemicalStackTemplate(holder, amount);
    }

    protected FluidStackTemplate fluidTemplate(Object source, int amount) {
        Objects.requireNonNull(source, "fluid source");
        if (amount < 1) {
            throw new IllegalArgumentException("fluid amount must be positive, was " + amount);
        }
        HolderGetter<Fluid> fluids = context().registries().lookupOrThrow(Registries.FLUID);
        Holder<Fluid> holder = switch (source) {
            case Holder<?> h -> (Holder<Fluid>) h;
            case ResourceKey<?> k -> fluids.getOrThrow((ResourceKey<Fluid>) k);
            case String s -> fluids.getOrThrow(ResourceKey.create(Registries.FLUID, Identifier.parse(s)));
            default ->
                    throw new IllegalArgumentException("Unsupported Mekanism fluid source: " + source.getClass().getName());
        };
        return new FluidStackTemplate(holder, amount);
    }
}
