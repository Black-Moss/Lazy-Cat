package com.blackmoss.lazycat.thaumaturge.research;

import com.blackmoss.lazycat.api.thaumaturge.research.ResearchContext;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.leclowndu93150.thaumaturge.api.capability.KnowledgeType;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@SuppressWarnings("unused")
public final class ResearchStageBuilder {
    private final ResearchContext context;
    private final ResearchEntryBuilder owner;
    private final List<String> recipes = new ArrayList<>();
    private final List<JsonObject> obtain = new ArrayList<>();
    private final List<JsonObject> craft = new ArrayList<>();
    private final List<JsonObject> knowledge = new ArrayList<>();
    private final List<JsonObject> requiredKnowledge = new ArrayList<>();
    private final List<String> requiredResearch = new ArrayList<>();
    private final List<JsonObject> constructCost = new ArrayList<>();
    private String text;
    private int xSize;
    private int ySize;
    private int zSize;
    private List<String> cells = List.of();
    private boolean construct;
    private int warp;

    public ResearchStageBuilder(ResearchContext context, ResearchEntryBuilder owner, String defaultText) {
        this.context = context;
        this.owner = owner;
        this.text = Objects.requireNonNull(defaultText, "defaultText");
    }

    private static void addIfPresent(JsonObject json, String name, List<JsonObject> entries) {
        if (!entries.isEmpty()) {
            json.add(name, objects(entries));
        }
    }

    private static JsonArray strings(List<String> values) {
        JsonArray array = new JsonArray();
        values.forEach(array::add);
        return array;
    }

    private static JsonArray objects(List<JsonObject> values) {
        JsonArray array = new JsonArray();
        values.forEach(array::add);
        return array;
    }

    public ResearchStageBuilder text(String text) {
        this.text = Objects.requireNonNull(text, "text");
        return this;
    }

    public ResearchStageBuilder recipe(Object recipe) {
        this.recipes.add(this.context.resolveText(recipe));
        return this;
    }

    public ResearchStageBuilder recipes(Object... recipes) {
        Objects.requireNonNull(recipes, "recipes");
        for (Object recipe : recipes) {
            recipe(recipe);
        }
        return this;
    }

    public ResearchStageBuilder obtain(Object items) {
        this.obtain.add(requirement(items, 1));
        return this;
    }

    public ResearchStageBuilder obtain(Object items, int amount) {
        this.obtain.add(requirement(items, amount));
        return this;
    }

    public ResearchStageBuilder craft(Object items) {
        this.craft.add(requirement(items, 1));
        return this;
    }

    public ResearchStageBuilder craft(Object items, int amount) {
        this.craft.add(requirement(items, amount));
        return this;
    }

    public ResearchStageBuilder obtain(Object items, int amount, JsonObject components) {
        this.obtain.add(requirement(items, amount, components));
        return this;
    }

    public ResearchStageBuilder craft(Object items, int amount, JsonObject components) {
        this.craft.add(requirement(items, amount, components));
        return this;
    }

    public ResearchStageBuilder knowledge(KnowledgeType type, Object category) {
        this.knowledge.add(reward(type, category, 1));
        return this;
    }

    public ResearchStageBuilder knowledge(KnowledgeType type, Object category, int amount) {
        this.knowledge.add(reward(type, category, amount));
        return this;
    }

    public ResearchStageBuilder requiredKnowledge(KnowledgeType type, Object category) {
        this.requiredKnowledge.add(reward(type, category, 1));
        return this;
    }

    public ResearchStageBuilder requiredKnowledge(KnowledgeType type, Object category, int amount) {
        this.requiredKnowledge.add(reward(type, category, amount));
        return this;
    }

    public ResearchStageBuilder requiredResearch(Object... research) {
        Objects.requireNonNull(research, "research");
        for (Object entry : research) {
            this.requiredResearch.add(this.context.resolveText(entry));
        }
        return this;
    }

    public ResearchStageBuilder warp(int warp) {
        this.warp = warp;
        return this;
    }

    public ResearchStageBuilder construct(int xSize, int ySize, int zSize, String... cells) {
        Objects.requireNonNull(cells, "cells");
        if (xSize < 1 || ySize < 1 || zSize < 1) {
            throw new IllegalArgumentException("Construct dimensions must be positive: %dx%dx%d".formatted(xSize, ySize, zSize));
        }
        int expected = xSize * ySize * zSize;
        if (cells.length != expected) {
            throw new IllegalArgumentException("Construct needs %d cells but got %d".formatted(expected, cells.length));
        }
        this.xSize = xSize;
        this.ySize = ySize;
        this.zSize = zSize;
        this.cells = List.of(cells);
        this.construct = true;
        return this;
    }

    public ResearchStageBuilder constructCost(Object aspect, int amount) {
        if (amount < 1) {
            throw new IllegalArgumentException("Aspect amount must be at least 1: " + amount);
        }
        JsonObject entry = new JsonObject();
        entry.addProperty("aspect", this.context.resolveText(aspect));
        entry.addProperty("amount", amount);
        this.constructCost.add(entry);
        return this;
    }

    public ResearchEntryBuilder end() {
        return this.owner;
    }

    public JsonObject toJson() {
        Objects.requireNonNull(this.text, "stage text");
        if (!this.constructCost.isEmpty() && !this.construct) {
            throw new IllegalStateException("constructCost requires a construct");
        }
        JsonObject json = new JsonObject();
        json.addProperty("text", this.text);
        if (!this.recipes.isEmpty()) {
            json.add("recipes", strings(this.recipes));
        }
        if (this.construct) {
            JsonObject structure = new JsonObject();
            structure.addProperty("x_size", this.xSize);
            structure.addProperty("y_size", this.ySize);
            structure.addProperty("z_size", this.zSize);
            structure.add("cells", strings(this.cells));
            if (!this.constructCost.isEmpty()) {
                structure.add("cost", objects(this.constructCost));
            }
            json.add("construct", structure);
        }
        addIfPresent(json, "obtain", this.obtain);
        addIfPresent(json, "craft", this.craft);
        addIfPresent(json, "knowledge", this.knowledge);
        addIfPresent(json, "required_knowledge", this.requiredKnowledge);
        if (!this.requiredResearch.isEmpty()) {
            json.add("required_research", strings(this.requiredResearch));
        }
        if (this.warp != 0) {
            json.addProperty("warp", this.warp);
        }
        return json;
    }

    private JsonObject requirement(Object items, int amount) {
        return requirement(items, amount, null);
    }

    private JsonObject requirement(Object items, int amount, JsonObject components) {
        if (amount < 1) {
            throw new IllegalArgumentException("Requirement amount must be at least 1: " + amount);
        }
        JsonObject json = new JsonObject();
        String ref = ResearchContext.itemRef(items);
        if (ref.startsWith("#")) {
            json.addProperty("items", ref);
        } else {
            JsonArray list = new JsonArray();
            list.add(ref);
            json.add("items", list);
        }
        if (components != null && !components.isEmpty()) {
            json.add("components", components);
        }
        json.addProperty("amount", amount);
        return json;
    }

    private JsonObject reward(KnowledgeType type, Object category, int amount) {
        Objects.requireNonNull(type, "type");
        if (amount < 1) {
            throw new IllegalArgumentException("Knowledge amount must be at least 1: " + amount);
        }
        JsonObject json = new JsonObject();
        json.addProperty("type", type.getSerializedName());
        json.addProperty("category", this.context.resolveText(category));
        json.addProperty("amount", amount);
        return json;
    }
}
