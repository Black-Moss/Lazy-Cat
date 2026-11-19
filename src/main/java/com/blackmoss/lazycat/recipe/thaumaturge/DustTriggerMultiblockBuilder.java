package com.blackmoss.lazycat.recipe.thaumaturge;

import com.blackmoss.lazycat.api.recipe.RecipeContext;
import com.blackmoss.lazycat.api.recipe.thaumaturge.AbstractDustTriggerRecipeBuilder;
import com.leclowndu93150.thaumaturge.api.recipe.Blueprint;
import com.leclowndu93150.thaumaturge.content.recipe.dust.DustTriggerMultiblockRecipe;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeUnlockAdvancementBuilder;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.crafting.Recipe;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

@SuppressWarnings("unused")
public final class DustTriggerMultiblockBuilder extends AbstractDustTriggerRecipeBuilder<DustTriggerMultiblockBuilder> {
    private @Nullable Identifier blueprintId;

    public DustTriggerMultiblockBuilder(RecipeContext context, Object result, Object blueprint) {
        super(context, result);
        this.blueprintId = resolveBlueprint(blueprint);
        defaultUnlock(resultIngredient());
    }

    @Override
    protected DustTriggerMultiblockBuilder self() {
        return this;
    }

    public DustTriggerMultiblockBuilder blueprint(Object source) {
        this.blueprintId = resolveBlueprint(source);
        return this;
    }

    private Identifier resolveBlueprint(Object source) {
        Objects.requireNonNull(source, "blueprint");
        return switch (source) {
            case Identifier id -> id;
            case String text -> text.indexOf(':') >= 0
                    ? Identifier.parse(text)
                    : Identifier.fromNamespaceAndPath(context().modid(), text);
            case ResourceKey<?> key -> key.identifier();
            case Blueprint blueprint -> throw new IllegalArgumentException(
                    "Blueprint instances have no id, pass its Identifier instead");
            default -> throw new IllegalArgumentException(
                    "Unsupported blueprint id: " + source.getClass().getName());
        };
    }

    @Override
    protected void validate() {
        super.validate();
        if (blueprintId == null) {
            throw new IllegalStateException("Dust trigger recipe requires a blueprint");
        }
    }

    @Override
    protected void build(RecipeOutput output, ResourceKey<Recipe<?>> key) {
        DustTriggerMultiblockRecipe recipe = new DustTriggerMultiblockRecipe(blueprintId, result(), gate());
        RecipeUnlockAdvancementBuilder advancement = new RecipeUnlockAdvancementBuilder();
        applyCriteria(advancement::unlockedBy);
        output.accept(key, recipe, advancement.build(output, key, category()));
    }
}
