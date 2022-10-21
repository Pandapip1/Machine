package me.pesekjak.machine.file;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.Getter;
import me.pesekjak.machine.Machine;
import me.pesekjak.machine.server.ServerProperty;
import me.pesekjak.machine.utils.NamespacedKey;
import me.pesekjak.machine.world.dimensions.DimensionType;

import java.io.*;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class DimensionsJson implements ServerFile, ServerProperty {

    public final static String DIMENSIONS_FILE_NAME = "dimensions.json";

    @Getter
    private final Machine server;
    private final Set<DimensionType> dimensions = new HashSet<>();

    public DimensionsJson(Machine server, File file) throws IOException {
        this.server = server;
        final JsonParser parser = new JsonParser();
        JsonObject json = parser.parse(new FileReader(file)).getAsJsonObject();
        JsonObject dimensions = json.get("dimensions").getAsJsonObject();
        for(Map.Entry<String, JsonElement> dimensionKey : dimensions.entrySet()) {
            String unparsed = dimensionKey.getKey();
            NamespacedKey key;
            try {
                key = NamespacedKey.parse(unparsed);
            } catch (Exception ignored) {
                server.getConsole().severe("Dimension '" + unparsed + "' uses illegal identifier and can't be registered");
                continue;
            }
            JsonObject dimension = dimensionKey.getValue().getAsJsonObject();

            Boolean natural = dimension.get("natural") != null ? dimension.get("natural").getAsBoolean() : null;
            Number ambientLight = dimension.get("ambient_light").getAsNumber();
            Boolean ceilingEnabled = dimension.get("has_ceiling") != null ? dimension.get("has_ceiling").getAsBoolean() : null;
            Boolean skylightEnabled = dimension.get("has_skylight") != null ? dimension.get("has_skylight").getAsBoolean() : null;
            Number fixedTime = dimension.get("fixed_time").getAsNumber();
            if(fixedTime != null && fixedTime.intValue() == -1) fixedTime = null; // nullable option
            Boolean raidCapable = dimension.get("has_raids") != null ? dimension.get("has_raids").getAsBoolean() : null;
            Boolean respawnAnchorSafe = dimension.get("respawn_anchor_works") != null ? dimension.get("respawn_anchor_works").getAsBoolean() : null;
            Boolean ultrawarm = dimension.get("ultrawarm") != null ? dimension.get("ultrawarm").getAsBoolean() : null;
            Boolean bedSafe = dimension.get("bed_works") != null ? dimension.get("bed_works").getAsBoolean() : null;
            NamespacedKey effects = null;
            try {
                effects = NamespacedKey.parse(dimension.get("effects").getAsString());
            } catch (Exception ignored) { }
            Boolean piglinSafe = dimension.get("piglin_safe") != null ? dimension.get("piglin_safe").getAsBoolean() : null;
            Number minY = dimension.get("min_y").getAsNumber();
            Number height = dimension.get("height").getAsNumber();
            Number logicalHeight = dimension.get("logical_height").getAsNumber();
            Number coordinateScale = dimension.get("coordinate_scale").getAsNumber();
            NamespacedKey infiniburn = null;
            try {
                infiniburn = NamespacedKey.minecraft(dimension.get("infiniburn").getAsString());
            } catch (Exception ignored) { }
            Number monsterSpawnBlockLightLimit = dimension.get("monster_spawn_block_light_limit").getAsNumber();
            Number monsterSpawnLightLevel = dimension.get("monster_spawn_light_level").getAsNumber();

            if(natural == null | ambientLight == null | ceilingEnabled == null |
            skylightEnabled == null | raidCapable == null | respawnAnchorSafe == null |
            ultrawarm == null | bedSafe == null | effects == null | piglinSafe == null |
            minY == null | height == null | logicalHeight == null | coordinateScale == null |
            infiniburn == null | monsterSpawnBlockLightLimit == null | monsterSpawnLightLevel == null) {
                server.getConsole().warning("Dimension '" + key + "' has missing properties, default values will be used instead");
            }

            final DimensionType defaultType = DimensionType.createDefault(server.getDimensionTypeManager());

            this.dimensions.add(DimensionType.builder(server.getDimensionTypeManager())
                    .name(key)
                    .natural(natural != null ? natural : defaultType.isNatural())
                    .ambientLight(ambientLight != null ? ambientLight.floatValue() : defaultType.getAmbientLight())
                    .ceilingEnabled(ceilingEnabled != null ? ceilingEnabled : defaultType.isCeilingEnabled())
                    .skylightEnabled(skylightEnabled != null ? skylightEnabled : defaultType.isSkylightEnabled())
                    .fixedTime(fixedTime != null ? fixedTime.longValue() : null) // nullable option
                    .raidCapable(raidCapable != null ? raidCapable : defaultType.isRaidCapable())
                    .respawnAnchorSafe(respawnAnchorSafe != null ? respawnAnchorSafe : defaultType.isRespawnAnchorSafe())
                    .ultrawarm(ultrawarm != null ? ultrawarm : defaultType.isUltrawarm())
                    .bedSafe(bedSafe != null ? natural : defaultType.isNatural())
                    .effects(effects != null ? effects : defaultType.getEffects())
                    .piglinSafe(piglinSafe != null ? piglinSafe : defaultType.isPiglinSafe())
                    .minY(minY != null ? minY.intValue() : defaultType.getMinY())
                    .height(height != null ? height.intValue() : defaultType.getHeight())
                    .logicalHeight(logicalHeight != null ? logicalHeight.intValue() : defaultType.getLogicalHeight())
                    .coordinateScale(coordinateScale != null ? coordinateScale.intValue() : defaultType.getCoordinateScale())
                    .infiniburn(infiniburn != null ? infiniburn : defaultType.getInfiniburn())
                    .monsterSpawnBlockLightLimit(monsterSpawnBlockLightLimit != null ? monsterSpawnBlockLightLimit.intValue() : defaultType.getMonsterSpawnBlockLightLimit())
                    .monsterSpawnLightLevel(monsterSpawnLightLevel != null ? monsterSpawnLightLevel.intValue() : defaultType.getMonsterSpawnLightLevel())
                    .build()
            );
        }
    }

    public Set<DimensionType> dimensions() {
        return dimensions;
    }

    @Override
    public String getName() {
        return DIMENSIONS_FILE_NAME;
    }

    @Override
    public InputStream getOriginal() {
        return Machine.CLASS_LOADER.getResourceAsStream(DIMENSIONS_FILE_NAME);
    }

}
