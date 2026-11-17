package com.blackmoss.lazycat.lang;

import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;

@SuppressWarnings("unused")
public abstract class LazyCatLanguageProvider extends LanguageProvider {
    private final String MODID;
    public LazyCatLanguageProvider(PackOutput output, String modid, String locale) {
        super(output, modid, locale);
        MODID = modid;
    }

    protected void addResearch(String researchId, String title, String... stage) {
        add("research.%s.%s.title".formatted(MODID, researchId), title);
        for (int i = 0; i < stage.length; i++) {
            add("research.%s.%s.stage_%d".formatted(MODID, researchId, i), stage[i]);
        }
    }

    protected void addResearchAddenda(String researchId, String... stage) {
        for (int i = 0; i < stage.length; i++) {
            add("research.%s.%s.addendum_%d".formatted(MODID, researchId, i), stage[i]);
        }
    }

    protected void addConfiguration(String configID, String translations) {
        add("%s.configuration.%s".formatted(MODID, configID), translations);
    }
}
