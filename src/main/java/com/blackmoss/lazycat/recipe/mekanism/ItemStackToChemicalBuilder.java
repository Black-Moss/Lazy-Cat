package com.blackmoss.lazycat.recipe.mekanism;

import com.blackmoss.lazycat.api.recipe.RecipeContext;
import com.blackmoss.lazycat.api.recipe.mekanism.AbstractMekanismRecipeBuilder;
import mekanism.api.datagen.recipe.builder.ItemStackToChemicalRecipeBuilder;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.crafting.Recipe;

import java.util.Objects;

@SuppressWarnings("unused")
public final class ItemStackToChemicalBuilder extends AbstractMekanismRecipeBuilder<ItemStackToChemicalBuilder> {
    private final ItemStackToChemicalType type;
    private Object input;
    private Object output;
    private int outputAmount = 1;

    public ItemStackToChemicalBuilder(RecipeContext context, ItemStackToChemicalType type) {
        super(context, null);
        this.type = Objects.requireNonNull(type, "type");
    }

    @Override
    protected ItemStackToChemicalBuilder self() {
        return this;
    }

    @Override
    protected String defaultIdPath() {
        return sourcePath(output, "output");
    }

    @Override
    protected String defaultFolder() {
        return folderOf(type);
    }

    public ItemStackToChemicalBuilder input(Object source) {
        this.input = Objects.requireNonNull(source, "input");
        return this;
    }

    public ItemStackToChemicalBuilder output(Object source) {
        return output(source, outputAmount);
    }

    public ItemStackToChemicalBuilder output(Object source, int amount) {
        this.output = Objects.requireNonNull(source, "output");
        this.outputAmount = amount;
        return this;
    }

    @Override
    protected void validate() {
        if (input == null) {
            throw new IllegalStateException("requires an item input");
        }
        if (output == null) {
            throw new IllegalStateException("requires a chemical output");
        }
        if (type == null) {
            throw new IllegalStateException("requires a rotary mode");
        }
    }

    @Override
    protected void build(RecipeOutput output, ResourceKey<Recipe<?>> key) {
        ItemStackToChemicalRecipeBuilder builder = switch (type) {
            case CHEMICAL_CONVERSION ->
                    ItemStackToChemicalRecipeBuilder.chemicalConversion(itemIngredient(input), chemicalTemplate(this.output, outputAmount));
            case OXIDIZING ->
                    ItemStackToChemicalRecipeBuilder.oxidizing(itemIngredient(input), chemicalTemplate(this.output, outputAmount));
            case PIGMENT_EXTRACTING ->
                    ItemStackToChemicalRecipeBuilder.pigmentExtracting(itemIngredient(input), chemicalTemplate(this.output, outputAmount));
        };
        builder.save(output, key.identifier());
    }

    public enum ItemStackToChemicalType {
        CHEMICAL_CONVERSION,
        OXIDIZING,
        PIGMENT_EXTRACTING
    }
}
