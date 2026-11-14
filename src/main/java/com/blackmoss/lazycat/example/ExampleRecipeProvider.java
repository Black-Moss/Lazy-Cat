//package com.blackmoss.lazycat.example;
//
//import com.blackmoss.lazycat.LazyCat;
//import com.blackmoss.lazycat.api.recipe.LazyCatRecipeProvider;
//import com.blackmoss.lazycat.recipe.thaumaturge.ArcaneShapelessBuilder;
//import com.leclowndu93150.thaumaturge.api.aspect.TTAspects;
//import net.minecraft.core.HolderLookup;
//import net.minecraft.data.PackOutput;
//import net.minecraft.data.recipes.RecipeOutput;
//import net.minecraft.data.recipes.RecipeProvider;
//import net.minecraft.world.item.Items;
//import org.jspecify.annotations.NonNull;
//
//import java.util.concurrent.CompletableFuture;
//
//public final class ExampleRecipeProvider extends LazyCatRecipeProvider {
//    private ExampleRecipeProvider(HolderLookup.Provider registries, RecipeOutput output, String namespace) {
//        super(registries, output, namespace);
//    }
//
//    @Override
//    protected void addRecipes() {
//        new ArcaneShapelessBuilder(context, Items.DIAMOND, 10)
//                .requires(Items.DIRT)
//                .save();
//    }
//
//    public static final class Runner extends RecipeProvider.Runner {
//        public Runner(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
//            super(output, lookupProvider);
//        }
//
//        @Override
//        protected @NonNull RecipeProvider createRecipeProvider(HolderLookup.@NonNull Provider provider, @NonNull RecipeOutput output) {
//            return new ExampleRecipeProvider(provider, output, LazyCat.MODID);
//        }
//
//        @Override
//        public @NonNull String getName() {
//            return "Lazy Cat Example Recipes";
//        }
//    }
//}
