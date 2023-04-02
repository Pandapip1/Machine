package org.machinemc.api.world.generation;

import org.machinemc.api.server.ServerProperty;
import org.machinemc.api.world.World;
import org.machinemc.api.world.blocks.BlockType;
import org.machinemc.nbt.NBTCompound;

/**
 * Represents generator of a world.
 */
// TODO Biome support
public interface Generator extends ServerProperty {

    /**
     * Generates entries for a section in a chunk.
     * @param chunkX x coordinate of the chunk
     * @param chunkZ z coordinate of the chunk
     * @param sectionIndex index of the section
     * @param world world of the chunk
     * @return content for the chunk section
     */
    SectionContent populateChunk(final int chunkX, final int chunkZ, final int sectionIndex, World world);

    /**
     * @return seed used by this generator
     */
    long getSeed();

    /**
     * Represents generated content of a section in a chunk.
     */
    interface SectionContent {

        int DATA_SIZE = 16*16*16;

        /**
         * @return palette of this section generated by the chunk
         * @apiNote size should be {@link SectionContent#DATA_SIZE}
         */
        BlockType[] getPalette();

        /**
         * Data of this section, values need to match the palette indices of
         * block types.
         * <p>
         * For index encoding and decoding use {@link SectionContent#index(int, int, int)} and {@link SectionContent#decode(int)}.
         * <p>
         * For specifying data of individual generated blocks {@link SectionContent#getTileEntitiesData()} is used.
         * @return data
         * @apiNote size has to be {@link SectionContent#DATA_SIZE}
         */
        short[] getData();

        /**
         * Used for generating of tile entities, indices of the array need to match encoding positions
         * using {@link SectionContent#index(int, int, int)}.
         * <p>
         * NBT Compounds provided by will merge to initialized compounds of the blocks.
         * @return nbt of tile entities generated in the section
         * @apiNote size has to be {@link SectionContent#DATA_SIZE}
         */
        NBTCompound[] getTileEntitiesData();

        static int index(final int x, final int y, final int z) {
            return (x & 0xF) | ((y << 4) & 0xF0) | (z << 8);
        }

        static int[] decode(final int index) {
            final int[] values = new int[3];
            values[0] = index & 0xF;
            values[1] = index & 0xF0;
            values[2] = index & 0xF00;
            return values;
        }

    }

}
