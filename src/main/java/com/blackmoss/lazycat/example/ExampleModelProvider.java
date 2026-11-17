//package com.blackmoss.lazycat.example;
//
//import com.blackmoss.lazycat.LazyCat;
//import com.blackmoss.lazycat.api.LazyIdentifier;
//import com.blackmoss.lazycat.api.model.BlockModelBuilder;
//import com.blackmoss.lazycat.api.model.ItemModelBuilder;
//import com.blackmoss.lazycat.api.model.ModelContext;
//import com.blackmoss.lazycat.model.LazyCatModelProvider;
//import com.blackmoss.lazycat.model.avaritia.AvaritiaModelBuilder;
//import committee.nova.mods.avaritia.Const;
//import net.minecraft.client.data.models.BlockModelGenerators;
//import net.minecraft.client.data.models.ItemModelGenerators;
//import net.minecraft.client.data.models.MultiVariant;
//import net.minecraft.client.renderer.block.dispatch.Variant;
//import net.minecraft.core.Holder;
//import net.minecraft.data.PackOutput;
//import net.minecraft.util.random.WeightedList;
//import net.minecraft.world.item.Item;
//import net.minecraft.world.item.Items;
//import net.minecraft.world.level.block.Block;
//import net.minecraft.world.level.block.Blocks;
//import org.jspecify.annotations.NonNull;
//
//import java.util.stream.Stream;
//
//@SuppressWarnings("deprecation")
//public final class ExampleModelProvider extends LazyCatModelProvider {
//    private ExampleModelProvider(PackOutput output) {
//        super(output, LazyCat.MODID);
//    }
//
//    @Override
//    protected @NonNull Stream<? extends Holder<Item>> getKnownItems() {
//        return Stream.of(
//                Items.IRON_INGOT.builtInRegistryHolder(),
//                Items.GOLD_INGOT.builtInRegistryHolder(),
//                Items.DIAMOND.builtInRegistryHolder(),
//                Items.EMERALD.builtInRegistryHolder(),
//                Items.BLAZE_ROD.builtInRegistryHolder(),
//                Items.NETHERITE_INGOT.builtInRegistryHolder(),
//                Items.AMETHYST_SHARD.builtInRegistryHolder(),
//                Items.ENDER_PEARL.builtInRegistryHolder(),
//                Items.END_ROD.builtInRegistryHolder(),
//                Items.STONE.builtInRegistryHolder(),
//                Items.FURNACE.builtInRegistryHolder(),
//                Items.GLASS.builtInRegistryHolder(),
//                Items.DIRT.builtInRegistryHolder(),
//                Items.COBBLESTONE.builtInRegistryHolder());
//    }
//
//    @Override
//    protected @NonNull Stream<? extends Holder<Block>> getKnownBlocks() {
//        return Stream.of(
//                Blocks.STONE.builtInRegistryHolder(),
//                Blocks.FURNACE.builtInRegistryHolder(),
//                Blocks.GLASS.builtInRegistryHolder(),
//                Blocks.DIRT.builtInRegistryHolder(),
//                Blocks.COBBLESTONE.builtInRegistryHolder());
//    }
//
//    @Override
//    protected void registerModels(@NonNull BlockModelGenerators blockModels, @NonNull ItemModelGenerators itemModels) {
//        ModelContext context = new ModelContext(itemModels, blockModels);
//
//        new ItemModelBuilder(context, Items.IRON_INGOT).save();
//
//        new ItemModelBuilder(context, Items.GOLD_INGOT)
//                .model(LazyIdentifier.lazyCat("item/my_gold_ingot"))
//                .save();
//
//        new BlockModelBuilder(context, Blocks.STONE).cubeAll().save();
//        new BlockModelBuilder(context, Blocks.FURNACE).facing().save();
//        new BlockModelBuilder(context, Blocks.GLASS).onlyItem().save();
//        new BlockModelBuilder(context, Blocks.DIRT).onlyState().save();
//
//        MultiVariant variant = new MultiVariant(WeightedList.of(
//                new Variant(LazyIdentifier.lazyCat("block/my_cobblestone"))));
//        new BlockModelBuilder(context, Blocks.COBBLESTONE).custom(variant).save();
//
//        AvaritiaModelBuilder avaritia = new AvaritiaModelBuilder(context);
//
//        avaritia.cosmic(Items.DIAMOND)
//                .mask(Const.rl("misc/cosmic_mask"))
//                .save();
//        avaritia.cosmicArc(Items.EMERALD).save();
//        avaritia.hell(Items.BLAZE_ROD).save();
//        avaritia.eternal(Items.NETHERITE_INGOT).save();
//        avaritia.unstable(Items.AMETHYST_SHARD).save();
//
//        avaritia.halo(Items.GOLD_INGOT)
//                .color(0xFF3366)
//                .size(8)
//                .pulse(true)
//                .save();
//
//        avaritia.haloCosmic(Items.ENDER_PEARL)
//                .mask(Const.rl("misc/cosmic_mask"))
//                .color(0x3366FF)
//                .size(10)
//                .pulse(true)
//                .save();
//
//        avaritia.haloEternal(Items.END_ROD).save();
//
//        // Do NOT call super.registerModels(...) if you already generated every known block here:
//        // it would generate the default models again and fail with "Duplicate model definition".
//    }
//
//    // Register it from a GatherDataEvent.Client listener:
//    //
//    // @SubscribeEvent
//    // public static void onGatherData(GatherDataEvent.Client event) {
//    //     event.createProvider(ExampleModelProvider::new);
//    // }
//}
