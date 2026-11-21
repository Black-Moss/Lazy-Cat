package com.blackmoss.lazycat.thaumaturge.blueprint;

import com.blackmoss.lazycat.api.thaumaturge.blueprint.BlueprintContext;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import org.jspecify.annotations.NonNull;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;

@SuppressWarnings("unused")
public abstract class LazyCatBlueprintProvider implements DataProvider {
    protected final BlueprintContext context;
    private String modid;

    protected LazyCatBlueprintProvider(PackOutput output, String modid) {
        this(BlueprintContext.of(output, modid));
        this.modid = Objects.requireNonNull(modid, "modid");
    }

    protected LazyCatBlueprintProvider(BlueprintContext context) {
        this.context = Objects.requireNonNull(context, "context");
    }

    protected final BlueprintContext context() {
        return this.context;
    }

    protected abstract void addBlueprints();

    @Override
    public @NonNull CompletableFuture<?> run(@NonNull CachedOutput cache) {
        addBlueprints();
        return this.context.save(cache);
    }

    @Override
    public @NonNull String getName() {
        return modid + " Thaumaturge Blueprints by Lazy Cat";
    }
}
