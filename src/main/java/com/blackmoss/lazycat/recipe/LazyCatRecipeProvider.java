package com.blackmoss.lazycat.recipe;

import com.blackmoss.lazycat.api.recipe.RecipeContext;
import com.blackmoss.lazycat.api.recipe.RecipeContributor;
import com.blackmoss.lazycat.api.recipe.RecipeGroupBuilder;
import com.blackmoss.lazycat.api.recipe.minecraft.CookingType;
import com.blackmoss.lazycat.recipe.minecraft.*;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.recipes.CustomCraftingRecipeBuilder;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.ItemLike;

import java.util.Objects;
import java.util.function.Supplier;

@SuppressWarnings("unused")
public abstract class LazyCatRecipeProvider extends RecipeProvider {
    protected final RecipeContext context;

    protected LazyCatRecipeProvider(
            HolderLookup.Provider registries,
            RecipeOutput output,
            String modid) {
        super(registries, output);
        context = RecipeContext.of(registries, output, modid);
    }

    protected final void contribute(RecipeContributor... contributors) {
        Objects.requireNonNull(contributors, "contributors");
        for (RecipeContributor contributor : contributors) {
            Objects.requireNonNull(contributor, "contributors contains null")
                    .addRecipes(context);
        }
    }

    @Override
    protected final void buildRecipes() {
        addRecipes();
    }

    protected abstract void addRecipes();

    protected CookingBuilder smelting(ItemLike result, Object input) {
        return cooking(result, input, CookingType.SMELTING);
    }

    protected CookingBuilder blasting(ItemLike result, Object input) {
        return cooking(result, input, CookingType.BLASTING);
    }

    protected CookingBuilder smoking(ItemLike result, Object input) {
        return cooking(result, input, CookingType.SMOKING);
    }

    protected CookingBuilder campfireCooking(ItemLike result, Object input) {
        return cooking(result, input, CookingType.CAMPFIRE);
    }

    protected CookingBuilder cooking(ItemLike result, Object input) {
        return cooking(result, input, CookingType.SMELTING);
    }

    protected CookingBuilder cooking(ItemLike result, Object input, CookingType type) {
        return new CookingBuilder(
                context,
                Objects.requireNonNull(result, "result"),
                Objects.requireNonNull(input, "input"),
                Objects.requireNonNull(type, "type"));
    }

    protected CookingBuilder ore(ItemLike result, Object input) {
        return cooking(result, input).types(CookingType.SMELTING, CookingType.BLASTING);
    }

    protected CookingBuilder food(ItemLike result, Object input) {
        return cooking(result, input).types(
                CookingType.SMELTING, CookingType.SMOKING, CookingType.CAMPFIRE);
    }

    protected ShapedBuilder shaped(ItemLike result) {
        return new ShapedBuilder(context, Objects.requireNonNull(result, "result"));
    }

    protected ShapelessBuilder shapeless(ItemLike result) {
        return new ShapelessBuilder(context, Objects.requireNonNull(result, "result"));
    }

    protected StonecuttingBuilder stonecutting(ItemLike result, Object input) {
        return new StonecuttingBuilder(
                context,
                Objects.requireNonNull(result, "result"),
                Objects.requireNonNull(input, "input"));
    }

    protected SmithingTransformBuilder smithing(ItemLike result) {
        return new SmithingTransformBuilder(context, Objects.requireNonNull(result, "result"));
    }

    protected SmithingTrimBuilder smithingTrim(Object pattern) {
        return new SmithingTrimBuilder(context, Objects.requireNonNull(pattern, "pattern"));
    }

    protected TransmuteBuilder transmute(ItemLike result) {
        return new TransmuteBuilder(context, Objects.requireNonNull(result, "result"));
    }

    protected CustomCraftingBuilder customCrafting(
            ItemLike result,
            CustomCraftingRecipeBuilder.Factory factory) {
        return new CustomCraftingBuilder(
                context,
                Objects.requireNonNull(result, "result"),
                Objects.requireNonNull(factory, "factory"));
    }

    protected SpecialBuilder special(Supplier<Recipe<?>> factory) {
        return new SpecialBuilder(context, Objects.requireNonNull(factory, "factory"));
    }

    protected SpecialBuilder special(String id, Supplier<Recipe<?>> factory) {
        return special(factory).id(id);
    }

    protected RecipeGroupBuilder compression9(ItemLike material, ItemLike storage) {
        return new RecipeGroupBuilder(
                shapeless(material).count(9).requires(storage).idForm(storage),
                shaped(storage)
                        .pattern(
                                "999",
                                "999",
                                "999")
                        .define('9', material)
                        .idForm(material))
                .category(RecipeCategory.BUILDING_BLOCKS);
    }

    protected RecipeGroupBuilder compression4(ItemLike material, ItemLike storage) {
        return new RecipeGroupBuilder(
                shapeless(material).count(4).requires(storage).idForm(storage),
                shaped(storage)
                        .pattern(
                                "44",
                                "44")
                        .define('4', material)
                        .idForm(material))
                .category(RecipeCategory.BUILDING_BLOCKS);
    }

    protected ShapelessBuilder oneForm(ItemLike one, ItemLike form) {
        return shapeless(one).requires(form).idForm(form);
    }

    protected RecipeGroupBuilder armor(
            ItemLike material,
            ItemLike helmet,
            ItemLike chestplate,
            ItemLike leggings,
            ItemLike boots) {
        return new RecipeGroupBuilder(
                shaped(helmet)
                        .pattern(
                                "MMM",
                                "M M")
                        .define('M', material),
                shaped(chestplate)
                        .pattern(
                                "M M",
                                "MMM",
                                "MMM")
                        .define('M', material),
                shaped(leggings)
                        .pattern(
                                "MMM",
                                "M M",
                                "M M")
                        .define('M', material),
                shaped(boots)
                        .pattern(
                                "M M",
                                "M M")
                        .define('M', material))
                .category(RecipeCategory.COMBAT);
    }

    protected RecipeGroupBuilder tools(
            ItemLike material,
            ItemLike sword,
            ItemLike pickaxe,
            ItemLike axe,
            ItemLike shovel,
            ItemLike hoe) {
        return new RecipeGroupBuilder(
                shaped(sword)
                        .pattern("M",
                                "M",
                                "S")
                        .define('M', material)
                        .define('S', Items.STICK),
                shaped(pickaxe)
                        .pattern("MMM",
                                " S ",
                                " S ")
                        .define('M', material)
                        .define('S', Items.STICK),
                shaped(axe)
                        .pattern("MM",
                                "MS",
                                " S")
                        .define('M', material)
                        .define('S', Items.STICK),
                shaped(shovel)
                        .pattern("M",
                                "S",
                                "S")
                        .define('M', material)
                        .define('S', Items.STICK),
                shaped(hoe)
                        .pattern("MM",
                                " S",
                                " S")
                        .define('M', material)
                        .define('S', Items.STICK))
                .category(RecipeCategory.TOOLS);
    }

    protected ShapedBuilder tree(ItemLike leaves, ItemLike sapling) {
        return shaped(leaves)
                .pattern("LBL",
                        " D ")
                .define('L', sapling)
                .define('B', Items.BONE_MEAL)
                .define('D', Items.DEAD_BUSH);
    }

    protected ShapedBuilder minecart(ItemLike minecartItem, ItemLike material) {
        return shaped(minecartItem)
                .pattern("IMI",
                        "III")
                .define('I', Items.IRON_INGOT)
                .define('M', material)
                .category(RecipeCategory.TRANSPORTATION);
    }
}
