package org.machinemc.server.world.region;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import lombok.Getter;
import org.jetbrains.annotations.Async;
import org.machinemc.api.server.schedule.Scheduler;
import org.machinemc.api.world.BlockPosition;
import org.machinemc.api.world.World;
import org.machinemc.landscape.Landscape;
import org.machinemc.landscape.LandscapeHandler;
import org.machinemc.server.chunk.ChunkUtils;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * Handles landscape region folder of a world.
 */
@SuppressWarnings("UnstableApiUsage")
public class LandscapeHelper {

    @Getter
    private final World source;
    private final Scheduler scheduler;
    private final File regionFolder;
    private final short height;
    @Getter
    private final LandscapeHandler handler;

    private final Cache<Long, Landscape> landscapes;

    public LandscapeHelper(final World source, final File regionFolder, final LandscapeHandler handler) {
        this.source = source;
        scheduler = source.getServer().getScheduler();
        this.regionFolder = regionFolder;
        height = (short) source.getDimensionType().getHeight();
        this.handler = handler;
        landscapes = CacheBuilder.newBuilder()
                .expireAfterAccess(1, TimeUnit.MINUTES)
                .removalListener(notification -> Scheduler.task((input, session) -> {
                    if(notification.wasEvicted()) {
                        Landscape landscape = (Landscape) notification.getValue();
                        assert landscape != null;
                        try {
                            landscape.flush();
                            landscape.close();
                        } catch (Exception exception) {
                            source.getServerExceptionHandler().handle(exception);
                        }
                    }
                    return null;
                }).run(scheduler))
                .build();
    }

    /**
     * Loads a landscape at given world coordinates.
     * @param x x coordinate
     * @param z z coordinate
     * @return landscape for give coordinates
     */
    public Landscape get(final int x, final int z) throws ExecutionException {
        final int chunkX = ChunkUtils.getChunkCoordinate(x);
        final int chunkZ = ChunkUtils.getChunkCoordinate(z);
        return landscapes.get(regionIndex(chunkX, chunkZ), () -> Landscape.of(regionFolder, chunkX >> 4, chunkZ >> 4, height, handler));
    }

    public Landscape get(final BlockPosition position) throws ExecutionException {
        return get(position.getX(), position.getZ());
    }

    @Async.Execute
    public void flush() {
        landscapes.asMap().values().forEach(Landscape::flush);
    }

    public void close() throws IOException {
        for (final Landscape landscape : landscapes.asMap().values()) {
            landscape.flush();
            landscape.close();
        }
        landscapes.asMap().clear();
    }

    private long regionIndex(final int chunkX, final int chunkZ) {
        return ((long)(chunkX >> 4) << 32) | ((chunkZ >> 4) & 0xFFFFFFFFL);
    }

}
