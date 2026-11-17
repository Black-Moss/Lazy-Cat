package com.blackmoss.lazycat.recipe.minecraft;

import com.blackmoss.lazycat.api.LazyIngredient;
import com.blackmoss.lazycat.api.recipe.AbstractRecipeBuilder;
import com.blackmoss.lazycat.api.recipe.RecipeContext;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.SingleItemRecipeBuilder;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.ItemLike;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@SuppressWarnings("unused")
public final class StonecuttingBuilder extends AbstractRecipeBuilder<StonecuttingBuilder> {
    private final List<LazyIngredient> inputs = new ArrayList<>();
    private LazyIngredient currentInput;

    public StonecuttingBuilder(RecipeContext context, ItemLike result, Object input) {
        super(context, result);
        LazyIngredient ingredient = LazyIngredient.from(input);
        inputs.add(ingredient);
        defaultUnlock(ingredient);
    }

    @Override
    protected StonecuttingBuilder self() {
        return this;
    }

    public StonecuttingBuilder input(Object source) {
        return inputs(source);
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
            emit(keyFor(input, multiple));
        }
        currentInput = null;
    }

    @Override
    public void save(String path) {
        id(path);
        save();
    }

    @Override
    public void save(Identifier id) {
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
    protected void build(RecipeOutput output, ResourceKey<Recipe<?>> key) {
        LazyIngredient input = currentInput != null ? currentInput : inputs.getFirst();
        SingleItemRecipeBuilder builder = SingleItemRecipeBuilder.stonecutting(
                input.resolve(context().items()),
                category(),
                resultItem().asItem(),
                count());
        builder.group(group());
        applyCriteria(builder::unlockedBy);
        builder.save(output, key);
    }

    private ResourceKey<Recipe<?>> keyFor(LazyIngredient input, boolean multiple) {
        Identifier explicit = explicitId();
        String namespace = explicit != null ? explicit.getNamespace() : context().namespace();
        String base = explicit != null ? explicit.getPath() : decoratePath(defaultIdPath());
        String path = multiple ? "%s_from_%s".formatted(base, sanitize(input.label())) : base;
        return ResourceKey.create(
                Registries.RECIPE,
                withFolder(Identifier.fromNamespaceAndPath(namespace, path)));
    }
}
