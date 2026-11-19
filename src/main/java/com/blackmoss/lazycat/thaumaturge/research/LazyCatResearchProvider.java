package com.blackmoss.lazycat.thaumaturge.research;

import com.blackmoss.lazycat.api.thaumaturge.research.ResearchContext;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import org.jspecify.annotations.NonNull;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;

@SuppressWarnings("unused")
public abstract class LazyCatResearchProvider implements DataProvider {
    private final ResearchContext context;
    private String modid;

    protected LazyCatResearchProvider(PackOutput output, String modid) {
        this(ResearchContext.of(output, modid));
        this.modid = modid;
    }

    protected LazyCatResearchProvider(ResearchContext context) {
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
        return this.modid + " Research by Lazy Cat";
    }
}
