package org.machinemc.api.world.generation;

import org.machinemc.api.server.ServerProperty;
import org.machinemc.api.world.BlockPosition;
import org.machinemc.api.world.blocks.BlockType;

/**
 * Represents generator of a world.
 */
public interface Generator extends ServerProperty {

    /**
     * Returns a block type that should generate at the given block position
     * @param position position to generate
     * @return block type to generate at that position
     */
    BlockType generate(BlockPosition position);

}
