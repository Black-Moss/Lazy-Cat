package com.blackmoss.lazycat.recipe.thaumaturge;

import com.blackmoss.lazycat.api.LazyIngredient;
import com.blackmoss.lazycat.api.recipe.RecipeContext;
import com.blackmoss.lazycat.api.recipe.thaumaturge.AbstractThaumaturgeRecipeBuilder;
import com.leclowndu93150.thaumaturge.api.items.InfusionEnchantment;
import com.leclowndu93150.thaumaturge.content.infusion.InfusionEnchantmentRecipe;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@SuppressWarnings("unused")
public final class InfusionEnchantmentBuilder extends AbstractThaumaturgeRecipeBuilder<InfusionEnchantmentBuilder> {
    private final InfusionEnchantment enchantment;
    private final List<Ingredient> components = new ArrayList<>();
    private final Ingredient displayCatalyst;

    public InfusionEnchantmentBuilder(RecipeContext context, Object enchantment, Object displayCatalyst) {
        super(context, null);
        this.enchantment = resolveEnchantment(enchantment);
        LazyIngredient catalyst = LazyIngredient.from(Objects.requireNonNull(displayCatalyst, "displayCatalyst"));
        this.displayCatalyst = catalyst.resolve(context.items());
        id("infusion_enchantment/" + this.enchantment.getSerializedName());
    }

    public InfusionEnchantmentBuilder(
            RecipeContext context,
            Object enchantment,
            Object displayCatalyst,
            String recipeId) {
        this(context, enchantment, displayCatalyst);
        if (recipeId != null) {
            id(recipeId);
        }
    }

    private static InfusionEnchantment resolveEnchantment(Object source) {
        Objects.requireNonNull(source, "enchantment");
        return switch (source) {
            case InfusionEnchantment enchantment -> enchantment;
            case StringRepresentable representable -> bySerializedName(representable.getSerializedName());
            case ResourceLocation identifier -> bySerializedName(identifier.getPath());
            case ResourceKey<?> key -> bySerializedName(key.location().getPath());
            case String text -> bySerializedName(text);
            default -> throw new IllegalArgumentException(
                    "Unsupported infusion enchantment: " + source.getClass().getName());
        };
    }

    private static InfusionEnchantment bySerializedName(String name) {
        Objects.requireNonNull(name, "name");
        String normalized = name.indexOf(':') >= 0 ? ResourceLocation.parse(name).getPath() : name;
        for (InfusionEnchantment enchantment : InfusionEnchantment.values()) {
            if (enchantment.getSerializedName().equals(normalized)) {
                return enchantment;
            }
        }
        throw new IllegalArgumentException("Unknown infusion enchantment: " + name);
    }

    @Override
    protected InfusionEnchantmentBuilder self() {
        return this;
    }

    public InfusionEnchantmentBuilder component(Object source) {
        return component(source, 1);
    }

    public InfusionEnchantmentBuilder component(Object source, int count) {
        if (count < 1) {
            throw new IllegalArgumentException("component count must be positive");
        }
        for (int i = 0; i < count; i++) {
            components.add(LazyIngredient.from(source).resolve(context().items()));
        }
        return this;
    }

    @Override
    protected void validate() {
        if (components.isEmpty()) {
            throw new IllegalStateException("Infusion enchantment recipe requires at least one component");
        }
        requireAspects();
    }

    @Override
    protected void build(RecipeOutput output, ResourceLocation id) {
        InfusionEnchantmentRecipe recipe = new InfusionEnchantmentRecipe(
                enchantment, components, buildAspects(), displayCatalyst, gate());
        output.accept(id, recipe, null);
    }
}