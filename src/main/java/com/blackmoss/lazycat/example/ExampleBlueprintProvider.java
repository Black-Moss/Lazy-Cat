//package com.blackmoss.lazycat.example;
//
//import com.blackmoss.lazycat.LazyCat;
//import com.blackmoss.lazycat.api.thaumaturge.blueprint.BlueprintContext;
//import com.blackmoss.lazycat.api.thaumaturge.blueprint.Blueprints;
//import com.blackmoss.lazycat.thaumaturge.blueprint.LazyCatBlueprintProvider;
//import net.minecraft.data.PackOutput;
//import net.minecraft.world.level.block.Blocks;
//
//public final class ExampleBlueprintProvider extends LazyCatBlueprintProvider {
//    public ExampleBlueprintProvider(PackOutput output) {
//        super(output, LazyCat.MODID);
//    }
//
//    @Override
//    protected void addBlueprints() {
//        context.blueprint("example/stone_floor")
//                .key('S', Blocks.STONE_BRICKS)
//                .row("SSS")
//                .row("SSS")
//                .row("SSS")
//                .save();
//
//        context.blueprint("example/altar")
//                .key('S', Blocks.STONE_BRICKS, Blocks.POLISHED_ANDESITE, 1)
//                .key('A', Blocks.AMETHYST_BLOCK)
//                .layer()
//                .row("SSS")
//                .row("SAS")
//                .row("SSS")
//                .layer()
//                .row("A A")
//                .row("A A")
//                .row("A A")
//                .ifModLoaded("thaumaturge")
//                .save();
//
//        context.blueprint("example/tag_wall")
//                .key('#', net.minecraft.tags.BlockTags.STONE_BRICKS, Blueprints.targetAir())
//                .layer("###")
//                .save();
//    }
//}
