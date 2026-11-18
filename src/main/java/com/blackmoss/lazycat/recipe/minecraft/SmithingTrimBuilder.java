package com.blackmoss.lazycat.recipe.minecraft;

import com.blackmoss.lazycat.api.LazyIngredient;
import com.blackmoss.lazycat.api.recipe.AbstractRecipeBuilder;
import com.blackmoss.lazycat.api.recipe.RecipeContext;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.SmithingTrimRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.armortrim.TrimPattern;

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
    private static net.minecraft.resources.ResourceKey<TrimPattern> castKey(net.minecraft.resources.ResourceKey<?> key) {
        return (net.minecraft.resources.ResourceKey<TrimPattern>) key;
    }

    @Override
    protected SmithingTrimBuilder self() {
        return this;
    }

    @Override
    protected String defaultIdPath() {
        return pattern.unwrapKey()
                .map(key -> "%s_smithing_trim".formatted(key.location().getPath()))
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
    protected void build(RecipeOutput output, ResourceLocation id) {
        SmithingTrimRecipeBuilder builder = SmithingTrimRecipeBuilder.smithingTrim(
                template.resolve(context().items()),
                base.resolve(context().items()),
                addition.resolve(context().items()),
                category());
        applyCriteria(builder::unlocks);
        builder.save(output, id);
    }

    private Holder<TrimPattern> resolvePattern(Object source) {
        Objects.requireNonNull(source, "source");
        return switch (source) {
            case Holder<?> holder -> castHolder(holder);
            case net.minecraft.resources.ResourceKey<?> key -> context().lookupProvide()
                    .lookupOrThrow(Registries.TRIM_PATTERN)
                    .getOrThrow(castKey(key));
            default -> throw new IllegalArgumentException(
                    "Unsupported trim pattern: " + source.getClass().getName());
        };
    }
}
