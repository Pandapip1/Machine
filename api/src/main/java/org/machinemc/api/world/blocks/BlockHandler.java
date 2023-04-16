package org.machinemc.api.world.blocks;

import org.jetbrains.annotations.ApiStatus;
import org.machinemc.api.world.BlockData;

/**
 * Specifies behaviour of a certain block type.
 */
// TODO Event arguments as source of the changes once event
//  system gets implemented
public interface BlockHandler {

    /**
     * Called when the the block is generated by the world generator.
     * @param block block that has been generated
     * @apiNote works only for tile entities, {@link BlockEntityType}
     */
    @ApiStatus.Experimental
    void onGeneration(WorldBlock.State block);

    /**
     * Called when a blockdata visual of a block is requested.
     * @param block block for which the blockdata is requested
     * @param original originally provided blockdata
     * @return new blockdata
     * @apiNote works only for block types with dynamic visual, {@link BlockType#hasDynamicVisual()}
     */
    @ApiStatus.Experimental
    BlockData onVisualRequest(WorldBlock.State block, BlockData original);

    /**
     * Called when entity starts destroying a block.
     * @param block state of the block being destroyed
     */
    void onStartDestroying(WorldBlock.State block);

    /**
     * Called when entity stops destroying the block.
     * @param block state of the block that was being destroyed
     */
    void onStopDestroying(WorldBlock.State block);

    /**
     * Called when entity interacts with the block.
     * @param block state of the block
     */
    void onInteract(WorldBlock.State block);

    /**
     * Called when entity attacks the block.
     * @param block state of the block
     */
    void onAttack(WorldBlock.State block);

    /**
     * Called when the block is placed.
     * @param block state of the placed block
     */
    void onPlace(WorldBlock.State block);

    /**
     * Called when the block is updated.
     * @param block state of the updated block
     */
    void onUpdate(WorldBlock.State block);

}
