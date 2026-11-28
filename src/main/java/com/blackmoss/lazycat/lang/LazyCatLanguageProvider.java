package com.blackmoss.lazycat.lang;

import com.leclowndu93150.thaumaturge.api.aspect.IAspect;
import com.leclowndu93150.thaumaturge.api.research.IResearchCategory;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.neoforge.common.data.LanguageProvider;
import org.jspecify.annotations.NonNull;

import java.util.Objects;

@SuppressWarnings("unused")
public abstract class LazyCatLanguageProvider extends LanguageProvider {
    private final String modid;
    private final String locale;

    public LazyCatLanguageProvider(PackOutput output, String modid, String locale) {
        super(output, Objects.requireNonNull(modid, "modid"), Objects.requireNonNull(locale, "locale"));
        if (modid.isBlank()) {
            throw new IllegalArgumentException("modid must not be blank");
        }
        this.modid = modid;
        this.locale = locale;
    }

    @Override
    public @NonNull String getName() {
        return "%s %s Language by Lazy Cat".formatted(modid, locale);
    }

    protected void addCreativeModeTab(CreativeModeTab tab, String translations) {
        Objects.requireNonNull(tab, "tab");
        Objects.requireNonNull(translations, "translations");
        Identifier key = BuiltInRegistries.CREATIVE_MODE_TAB.getKey(tab);
        if (key != null) {
            add("item_group." + key.toLanguageKey(), translations);
        }
    }

    protected void addTooltip(String tooltip, String translations) {
        Objects.requireNonNull(tooltip, "tooltip");
        add("tooltip.%s.%s".formatted(modid, tooltip), Objects.requireNonNull(translations, "translations"));
    }

    protected void addResearch(String researchId, String title, String... stage) {
        Objects.requireNonNull(researchId, "researchId");
        Objects.requireNonNull(stage, "stage");
        if (title != null) {
            add("research.%s.%s.title".formatted(modid, researchId), title);
        }
        for (int i = 0; i < stage.length; i++) {
            add("research.%s.%s.stage_%d".formatted(modid, researchId, i),
                    Objects.requireNonNull(stage[i], "stage contains null"));
        }
    }

    protected void addResearchCategory(String researchId, String translations) {
        Objects.requireNonNull(researchId, "researchId");
        add("research_category.%s.%s".formatted(modid, researchId),
                Objects.requireNonNull(translations, "translations"));
    }

    protected void addResearchCategory(ResourceKey<IResearchCategory> category, String translations) {
        Objects.requireNonNull(category, "category");
        Objects.requireNonNull(translations, "translations");
        Identifier id = category.identifier();
        add("research_category.%s.%s".formatted(id.getNamespace(), id.getPath()), translations);
    }

    protected void addResearchMeme(String researchId, String translations) {
        Objects.requireNonNull(researchId, "researchId");
        add("research.%s.%s.meme".formatted(modid, researchId),
                Objects.requireNonNull(translations, "translations"));
    }

    protected void addResearchAddenda(String researchId, String... stage) {
        Objects.requireNonNull(researchId, "researchId");
        Objects.requireNonNull(stage, "stage");
        for (int i = 0; i < stage.length; i++) {
            add("research.%s.%s.addendum_%d".formatted(modid, researchId, i),
                    Objects.requireNonNull(stage[i], "stage contains null"));
        }
    }

    protected void addConfiguration(String configID, String translations) {
        Objects.requireNonNull(configID, "configID");
        add("%s.configuration.%s".formatted(modid, configID),
                Objects.requireNonNull(translations, "translations"));
    }

    protected void addContainer(String containerId, String translations) {
        Objects.requireNonNull(containerId, "containerId");
        add("container.%s.%s".formatted(modid, containerId),
                Objects.requireNonNull(translations, "translations"));
    }

    protected void addRecipeType(ResourceKey<RecipeType<?>> type, String translations) {
        Objects.requireNonNull(type, "type");
        add("recipe.type.%s.%s".formatted(modid, type.identifier().getPath()),
                Objects.requireNonNull(translations, "translations"));
    }

    protected void addEnchantment(String enchantmentId, String translations) {
        Objects.requireNonNull(enchantmentId, "enchantmentId");
        add("enchantment.%s.%s".formatted(modid, enchantmentId),
                Objects.requireNonNull(translations, "translations"));
    }

    protected void addAspect(ResourceKey<IAspect> aspect, String name, String desc, String help) {
        Objects.requireNonNull(aspect, "aspect");
        var id = aspect.identifier().getPath();
        add("aspect.%s.%s".formatted(modid, id), Objects.requireNonNull(name, "name"));
        add("aspect.%s.%s.desc".formatted(modid, id), Objects.requireNonNull(desc, "desc"));
        add("aspect.%s.%s.help".formatted(modid, id), Objects.requireNonNull(help, "help"));
    }

    protected void addWandCap(String wandCapId, String translations) {
        Objects.requireNonNull(wandCapId, "wandCapId");
        add("wand.thaumaturge.cap.%s".formatted(wandCapId),
                Objects.requireNonNull(translations, "translations"));
    }

    protected void addWandRod(String wandRodId, String translations) {
        Objects.requireNonNull(wandRodId, "wandRodId");
        add("wand.thaumaturge.rod.%s".formatted(wandRodId),
                Objects.requireNonNull(translations, "translations"));
    }
}
