//package com.blackmoss.lazycat.example;
//
//import com.blackmoss.lazycat.LazyCat;
//import com.blackmoss.lazycat.api.LazyIdentifier;
//import com.blackmoss.lazycat.api.datamap.DataMapContext;
//import com.blackmoss.lazycat.datamap.LazyCatDataMapProvider;
//import com.blackmoss.lazycat.datamap.neoforge.CompostableBuilder;
//import com.blackmoss.lazycat.datamap.neoforge.FurnaceFuelBuilder;
//import com.blackmoss.lazycat.datamap.neoforge.StrippableBuilder;
//import com.blackmoss.lazycat.datamap.neoforge.VibrationFrequencyBuilder;
//import com.blackmoss.lazycat.datamap.thaumaturge.BaseAspectBuilder;
//import com.leclowndu93150.thaumaturge.api.aspect.TTAspects;
//import net.minecraft.core.HolderLookup;
//import net.minecraft.data.PackOutput;
//import net.minecraft.tags.TagKey;
//import net.minecraft.world.item.Item;
//import net.minecraft.world.item.Items;
//import net.minecraft.world.level.block.Blocks;
//import net.minecraft.world.level.gameevent.GameEvent;
//
//import java.util.concurrent.CompletableFuture;
//
//@SuppressWarnings("unused")
//public final class ExampleDataMapProvider extends LazyCatDataMapProvider {
//    public static final TagKey<Item> COOKED_MEAT = LazyIdentifier.getItemTag("c", "foods/cooked_meat");
//
//    public ExampleDataMapProvider(
//            PackOutput output,
//            CompletableFuture<HolderLookup.Provider> registries) {
//        super(output, registries, LazyCat.MODID);
//    }
//
//    @Override
//    protected void buildDataMaps() {
//        new BaseAspectBuilder(context, Items.BREAD)
//                .aspect(TTAspects.HERBA, 1)
//                .save();
//
//        new BaseAspectBuilder(context, Items.IRON_SWORD)
//                .aspect(TTAspects.INSTRUMENTUM, 5)
//                .aspect(TTAspects.AVERSIO, 1)
//                .save();
//
//        new BaseAspectBuilder(context, COOKED_MEAT)
//                .aspect(TTAspects.IGNIS, 1)
//                .aspect(TTAspects.VICTUS, 1)
//                .save();
//
//        new FurnaceFuelBuilder(context, Items.ANVIL)
//                .burnTime(40)
//                .save();
//
//        new CompostableBuilder(context, Items.ACACIA_LOG)
//                .chance(0.5f)
//                .save();
//
//        new StrippableBuilder(context, Blocks.OAK_WOOD)
//                .stripped(Blocks.STRIPPED_OAK_WOOD)
//                .save();
//
//        new VibrationFrequencyBuilder(context, GameEvent.BLOCK_ACTIVATE)
//                .frequency(2)
//                .save();
//    }
//}
