package com.blackmoss.lazycat.api.recipe;

import com.blackmoss.lazycat.api.recipe.minecraft.AbstractRecipeBuilder;
import net.minecraft.advancements.Criterion;
import net.minecraft.data.recipes.RecipeCategory;
import net.neoforged.neoforge.common.conditions.ICondition;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

@SuppressWarnings("unused")
public final class RecipeGroupBuilder {
    private final List<AbstractRecipeBuilder<?>> builders = new ArrayList<>();

    public RecipeGroupBuilder(AbstractRecipeBuilder<?>... builders) {
        add(builders);
    }

    public RecipeGroupBuilder add(AbstractRecipeBuilder<?>... builders) {
        Objects.requireNonNull(builders, "builders");
        for (AbstractRecipeBuilder<?> builder : builders) {
            this.builders.add(Objects.requireNonNull(builder, "builders contains null"));
        }
        return this;
    }

    public RecipeGroupBuilder and(AbstractRecipeBuilder<?>... builders) {
        return add(builders);
    }

    public RecipeGroupBuilder category(RecipeCategory category) {
        Objects.requireNonNull(category, "category");
        for (AbstractRecipeBuilder<?> builder : builders) {
            builder.category(category);
        }
        return this;
    }

    public RecipeGroupBuilder group(String group) {
        for (AbstractRecipeBuilder<?> builder : builders) {
            builder.group(group);
        }
        return this;
    }

    public RecipeGroupBuilder folder(String folder) {
        Objects.requireNonNull(folder, "folder");
        for (AbstractRecipeBuilder<?> builder : builders) {
            builder.folder(folder);
        }
        return this;
    }

    public RecipeGroupBuilder suffix(String suffix) {
        Objects.requireNonNull(suffix, "suffix");
        for (AbstractRecipeBuilder<?> builder : builders) {
            builder.suffix(suffix);
        }
        return this;
    }

    public RecipeGroupBuilder unlockedBy(String name, Criterion<?> criterion) {
        for (AbstractRecipeBuilder<?> builder : builders) {
            builder.unlockedBy(name, criterion);
        }
        return this;
    }

    public RecipeGroupBuilder unlock(Object source) {
        Objects.requireNonNull(source, "source");
        for (AbstractRecipeBuilder<?> builder : builders) {
            builder.unlock(source);
        }
        return this;
    }

    public RecipeGroupBuilder when(ICondition... conditions) {
        for (AbstractRecipeBuilder<?> builder : builders) {
            builder.when(conditions);
        }
        return this;
    }

    public RecipeGroupBuilder ifModLoaded(String... modIds) {
        for (AbstractRecipeBuilder<?> builder : builders) {
            builder.ifModLoaded(modIds);
        }
        return this;
    }

    public RecipeGroupBuilder map(Consumer<AbstractRecipeBuilder<?>> action) {
        Objects.requireNonNull(action, "action");
        for (AbstractRecipeBuilder<?> builder : builders) {
            action.accept(builder);
        }
        return this;
    }

    public List<AbstractRecipeBuilder<?>> builders() {
        return List.copyOf(builders);
    }

    public void save() {
        for (AbstractRecipeBuilder<?> builder : builders) {
            builder.save();
        }
    }
}
