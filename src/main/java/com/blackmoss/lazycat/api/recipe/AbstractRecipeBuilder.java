package com.blackmoss.lazycat.api.recipe;

import com.blackmoss.lazycat.api.LazyIngredient;
import com.blackmoss.lazycat.api.condition.ConditionalBuilder;
import net.minecraft.advancements.*;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
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
    private ResourceLocation id;
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
            return ResourceLocation.parse(value).getPath();
        }
        return value.replace(' ', '_');
    }

    protected static String folderOf(Enum<?> mode) {
        return mode == null ? "" : mode.name().toLowerCase(Locale.ROOT);
    }

    @Override
    public List<ICondition> conditionList() {
        return conditions;
    }

    protected abstract B self();

    protected abstract void build(RecipeOutput output, ResourceLocation id);

    protected void validate() {
    }

    protected String defaultIdPath() {
        return resultIngredient().label();
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
        return result().getCount();
    }

    protected final LazyIngredient resultIngredient() {
        if (result == null) {
            throw new IllegalStateException("This recipe has no result item");
        }
        return result;
    }

    protected final ItemLike resultItem() {
        return result().getItem();
    }

    protected final ItemStack result() {
        LazyIngredient ingredient = count == null ? resultIngredient() : resultIngredient().count(count);
        return ingredient.stack(context().items());
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

    protected final ResourceLocation explicitId() {
        return id;
    }

    protected final void defaultUnlock(LazyIngredient ingredient) {
        if (defaultUnlock == null) {
            defaultUnlock = Objects.requireNonNull(ingredient, "ingredient");
        }
    }

    protected final void replaceDefaultUnlock(LazyIngredient ingredient) {
        this.defaultUnlock = Objects.requireNonNull(ingredient, "ingredient");
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
                ? ResourceLocation.parse(id)
                : ResourceLocation.fromNamespaceAndPath(context.modid(), sanitize(id));
        return self();
    }

    public B id(ResourceLocation id) {
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

    public void save() {
        validate();
        emit(resolveId());
    }

    public void save(String path) {
        id(path);
        save();
    }

    public void save(ResourceLocation id) {
        id(id);
        save();
    }

    protected final ResourceLocation resolveId() {
        ResourceLocation target = id != null
                ? id
                : ResourceLocation.fromNamespaceAndPath(context.modid(), decoratePath(defaultIdPath()));
        return withFolder(target);
    }

    protected final ResourceLocation withFolder(ResourceLocation target) {
        Objects.requireNonNull(target, "target");
        String name = defaultFolder();
        if (name.isEmpty() || target.getPath().startsWith(name + "/")) {
            return target;
        }
        return ResourceLocation.fromNamespaceAndPath(target.getNamespace(), name + "/" + target.getPath());
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

    protected final void emit(ResourceLocation id) {
        Objects.requireNonNull(id, "id");
        RecipeOutput target = context.output();
        if (hasConditions()) {
            target = Objects.requireNonNull(
                    target.withConditions(conditionsArray()),
                    "conditional recipe output");
        }
        build(target, id);
    }

    protected final void applyCriteria(BiConsumer<String, Criterion<?>> consumer) {
        Objects.requireNonNull(consumer, "consumer");
        if (criteria.isEmpty() && !allowEmptyAdvancement) {
            criteria.put("has", defaultCriterion());
        }
        criteria.forEach(consumer);
    }

    protected final AdvancementHolder advancement(RecipeOutput output, ResourceLocation id) {
        Advancement.Builder builder = output.advancement()
                .addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(id))
                .rewards(AdvancementRewards.Builder.recipe(id))
                .requirements(AdvancementRequirements.Strategy.OR);
        applyCriteria(builder::addCriterion);
        return builder.build(id.withPrefix("recipes/" + category().getFolderName() + "/"));
    }

    protected Criterion<?> defaultCriterion() {
        return defaultUnlock != null ? context.has(defaultUnlock) : context.impossible();
    }
}
