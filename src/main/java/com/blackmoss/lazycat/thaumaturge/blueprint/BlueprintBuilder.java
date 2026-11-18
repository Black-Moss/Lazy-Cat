package com.blackmoss.lazycat.thaumaturge.blueprint;

import com.blackmoss.lazycat.api.condition.ConditionalBuilder;
import com.blackmoss.lazycat.api.thaumaturge.blueprint.BlueprintContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.leclowndu93150.thaumaturge.api.recipe.Blueprint;
import com.leclowndu93150.thaumaturge.api.recipe.BlueprintPart;
import com.leclowndu93150.thaumaturge.api.recipe.BlueprintSource;
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
    private int xSize = -1;
    private int ySize = -1;
    private int zSize = -1;

    public BlueprintBuilder(BlueprintContext context, ResourceLocation id) {
        this.context = Objects.requireNonNull(context, "context");
        this.id = Objects.requireNonNull(id, "id");
    }

    public BlueprintBuilder size(int x, int y, int z) {
        if (x < 1 || y < 1 || z < 1) {
            throw new IllegalArgumentException("Blueprint size must be positive: %dx%dx%d".formatted(x, y, z));
        }
        this.xSize = x;
        this.ySize = y;
        this.zSize = z;
        return this;
    }

    public BlueprintBuilder key(char symbol, BlueprintSource source) {
        return key(symbol, source, BlueprintTarget.Keep.INSTANCE);
    }

    public BlueprintBuilder key(char symbol, BlueprintSource source, BlueprintTarget target) {
        return key(symbol, source, target, 0);
    }

    public BlueprintBuilder key(char symbol, BlueprintSource source, BlueprintTarget target, int priority) {
        if (symbol == EMPTY) {
            throw new IllegalArgumentException("Space is reserved for empty cells");
        }
        Objects.requireNonNull(source, "source");
        Objects.requireNonNull(target, "target");
        this.keys.put(symbol, new BlueprintPart(source, target, priority));
        return this;
    }

    public BlueprintBuilder layer(String... rows) {
        Objects.requireNonNull(rows, "rows");
        if (rows.length == 0) {
            throw new IllegalArgumentException("A blueprint layer needs at least one row");
        }
        List<String> layer = new ArrayList<>(rows.length);
        int width = -1;
        for (String row : rows) {
            Objects.requireNonNull(row, "rows contains null");
            if (width < 0) {
                width = row.length();
            } else if (row.length() != width) {
                throw new IllegalArgumentException("Blueprint rows must share one width: \"%s\" vs %d".formatted(row, width));
            }
            layer.add(row);
        }
        if (width < 1) {
            throw new IllegalArgumentException("A blueprint row must not be empty");
        }
        this.pattern.add(List.copyOf(layer));
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
        emit(ResourceLocation.fromNamespaceAndPath(this.context.namespace(), path));
    }

    private void emit(ResourceLocation outputId) {
        validate(outputId);
        int x = this.xSize > 0 ? this.xSize : this.pattern.getFirst().size();
        int y = this.ySize > 0 ? this.ySize : this.pattern.size();
        int z = this.zSize > 0 ? this.zSize : this.pattern.getFirst().getFirst().length();
        Blueprint blueprint = new Blueprint(x, y, z, Map.copyOf(this.keys), List.copyOf(this.pattern));
        JsonElement encoded = Blueprint.CODEC.encodeStart(JsonOps.INSTANCE, blueprint).result()
                .orElseThrow(() -> new IllegalStateException("Failed to encode thaumaturge blueprint " + outputId));
        JsonObject json = encoded.getAsJsonObject();
        dropDefaultPriorities(json);
        writeConditions(json);
        this.context.submit(outputId, json);
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

    private void validate(ResourceLocation outputId) {
        if (this.pattern.isEmpty()) {
            throw new IllegalStateException("Blueprint " + outputId + " has no layers");
        }
        int xCount = this.pattern.getFirst().size();
        int zLength = this.pattern.getFirst().getFirst().length();
        for (List<String> layer : this.pattern) {
            if (layer.size() != xCount) {
                throw new IllegalStateException("Blueprint " + outputId + " is not a consistent cuboid");
            }
            for (String row : layer) {
                if (row.length() != zLength) {
                    throw new IllegalStateException("Blueprint " + outputId + " is not a consistent cuboid");
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
