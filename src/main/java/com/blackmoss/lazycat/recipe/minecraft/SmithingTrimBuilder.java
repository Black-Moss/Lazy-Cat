package com.blackmoss.lazycat.recipe.minecraft;

import com.blackmoss.lazycat.api.LazyIngredient;
import com.blackmoss.lazycat.api.recipe.AbstractRecipeBuilder;
import com.blackmoss.lazycat.api.recipe.RecipeContext;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.SmithingTrimRecipeBuilder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.equipment.trim.TrimPattern;

import java.util.Objects;

@SuppressWarnings("unused")
public final class SmithingTrimBuilder extends AbstractRecipeBuilder<SmithingTrimBuilder> {
    private Holder<TrimPattern> pattern;
    private LazyIngredient template;
    private LazyIngredient base;
    private LazyIngredient addition;

    public SmithingTrimBuilder(RecipeContext context, Object pattern) {
        super(context, null);
        this.pattern = resolvePattern(pattern);
    }

    @SuppressWarnings("unchecked")
    private static Holder<TrimPattern> castHolder(Holder<?> holder) {
        return (Holder<TrimPattern>) holder;
    }

    @SuppressWarnings("unchecked")
    private static ResourceKey<TrimPattern> castKey(ResourceKey<?> key) {
        return (ResourceKey<TrimPattern>) key;
    }

    @Override
    protected SmithingTrimBuilder self() {
        return this;
    }

    @Override
    protected String defaultIdPath() {
        return pattern.unwrapKey()
                .map(key -> "%s_smithing_trim".formatted(key.identifier().getPath()))
                .orElseThrow(() -> new IllegalStateException(
                        "Smithing trim recipe requires an explicit id()"));
    }

    public SmithingTrimBuilder pattern(Object source) {
        pattern = resolvePattern(source);
        return this;
    }

    public SmithingTrimBuilder template(Object source) {
        template = LazyIngredient.from(source);
        return this;
    }

    public SmithingTrimBuilder base(Object source) {
        base = LazyIngredient.from(source);
        defaultUnlock(base);
        return this;
    }

    public SmithingTrimBuilder addition(Object source) {
        addition = LazyIngredient.from(source);
        return this;
    }

    @Override
    protected void validate() {
        if (template == null || base == null || addition == null || pattern == null) {
            throw new IllegalStateException("Smithing trim recipe requires pattern, template, base and addition");
        }
    }

    @Override
    protected void build(RecipeOutput output, ResourceKey<Recipe<?>> key) {
        SmithingTrimRecipeBuilder builder = new SmithingTrimRecipeBuilder(
                category(),
                template.resolve(context().items()),
                base.resolve(context().items()),
                addition.resolve(context().items()),
                pattern);
        applyCriteria(builder::unlocks);
        builder.save(output, key);
    }

    private Holder<TrimPattern> resolvePattern(Object source) {
        Objects.requireNonNull(source, "source");
        return switch (source) {
            case Holder<?> holder -> castHolder(holder);
            case ResourceKey<?> key -> context().lookupProvide()
                    .lookupOrThrow(Registries.TRIM_PATTERN)
                    .getOrThrow(castKey(key));
            default -> throw new IllegalArgumentException(
                    "Unsupported trim pattern: " + source.getClass().getName());
        };
    }
}
