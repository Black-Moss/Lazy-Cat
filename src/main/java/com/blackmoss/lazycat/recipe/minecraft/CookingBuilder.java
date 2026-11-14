package com.blackmoss.lazycat.recipe.minecraft;

import com.blackmoss.lazycat.api.recipe.AbstractRecipeBuilder;
import com.blackmoss.lazycat.api.recipe.CookingType;
import com.blackmoss.lazycat.api.recipe.RecipeContext;
import com.blackmoss.lazycat.api.recipe.RecipeIngredient;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.SimpleCookingRecipeBuilder;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.crafting.CookingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.ItemLike;

import java.util.EnumSet;
import java.util.Objects;

public final class CookingBuilder extends AbstractRecipeBuilder<CookingBuilder> {
    private final EnumSet<CookingType> types;
    private RecipeIngredient input;
    private Float experience;
    private Integer time;
    private CookingBookCategory bookCategory;
    private CookingType cookingType;

    public CookingBuilder(RecipeContext context, ItemLike result, Object input, CookingType type) {
        super(context, result);
        types = EnumSet.of(Objects.requireNonNull(type, "type"));
        this.input = RecipeIngredient.from(input);
        category(type.defaultCategory());
        defaultUnlock(this.input);
    }

    @Override
    protected CookingBuilder self() {
        return this;
    }

    public CookingBuilder input(Object source) {
        input = RecipeIngredient.from(source);
        return this;
    }

    public CookingBuilder types(CookingType... types) {
        Objects.requireNonNull(types, "types");
        if (types.length == 0) {
            throw new IllegalArgumentException("types must not be empty");
        }
        this.types.clear();
        for (CookingType type : types) {
            this.types.add(Objects.requireNonNull(type, "types contains null"));
        }
        return this;
    }

    public CookingBuilder all() {
        return types(CookingType.values());
    }

    public CookingBuilder exp(float experience) {
        this.experience = experience;
        return this;
    }

    public CookingBuilder time(int time) {
        this.time = time;
        return this;
    }

    public CookingBuilder bookCategory(CookingBookCategory bookCategory) {
        this.bookCategory = Objects.requireNonNull(bookCategory, "bookCategory");
        return this;
    }

    @Override
    public void save() {
        for (CookingType type : types) {
            cookingType = type;
            emit(keyFor(type));
        }
        cookingType = null;
    }

    @Override
    public void save(String path) {
        id(path);
        save();
    }

    @Override
    public void save(Identifier id) {
        this.id(id);
        save();
    }

    @Override
    protected void validate() {
        if (experience != null && experience < 0.0f) {
            throw new IllegalArgumentException("experience must not be negative");
        }
        if (time != null && time <= 0) {
            throw new IllegalArgumentException("time must be positive");
        }
    }

    @Override
    protected void build(RecipeOutput output, ResourceKey<Recipe<?>> key) {
        CookingType type = cookingType != null ? cookingType : types.iterator().next();
        Ingredient ingredient = input.resolve(context().items());
        float experience = this.experience != null ? this.experience : type.defaultExperience();
        int duration = time != null ? time : type.defaultTime();
        CookingBookCategory book = bookCategory != null ? bookCategory : toBookCategory(category());

        SimpleCookingRecipeBuilder builder = switch (type) {
            case SMELTING -> SimpleCookingRecipeBuilder.smelting(
                    ingredient, category(), book, result(), experience, duration);
            case BLASTING -> SimpleCookingRecipeBuilder.blasting(
                    ingredient, category(), book, result(), experience, duration);
            case SMOKING -> SimpleCookingRecipeBuilder.smoking(
                    ingredient, category(), result(), experience, duration);
            case CAMPFIRE -> SimpleCookingRecipeBuilder.campfireCooking(
                    ingredient, category(), result(), experience, duration);
        };

        builder.group(group());
        applyCriteria(builder::unlockedBy);
        builder.save(output, key);
    }

    private ResourceKey<Recipe<?>> keyFor(CookingType type) {
        Identifier explicit = explicitId();
        String namespace = explicit != null ? explicit.getNamespace() : context().namespace();
        String path;
        if (explicit != null) {
            path = types.size() == 1
                    ? explicit.getPath()
                    : explicit.getPath() + "_" + type.folder();
        } else {
            path = type.folder() + "/" + decoratePath(defaultIdPath());
        }
        return ResourceKey.create(
                Registries.RECIPE,
                Identifier.fromNamespaceAndPath(namespace, path));
    }

    private static CookingBookCategory toBookCategory(RecipeCategory category) {
        return switch (category) {
            case FOOD -> CookingBookCategory.FOOD;
            case DECORATIONS, BUILDING_BLOCKS -> CookingBookCategory.BLOCKS;
            default -> CookingBookCategory.MISC;
        };
    }
}
