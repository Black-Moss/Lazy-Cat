package com.blackmoss.lazycat.api.thaumaturge.research;

import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import org.jspecify.annotations.NonNull;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;

public abstract class ThaumaturgeResearchProvider implements DataProvider {
    private final ResearchContext context;

    protected ThaumaturgeResearchProvider(PackOutput output, String namespace) {
        this(ResearchContext.of(output, namespace));
    }

    protected ThaumaturgeResearchProvider(ResearchContext context) {
        this.context = Objects.requireNonNull(context, "context");
    }

    protected final ResearchContext context() {
        return this.context;
    }

    protected abstract void addResearch(ResearchContext context);

    @Override
    public @NonNull CompletableFuture<?> run(@NonNull CachedOutput cache) {
        addResearch(this.context);
        return this.context.save(cache);
    }

    @Override
    public @NonNull String getName() {
        return "Lazy Cat Research";
    }
}
