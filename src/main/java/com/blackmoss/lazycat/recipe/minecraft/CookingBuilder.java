package com.blackmoss.lazycat.recipe.minecraft;

import com.blackmoss.lazycat.api.LazyIngredient;
import com.blackmoss.lazycat.api.recipe.AbstractRecipeBuilder;
import com.blackmoss.lazycat.api.recipe.RecipeContext;
import com.blackmoss.lazycat.api.recipe.minecraft.CookingType;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.SimpleCookingRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.EnumSet;
import java.util.Objects;

@SuppressWarnings("unused")
public final class CookingBuilder extends AbstractRecipeBuilder<CookingBuilder> {
    private final EnumSet<CookingType> types;
    private LazyIngredient input;
    private Float experience;
    private Integer time;
    private CookingType type;

    public CookingBuilder(RecipeContext context, Object result, Object input, CookingType type) {
        super(context, result);
        types = EnumSet.of(Objects.requireNonNull(type, "type"));
        this.input = LazyIngredient.from(input);
        category(type.defaultCategory());
        defaultUnlock(this.input);
    }

    @Override
    protected CookingBuilder self() {
        return this;
    }

    public CookingBuilder input(Object source) {
        input = LazyIngredient.from(source);
        replaceDefaultUnlock(input);
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

    @Override
    public void save() {
        for (CookingType type : types) {
            this.type = type;
            validate();
            emit(idFor(type));
        }
    }

    @Override
    public void save(String path) {
        id(path);
        save();
    }

    @Override
    public void save(ResourceLocation id) {
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
        if (type == null) {
            throw new IllegalStateException("requires a type");
        }
    }

    @Override
    protected void build(RecipeOutput output, ResourceLocation id) {
        CookingType type = this.type != null ? this.type : types.iterator().next();
        Ingredient ingredient = input.resolve(context().items());
        float exp = experience != null ? experience : type.defaultExperience();
        int duration = time != null ? time : type.defaultTime();
        SimpleCookingRecipeBuilder builder = switch (type) {
            case SMELTING -> SimpleCookingRecipeBuilder.smelting(ingredient, category(), result(), exp, duration);
            case BLASTING -> SimpleCookingRecipeBuilder.blasting(ingredient, category(), result(), exp, duration);
            case SMOKING -> SimpleCookingRecipeBuilder.smoking(ingredient, category(), result(), exp, duration);
            case CAMPFIRE ->
                    SimpleCookingRecipeBuilder.campfireCooking(ingredient, category(), result(), exp, duration);
        };

        builder.group(group());
        applyCriteria(builder::unlockedBy);
        builder.save(output, id);
    }

    private ResourceLocation idFor(CookingType type) {
        ResourceLocation explicit = explicitId();
        String modid = explicit != null ? explicit.getNamespace() : context().modid();
        String path;
        if (explicit != null) {
            path = types.size() == 1
                    ? explicit.getPath()
                    : explicit.getPath() + "_" + type.folder();
        } else {
            path = type.folder() + "/" + decoratePath(defaultIdPath());
        }
        return withFolder(ResourceLocation.fromNamespaceAndPath(modid, path));
    }
}
