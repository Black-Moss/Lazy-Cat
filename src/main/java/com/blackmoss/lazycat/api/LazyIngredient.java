package com.blackmoss.lazycat.api;

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

public final class LazyIngredient {
    private final Function<HolderGetter<Item>, Ingredient> resolver;
    private final String label;
    private final String reference;

    private LazyIngredient(Function<HolderGetter<Item>, Ingredient> resolver, String label) {
        this(resolver, label, null);
    }

    private LazyIngredient(Function<HolderGetter<Item>, Ingredient> resolver, String label, String reference) {
        this.resolver = Objects.requireNonNull(resolver, "resolver");
        this.label = Objects.requireNonNull(label, "label");
        this.reference = reference;
    }

    public static LazyIngredient of(ItemLike item) {
        Objects.requireNonNull(item, "item");
        return new LazyIngredient(getter -> Ingredient.of(item), itemPath(item), itemId(item));
    }

    public static LazyIngredient of(ItemLike... items) {
        Objects.requireNonNull(items, "items");
        if (items.length == 0) {
            throw new IllegalArgumentException("items must not be empty");
        }
        ItemLike[] copy = Arrays.copyOf(items, items.length);
        for (ItemLike item : copy) {
            Objects.requireNonNull(item, "items contains null");
        }
        return new LazyIngredient(getter -> Ingredient.of(copy), itemPath(copy[0]));
    }

    public static LazyIngredient of(TagKey<Item> tag) {
        Objects.requireNonNull(tag, "tag");
        return new LazyIngredient(
                getter -> Ingredient.of(getter.getOrThrow(tag)),
                tag.location().getPath(),
                "#" + tag.location());
    }

    public static LazyIngredient of(Ingredient ingredient) {
        Objects.requireNonNull(ingredient, "ingredient");
        //noinspection deprecation
        String label = ingredient.items()
                .findFirst()
                .map(holder -> itemPath(holder.value()))
                .orElse("ingredient");
        return new LazyIngredient(getter -> ingredient, label);
    }

    public static LazyIngredient of(Holder<?> holder) {
        Objects.requireNonNull(holder, "holder");
        ItemLike item = (ItemLike) holder.value();
        return new LazyIngredient(getter -> Ingredient.of(item), itemPath(item), itemId(item));
    }

    public static LazyIngredient from(Object source) {
        Objects.requireNonNull(source, "source");
        return switch (source) {
            case LazyIngredient ingredient -> ingredient;
            case Ingredient ingredient -> of(ingredient);
            case TagKey<?> tag -> tag.cast(Registries.ITEM)
                    .map(LazyIngredient::of)
                    .orElseThrow(() -> new IllegalArgumentException("Not an item tag: " + tag));
            case Holder<?> holder -> of(holder);
            case ResourceKey<?> key -> {
                //noinspection unchecked
                ResourceKey<Item> itemKey = (ResourceKey<Item>) key;
                yield new LazyIngredient(
                        getter -> Ingredient.of(getter.getOrThrow(itemKey).value()),
                        itemKey.identifier().getPath(),
                        itemKey.identifier().toString());
            }
            case Identifier identifierId -> fromId(identifierId.toString());
            case String stringId -> fromId(stringId);
            case ItemLike item -> of(item);
            case ItemLike[] items -> of(items);
            case Iterable<?> elements -> fromArray(toArray(elements));
            case Object[] values -> fromArray(values);
            default -> throw new IllegalArgumentException(
                    "Unsupported recipe ingredient: " + source.getClass().getName());
        };
    }

    private static LazyIngredient fromId(String text) {
        Objects.requireNonNull(text, "text");
        if (text.isBlank()) {
            throw new IllegalArgumentException("item id must not be blank");
        }
        if (text.startsWith("#")) {
            return of(TagKey.create(Registries.ITEM, Identifier.parse(text.substring(1))));
        }
        Identifier identifier = Identifier.parse(text);
        ResourceKey<Item> key = ResourceKey.create(Registries.ITEM, identifier);
        return new LazyIngredient(
                getter -> Ingredient.of(getter.getOrThrow(key).value()),
                identifier.getPath(),
                identifier.toString());
    }

    private static Object[] toArray(Iterable<?> elements) {
        Objects.requireNonNull(elements, "elements");
        return StreamSupport.stream(elements.spliterator(), false).toArray();
    }

    private static LazyIngredient fromArray(Object[] values) {
        if (values.length == 0) {
            throw new IllegalArgumentException("ingredient sources must not be empty");
        }
        Object[] copy = Arrays.copyOf(values, values.length);
        String label = from(copy[0]).label();
        //noinspection deprecation
        return new LazyIngredient(
                getter -> Ingredient.of(Arrays.stream(copy)
                        .flatMap(value -> from(value).resolve(getter).items())
                        .map(Holder::value)),
                label);
    }

    public static String itemPath(ItemLike item) {
        Objects.requireNonNull(item, "item");
        return BuiltInRegistries.ITEM.getKey(item.asItem()).getPath();
    }

    private static String itemId(ItemLike item) {
        Objects.requireNonNull(item, "item");
        Identifier id = Objects.requireNonNull(BuiltInRegistries.ITEM.getKey(item.asItem()),
                () -> "Unregistered item: " + item);
        return id.toString();
    }

    public Ingredient resolve(HolderGetter<Item> items) {
        Objects.requireNonNull(items, "items");
        return Objects.requireNonNull(resolver.apply(items), "resolved ingredient");
    }

    public String label() {
        return label;
    }

    public String reference() {
        if (this.reference == null) {
            throw new IllegalStateException("Ingredient holds several items and has no single reference");
        }
        return this.reference;
    }
}
