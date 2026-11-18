package com.blackmoss.lazycat.api.recipe;

import com.blackmoss.lazycat.api.LazyIngredient;
import com.blackmoss.lazycat.api.condition.ConditionalBuilder;
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

import java.util.*;
import java.util.function.BiConsumer;

@SuppressWarnings({"UnusedReturnValue", "SameParameterValue", "unused"})
public abstract class AbstractRecipeBuilder<B extends AbstractRecipeBuilder<B>> implements ConditionalBuilder<B> {
    private final RecipeContext context;
    private final Map<String, Criterion<?>> criteria = new LinkedHashMap<>();
    private final List<ICondition> conditions = new ArrayList<>();
    private final LazyIngredient result;

    private RecipeCategory category = RecipeCategory.MISC;
    private Integer count;
    private String group;
    private Identifier id;
    private String folder;
    private String suffix;
    private LazyIngredient defaultUnlock;
    private boolean allowEmptyAdvancement;

    protected AbstractRecipeBuilder(RecipeContext context, Object result) {
        this.context = Objects.requireNonNull(context, "context");
        this.result = result == null ? null : LazyIngredient.from(result);
    }

    protected static String sanitize(String value) {
        Objects.requireNonNull(value, "value");
        if (value.indexOf(':') >= 0) {
            return Identifier.parse(value).getPath();
        }
        return value.replace(' ', '_');
    }

    protected static String folderOf(Enum<?> mode) {
        return mode == null ? "" : mode.name().toLowerCase(Locale.ROOT);
    }

    protected abstract B self();

    protected abstract void build(RecipeOutput output, ResourceKey<Recipe<?>> key);

    protected void validate() {
    }

    protected String defaultIdPath() {
        return resultIngredient().label();
    }

    protected String defaultFolder() {
        return "";
    }

    public B count(int count) {
        if (count < 1 || count > 99) {
            throw new IllegalArgumentException("count must be between 1 and 99");
        }
        this.count = count;
        return self();
    }

    protected final int count() {
        return count != null ? count : resultIngredient().template(context().items()).count();
    }

    protected final LazyIngredient resultIngredient() {
        if (result == null) {
            throw new IllegalStateException("This recipe has no result item");
        }
        return result;
    }

    protected final ItemLike resultItem() {
        return resultIngredient().template(context().items()).item().value();
    }

    protected final ItemStackTemplate result() {
        LazyIngredient ingredient = count == null ? resultIngredient() : resultIngredient().count(count);
        return ingredient.template(context().items());
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

    @Override
    public List<ICondition> conditionList() {
        return this.conditions;
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
        if (hasConditions()) {
            target = Objects.requireNonNull(
                    target.withConditions(conditionsArray()),
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
