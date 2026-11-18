package com.blackmoss.lazycat.api.recipe.thaumaturge;

import com.blackmoss.lazycat.api.recipe.AbstractRecipeBuilder;
import com.blackmoss.lazycat.api.recipe.RecipeContext;
import com.leclowndu93150.thaumaturge.api.aspect.AspectInstance;
import com.leclowndu93150.thaumaturge.api.aspect.AspectList;
import com.leclowndu93150.thaumaturge.api.aspect.IAspect;
import com.leclowndu93150.thaumaturge.api.recipe.ResearchGate;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@SuppressWarnings({"unchecked", "unused"})
public abstract class AbstractThaumaturgeRecipeBuilder<B extends AbstractThaumaturgeRecipeBuilder<B>>
        extends AbstractRecipeBuilder<B> {
    private final RecipeContext context;
    private HolderGetter<IAspect> aspects;
    private final List<AspectInstance> entries = new ArrayList<>();
    private ResearchGate gate;

    protected AbstractThaumaturgeRecipeBuilder(RecipeContext context, Object result) {
        super(context, result);
        this.context = Objects.requireNonNull(context, "context");
    }

    public static Holder<IAspect> resolveAspect(HolderGetter<IAspect> getter, Object source, int amount) {
        Objects.requireNonNull(source, "aspect source");
        if (amount < 1) {
            throw new IllegalArgumentException("aspect amount must be positive, was " + amount);
        }
        return switch (source) {
            case Holder<?> holder -> (Holder<IAspect>) holder;
            case ResourceKey<?> key -> getter.getOrThrow((ResourceKey<IAspect>) key);
            case String text -> getter.getOrThrow(
                    ResourceKey.create(IAspect.REGISTRY_KEY, ResourceLocation.parse(text)));
            case AspectInstance instance -> instance.aspect();
            default -> throw new IllegalArgumentException(
                    "Unsupported aspect source: " + source.getClass().getName());
        };
    }

    protected final HolderGetter<IAspect> aspects() {
        if (this.aspects == null) {
            this.aspects = this.context.lookupProvide().lookupOrThrow(IAspect.REGISTRY_KEY);
        }
        return this.aspects;
    }

    protected final AspectList buildAspects() {
        return AspectList.ofEntries(entries);
    }

    protected final Optional<ResearchGate> gate() {
        return Optional.ofNullable(gate);
    }

    protected final void requireAspects() {
        if (entries.isEmpty()) {
            throw new IllegalStateException("Thaumaturge recipe requires at least one aspect");
        }
    }

    public B aspect(Object source) {
        return aspect(source, 1);
    }

    public B aspect(Object source, int amount) {
        entries.add(new AspectInstance(resolveAspect(aspects(), source, amount), amount));
        return self();
    }

    public B gate(ResearchGate gate) {
        this.gate = Objects.requireNonNull(gate, "gate");
        return self();
    }

    public B gate(ResourceLocation entry) {
        return gate(new ResearchGate(entry, Optional.empty(), false));
    }

    public B gate(String entry) {
        return gate(ResourceLocation.parse(entry));
    }
}
