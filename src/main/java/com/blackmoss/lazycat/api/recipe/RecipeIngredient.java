package com.blackmoss.lazycat.api.recipe;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;

import java.util.Arrays;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.StreamSupport;

public final class RecipeIngredient {
    private final Function<HolderGetter<Item>, Ingredient> resolver;
    private final String label;

    private RecipeIngredient(Function<HolderGetter<Item>, Ingredient> resolver, String label) {
        this.resolver = Objects.requireNonNull(resolver, "resolver");
        this.label = Objects.requireNonNull(label, "label");
    }

    public static RecipeIngredient of(ItemLike item) {
        Objects.requireNonNull(item, "item");
        return new RecipeIngredient(getter -> Ingredient.of(item), itemPath(item));
    }

    public static RecipeIngredient of(ItemLike... items) {
        Objects.requireNonNull(items, "items");
        if (items.length == 0) {
            throw new IllegalArgumentException("items must not be empty");
        }
        ItemLike[] copy = Arrays.copyOf(items, items.length);
        for (ItemLike item : copy) {
            Objects.requireNonNull(item, "items contains null");
        }
        return new RecipeIngredient(getter -> Ingredient.of(copy), itemPath(copy[0]));
    }

    public static RecipeIngredient of(TagKey<Item> tag) {
        Objects.requireNonNull(tag, "tag");
        return new RecipeIngredient(
                getter -> Ingredient.of(getter.getOrThrow(tag)),
                tag.location().getPath());
    }

    public static RecipeIngredient of(Ingredient ingredient) {
        Objects.requireNonNull(ingredient, "ingredient");
        //noinspection deprecation
        String label = ingredient.items()
                .findFirst()
                .map(holder -> itemPath(holder.value()))
                .orElse("ingredient");
        return new RecipeIngredient(getter -> ingredient, label);
    }

    public static RecipeIngredient from(Object source) {
        Objects.requireNonNull(source, "source");
        return switch (source) {
            case RecipeIngredient ingredient -> ingredient;
            case Ingredient ingredient -> of(ingredient);
            case TagKey<?> tag -> tag.cast(Registries.ITEM)
                    .map(RecipeIngredient::of)
                    .orElseThrow(() -> new IllegalArgumentException("Not an item tag: " + tag));
            case String text -> fromItemId(text);
            case ItemLike item -> of(item);
            case ItemLike[] items -> of(items);
            case Iterable<?> elements -> fromArray(toArray(elements));
            case Object[] values -> fromArray(values);
            default -> throw new IllegalArgumentException(
                    "Unsupported recipe ingredient: " + source.getClass().getName());
        };
    }

    private static RecipeIngredient fromItemId(String text) {
        Objects.requireNonNull(text, "text");
        if (text.isBlank()) {
            throw new IllegalArgumentException("item id must not be blank");
        }
        Identifier identifier = Identifier.parse(text);
        ResourceKey<Item> key = ResourceKey.create(Registries.ITEM, identifier);
        return new RecipeIngredient(
                getter -> Ingredient.of(getter.getOrThrow(key).value()),
                identifier.getPath());
    }

    private static Object[] toArray(Iterable<?> elements) {
        Objects.requireNonNull(elements, "elements");
        return StreamSupport.stream(elements.spliterator(), false).toArray();
    }

    private static RecipeIngredient fromArray(Object[] values) {
        if (values.length == 0) {
            throw new IllegalArgumentException("ingredient sources must not be empty");
        }
        Object[] copy = Arrays.copyOf(values, values.length);
        String label = from(copy[0]).label();
        //noinspection deprecation
        return new RecipeIngredient(
                getter -> Ingredient.of(Arrays.stream(copy)
                        .flatMap(value -> from(value).resolve(getter).items())
                        .map(Holder::value)),
                label);
    }

    public Ingredient resolve(HolderGetter<Item> items) {
        Objects.requireNonNull(items, "items");
        return Objects.requireNonNull(resolver.apply(items), "resolved ingredient");
    }

    public String label() {
        return label;
    }

    public static String itemPath(ItemLike item) {
        Objects.requireNonNull(item, "item");
        return BuiltInRegistries.ITEM.getKey(item.asItem()).getPath();
    }
}
