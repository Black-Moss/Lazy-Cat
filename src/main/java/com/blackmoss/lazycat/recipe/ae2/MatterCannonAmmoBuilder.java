package com.blackmoss.lazycat.recipe.ae2;

import com.blackmoss.lazycat.api.recipe.AbstractRecipeBuilder;
import com.blackmoss.lazycat.api.recipe.RecipeContext;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.ItemLike;
import appeng.recipes.mattercannon.MatterCannonAmmo;

public final class MatterCannonAmmoBuilder extends AbstractRecipeBuilder<MatterCannonAmmoBuilder> {
    private final float weight;

    public MatterCannonAmmoBuilder(RecipeContext context, ItemLike ammo, float weight) {
        super(context, ammo);
        if (weight < 0f) {
            throw new IllegalArgumentException("weight must not be negative");
        }
        this.weight = weight;
    }

    @Override
    protected MatterCannonAmmoBuilder self() {
        return this;
    }

    @Override
    protected void build(RecipeOutput output, ResourceKey<Recipe<?>> key) {
        output.accept(key, new MatterCannonAmmo(toIngredient(resultItem()), weight), null);
    }
}
