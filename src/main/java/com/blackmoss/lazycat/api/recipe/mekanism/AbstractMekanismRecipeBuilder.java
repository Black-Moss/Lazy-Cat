package com.blackmoss.lazycat.api.recipe.mekanism;

import com.blackmoss.lazycat.api.LazyIngredient;
import com.blackmoss.lazycat.api.recipe.AbstractRecipeBuilder;
import com.blackmoss.lazycat.api.recipe.RecipeContext;
import mekanism.api.MekanismAPI;
import mekanism.api.chemical.Chemical;
import mekanism.api.chemical.ChemicalStack;
import mekanism.api.recipes.ingredients.ChemicalStackIngredient;
import mekanism.api.recipes.ingredients.FluidStackIngredient;
import mekanism.api.recipes.ingredients.ItemStackIngredient;
import mekanism.api.recipes.ingredients.creator.IngredientCreatorAccess;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.FluidStack;

import java.util.Objects;

@SuppressWarnings("unchecked")
public abstract class AbstractMekanismRecipeBuilder<B extends AbstractMekanismRecipeBuilder<B>>
        extends AbstractRecipeBuilder<B> {

    protected AbstractMekanismRecipeBuilder(RecipeContext context, Object result) {
        super(context, result);
    }

    protected final HolderGetter<Chemical> chemicals() {
        return context().lookupProvide().lookupOrThrow(MekanismAPI.CHEMICAL_REGISTRY_NAME);
    }

    protected final HolderGetter<Fluid> fluids() {
        return context().lookupProvide().lookupOrThrow(Registries.FLUID);
    }

    protected ItemStackIngredient itemIngredient(Object source) {
        Objects.requireNonNull(source, "item source");
        LazyIngredient ingredient = LazyIngredient.from(source);
        if (ingredient.tag().isPresent()) {
            return IngredientCreatorAccess.item().from(ingredient.tag().get());
        }
        return IngredientCreatorAccess.item().from(ingredient.stack(context().items()));
    }

    protected ChemicalStackIngredient chemicalIngredient(Object source, int amount) {
        Objects.requireNonNull(source, "chemical source");
        if (amount < 1) {
            throw new IllegalArgumentException("chemical amount must be positive, was " + amount);
        }
        return switch (source) {
            case TagKey<?> tag -> IngredientCreatorAccess.chemicalStack().from((TagKey<Chemical>) tag, amount);
            case Holder<?> holder ->
                    IngredientCreatorAccess.chemicalStack().fromHolder((Holder<Chemical>) holder, amount);
            case ResourceKey<?> key -> IngredientCreatorAccess.chemicalStack()
                    .fromHolder(chemicals().getOrThrow((ResourceKey<Chemical>) key), amount);
            case String text -> IngredientCreatorAccess.chemicalStack().fromHolder(
                    chemicals().getOrThrow(ResourceKey.create(
                            MekanismAPI.CHEMICAL_REGISTRY_NAME, ResourceLocation.parse(text))), amount);
            default -> throw new IllegalArgumentException(
                    "Unsupported Mekanism chemical source: " + source.getClass().getName());
        };
    }

    protected FluidStackIngredient fluidIngredient(Object source, int amount) {
        Objects.requireNonNull(source, "fluid source");
        if (amount < 1) {
            throw new IllegalArgumentException("fluid amount must be positive, was " + amount);
        }
        return switch (source) {
            case TagKey<?> tag -> IngredientCreatorAccess.fluid().from((TagKey<Fluid>) tag, amount);
            case Holder<?> holder -> IngredientCreatorAccess.fluid().fromHolder((Holder<Fluid>) holder, amount);
            case ResourceKey<?> key -> IngredientCreatorAccess.fluid()
                    .fromHolder(fluids().getOrThrow((ResourceKey<Fluid>) key), amount);
            case String text -> IngredientCreatorAccess.fluid().fromHolder(
                    fluids().getOrThrow(ResourceKey.create(Registries.FLUID, ResourceLocation.parse(text))), amount);
            default -> throw new IllegalArgumentException(
                    "Unsupported Mekanism fluid source: " + source.getClass().getName());
        };
    }

    protected ChemicalStack chemicalStack(Object source, int amount) {
        Objects.requireNonNull(source, "chemical source");
        if (amount < 1) {
            throw new IllegalArgumentException("chemical amount must be positive, was " + amount);
        }
        Holder<Chemical> holder = switch (source) {
            case Holder<?> h -> (Holder<Chemical>) h;
            case ResourceKey<?> key -> chemicals().getOrThrow((ResourceKey<Chemical>) key);
            case String text -> chemicals().getOrThrow(ResourceKey.create(
                    MekanismAPI.CHEMICAL_REGISTRY_NAME, ResourceLocation.parse(text)));
            default -> throw new IllegalArgumentException(
                    "Unsupported Mekanism chemical source: " + source.getClass().getName());
        };
        return new ChemicalStack(holder, amount);
    }

    protected FluidStack fluidStack(Object source, int amount) {
        Objects.requireNonNull(source, "fluid source");
        if (amount < 1) {
            throw new IllegalArgumentException("fluid amount must be positive, was " + amount);
        }
        Holder<Fluid> holder = switch (source) {
            case Holder<?> h -> (Holder<Fluid>) h;
            case ResourceKey<?> key -> fluids().getOrThrow((ResourceKey<Fluid>) key);
            case String text -> fluids().getOrThrow(ResourceKey.create(Registries.FLUID, ResourceLocation.parse(text)));
            default -> throw new IllegalArgumentException(
                    "Unsupported Mekanism fluid source: " + source.getClass().getName());
        };
        return new FluidStack(holder, amount);
    }

    protected final String sourcePath(Object source, String field) {
        Objects.requireNonNull(source, field);
        String path = switch (source) {
            case String text -> sanitize(text);
            case ResourceLocation identifier -> identifier.getPath();
            case ResourceKey<?> key -> sanitize(key.location().toString());
            case TagKey<?> tag -> sanitize(tag.location().toString());
            case ItemLike item -> LazyIngredient.from(item).label();
            case Holder<?> holder -> holder.unwrapKey()
                    .map(key -> sanitize(key.location().toString()))
                    .orElseThrow(() -> new IllegalStateException(field + " holder has no registry key"));
            default -> throw new IllegalArgumentException(
                    "Unsupported " + field + " source: " + source.getClass().getName());
        };
        if (path.isBlank()) {
            throw new IllegalStateException(field + " has an empty id path");
        }
        return path;
    }
}