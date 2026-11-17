package com.blackmoss.lazycat.recipe.ae2;

import appeng.recipes.handlers.InscriberProcessType;
import appeng.recipes.handlers.InscriberRecipe;
import com.blackmoss.lazycat.api.recipe.AbstractRecipeBuilder;
import com.blackmoss.lazycat.api.recipe.RecipeContext;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.ItemLike;

import java.util.Objects;
import java.util.Optional;

@SuppressWarnings("unused")
public final class InscriberBuilder extends AbstractRecipeBuilder<InscriberBuilder> {
    private final InscriberProcessType mode;
    private Object middle;
    private Object top;
    private Object bottom;

    public InscriberBuilder(RecipeContext context, ItemLike result, InscriberProcessType mode) {
        super(context, result);
        this.mode = Objects.requireNonNull(mode, "mode");
    }

    @Override
    protected InscriberBuilder self() {
        return this;
    }

    public InscriberBuilder middle(Object source) {
        this.middle = Objects.requireNonNull(source, "middle");
        return this;
    }

    public InscriberBuilder top(Object source) {
        this.top = Objects.requireNonNull(source, "top");
        return this;
    }

    public InscriberBuilder bottom(Object source) {
        this.bottom = Objects.requireNonNull(source, "bottom");
        return this;
    }

    @Override
    protected void validate() {
        if (middle == null) {
            throw new IllegalStateException("Inscriber recipe requires a middle ingredient");
        }
    }

    @Override
    protected void build(RecipeOutput output, ResourceKey<Recipe<?>> key) {
        Ingredient middleIngredient = toIngredient(middle);
        Ingredient topIngredient = top == null ? null : toIngredient(top);
        Ingredient bottomIngredient = bottom == null ? null : toIngredient(bottom);
        output.accept(key, new InscriberRecipe(
                middleIngredient,
                result(),
                Optional.ofNullable(topIngredient),
                Optional.ofNullable(bottomIngredient),
                mode), null);
    }
}
