package com.blackmoss.lazycat.recipe.ae2;

import com.blackmoss.lazycat.api.LazyIngredient;
import com.blackmoss.lazycat.api.recipe.AbstractRecipeBuilder;
import com.blackmoss.lazycat.api.recipe.RecipeContext;
import appeng.recipes.handlers.InscriberProcessType;
import appeng.recipes.handlers.InscriberRecipe;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.Objects;

@SuppressWarnings("unused")
public final class InscriberBuilder extends AbstractRecipeBuilder<InscriberBuilder> {
    private final InscriberProcessType mode;
    private Object input;
    private Object plate;
    private Object fluid;
    private Object item;

    public InscriberBuilder(RecipeContext context, Object result, InscriberProcessType mode) {
        super(context, result);
        this.mode = Objects.requireNonNull(mode, "mode");
    }

    @Override
    protected InscriberBuilder self() {
        return this;
    }

    @Override
    protected String defaultFolder() {
        return "inscriber";
    }

    public InscriberBuilder input(Object source) {
        this.input = Objects.requireNonNull(source, "input");
        return this;
    }

    public InscriberBuilder plate(Object source) {
        this.plate = Objects.requireNonNull(source, "plate");
        return this;
    }

    public InscriberBuilder fluid(Object source) {
        this.fluid = Objects.requireNonNull(source, "fluid");
        return this;
    }

    public InscriberBuilder item(Object source) {
        this.item = Objects.requireNonNull(source, "item");
        return this;
    }

    @Override
    protected void validate() {
        if (input == null || plate == null || fluid == null || item == null) {
            throw new IllegalStateException("Inscriber recipe requires input, plate, fluid and item");
        }
    }

    @Override
    protected void build(RecipeOutput output, ResourceLocation id) {
        Ingredient inputIngredient = LazyIngredient.from(input).resolve(context().items());
        Ingredient plateIngredient = LazyIngredient.from(plate).resolve(context().items());
        Ingredient fluidIngredient = LazyIngredient.from(fluid).resolve(context().items());
        Ingredient itemIngredient = LazyIngredient.from(item).resolve(context().items());
        InscriberRecipe recipe = new InscriberRecipe(
                inputIngredient, result(), fluidIngredient, itemIngredient, mode);
        output.accept(id, recipe, null);
    }
}