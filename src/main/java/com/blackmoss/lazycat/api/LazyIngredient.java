package com.blackmoss.lazycat.api;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.StreamSupport;

@SuppressWarnings("unused")
public final class LazyIngredient {
    private final Function<HolderGetter<Item>, Ingredient> resolver;
    private final Function<HolderGetter<Item>, ItemStack> stackResolver;
    private final String label;
    private final String reference;
    private final TagKey<Item> tag;

    private LazyIngredient(
            Function<HolderGetter<Item>, Ingredient> resolver,
            String label,
            String reference) {
        this(resolver, null, label, reference, null);
    }

    private LazyIngredient(
            Function<HolderGetter<Item>, Ingredient> resolver,
            Function<HolderGetter<Item>, ItemStack> stackResolver,
            String label,
            String reference,
            TagKey<Item> tag) {
        this.resolver = Objects.requireNonNull(resolver, "resolver");
        this.stackResolver = stackResolver;
        this.label = Objects.requireNonNull(label, "label");
        this.reference = reference;
        this.tag = tag;
    }

    public static LazyIngredient of(ItemLike item) {
        Objects.requireNonNull(item, "item");
        return new LazyIngredient(
                ignored -> Ingredient.of(item),
                ignored -> new ItemStack(item),
                LazyIdentifier.getItemPath(item),
                LazyIdentifier.getItemId(item),
                null);
    }

    public static LazyIngredient of(ItemStack stack) {
        Objects.requireNonNull(stack, "stack");
        if (stack.isEmpty()) {
            throw new IllegalArgumentException("stack must not be empty");
        }
        return new LazyIngredient(
                items -> Ingredient.of(stack.getItem()),
                items -> stack.copy(),
                LazyIdentifier.getItemPath(stack.getItem()),
                LazyIdentifier.getItemId(stack.getItem()),
                null);
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
        return new LazyIngredient(
                ignored -> Ingredient.of(copy),
                null,
                LazyIdentifier.getItemPath(copy[0]),
                null,
                null);
    }

    public static LazyIngredient of(TagKey<Item> tag) {
        Objects.requireNonNull(tag, "tag");
        return new LazyIngredient(
                ignored -> Ingredient.of(tag),
                getter -> new ItemStack(getter.getOrThrow(tag).get(0)),
                tag.location().getPath(),
                "#" + tag.location(),
                tag);
    }

    public static LazyIngredient of(Ingredient ingredient) {
        Objects.requireNonNull(ingredient, "ingredient");
        String label = Arrays.stream(ingredient.getItems())
                .findFirst()
                .filter(stack -> !stack.isEmpty())
                .map(stack -> LazyIdentifier.getItemPath(stack.getItem()))
                .orElse("ingredient");
        return new LazyIngredient(ignored -> ingredient, label, null);
    }

    public static LazyIngredient of(Holder<Item> holder) {
        Objects.requireNonNull(holder, "holder");
        Item item = holder.value();
        return new LazyIngredient(
                ignored -> Ingredient.of(item),
                ignored -> new ItemStack(holder),
                LazyIdentifier.getItemPath(item),
                LazyIdentifier.getItemId(item),
                null);
    }

    public static LazyIngredient from(Object source) {
        Objects.requireNonNull(source, "source");
        return switch (source) {
            case LazyIngredient ingredient -> ingredient;
            case ItemStack stack -> of(stack);
            case Ingredient ingredient -> of(ingredient);
            case TagKey<?> tag -> tag.cast(Registries.ITEM)
                    .map(LazyIngredient::of)
                    .orElseThrow(() -> new IllegalArgumentException("Not an item tag: " + tag));
            case Holder<?> holder -> of(castItemHolder(holder));
            case ResourceKey<?> key -> ofKey(castItemKey(key));
            case ResourceLocation id -> fromId(id.toString());
            case String text -> fromId(text);
            case ItemLike item -> of(item);
            case ItemLike[] items -> of(items);
            case Iterable<?> elements -> fromArray(toArray(elements));
            case Object[] values -> fromArray(values);
            default -> throw new IllegalArgumentException(
                    "Unsupported recipe ingredient: " + source.getClass().getName());
        };
    }

    @SuppressWarnings("unchecked")
    private static Holder<Item> castItemHolder(Holder<?> holder) {
        if (!(holder.value() instanceof Item)) {
            throw new IllegalArgumentException("Not an item holder: " + holder);
        }
        return (Holder<Item>) holder;
    }

    @SuppressWarnings("unchecked")
    private static ResourceKey<Item> castItemKey(ResourceKey<?> key) {
        return (ResourceKey<Item>) key;
    }

    private static LazyIngredient ofKey(ResourceKey<Item> key) {
        return new LazyIngredient(
                getter -> Ingredient.of(getter.getOrThrow(key).value()),
                getter -> new ItemStack(getter.getOrThrow(key)),
                key.location().getPath(),
                key.location().toString(),
                null);
    }

    private static LazyIngredient fromId(String text) {
        Objects.requireNonNull(text, "text");
        if (text.isBlank()) {
            throw new IllegalArgumentException("item id must not be blank");
        }
        if (text.startsWith("#")) {
            return of(TagKey.create(Registries.ITEM, ResourceLocation.parse(text.substring(1))));
        }
        return ofKey(ResourceKey.create(Registries.ITEM, ResourceLocation.parse(text)));
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
        return new LazyIngredient(
                getter -> Ingredient.of(Arrays.stream(copy)
                        .flatMap(value -> Arrays.stream(from(value).resolve(getter).getItems()))),
                null,
                label,
                null,
                null);
    }

    public Ingredient resolve(HolderGetter<Item> items) {
        Objects.requireNonNull(items, "items");
        return Objects.requireNonNull(resolver.apply(items), "resolved ingredient");
    }

    public ItemStack stack(HolderGetter<Item> items) {
        Objects.requireNonNull(items, "items");
        if (stackResolver == null) {
            throw new IllegalStateException("Ingredient has no single item stack");
        }
        return Objects.requireNonNull(stackResolver.apply(items), "resolved item stack");
    }

    public LazyIngredient count(int count) {
        if (count < 1 || count > 64) {
            throw new IllegalArgumentException("count must be between 1 and 64, was " + count);
        }
        if (stackResolver == null) {
            throw new IllegalStateException("Ingredient has no single item stack");
        }
        Function<HolderGetter<Item>, ItemStack> next = getter -> {
            ItemStack stack = stackResolver.apply(getter).copy();
            stack.setCount(count);
            return stack;
        };
        return new LazyIngredient(resolver, next, label, reference, tag);
    }

    public LazyIngredient components(DataComponentPatch components) {
        Objects.requireNonNull(components, "components");
        if (stackResolver == null) {
            throw new IllegalStateException("Ingredient has no single item stack");
        }
        Function<HolderGetter<Item>, ItemStack> next = getter -> {
            ItemStack stack = stackResolver.apply(getter).copy();
            stack.applyComponents(components);
            return stack;
        };
        return new LazyIngredient(resolver, next, label, reference, tag);
    }

    public String label() {
        return label;
    }

    public String reference() {
        if (reference == null) {
            throw new IllegalStateException("Ingredient holds several items and has no single reference");
        }
        return reference;
    }

    public Optional<TagKey<Item>> tag() {
        return Optional.ofNullable(tag);
    }
}
