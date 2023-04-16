package org.machinemc.api.network;

import io.netty.channel.ChannelFuture;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.machinemc.api.auth.PublicKeyData;
import org.machinemc.api.entities.Player;
import org.machinemc.api.network.packets.Packet;
import org.machinemc.api.server.ServerProperty;
import org.jetbrains.annotations.*;
import org.machinemc.scriptive.components.Component;
import org.machinemc.scriptive.components.TranslationComponent;

import java.net.InetSocketAddress;
import java.util.LinkedHashSet;
import java.util.Set;

import static org.machinemc.api.network.packets.Packet.PacketState.*;

/**
 * Represents a connection of a client.
 */
public interface PlayerConnection extends ServerProperty {

    /**
     * @return client state of the connection
     */
    ClientState getState();

    /**
     * @return public key data of the connection
     */
    @Nullable PublicKeyData getPublicKeyData();

    /**
     * @return login username of the connection
     */
    String getLoginUsername();

    /**
     * @return address with which the client connected
     */
    InetSocketAddress getAddress();

    /**
     * @return owner of the connection
     */
    @Nullable Player getOwner();

    /**
     * Disconnects the client and closes the connection.
     * @param reason reason for the disconnection
     */
    ChannelFuture disconnect(Component reason);

    /**
     * Disconnects the player with the default reason.
     */
    default void disconnect() {
        disconnect(TranslationComponent.of("disconnect.disconnected"));
    }

    /**
     * Closes the client connection.
     */
    ChannelFuture close();

    /**
     * Sends packet to the connection.
     * @param packet packet to send
     * @return if the operation was successful
     */
    ChannelFuture send(Packet packet);

    /**
     * Whether the connection is open and can receive packets.
     * @return whether the connection is open
     */
    boolean isOpen();

    /**
     * Client state of the connection, use to determinate the correct
     * group of packets to write/read.
     */
    @Getter
    @AllArgsConstructor
    enum ClientState {
        HANDSHAKE(HANDSHAKING_IN, HANDSHAKING_OUT),
        STATUS(STATUS_IN, STATUS_OUT),
        LOGIN(LOGIN_IN, LOGIN_OUT),
        PLAY(PLAY_IN, PLAY_OUT),
        DISCONNECTED(null, null);

        protected final @Nullable Packet.PacketState in;
        protected final @Nullable Packet.PacketState out;

        /**
         * Returns client states using the given packet state.
         * @param state state of the client state
         * @return client states
         */
        @Contract(pure = true)
        public static ClientState[] fromState(Packet.PacketState state) {
            final Set<ClientState> clientStates = new LinkedHashSet<>();
            for(ClientState clientState : values()) {
                if(clientState.in == state || clientState.out == state)
                    clientStates.add(clientState);
            }
            return clientStates.toArray(new ClientState[0]);
        }
    }

}
