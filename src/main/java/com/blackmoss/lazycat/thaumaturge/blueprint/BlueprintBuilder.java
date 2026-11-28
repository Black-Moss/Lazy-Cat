package com.blackmoss.lazycat.thaumaturge.blueprint;

import com.blackmoss.lazycat.api.condition.ConditionalBuilder;
import com.blackmoss.lazycat.api.thaumaturge.blueprint.BlueprintContext;
import com.blackmoss.lazycat.api.thaumaturge.blueprint.Blueprints;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.leclowndu93150.thaumaturge.api.recipe.Blueprint;
import com.leclowndu93150.thaumaturge.api.recipe.BlueprintPart;
import com.leclowndu93150.thaumaturge.api.recipe.BlueprintTarget;
import com.mojang.serialization.JsonOps;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.conditions.ICondition;

import java.util.*;

@SuppressWarnings("unused")
public final class BlueprintBuilder implements ConditionalBuilder<BlueprintBuilder> {
    public static final char EMPTY = ' ';

    private final BlueprintContext context;
    private final ResourceLocation id;
    private final Map<Character, BlueprintPart> keys = new LinkedHashMap<>();
    private final List<List<String>> pattern = new ArrayList<>();
    private final List<ICondition> conditions = new ArrayList<>();

    public BlueprintBuilder(BlueprintContext context, ResourceLocation id) {
        this.context = Objects.requireNonNull(context, "context");
        this.id = Objects.requireNonNull(id, "id");
    }

    private static void dropDefaultPriorities(JsonObject blueprint) {
        JsonElement keys = blueprint.get("keys");
        if (keys == null || !keys.isJsonObject()) {
            return;
        }
        for (Map.Entry<String, JsonElement> entry : keys.getAsJsonObject().entrySet()) {
            if (!entry.getValue().isJsonObject()) {
                continue;
            }
            JsonObject part = entry.getValue().getAsJsonObject();
            JsonElement priority = part.get("priority");
            if (priority != null && priority.isJsonPrimitive() && priority.getAsInt() == 0) {
                part.remove("priority");
            }
        }
    }

    public BlueprintBuilder key(char symbol, Object source) {
        return key(symbol, source, BlueprintTarget.Keep.INSTANCE);
    }

    public BlueprintBuilder key(char symbol, Object source, Object target) {
        return key(symbol, source, target, 0);
    }

    public BlueprintBuilder key(char symbol, Object source, Object target, int priority) {
        if (symbol == EMPTY) {
            throw new IllegalArgumentException("Space is reserved for empty cells");
        }
        this.keys.put(symbol, new BlueprintPart(
                Blueprints.sourceOf(source), Blueprints.targetOf(target), priority));
        return this;
    }

    public BlueprintBuilder layer() {
        if (!this.pattern.isEmpty() && this.pattern.getLast().isEmpty()) {
            throw new IllegalStateException("Previous blueprint layer has no rows");
        }
        this.pattern.add(new ArrayList<>());
        return this;
    }

    public BlueprintBuilder row(String row) {
        Objects.requireNonNull(row, "row");
        if (row.isEmpty()) {
            throw new IllegalArgumentException("A blueprint row must not be empty");
        }
        if (this.pattern.isEmpty()) {
            this.pattern.add(new ArrayList<>());
        }
        List<String> current = this.pattern.getLast();
        if (!current.isEmpty() && current.getFirst().length() != row.length()) {
            throw new IllegalArgumentException(
                    "Blueprint rows must share one width: \"%s\" vs %d".formatted(row, current.getFirst().length()));
        }
        current.add(row);
        return this;
    }

    public BlueprintBuilder layer(String... rows) {
        Objects.requireNonNull(rows, "rows");
        if (rows.length == 0) {
            throw new IllegalArgumentException("A blueprint layer needs at least one row");
        }
        layer();
        for (String row : rows) {
            row(row);
        }
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
        emit(this.id);
    }

    public void save(String path) {
        emit(ResourceLocation.fromNamespaceAndPath(this.context.modid(), path));
    }

    private void emit(ResourceLocation outputId) {
        validate(outputId);
        int x = this.pattern.getFirst().size();
        int y = this.pattern.size();
        int z = this.pattern.getFirst().getFirst().length();
        Blueprint blueprint = new Blueprint(x, y, z, Map.copyOf(this.keys), List.copyOf(this.pattern));
        JsonElement encoded = Blueprint.CODEC.encodeStart(JsonOps.INSTANCE, blueprint).result()
                .orElseThrow(() -> new IllegalStateException("Failed to encode thaumaturge blueprint " + outputId));
        JsonObject json = encoded.getAsJsonObject();
        dropDefaultPriorities(json);
        writeConditions(json);
        this.context.submit(outputId, json);
    }

    private void validate(ResourceLocation outputId) {
        if (this.pattern.isEmpty() || this.pattern.getFirst().isEmpty()) {
            throw new IllegalStateException("Blueprint %s has no rows".formatted(outputId));
        }
        int xCount = this.pattern.getFirst().size();
        int zLength = this.pattern.getFirst().getFirst().length();
        for (List<String> layer : this.pattern) {
            if (layer.size() != xCount) {
                throw new IllegalStateException("Blueprint %s is not a consistent cuboid".formatted(outputId));
            }
            for (String row : layer) {
                if (row.length() != zLength) {
                    throw new IllegalStateException("Blueprint %s is not a consistent cuboid".formatted(outputId));
                }
                for (int i = 0; i < row.length(); i++) {
                    char symbol = row.charAt(i);
                    if (symbol != EMPTY && !this.keys.containsKey(symbol)) {
                        throw new IllegalStateException(
                                "Unknown key '%s' in blueprint %s".formatted(symbol, outputId));
                    }
                }
            }
        }
    }
}
