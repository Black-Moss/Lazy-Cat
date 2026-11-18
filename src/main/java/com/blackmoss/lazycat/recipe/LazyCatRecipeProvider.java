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
    protected final String modid;

    protected LazyCatRecipeProvider(
            PackOutput output,
            CompletableFuture<HolderLookup.Provider> registries,
            String modid) {
        super(output, registries);
        Objects.requireNonNull(modid, "modid");
        if (modid.isBlank()) {
            throw new IllegalArgumentException("modid must not be blank");
        }
        this.modid = modid;
    }

    @Override
    protected final void buildRecipes(@NonNull RecipeOutput output, HolderLookup.@NonNull Provider registries) {
        addRecipes(RecipeContext.of(registries, output, modid));
    }

    protected abstract void addRecipes(RecipeContext context);
}
