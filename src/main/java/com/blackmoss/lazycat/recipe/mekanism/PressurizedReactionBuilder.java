package com.blackmoss.lazycat.recipe.mekanism;

import com.blackmoss.lazycat.api.recipe.RecipeContext;
import com.blackmoss.lazycat.api.recipe.mekanism.AbstractMekanismRecipeBuilder;
import mekanism.api.MekanismAPI;
import mekanism.api.chemical.Chemical;
import mekanism.api.chemical.ChemicalStackTemplate;
import mekanism.api.datagen.recipe.builder.PressurizedReactionRecipeBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.ItemLike;

import java.util.Objects;

public final class PressurizedReactionBuilder extends AbstractMekanismRecipeBuilder<PressurizedReactionBuilder> {
    private Object solid;
    private Object fluid;
    private int fluidAmount = 1;
    private Object chemical;
    private int chemicalAmount = 1;
    private int duration = 100;
    private Object chemicalOutput;
    private int chemicalOutputAmount = 1;

    public PressurizedReactionBuilder(RecipeContext context, ItemLike result) {
        super(context, result);
    }

    @Override
    protected PressurizedReactionBuilder self() {
        return this;
    }

    @Override
    protected String defaultFolder() {
        return "reaction";
    }

    public PressurizedReactionBuilder solid(Object source) {
        this.solid = Objects.requireNonNull(source, "solid");
        return this;
    }

    public PressurizedReactionBuilder fluid(Object source) {
        return fluid(source, fluidAmount);
    }

    public PressurizedReactionBuilder fluid(Object source, int amount) {
        this.fluid = Objects.requireNonNull(source, "fluid");
        this.fluidAmount = amount;
        return this;
    }

    public PressurizedReactionBuilder chemical(Object source) {
        return chemical(source, chemicalAmount);
    }

    public PressurizedReactionBuilder chemical(Object source, int amount) {
        this.chemical = Objects.requireNonNull(source, "chemical");
        this.chemicalAmount = amount;
        return this;
    }

    public PressurizedReactionBuilder duration(int ticks) {
        this.duration = ticks;
        return this;
    }

    public PressurizedReactionBuilder chemicalOutput(Object source) {
        return chemicalOutput(source, chemicalOutputAmount);
    }

    public PressurizedReactionBuilder chemicalOutput(Object source, int amount) {
        this.chemicalOutput = Objects.requireNonNull(source, "chemicalOutput");
        this.chemicalOutputAmount = amount;
        return this;
    }

    @SuppressWarnings("unchecked")
    private Holder<Chemical> chemicalHolder(Object source) {
        HolderGetter<Chemical> chemicals = context().lookupProvide().lookupOrThrow(MekanismAPI.CHEMICAL_REGISTRY_NAME);
        if (source instanceof Holder<?> h) {
            return (Holder<Chemical>) h;
        }
        if (source instanceof ResourceKey<?> k) {
            return chemicals.getOrThrow((ResourceKey<Chemical>) k);
        }
        if (source instanceof String s) {
            return chemicals.getOrThrow(ResourceKey.create(MekanismAPI.CHEMICAL_REGISTRY_NAME, Identifier.parse(s)));
        }
        throw new IllegalArgumentException("Unsupported Mekanism chemical source: " + source.getClass().getName());
    }

    @Override
    protected void validate() {
        if (solid == null) {
            throw new IllegalStateException("requires a solid input");
        }
        if (fluid == null) {
            throw new IllegalStateException("requires a fluid input");
        }
        if (chemical == null) {
            throw new IllegalStateException("requires a chemical input");
        }
        if (chemicalOutput == null) {
            throw new IllegalStateException("requires a chemical output");
        }
    }

    @Override
    protected void build(RecipeOutput output, ResourceKey<Recipe<?>> key) {
        ChemicalStackTemplate outputChem = new ChemicalStackTemplate(chemicalHolder(chemicalOutput), chemicalOutputAmount);
        PressurizedReactionRecipeBuilder.reaction(
                        itemIngredient(solid), fluidIngredient(fluid, fluidAmount), chemicalIngredient(chemical, chemicalAmount),
                        duration, result(), outputChem)
                .save(output, key.identifier());
    }
}
