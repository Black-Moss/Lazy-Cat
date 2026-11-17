//package com.blackmoss.lazycat.example;
//
//import com.blackmoss.lazycat.LazyCat;
//import com.blackmoss.lazycat.api.thaumaturge.researrch.ResearchContext;
//import com.blackmoss.lazycat.compat.thaumaturge.research.ThaumaturgeResearchProvider;
//import com.leclowndu93150.thaumaturge.TTIds;
//import com.leclowndu93150.thaumaturge.api.aspect.TTAspects;
//import com.leclowndu93150.thaumaturge.api.capability.KnowledgeType;
//import com.leclowndu93150.thaumaturge.api.research.ResearchEntryMeta;
//import com.leclowndu93150.thaumaturge.api.research.ResearchIcon;
//import net.minecraft.data.PackOutput;
//import net.minecraft.resources.Identifier;
//import net.minecraft.tags.ItemTags;
//import net.minecraft.world.item.Items;
//import org.jspecify.annotations.NonNull;
//
//public final class ExampleResearchProvider extends ThaumaturgeResearchProvider {
//    private ExampleResearchProvider(PackOutput output) {
//        super(output, LazyCat.MODID);
//    }
//
//    @Override
//    protected void addResearch(ResearchContext context) {
//        context.category(LazyCat.lazyCat("example_category"))   // lazycat:example_category
//                .icon(ResearchIcon.ofTexture(TTIds.rl("textures/research/r_basics.png")))
//                .background(TTIds.rl("textures/research/back_basics.png"))
//                .overlayBackground(TTIds.rl("textures/research/back_basics_over.png"))
//                .formula(TTAspects.AQUA, 5)
//                .formula("thaumaturge:machina", 3)
//                .requiredResearch("thaumaturge:research_basics")
//                .index(4)
//                .save();
//
//        context.entry("thaumaturge:artifice", "research.lazycat.example.title")
//                .id("example_entry")
//                .position(-2, 3)
//                .parent("thaumaturge:research_basics")
//                .parent("thaumaturge:basic_aura", 2)
//                .inheritParent("thaumaturge:unbound_letters")
//                .sibling("thaumaturge:advanced_turret")
//                .meta(
//                        ResearchEntryMeta.ROUND,
//                        ResearchEntryMeta.HEX,
//                        ResearchEntryMeta.HIDDEN)
//                .icon(Items.DIAMOND)
//                .icon(ResearchIcon.ofTexture(LazyCat.lazyCat("textures/research/example.png")))
//                .icon(ResearchIcon.ofFocus(Identifier.withDefaultNamespace("touch")))
//                .noteAspect("thaumaturge:sensus", 3)
//                .complexity(3)
//
//                // text key defaults to <name>.stage_N: research.lazycat.example.title.stage_1
//                .stage()
//                .recipe("minecraft:crafting_table")
//                .recipes("minecraft:stick", "minecraft:torch")
//                .knowledge(KnowledgeType.OBSERVATION, "thaumaturge:artifice", 1)
//                .requiredKnowledge(KnowledgeType.THEORY, "thaumaturge:basics", 2)
//                .requiredResearch("thaumaturge:research_basics")
//                .warp(1)
//                .end()
//
//                .stage()
//                .text("research.lazycat.example.custom_stage")   // only needed for a different key
//                .obtain(Items.DIAMOND, 2)
//                .obtain(ItemTags.LOGS, 1)
//                .craft("minecraft:gold_ingot", 3)
//                .end()
//
//                .stage()
//                .construct(2, 1, 1,
//                        "minecraft:stone", "minecraft:stone")
//                .constructCost("thaumaturge:terra", 4)
//                .end()
//
//                // several addenda default to <name>.addendum_1 / _2 (a lone one uses <name>.addendum)
//                .addendum()
//                .recipe("minecraft:stick")
//                .recipes("minecraft:torch", "minecraft:crafting_table")
//                .requiredResearch("thaumaturge:research_basics")
//                .end()
//
//                .addendum()
//                .text("research.lazycat.example.extra_addendum")   // explicit override
//                .recipe("minecraft:crafting_table")
//                .end()
//                .save();
//    }
//
//    @Override
//    public @NonNull String getName() {
//        return "Lazy Cat Example Research";
//    }
//
//    // @SubscribeEvent
//    // public static void onGatherData(GatherDataEvent.Client event) {
//    //     event.createProvider(ExampleResearchProvider::new);
//    // }
//}
