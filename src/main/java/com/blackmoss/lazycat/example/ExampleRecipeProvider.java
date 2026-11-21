//package com.blackmoss.lazycat.example;
//
//import com.blackmoss.lazycat.LazyCat;
//import com.blackmoss.lazycat.LazyCatData;
//import com.blackmoss.lazycat.api.LazyIngredient;
//import com.blackmoss.lazycat.api.recipe.RecipeContext;
//import com.blackmoss.lazycat.api.recipe.minecraft.CookingType;
//import com.blackmoss.lazycat.recipe.LazyCatRecipeProvider;
//import com.blackmoss.lazycat.recipe.minecraft.CookingBuilder;
//import com.blackmoss.lazycat.recipe.minecraft.ShapedBuilder;
//import com.blackmoss.lazycat.recipe.minecraft.ShapelessBuilder;
//import com.blackmoss.lazycat.recipe.minecraft.SmithingTransformBuilder;
//import com.blackmoss.lazycat.recipe.minecraft.StonecuttingBuilder;
//import net.minecraft.core.HolderLookup;
//import net.minecraft.core.component.DataComponentPatch;
//import net.minecraft.data.PackOutput;
//import net.minecraft.core.component.DataComponents;
//import net.minecraft.network.chat.Component;
//import net.minecraft.resources.ResourceLocation;
//import net.minecraft.world.item.Items;
//
//import java.util.concurrent.CompletableFuture;
//
//public final class ExampleRecipeProvider extends LazyCatRecipeProvider {
//    public ExampleRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
//        super(output, registries, LazyCat.MODID);
//    }
//
//    @Override
//    protected void addRecipes() {
//        new ShapedBuilder(context, Items.DIAMOND)
//                .pattern("DDD", "D D", "DDD")
//                .define('D', Items.DIRT)
//                .id("diamond_from_dirt")
//                .save();
//
//        new ShapelessBuilder(context, Items.EMERALD)
//                .requires("#minecraft:logs")
//                .requires(Items.STICK, 2)
//                .save();
//
//        new ShapelessBuilder(context, LazyIngredient.of(Items.DIAMOND)
//                .count(7)
//                .components(DataComponentPatch.builder()
//                        .set(DataComponents.CUSTOM_NAME, Component.literal("Lazy Cat"))
//                        .build()))
//                .requires(Items.DIRT)
//                .id("component_result")
//                .save();
//
//        new CookingBuilder(context, Items.IRON_INGOT, Items.RAW_IRON, CookingType.SMELTING)
//                .exp(0.7f)
//                .time(200)
//                .types(CookingType.SMELTING, CookingType.BLASTING)
//                .save();
//
//        new StonecuttingBuilder(context, Items.STONE_SLAB, Items.STONE)
//                .and(Items.COBBLESTONE)
//                .save();
//
//        new SmithingTransformBuilder(context, Items.NETHERITE_PICKAXE)
//                .template(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE)
//                .base(Items.DIAMOND_PICKAXE)
//                .addition(Items.NETHERITE_INGOT)
//                .save();
//
//        new ShapedBuilder(context, Items.GOLD_INGOT)
//                .pattern("GGG")
//                .define('G', "#c:ingots")
//                .ifModLoaded("minecraft")
//                .unlockedBy("has_gold", context.has(Items.GOLD_INGOT))
//                .save(ResourceLocation.fromNamespaceAndPath(LazyCat.MODID, "gold_ingot_custom"));
//    }
//
//    public static void register(net.neoforged.neoforge.data.event.GatherDataEvent event) {
//        LazyCatData.recipes(event, LazyCat.MODID, ExampleRecipeProvider::new);
//    }
//}
