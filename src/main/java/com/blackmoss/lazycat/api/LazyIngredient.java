package com.blackmoss.lazycat.api;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.StreamSupport;

public final class LazyIngredient {
    private final Function<HolderGetter<Item>, Ingredient> resolver;
    private final Function<HolderGetter<Item>, ItemStackTemplate> templateResolver;
    private final String label;
    private final String reference;
    private final TagKey<Item> tag;

    private LazyIngredient(
            Function<HolderGetter<Item>, Ingredient> resolver,
            Function<HolderGetter<Item>, ItemStackTemplate> templateResolver,
            String label,
            String reference) {
        this(resolver, templateResolver, label, reference, null);
    }

    private LazyIngredient(
            Function<HolderGetter<Item>, Ingredient> resolver,
            Function<HolderGetter<Item>, ItemStackTemplate> templateResolver,
            String label,
            String reference,
            TagKey<Item> tag) {
        this.resolver = Objects.requireNonNull(resolver, "resolver");
        this.templateResolver = templateResolver;
        this.label = Objects.requireNonNull(label, "label");
        this.reference = reference;
        this.tag = tag;
    }

    public static LazyIngredient of(ItemLike item) {
        Objects.requireNonNull(item, "item");
        return new LazyIngredient(
                _ -> Ingredient.of(item),
                _ -> new ItemStackTemplate(item.asItem()),
                LazyIdentifier.getItemPath(item),
                LazyIdentifier.getItemId(item));
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
                _ -> Ingredient.of(copy),
                null,
                LazyIdentifier.getItemPath(copy[0]),
                null);
    }

    public static LazyIngredient of(TagKey<Item> tag) {
        Objects.requireNonNull(tag, "tag");
        return new LazyIngredient(
                getter -> Ingredient.of(getter.getOrThrow(tag)),
                getter -> new ItemStackTemplate(getter.getOrThrow(tag).get(0)),
                tag.location().getPath(),
                "#" + tag.location(),
                tag);
    }

    public static LazyIngredient of(Ingredient ingredient) {
        Objects.requireNonNull(ingredient, "ingredient");
        //noinspection deprecation
        String label = ingredient.items()
                .findFirst()
                .map(holder -> LazyIdentifier.getItemPath(holder.value()))
                .orElse("ingredient");
        return new LazyIngredient(_ -> ingredient, null, label, null);
    }

    public static LazyIngredient of(ItemStackTemplate template) {
        Objects.requireNonNull(template, "template");
        return new LazyIngredient(
                _ -> Ingredient.of(template.item().value()),
                _ -> template,
                LazyIdentifier.getItemPath(template.item().value()),
                LazyIdentifier.getItemId(template.item().value()));
    }

    public static LazyIngredient of(Holder<Item> holder) {
        Objects.requireNonNull(holder, "holder");
        Item item = holder.value();
        return new LazyIngredient(
                _ -> Ingredient.of(item),
                _ -> new ItemStackTemplate(holder),
                LazyIdentifier.getItemPath(item),
                LazyIdentifier.getItemId(item));
    }

    public static LazyIngredient from(Object source) {
        Objects.requireNonNull(source, "source");
        return switch (source) {
            case LazyIngredient ingredient -> ingredient;
            case Ingredient ingredient -> of(ingredient);
            case ItemStackTemplate template -> of(template);
            case TagKey<?> tag -> tag.cast(Registries.ITEM)
                    .map(LazyIngredient::of)
                    .orElseThrow(() -> new IllegalArgumentException("Not an item tag: " + tag));
            case Holder<?> holder -> of(castItemHolder(holder));
            case ResourceKey<?> key -> ofKey(castItemKey(key));
            case Identifier identifier -> fromId(identifier.toString());
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
                getter -> new ItemStackTemplate(getter.getOrThrow(key)),
                key.identifier().getPath(),
                key.identifier().toString());
    }

    private static LazyIngredient fromId(String text) {
        Objects.requireNonNull(text, "text");
        if (text.isBlank()) {
            throw new IllegalArgumentException("item id must not be blank");
        }
        if (text.startsWith("#")) {
            return of(TagKey.create(Registries.ITEM, Identifier.parse(text.substring(1))));
        }
        return ofKey(ResourceKey.create(Registries.ITEM, Identifier.parse(text)));
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
                null,
                label,
                null);
    }

    public Ingredient resolve(HolderGetter<Item> items) {
        Objects.requireNonNull(items, "items");
        return Objects.requireNonNull(resolver.apply(items), "resolved ingredient");
    }

    public ItemStackTemplate template(HolderGetter<Item> items) {
        Objects.requireNonNull(items, "items");
        if (this.templateResolver == null) {
            throw new IllegalStateException("Ingredient has no single item template");
        }
        return Objects.requireNonNull(this.templateResolver.apply(items), "resolved item template");
    }

    public LazyIngredient count(int count) {
        if (count < 1 || count > 64) {
            throw new IllegalArgumentException("count must be between 1 and 64, was " + count);
        }
        if (this.templateResolver == null) {
            throw new IllegalStateException("Ingredient has no single item template");
        }
        Function<HolderGetter<Item>, ItemStackTemplate> next =
                getter -> this.templateResolver.apply(getter).withCount(count);
        return new LazyIngredient(this.resolver, next, this.label, this.reference);
    }

    public ItemStack stack(HolderGetter<Item> items) {
        ItemStackTemplate template = template(items);
        return new ItemStack(template.item(), template.count(), template.components());
    }

    public LazyIngredient components(DataComponentPatch components) {
        Objects.requireNonNull(components, "components");
        if (this.templateResolver == null) {
            throw new IllegalStateException("Ingredient has no single item template");
        }
        Function<HolderGetter<Item>, ItemStackTemplate> next = getter -> {
            ItemStackTemplate current = this.templateResolver.apply(getter);
            return new ItemStackTemplate(current.item(), current.count(), components);
        };
        return new LazyIngredient(this.resolver, next, this.label, this.reference);
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

    public Optional<TagKey<Item>> tag() {
        return Optional.ofNullable(tag);
    }
}
