package org.machinemc.api.world.biomes;

import org.machinemc.api.server.NBTSerializable;
import org.machinemc.api.utils.NamespacedKey;

public interface Biome extends NBTSerializable {

    /**
     * @return name of the biome
     */
    NamespacedKey getName();

    /**
     * @return depth of the biome
     */
    float getDepth();

    /**
     * @return temperature of the biome
     */
    float getTemperature();

    /**
     * @return scale of the biome
     */
    float getScale();

    /**
     * @return downfall of the biome
     */
    float getDownfall();

    /**
     * @return category of the biome
     */
    Category getCategory();

    /**
     * @return biome effects of the biome
     */
    BiomeEffects getEffects();

    /**
     * @return precipitation of the biome
     */
    Precipitation getPrecipitation();

    /**
     * @return temperature modifier of the biome
     */
    TemperatureModifier getTemperatureModifier();

    /**
     * Represents the raining type in the biome.
     */
    enum Precipitation {
        NONE, RAIN, SNOW;
    }

    /**
     * Represents category of the biome.
     */
    enum Category {
        NONE, TAIGA, EXTREME_HILLS, JUNGLE, MESA, PLAINS,
        SAVANNA, ICY, THE_END, BEACH, FOREST, OCEAN,
        DESERT, RIVER, SWAMP, MUSHROOM, NETHER, UNDERGROUND,
        MOUNTAIN;
    }

    /**
     * Represents the temperature of the biome.
     */
    enum TemperatureModifier {
        NONE, FROZEN;
    }

}
