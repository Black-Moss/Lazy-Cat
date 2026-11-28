package com.blackmoss.lazycat.recipe;

import com.blackmoss.lazycat.api.recipe.RecipeContext;
import com.blackmoss.lazycat.api.recipe.RecipeGroupBuilder;
import com.blackmoss.lazycat.api.recipe.minecraft.CookingType;
import com.blackmoss.lazycat.recipe.minecraft.*;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import org.jspecify.annotations.NonNull;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;

@SuppressWarnings("unused")
public abstract class LazyCatRecipeProvider extends RecipeProvider {
    protected final String modid;
    protected RecipeContext context;

    protected LazyCatRecipeProvider(
            PackOutput output,
            CompletableFuture<HolderLookup.Provider> registries,
            String modid) {
        super(output, registries);
        Objects.requireNonNull(modid, "modid");
        if (modid.isBlank()) {
            throw new IllegalArgumentException("modid must not be blank");
        }
        this.modid = modid;
    }

    @Override
    protected final void buildRecipes(@NonNull RecipeOutput output, HolderLookup.@NonNull Provider registries) {
        this.context = RecipeContext.of(registries, output, modid);
        addRecipes();
    }

    protected abstract void addRecipes();

    protected CookingBuilder smelting(Object result, Object input) {
        return this.cooking(result, input, CookingType.SMELTING);
    }

    protected CookingBuilder blasting(Object result, Object input) {
        return this.cooking(result, input, CookingType.BLASTING);
    }

    protected CookingBuilder smoking(Object result, Object input) {
        return this.cooking(result, input, CookingType.SMOKING);
    }

    protected CookingBuilder campfireCooking(Object result, Object input) {
        return this.cooking(result, input, CookingType.CAMPFIRE);
    }

    protected CookingBuilder cooking(Object result, Object input) {
        return this.cooking(result, input, CookingType.SMELTING);
    }

    protected CookingBuilder cooking(Object result, Object input, CookingType type) {
        return new CookingBuilder(this.context, Objects.requireNonNull(result, "result"), Objects.requireNonNull(input, "input"), Objects.requireNonNull(type, "type"));
    }

    protected CookingBuilder ore(Object result, Object input) {
        return this.cooking(result, input).types(CookingType.SMELTING, CookingType.BLASTING);
    }

    protected CookingBuilder food(Object result, Object input) {
        return this.cooking(result, input).types(CookingType.SMELTING, CookingType.SMOKING, CookingType.CAMPFIRE);
    }

    protected ShapedBuilder shaped(Object result) {
        return new ShapedBuilder(this.context, Objects.requireNonNull(result, "result"));
    }

    protected ShapelessBuilder shapeless(Object result) {
        return new ShapelessBuilder(this.context, Objects.requireNonNull(result, "result"));
    }

    protected StonecuttingBuilder stonecutting(Object result, Object input) {
        return new StonecuttingBuilder(this.context, Objects.requireNonNull(result, "result"), Objects.requireNonNull(input, "input"));
    }

    protected SmithingTransformBuilder smithing(Object result) {
        return new SmithingTransformBuilder(this.context, Objects.requireNonNull(result, "result"));
    }

    protected SmithingTrimBuilder smithingTrim(Object pattern) {
        return new SmithingTrimBuilder(this.context, Objects.requireNonNull(pattern, "pattern"));
    }

    protected RecipeGroupBuilder compression9(ItemLike material, ItemLike storage) {
        return new RecipeGroupBuilder(this.shapeless(material)
                .count(9).requires(storage).idForm(storage),
                this.shaped(storage)
                        .pattern("999", "999", "999")
                        .define('9', material)
                        .idForm(material))
                .category(RecipeCategory.BUILDING_BLOCKS);
    }

    protected RecipeGroupBuilder compression4(ItemLike material, ItemLike storage) {
        return new RecipeGroupBuilder(this.shapeless(material)
                .count(4).requires(storage).idForm(storage),
                this.shaped(storage)
                        .pattern("44", "44")
                        .define('4', material)
                        .idForm(material))
                .category(RecipeCategory.BUILDING_BLOCKS);
    }

    protected ShapelessBuilder oneForm(ItemLike one, ItemLike form) {
        return this.shapeless(one).requires(form).idForm(form);
    }

    protected RecipeGroupBuilder armor(ItemLike material, ItemLike helmet, ItemLike chestplate, ItemLike leggings, ItemLike boots) {
        return (new RecipeGroupBuilder(this.shaped(helmet).pattern("MMM", "M M").define('M', material), this.shaped(chestplate).pattern("M M", "MMM", "MMM").define('M', material), this.shaped(leggings).pattern("MMM", "M M", "M M").define('M', material), this.shaped(boots).pattern("M M", "M M").define('M', material))).category(RecipeCategory.COMBAT);
    }

    protected RecipeGroupBuilder tools(ItemLike material, ItemLike sword, ItemLike pickaxe, ItemLike axe, ItemLike shovel, ItemLike hoe) {
        return (new RecipeGroupBuilder(this.shaped(sword).pattern("M", "M", "S").define('M', material).define('S', Items.STICK), this.shaped(pickaxe).pattern("MMM", " S ", " S ").define('M', material).define('S', Items.STICK), this.shaped(axe).pattern("MM", "MS", " S").define('M', material).define('S', Items.STICK), this.shaped(shovel).pattern("M", "S", "S").define('M', material).define('S', Items.STICK), this.shaped(hoe).pattern("MM", " S", " S").define('M', material).define('S', Items.STICK))).category(RecipeCategory.TOOLS);
    }

    protected ShapedBuilder tree(ItemLike leaves, ItemLike sapling) {
        return this.shaped(leaves).pattern("LBL", " D ").define('L', sapling).define('B', Items.BONE_MEAL).define('D', Items.DEAD_BUSH);
    }

    protected ShapedBuilder minecart(ItemLike minecartItem, ItemLike material) {
        return this.shaped(minecartItem).pattern("IMI", "III").define('I', Items.IRON_INGOT).define('M', material).category(RecipeCategory.TRANSPORTATION);
    }
}
