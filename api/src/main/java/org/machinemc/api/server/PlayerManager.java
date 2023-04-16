package org.machinemc.api.server;

import org.machinemc.api.entities.Player;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.util.Set;
import java.util.UUID;
import java.util.function.Predicate;

/**
 * Manages players connected to the server.
 */
public interface PlayerManager extends ServerProperty {

    /**
     * Adds the player to the manager.
     * @param player player to add
     */
    void addPlayer(Player player);

    /**
     * Removes the player from the manager.
     * @param player player to remove
     */
    void removePlayer(Player player);

    /**
     * Removes the player with given uuid from the manager.
     * @param uuid uuid of the player
     */
    default void removePlayer(final UUID uuid) {
        final Player player = getPlayer(uuid);
        if (player == null) return;
        removePlayer(player);
    }

    /**
     * Removes the player with given name from the manager.
     * @param name name of the player
     */
    default void removePlayer(final String name) {
        final Player player = getPlayer(name);
        if (player == null) return;
        removePlayer(player);
    }

    /**
     * Returns player with given uuid in the manager.
     * @param uuid uuid of the player
     * @return player with given uuid
     */
    @Nullable Player getPlayer(UUID uuid);

    /**
     * Returns player with given name in the manager.
     * @param name name of the player
     * @return player with given name
     */
    @Nullable Player getPlayer(String name);

    /**
     * @return unmodifiable set of all players in this manager
     */
    @Unmodifiable Set<Player> getPlayers();

    /**
     * Returns all players in this manager passing the predicate.
     * @param predicate predicate
     * @return unmodifiable set of the players
     */
    @Unmodifiable Set<Player> getPlayers(Predicate<Player> predicate);

}
