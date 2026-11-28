package com.blackmoss.lazycat.recipe.minecraft;

import com.blackmoss.lazycat.api.LazyIngredient;
import com.blackmoss.lazycat.api.recipe.AbstractRecipeBuilder;
import com.blackmoss.lazycat.api.recipe.RecipeContext;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.SingleItemRecipeBuilder;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@SuppressWarnings("unused")
public final class StonecuttingBuilder extends AbstractRecipeBuilder<StonecuttingBuilder> {
    private final List<LazyIngredient> inputs = new ArrayList<>();
    private LazyIngredient currentInput;

    public StonecuttingBuilder(RecipeContext context, Object result, Object input) {
        super(context, result);
        inputs.add(LazyIngredient.from(input));
    }

    @Override
    protected StonecuttingBuilder self() {
        return this;
    }

    public StonecuttingBuilder input(Object source) {
        Objects.requireNonNull(source, "source");
        LazyIngredient ingredient = LazyIngredient.from(source);
        if (inputs.isEmpty()) {
            inputs.add(ingredient);
        } else {
            inputs.set(0, ingredient);
        }
        return this;
    }

    public StonecuttingBuilder inputs(Object... sources) {
        Objects.requireNonNull(sources, "sources");
        if (sources.length == 0) {
            throw new IllegalArgumentException("sources must not be empty");
        }
        inputs.clear();
        for (Object source : sources) {
            inputs.add(LazyIngredient.from(source));
        }
        return this;
    }

    public StonecuttingBuilder and(Object source) {
        inputs.add(LazyIngredient.from(source));
        return this;
    }

    @Override
    public void save() {
        boolean multiple = inputs.size() > 1;
        for (LazyIngredient input : inputs) {
            currentInput = input;
            replaceDefaultUnlock(input);
            validate();
            emit(idFor(input, multiple));
        }
        currentInput = null;
    }

    @Override
    public void save(String path) {
        id(path);
        save();
    }

    @Override
    public void save(ResourceLocation id) {
        this.id(id);
        save();
    }

    @Override
    protected void validate() {
        if (inputs.isEmpty()) {
            throw new IllegalStateException("Stonecutting recipe %s has no input".formatted(defaultIdPath()));
        }
    }

    @Override
    protected void build(RecipeOutput output, ResourceLocation id) {
        LazyIngredient input = currentInput != null ? currentInput : inputs.getFirst();
        SingleItemRecipeBuilder builder = SingleItemRecipeBuilder.stonecutting(
                input.resolve(context().items()),
                category(),
                resultItem(),
                result().getCount());
        builder.group(group());
        applyCriteria(builder::unlockedBy);
        builder.save(output, id);
    }

    private ResourceLocation idFor(LazyIngredient input, boolean multiple) {
        ResourceLocation explicit = explicitId();
        String modid = explicit != null ? explicit.getNamespace() : context().modid();
        String base = explicit != null ? explicit.getPath() : decoratePath(defaultIdPath());
        String path = multiple ? "%s_from_%s".formatted(base, sanitize(input.label())) : base;
        return withFolder(ResourceLocation.fromNamespaceAndPath(modid, path));
    }
}
