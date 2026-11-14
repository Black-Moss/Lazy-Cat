package com.blackmoss.lazycat.api.recipe.thaumaturge;

import com.blackmoss.lazycat.api.recipe.AbstractRecipeBuilder;
import com.blackmoss.lazycat.api.recipe.RecipeContext;
import com.leclowndu93150.thaumaturge.api.aspect.AspectInstance;
import com.leclowndu93150.thaumaturge.api.aspect.AspectList;
import com.leclowndu93150.thaumaturge.api.aspect.IAspect;
import com.leclowndu93150.thaumaturge.api.recipe.ResearchGate;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;


public abstract class AbstractThaumaturgeRecipeBuilder<B extends AbstractThaumaturgeRecipeBuilder<B>>
        extends AbstractRecipeBuilder<B> {
    private final HolderGetter<IAspect> aspects;
    private final List<AspectInstance> entries = new ArrayList<>();
    private @Nullable ResearchGate gate;

    protected AbstractThaumaturgeRecipeBuilder(RecipeContext context, ItemLike result) {
        super(context, result);
        this.aspects = context.lookupProvide().lookupOrThrow(IAspect.REGISTRY_KEY);
    }

    protected final HolderGetter<IAspect> aspects() {
        return aspects;
    }

    protected final AspectList buildAspects() {
        AspectList list = AspectList.EMPTY;
        for (AspectInstance entry : entries) {
            list = list.add(entry);
        }
        return list;
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
        entries.add(new AspectInstance(resolveAspect(aspects, source, amount), amount));
        return self();
    }

    @SuppressWarnings("unchecked")
    public static Holder<IAspect> resolveAspect(HolderGetter<IAspect> getter, Object source, int amount) {
        Objects.requireNonNull(source, "source");
        if (amount < 1) {
            throw new IllegalArgumentException("aspect amount must be positive, was " + amount);
        }
        return switch (source) {
            case Holder<?> h -> (Holder<IAspect>) h;
            case ResourceKey<?> k -> getter.getOrThrow((ResourceKey<IAspect>) k);
            case String s -> getter.getOrThrow(ResourceKey.create(IAspect.REGISTRY_KEY, Identifier.parse(s)));
            case AspectInstance instance -> instance.aspect();
            default -> throw new IllegalArgumentException(
                    "Unsupported aspect source: " + source.getClass().getName());
        };
    }

    public B gate(ResearchGate gate) {
        this.gate = Objects.requireNonNull(gate, "gate");
        return self();
    }

    public B gate(Identifier entry) {
        return gate(new ResearchGate(entry, Optional.empty(), false));
    }

    public B gate(String entry) {
        return gate(Identifier.parse(entry));
    }
}
