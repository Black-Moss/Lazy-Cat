package com.blackmoss.lazycat.recipe.ae2;

import appeng.recipes.entropy.EntropyMode;
import appeng.recipes.entropy.EntropyRecipe;
import appeng.recipes.entropy.EntropyRecipeBuilder;
import com.blackmoss.lazycat.api.recipe.AbstractRecipeBuilder;
import com.blackmoss.lazycat.api.recipe.RecipeContext;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;

import java.util.Locale;
import java.util.Objects;

@SuppressWarnings("unused")
public final class EntropyBuilder extends AbstractRecipeBuilder<EntropyBuilder> {
    private final EntropyRecipeBuilder delegate;
    private final EntropyMode mode;

    public EntropyBuilder(RecipeContext context, EntropyMode mode) {
        super(context, null);
        this.mode = Objects.requireNonNull(mode, "mode");
        this.delegate = new EntropyRecipeBuilder().setMode(this.mode);
    }

    public static EntropyBuilder cool(RecipeContext context) {
        return new EntropyBuilder(context, EntropyMode.COOL);
    }

    public static EntropyBuilder heat(RecipeContext context) {
        return new EntropyBuilder(context, EntropyMode.HEAT);
    }

    @Override
    protected EntropyBuilder self() {
        return this;
    }

    @Override
    protected String defaultFolder() {
        return "entropy/" + mode.name().toLowerCase(Locale.ROOT);
    }

    public EntropyBuilder inputBlock(Block block) {
        delegate.setInputBlock(Objects.requireNonNull(block, "block"));
        return this;
    }

    public EntropyBuilder inputFluid(Fluid fluid) {
        delegate.setInputFluid(Objects.requireNonNull(fluid, "fluid"));
        return this;
    }

    public EntropyBuilder outputBlock(Block block) {
        delegate.setOutputBlock(Objects.requireNonNull(block, "block"));
        return this;
    }

    public EntropyBuilder outputBlockKeep(boolean keep) {
        delegate.setOutputBlockKeep(keep);
        return this;
    }

    public EntropyBuilder outputFluid(Fluid fluid) {
        delegate.setOutputFluid(Objects.requireNonNull(fluid, "fluid"));
        return this;
    }

    public EntropyBuilder outputFluidKeep(boolean keep) {
        delegate.setOutputFluidKeep(keep);
        return this;
    }

    public EntropyBuilder drops(ItemStack... drops) {
        delegate.setDrops(drops);
        return this;
    }

    @Override
    protected void validate() {
        if (explicitId() == null) {
            throw new IllegalStateException("Entropy recipe requires an explicit id");
        }
    }

    @Override
    protected void build(RecipeOutput output, ResourceLocation id) {
        EntropyRecipe recipe = delegate.build();
        output.accept(id, recipe, null);
    }
}