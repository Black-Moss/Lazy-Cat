package com.blackmoss.lazycat.thaumaturge.research;

import com.blackmoss.lazycat.api.condition.ConditionalBuilder;
import com.blackmoss.lazycat.api.thaumaturge.research.ResearchContext;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.leclowndu93150.thaumaturge.api.research.ResearchEntryMeta;
import com.leclowndu93150.thaumaturge.api.research.ResearchParent;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.conditions.ICondition;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

@SuppressWarnings({"unused", "UnusedReturnValue"})
public final class ResearchEntryBuilder implements ConditionalBuilder<ResearchEntryBuilder> {
    private final ResearchContext context;
    private final String category;
    private final String name;
    private final List<ICondition> conditions = new ArrayList<>();
    private final List<String> parents = new ArrayList<>();
    private final List<String> siblings = new ArrayList<>();
    private final List<String> meta = new ArrayList<>();
    private final List<String> icons = new ArrayList<>();
    private final List<AddendumBuilder> addenda = new ArrayList<>();
    private final List<JsonObject> noteAspects = new ArrayList<>();
    private final List<ResearchStageBuilder> stages = new ArrayList<>();
    private ResourceLocation id;
    private int x;
    private int y;
    private int complexity = 1;

    public ResearchEntryBuilder(ResearchContext context, Object category, String nameKey) {
        this.context = context;
        this.category = context.resolveText(category);
        this.name = Objects.requireNonNull(nameKey, "nameKey");
    }

    private static JsonArray strings(List<String> values) {
        JsonArray array = new JsonArray();
        values.forEach(array::add);
        return array;
    }

    @Override
    public List<ICondition> conditionList() {
        return this.conditions;
    }

    public ResearchEntryBuilder id(ResourceLocation id) {
        this.id = Objects.requireNonNull(id, "id");
        return this;
    }

    public ResearchEntryBuilder id(String path) {
        return id(this.context.resolveId(path));
    }

    public ResearchEntryBuilder position(int x, int y) {
        this.x = x;
        this.y = y;
        return this;
    }

    public ResearchEntryBuilder x(int x) {
        this.x = x;
        return this;
    }

    public ResearchEntryBuilder y(int y) {
        this.y = y;
        return this;
    }

    public ResearchEntryBuilder parent(Object parent) {
        return parent(parent, 0, false);
    }

    public ResearchEntryBuilder parent(Object parent, int stage) {
        return parent(parent, stage, false);
    }

    public ResearchEntryBuilder inheritParent(Object parent) {
        return parent(parent, 0, true);
    }

    public ResearchEntryBuilder inheritParent(Object parent, int stage) {
        return parent(parent, stage, true);
    }

    public ResearchEntryBuilder parent(ResearchParent parent) {
        Objects.requireNonNull(parent, "parent");
        return parent(parent.id(), parent.stage(), parent.inherit());
    }

    public ResearchEntryBuilder sibling(Object sibling) {
        this.siblings.add(this.context.resolveText(sibling));
        return this;
    }

    public ResearchEntryBuilder meta(ResearchEntryMeta... meta) {
        Objects.requireNonNull(meta, "meta");
        for (ResearchEntryMeta entry : meta) {
            this.meta.add(Objects.requireNonNull(entry, "meta").getSerializedName());
        }
        return this;
    }

    public ResearchEntryBuilder icon(Object icon) {
        this.icons.add(ResearchContext.iconRef(icon));
        return this;
    }

    public ResearchEntryBuilder noteAspect(Object aspect) {
        noteAspect(aspect, 5);
        return this;
    }

    public ResearchEntryBuilder noteAspect(Object aspect, int amount) {
        if (amount < 1) {
            throw new IllegalArgumentException("Aspect amount must be at least 1: " + amount);
        }
        JsonObject entry = new JsonObject();
        entry.addProperty("aspect", this.context.resolveText(aspect));
        entry.addProperty("amount", amount);
        this.noteAspects.add(entry);
        return this;
    }

    public ResearchEntryBuilder complexity(int complexity) {
        if (complexity < 1 || complexity > 3) {
            throw new IllegalArgumentException("Complexity must be between 1 and 3: " + complexity);
        }
        this.complexity = complexity;
        return this;
    }

    public AddendumBuilder addendum() {
        AddendumBuilder addendum = new AddendumBuilder(this);
        this.addenda.add(addendum);
        return addendum;
    }

    public AddendumBuilder addendum(String textKey) {
        return addendum().text(textKey);
    }

    public ResearchStageBuilder stage() {
        ResearchStageBuilder stage = new ResearchStageBuilder(this.context, this, "%s.stage_%d".formatted(this.name, this.stages.size() + 1));
        this.stages.add(stage);
        return stage;
    }

    public ResearchEntryBuilder stage(Consumer<ResearchStageBuilder> stage) {
        Objects.requireNonNull(stage, "stage");
        stage.accept(stage());
        return this;
    }

    public void save() {
        if (this.id == null) {
            throw new IllegalStateException("Research entry '%s' has no id, call id(...) or save(...)".formatted(this.name));
        }
        save(this.id);
    }

    public void save(String path) {
        save(ResourceLocation.fromNamespaceAndPath(this.context.namespace(), path));
    }

    public void save(ResourceLocation id) {
        Objects.requireNonNull(id, "id");
        if (this.stages.isEmpty()) {
            throw new IllegalStateException("Research entry '%s' needs at least one stage".formatted(id));
        }
        JsonObject json = new JsonObject();
        json.addProperty("category", this.category);
        json.addProperty("name", this.name);
        if (!this.parents.isEmpty()) {
            json.add("parents", strings(this.parents));
        }
        if (!this.siblings.isEmpty()) {
            json.add("siblings", strings(this.siblings));
        }
        json.addProperty("column", this.x);
        json.addProperty("row", this.y);
        JsonArray stageArray = new JsonArray();
        this.stages.forEach(stage -> stageArray.add(stage.toJson()));
        json.add("stages", stageArray);
        if (!this.meta.isEmpty()) {
            json.add("meta", strings(this.meta));
        }
        if (!this.icons.isEmpty()) {
            json.add("icons", strings(this.icons));
        }
        if (!this.addenda.isEmpty()) {
            JsonArray array = new JsonArray();
            for (int i = 0; i < this.addenda.size(); i++) {
                String defaultText = this.name + (this.addenda.size() == 1 ? ".addendum" : ".addendum_" + (i + 1));
                array.add(this.addenda.get(i).toJson(defaultText));
            }
            json.add("addenda", array);
        }
        if (!this.noteAspects.isEmpty()) {
            JsonArray array = new JsonArray();
            this.noteAspects.forEach(array::add);
            json.add("note_aspects", array);
        }
        if (this.complexity != 1) {
            json.addProperty("complexity", this.complexity);
        }
        writeConditions(json);
        this.context.submitEntry(id, json);
    }

    private ResearchEntryBuilder parent(Object parent, int stage, boolean inherit) {
        if (stage < 0) {
            throw new IllegalArgumentException("Parent stage must not be negative: " + stage);
        }
        String reference = (inherit ? "~" : "") + this.context.resolveText(parent) + (stage > 0 ? "@" + stage : "");
        this.parents.add(reference);
        return this;
    }

    public static final class AddendumBuilder {
        private final ResearchEntryBuilder owner;
        private final List<String> recipes = new ArrayList<>();
        private final List<String> requiredResearch = new ArrayList<>();
        private String text;

        private AddendumBuilder(ResearchEntryBuilder owner) {
            this.owner = owner;
        }

        public AddendumBuilder text(String textKey) {
            this.text = Objects.requireNonNull(textKey, "textKey");
            return this;
        }

        public AddendumBuilder recipe(Object recipe) {
            this.recipes.add(this.owner.context.resolveText(recipe));
            return this;
        }

        public AddendumBuilder recipes(Object... recipes) {
            Objects.requireNonNull(recipes, "recipes");
            for (Object recipe : recipes) {
                recipe(recipe);
            }
            return this;
        }

        public AddendumBuilder requiredResearch(Object... research) {
            Objects.requireNonNull(research, "research");
            for (Object entry : research) {
                this.requiredResearch.add(this.owner.context.resolveText(entry));
            }
            return this;
        }

        public ResearchEntryBuilder end() {
            return this.owner;
        }

        private JsonObject toJson(String defaultText) {
            JsonObject json = new JsonObject();
            json.addProperty("text", this.text != null ? this.text : defaultText);
            if (!this.recipes.isEmpty()) {
                json.add("recipes", strings(this.recipes));
            }
            if (!this.requiredResearch.isEmpty()) {
                json.add("required_research", strings(this.requiredResearch));
            }
            return json;
        }
    }
}
