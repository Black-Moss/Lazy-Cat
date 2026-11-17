package com.blackmoss.lazycat.recipe.thaumaturge;

import com.blackmoss.lazycat.api.LazyIngredient;
import com.blackmoss.lazycat.api.recipe.AbstractRecipeBuilder;
import com.blackmoss.lazycat.api.recipe.RecipeContext;
import com.blackmoss.lazycat.api.recipe.thaumaturge.AbstractThaumaturgeRecipeBuilder;
import com.leclowndu93150.thaumaturge.api.aspect.AspectInstance;
import com.leclowndu93150.thaumaturge.api.aspect.AspectList;
import com.leclowndu93150.thaumaturge.api.aspect.IAspect;
import com.leclowndu93150.thaumaturge.api.items.InfusionEnchantment;
import com.leclowndu93150.thaumaturge.api.recipe.ResearchGate;
import com.leclowndu93150.thaumaturge.content.infusion.InfusionEnchantmentRecipe;
import net.minecraft.core.HolderGetter;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@SuppressWarnings("unused")
public final class InfusionEnchantmentBuilder extends AbstractRecipeBuilder<InfusionEnchantmentBuilder> {
    private final HolderGetter<IAspect> aspects;
    private final InfusionEnchantment enchantment;
    private final List<AspectInstance> entries = new ArrayList<>();
    private final List<Ingredient> components = new ArrayList<>();
    private final Ingredient displayCatalyst;
    private @Nullable ResearchGate gate;

    public InfusionEnchantmentBuilder(RecipeContext context, Object enchantment, Object displayCatalyst) {
        super(context, null);
        this.aspects = context.lookupProvide().lookupOrThrow(IAspect.REGISTRY_KEY);
        this.enchantment = resolveEnchantment(enchantment);
        this.displayCatalyst = LazyIngredient.from(Objects.requireNonNull(displayCatalyst, "displayCatalyst"))
                .resolve(context.items());
        id("infusion_enchantment/" + this.enchantment.getSerializedName());
    }

    private static InfusionEnchantment resolveEnchantment(Object source) {
        Objects.requireNonNull(source, "enchantment");
        return switch (source) {
            case InfusionEnchantment enchantment -> enchantment;
            case StringRepresentable representable -> bySerializedName(representable.getSerializedName());
            case Identifier identifier -> bySerializedName(identifier.getPath());
            case ResourceKey<?> key -> bySerializedName(key.identifier().getPath());
            case String text -> bySerializedName(text);
            default -> throw new IllegalArgumentException(
                    "Unsupported infusion enchantment: " + source.getClass().getName());
        };
    }

    private static InfusionEnchantment bySerializedName(String name) {
        Objects.requireNonNull(name, "name");
        String normalized = name.indexOf(':') >= 0 ? Identifier.parse(name).getPath() : name;
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
        for (int i = 0; i < count; i++) {
            components.add(LazyIngredient.from(source).resolve(context().items()));
        }
        return this;
    }

    public InfusionEnchantmentBuilder aspect(Object source) {
        return aspect(source, 1);
    }

    public InfusionEnchantmentBuilder aspect(Object source, int amount) {
        entries.add(new AspectInstance(AbstractThaumaturgeRecipeBuilder.resolveAspect(aspects, source, amount), amount));
        return this;
    }

    public InfusionEnchantmentBuilder gate(ResearchGate gate) {
        this.gate = Objects.requireNonNull(gate, "gate");
        return this;
    }

    public InfusionEnchantmentBuilder gate(Identifier entry) {
        return gate(new ResearchGate(entry, Optional.empty(), false));
    }

    public InfusionEnchantmentBuilder gate(String entry) {
        return gate(Identifier.parse(entry));
    }

    @Override
    protected void validate() {
        if (components.isEmpty()) {
            throw new IllegalStateException("Infusion enchantment recipe requires at least one component");
        }
        if (entries.isEmpty()) {
            throw new IllegalStateException("Infusion enchantment recipe requires at least one aspect");
        }
    }

    @Override
    protected void build(RecipeOutput output, ResourceKey<Recipe<?>> key) {
        AspectList aspects = AspectList.EMPTY;
        for (AspectInstance entry : entries) {
            aspects = aspects.add(entry);
        }
        InfusionEnchantmentRecipe recipe = new InfusionEnchantmentRecipe(
                enchantment, components, aspects, displayCatalyst, Optional.ofNullable(gate));
        output.accept(key, recipe, null);
    }
}
