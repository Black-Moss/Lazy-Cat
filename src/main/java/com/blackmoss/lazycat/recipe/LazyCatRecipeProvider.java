package com.blackmoss.lazycat.recipe;

import com.blackmoss.lazycat.api.recipe.RecipeContext;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import org.jspecify.annotations.NonNull;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;

@SuppressWarnings("unused")
public abstract class LazyCatRecipeProvider extends RecipeProvider {
    protected final String namespace;

    protected LazyCatRecipeProvider(
            PackOutput output,
            CompletableFuture<HolderLookup.Provider> registries,
            String namespace) {
        super(output, registries);
        Objects.requireNonNull(namespace, "namespace");
        if (namespace.isBlank()) {
            throw new IllegalArgumentException("namespace must not be blank");
        }
        this.namespace = namespace;
    }

    @Override
    protected final void buildRecipes(@NonNull RecipeOutput output, HolderLookup.@NonNull Provider registries) {
        addRecipes(RecipeContext.of(registries, output, namespace));
    }

    protected abstract void addRecipes(RecipeContext context);
}
