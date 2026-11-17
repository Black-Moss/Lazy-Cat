package com.blackmoss.lazycat.recipe.ae2;

import appeng.recipes.entropy.EntropyMode;
import appeng.recipes.entropy.EntropyRecipeBuilder;
import appeng.recipes.entropy.PropertyValueMatcher;
import com.blackmoss.lazycat.api.recipe.AbstractRecipeBuilder;
import com.blackmoss.lazycat.api.recipe.RecipeContext;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;

import java.util.Objects;

@SuppressWarnings("unused")
public final class EntropyBuilder extends AbstractRecipeBuilder<EntropyBuilder> {
    private final EntropyRecipeBuilder delegate;
    private final EntropyMode mode;

    public EntropyBuilder(RecipeContext context, EntropyMode mode) {
        super(context, null);
        Objects.requireNonNull(mode, "mode");
        this.mode = mode;
        this.delegate = new EntropyRecipeBuilder().setMode(mode);
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
        return "entropy/" + folderOf(mode);
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

    public EntropyBuilder drops(ItemStackTemplate... drops) {
        delegate.setDrops(drops);
        return this;
    }

    public EntropyBuilder blockStateMatcher(String property, PropertyValueMatcher matcher) {
        delegate.addBlockStateMatcher(property, Objects.requireNonNull(matcher, "matcher"));
        return this;
    }

    public EntropyBuilder fluidStateMatcher(String property, PropertyValueMatcher matcher) {
        delegate.addFluidStateMatcher(property, Objects.requireNonNull(matcher, "matcher"));
        return this;
    }

    public EntropyBuilder blockStateApplier(String property, String value) {
        delegate.addBlockStateAppliers(property, value);
        return this;
    }

    public EntropyBuilder fluidStateApplier(String property, String value) {
        delegate.addFluidStateAppliers(property, value);
        return this;
    }

    @Override
    protected void validate() {
        if (explicitId() == null) {
            throw new IllegalStateException("Entropy recipe requires an explicit id");
        }
    }

    @Override
    protected void build(RecipeOutput output, ResourceKey<Recipe<?>> key) {
        output.accept(key, delegate.build(), null);
    }
}
