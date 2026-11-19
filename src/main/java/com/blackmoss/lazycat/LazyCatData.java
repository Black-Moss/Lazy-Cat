package com.blackmoss.lazycat;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;

@SuppressWarnings("unused")
public final class LazyCatData {
    public static <P extends RecipeProvider> P recipes(
            GatherDataEvent event,
            String modid,
            RecipeProviderFactory<P> factory) {
        Objects.requireNonNull(event, "event");
        Objects.requireNonNull(modid, "modid");
        Objects.requireNonNull(factory, "factory");
        if (!event.includeServer()) {
            return null;
        }
        return event.createProvider(factory::create);
    }

    @FunctionalInterface
    public interface RecipeProviderFactory<P extends RecipeProvider> {
        P create(PackOutput output, CompletableFuture<HolderLookup.Provider> registries);
    }
}
