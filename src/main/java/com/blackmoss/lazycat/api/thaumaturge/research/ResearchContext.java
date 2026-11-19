package com.blackmoss.lazycat.api.thaumaturge.research;

import com.blackmoss.lazycat.api.LazyIngredient;
import com.blackmoss.lazycat.thaumaturge.research.ResearchCategoryBuilder;
import com.blackmoss.lazycat.thaumaturge.research.ResearchEntryBuilder;
import com.google.gson.JsonObject;
import com.leclowndu93150.thaumaturge.api.research.IResearchCategory;
import com.leclowndu93150.thaumaturge.api.research.ResearchIcon;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;

import java.util.*;
import java.util.concurrent.CompletableFuture;

@SuppressWarnings("unused")
public final class ResearchContext {
    private static final String CATEGORY_DIRECTORY = "thaumaturge/research_category";
    private static final String ENTRY_DIRECTORY = "thaumaturge/research_entry";

    private final PackOutput.PathProvider categories;
    private final PackOutput.PathProvider entries;
    private final Map<Identifier, JsonObject> categoryData = new LinkedHashMap<>();
    private final Map<Identifier, JsonObject> entryData = new LinkedHashMap<>();
    private final String modid;

    private ResearchContext(PackOutput output, String modid) {
        this.categories = output.createPathProvider(PackOutput.Target.DATA_PACK, CATEGORY_DIRECTORY);
        this.entries = output.createPathProvider(PackOutput.Target.DATA_PACK, ENTRY_DIRECTORY);
        this.modid = modid;
    }

    public static ResearchContext of(PackOutput output, String modid) {
        Objects.requireNonNull(output, "output");
        Objects.requireNonNull(modid, "modid");
        return new ResearchContext(output, modid);
    }

    public static String itemRef(Object source) {
        return LazyIngredient.from(source).reference();
    }

    public static String iconRef(Object source) {
        Objects.requireNonNull(source, "source");
        if (source instanceof ResearchIcon(Identifier id, ResearchIcon.Kind kind)) {
            return kind == ResearchIcon.Kind.FOCUS ? "focus:" + id : id.toString();
        }
        return itemRef(source);
    }

    private static void submit(Map<Identifier, JsonObject> target, Identifier id, JsonObject json, String type) {
        if (target.putIfAbsent(id, json) != null) {
            throw new IllegalStateException("Duplicate thaumaturge %s: %s".formatted(type, id));
        }
    }

    public String modid() {
        return this.modid;
    }

    public ResearchCategoryBuilder category(ResourceKey<IResearchCategory> key) {
        Objects.requireNonNull(key, "key");
        return new ResearchCategoryBuilder(this, key.identifier());
    }

    public ResearchCategoryBuilder category(Identifier id) {
        Objects.requireNonNull(id, "id");
        return new ResearchCategoryBuilder(this, id);
    }

    public ResearchCategoryBuilder category(String id) {
        return new ResearchCategoryBuilder(this, resolveId(id));
    }

    public ResearchEntryBuilder entry(Object category, String id) {
        return new ResearchEntryBuilder(this, category, id);
    }

    public CompletableFuture<?> save(CachedOutput cache) {
        Objects.requireNonNull(cache, "cache");
        List<CompletableFuture<?>> tasks = new ArrayList<>(this.categoryData.size() + this.entryData.size());
        this.categoryData.forEach((id, json) -> tasks.add(DataProvider.saveStable(cache, json, this.categories.json(id))));
        this.entryData.forEach((id, json) -> tasks.add(DataProvider.saveStable(cache, json, this.entries.json(id))));
        return CompletableFuture.allOf(tasks.toArray(CompletableFuture[]::new));
    }

    public void submitCategory(Identifier id, JsonObject json) {
        submit(this.categoryData, id, json, "category");
    }

    public void submitEntry(Identifier id, JsonObject json) {
        submit(this.entryData, id, json, "research entry");
    }

    public Identifier resolveId(Object source) {
        Objects.requireNonNull(source, "source");
        return switch (source) {
            case Identifier id -> id;
            case ResourceKey<?> key -> key.identifier();
            case String text ->
                    text.contains(":") ? Identifier.parse(text) : Identifier.fromNamespaceAndPath(this.modid, text);
            default -> throw new IllegalArgumentException("Unsupported lazyCat source: " + source.getClass().getName());
        };
    }

    public String resolveText(Object source) {
        return this.resolveId(source).toString();
    }

    public String resolvePath(Object source) {
        return this.resolveId(source).getPath();
    }
}
