package com.blackmoss.lazycat.recipe.thaumaturge;

import com.blackmoss.lazycat.api.recipe.RecipeContext;
import com.blackmoss.lazycat.api.recipe.thaumaturge.AbstractDustTriggerRecipeBuilder;
import com.leclowndu93150.thaumaturge.content.recipe.dust.DustTriggerSimpleRecipe;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeUnlockAdvancementBuilder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("unused")
public final class DustTriggerSimpleBuilder extends AbstractDustTriggerRecipeBuilder<DustTriggerSimpleBuilder> {
    private @Nullable Block target;

    public DustTriggerSimpleBuilder(RecipeContext context, Object result, Object target) {
        super(context, result);
        this.target = resolveBlock(target);
        defaultUnlock(resultIngredient());
    }

    @Override
    protected DustTriggerSimpleBuilder self() {
        return this;
    }

    public DustTriggerSimpleBuilder target(Object source) {
        this.target = resolveBlock(source);
        return this;
    }

    @Override
    protected void validate() {
        super.validate();
        if (target == null) {
            throw new IllegalStateException("Dust trigger recipe requires a target block");
        }
    }

    @Override
    protected void build(RecipeOutput output, ResourceKey<Recipe<?>> key) {
        DustTriggerSimpleRecipe recipe = new DustTriggerSimpleRecipe(target, result(), gate());
        RecipeUnlockAdvancementBuilder advancement = new RecipeUnlockAdvancementBuilder();
        applyCriteria(advancement::unlockedBy);
        output.accept(key, recipe, advancement.build(output, key, category()));
    }
}
