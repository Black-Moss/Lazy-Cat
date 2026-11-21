package com.blackmoss.lazycat.datamap;

import com.blackmoss.lazycat.api.datamap.DataMapContext;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.DataMapProvider;
import org.jspecify.annotations.NonNull;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;

@SuppressWarnings("unused")
public abstract class LazyCatDataMapProvider extends DataMapProvider {
    private final String modid;
    protected DataMapContext context;

    protected LazyCatDataMapProvider(
            PackOutput output,
            CompletableFuture<HolderLookup.Provider> registries,
            String modid) {
        super(output, registries);
        this.modid = Objects.requireNonNull(modid, "modid");
    }

    @Override
    protected final void gather(HolderLookup.@NonNull Provider registries) {
        this.context = new DataMapContext(registries, this);
        buildDataMaps();
    }

    protected abstract void buildDataMaps();

    @Override
    public @NonNull String getName() {
        return this.modid + " Data Maps by Lazy Cat";
    }
}
