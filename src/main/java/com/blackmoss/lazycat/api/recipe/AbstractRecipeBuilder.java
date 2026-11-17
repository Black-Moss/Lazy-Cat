package com.blackmoss.lazycat.api.recipe;

import com.blackmoss.lazycat.api.LazyIdentifier;
import com.blackmoss.lazycat.api.LazyIngredient;
import net.minecraft.advancements.Criterion;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.common.conditions.ICondition;
import net.neoforged.neoforge.common.conditions.ModLoadedCondition;

import java.util.*;
import java.util.function.BiConsumer;

@SuppressWarnings({"UnusedReturnValue", "SameParameterValue"})
public abstract class AbstractRecipeBuilder<B extends AbstractRecipeBuilder<B>> {
    private final RecipeContext context;
    private final Map<String, Criterion<?>> criteria = new LinkedHashMap<>();
    private final List<ICondition> conditions = new ArrayList<>();
    private final ItemLike result;

    private RecipeCategory category = RecipeCategory.MISC;
    private int count = 1;
    private String group;
    private Identifier id;
    private String folder;
    private String suffix;
    private LazyIngredient defaultUnlock;
    private boolean allowEmptyAdvancement;

    protected AbstractRecipeBuilder(RecipeContext context, ItemLike result) {
        this.context = Objects.requireNonNull(context, "context");
        this.result = result;
    }

    protected static String sanitize(String value) {
        Objects.requireNonNull(value, "value");
        if (value.indexOf(':') >= 0) {
            return Identifier.parse(value).getPath();
        }
        return value.replace(' ', '_');
    }

    protected static String folderOf(Enum<?> mode)l {
        return mode == null ? "" : mode.name().toLowerCase(Locale.ROOT);
    }

    protected abstract B self();

    protected abstract void build(RecipeOutput output, ResourceKey<Recipe<?>> key);

    protected void validate() {
    }

    protected String defaultIdPath() {
        return LazyIdentifier.getItemPath(resultItem());
    }

    protected String defaultFolder() {
        return "";
    }

    public B count(int count) {
        if (count < 1 || count > 64) {
            throw new IllegalArgumentException("count must be between 1 and 64");
        }
        this.count = count;
        return self();
    }

    protected final int count() {
        return count;
    }

    protected final ItemLike resultItem() {
        if (result == null) {
            throw new IllegalStateException("This recipe has no result item");
        }
        return result;
    }

    protected final ItemStackTemplate result() {
        return new ItemStackTemplate(resultItem().asItem(), count);
    }

    protected final RecipeContext context() {
        return context;
    }

    protected final Ingredient toIngredient(Object source) {
        Objects.requireNonNull(source, "source");
        return context().ingredient(source).resolve(context().items());
    }

    protected final RecipeCategory category() {
        return category;
    }

    protected final String group() {
        return group;
    }

    protected final Identifier explicitId() {
        return id;
    }

    protected final void defaultUnlock(LazyIngredient ingredient) {
        if (defaultUnlock == null) {
            defaultUnlock = Objects.requireNonNull(ingredient, "ingredient");
        }
    }

    protected final void allowEmptyAdvancement(boolean allow) {
        allowEmptyAdvancement = allow;
    }

    public B category(RecipeCategory category) {
        this.category = Objects.requireNonNull(category, "category");
        return self();
    }

    public B group(String group) {
        this.group = group;
        return self();
    }

    public B id(String id) {
        Objects.requireNonNull(id, "id");
        this.id = id.indexOf(':') >= 0
                ? Identifier.parse(id)
                : Identifier.fromNamespaceAndPath(context.namespace(), sanitize(id));
        return self();
    }

    public B id(Identifier id) {
        this.id = Objects.requireNonNull(id, "id");
        return self();
    }

    public B folder(String folder) {
        Objects.requireNonNull(folder, "folder");
        this.folder = sanitize(folder);
        return self();
    }

    public B suffix(String suffix) {
        Objects.requireNonNull(suffix, "suffix");
        this.suffix = sanitize(suffix);
        return self();
    }

    public B idForm(Object form) {
        Objects.requireNonNull(form, "form");
        String label = form instanceof String text ? text : LazyIngredient.from(form).label();
        return id(defaultIdPath() + "_from_" + sanitize(label));
    }

    public B unlockedBy(String name, Criterion<?> criterion) {
        Objects.requireNonNull(name, "name");
        criteria.put(name, Objects.requireNonNull(criterion, "criterion"));
        return self();
    }

    public B unlock(Object source) {
        Objects.requireNonNull(source, "source");
        LazyIngredient ingredient = LazyIngredient.from(source);
        return unlockedBy("has_" + ingredient.label(), context.has(source));
    }

    public B when(ICondition... conditions) {
        Objects.requireNonNull(conditions, "conditions");
        for (ICondition condition : conditions) {
            this.conditions.add(Objects.requireNonNull(condition, "conditions contains null"));
        }
        return self();
    }

    public B ifModLoaded(String... modIds) {
        Objects.requireNonNull(modIds, "modIds");
        for (String modId : modIds) {
            Objects.requireNonNull(modId, "modIds contains null");
            if (modId.isBlank()) {
                throw new IllegalArgumentException("modId must not be blank");
            }
            conditions.add(new ModLoadedCondition(modId));
        }
        return self();
    }

    public void save() {
        emit(resolveKey());
    }

    public void save(String path) {
        id(path);
        emit(resolveKey());
    }

    public void save(Identifier id) {
        id(id);
        emit(resolveKey());
    }

    public void save(ResourceKey<Recipe<?>> key) {
        emit(Objects.requireNonNull(key, "key"));
    }

    protected final ResourceKey<Recipe<?>> resolveKey() {
        Identifier target = id != null
                ? id
                : Identifier.fromNamespaceAndPath(context.namespace(), decoratePath(defaultIdPath()));
        return ResourceKey.create(Registries.RECIPE, withFolder(target));
    }

    protected final Identifier withFolder(Identifier target) {
        Objects.requireNonNull(target, "target");
        String folder = defaultFolder();
        if (folder.isEmpty() || target.getPath().startsWith(folder + "/")) {
            return target;
        }
        return Identifier.fromNamespaceAndPath(target.getNamespace(), folder + "/" + target.getPath());
    }

    protected final String decoratePath(String path) {
        Objects.requireNonNull(path, "path");
        String decorated = path;
        if (suffix != null && !suffix.isEmpty()) {
            decorated = decorated + "_" + suffix;
        }
        if (folder != null && !folder.isEmpty()) {
            decorated = folder + "/" + decorated;
        }
        return decorated;
    }

    protected final void emit(ResourceKey<Recipe<?>> key) {
        Objects.requireNonNull(key, "key");
        validate();
        RecipeOutput target = context.output();
        if (!conditions.isEmpty()) {
            target = Objects.requireNonNull(
                    target.withConditions(conditions.toArray(ICondition[]::new)),
                    "conditional recipe output");
        }
        build(target, key);
    }

    protected final void applyCriteria(BiConsumer<String, Criterion<?>> consumer) {
        Objects.requireNonNull(consumer, "consumer");
        if (criteria.isEmpty() && !allowEmptyAdvancement) {
            criteria.put("has", defaultCriterion());
        }
        criteria.forEach(consumer);
    }

    protected Criterion<?> defaultCriterion() {
        return defaultUnlock != null ? context.has(defaultUnlock) : context.impossible();
    }
}
