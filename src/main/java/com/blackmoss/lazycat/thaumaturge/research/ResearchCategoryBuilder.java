package com.blackmoss.lazycat.thaumaturge.research;

import com.blackmoss.lazycat.api.condition.ConditionalBuilder;
import com.blackmoss.lazycat.api.thaumaturge.research.ResearchContext;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.conditions.ICondition;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@SuppressWarnings("unused")
public final class ResearchCategoryBuilder implements ConditionalBuilder<ResearchCategoryBuilder> {
    private final ResearchContext context;
    private final ResourceLocation id;
    private final List<JsonObject> formula = new ArrayList<>();
    private final List<ICondition> conditions = new ArrayList<>();
    private String requiredResearch;
    private String icon;
    private String background;
    private String overlayBackground;
    private int index;

    public ResearchCategoryBuilder(ResearchContext context, ResourceLocation id) {
        this.context = context;
        this.id = id;
    }

    public ResearchCategoryBuilder requiredResearch(Object research) {
        this.requiredResearch = this.context.resolveText(research);
        return this;
    }

    public ResearchCategoryBuilder formula(Object aspect, int amount) {
        if (amount < 1) {
            throw new IllegalArgumentException("Aspect amount must be at least 1: " + amount);
        }
        JsonObject entry = new JsonObject();
        entry.addProperty("aspect", this.context.resolveText(aspect));
        entry.addProperty("amount", amount);
        this.formula.add(entry);
        return this;
    }

    public ResearchCategoryBuilder icon(Object icon) {
        this.icon = ResearchContext.iconRef(icon);
        return this;
    }

    public ResearchCategoryBuilder background(Object background) {
        this.background = ResearchContext.iconRef(background);
        return this;
    }

    public ResearchCategoryBuilder overlayBackground(Object overlayBackground) {
        this.overlayBackground = ResearchContext.iconRef(overlayBackground);
        return this;
    }

    public ResearchCategoryBuilder index(int index) {
        if (index < 0) {
            throw new IllegalArgumentException("Category index must not be negative: " + index);
        }
        this.index = index;
        return this;
    }

    @Override
    public List<ICondition> conditionList() {
        return this.conditions;
    }

    public ResourceLocation id() {
        return this.id;
    }

    public void save() {
        Objects.requireNonNull(this.icon, "category icon");
        Objects.requireNonNull(this.background, "category background");
        JsonObject json = new JsonObject();
        json.addProperty("icon", this.icon);
        json.addProperty("background", this.background);
        JsonArray aspects = new JsonArray();
        this.formula.forEach(aspects::add);
        json.add("formula", aspects);
        if (this.requiredResearch != null) {
            json.addProperty("required_research", this.requiredResearch);
        }
        if (this.overlayBackground != null) {
            json.addProperty("overlay_background", this.overlayBackground);
        }
        if (this.index != 0) {
            json.addProperty("index", this.index);
        }
        writeConditions(json);
        this.context.submitCategory(this.id, json);
    }
}
