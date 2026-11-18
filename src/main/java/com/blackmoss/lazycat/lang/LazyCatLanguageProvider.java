package com.blackmoss.lazycat.lang;

import com.leclowndu93150.thaumaturge.api.aspect.IAspect;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.neoforge.common.data.LanguageProvider;
import org.jspecify.annotations.NonNull;

@SuppressWarnings("unused")
public abstract class LazyCatLanguageProvider extends LanguageProvider {
    private final String modid;
    private final String locale;

    public LazyCatLanguageProvider(PackOutput output, String modid, String locale) {
        super(output, modid, locale);
        this.modid = modid;
        this.locale = locale;
    }

    @Override
    public @NonNull String getName() {
        return "%s %s Language by Lazy Cat".formatted(modid, locale);
    }

    protected void addCreativeModeTab(CreativeModeTab tab, String translations) {
        Identifier key = BuiltInRegistries.CREATIVE_MODE_TAB.getKey(tab);
        if (key != null) {
            add("itemGroup." + key.toLanguageKey(), translations);
        }
    }

    protected void addTooltip(String tooltip, String translations) {
        add("tooltip.%s.%s".formatted(modid, tooltip), translations);
    }

    protected void addResearch(String researchId, String title, String... stage) {
        add("research.%s.%s.title".formatted(modid, researchId), title);
        for (int i = 0; i < stage.length; i++) {
            add("research.%s.%s.stage_%d".formatted(modid, researchId, i), stage[i]);
        }
    }

    protected void addResearchCategory(String researchId, String translations) {
        add("research_category.%s.%s".formatted(modid, researchId), translations);
    }

    protected void addResearchAddenda(String researchId, String... stage) {
        for (int i = 0; i < stage.length; i++) {
            add("research.%s.%s.addendum_%d".formatted(modid, researchId, i), stage[i]);
        }
    }

    protected void addConfiguration(String configID, String translations) {
        add("%s.configuration.%s".formatted(modid, configID), translations);
    }

    protected void addContainer(String containerId, String translations) {
        add("container.%s.%s".formatted(modid, containerId), translations);
    }

    protected void addRecipeType(ResourceKey<RecipeType<?>> type, String translations) {
        add("recipe.type.%s.%s".formatted(modid, type.identifier().getPath()), translations);
    }

    protected void addEnchantment(String enchantmentId, String translations) {
        add("enchantment.%s.%s".formatted(modid, enchantmentId), translations);
    }

    protected void addAspect(ResourceKey<IAspect> aspect, String name, String desc, String help) {
        var id = aspect.identifier().getPath();
        add("aspect.%s.%s".formatted(modid, id), name);
        add("aspect.%s.%s.desc".formatted(modid, id), desc);
        add("aspect.%s.%s.help".formatted(modid, id), help);
    }

    protected void addWandCap(String wandCapId, String translations) {
        add("wand.thaumaturge.cap.%s".formatted(wandCapId), translations);
    }

    protected void addWandRod(String wandRodId, String translations) {
        add("wand.thaumaturge.rod.%s".formatted(wandRodId), translations);
    }
}
