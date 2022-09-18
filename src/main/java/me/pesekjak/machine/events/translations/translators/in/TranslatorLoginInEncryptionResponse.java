package me.pesekjak.machine.events.translations.translators.in;

import me.pesekjak.machine.auth.MojangAuth;
import me.pesekjak.machine.auth.OnlineServer;
import me.pesekjak.machine.entities.Player;
import me.pesekjak.machine.entities.player.PlayerProfile;
import me.pesekjak.machine.events.translations.PacketTranslator;
import me.pesekjak.machine.network.ClientConnection;
import me.pesekjak.machine.network.packets.in.PacketLoginInEncryptionResponse;
import me.pesekjak.machine.network.packets.out.PacketLoginOutSuccess;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.math.BigInteger;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

import static me.pesekjak.machine.network.packets.out.PacketLoginOutEncryptionRequest.SERVER_ID;

public class TranslatorLoginInEncryptionResponse extends PacketTranslator<PacketLoginInEncryptionResponse> {

    @Override
    public boolean translate(ClientConnection connection, PacketLoginInEncryptionResponse packet) {
        return true;
    }

    @Override
    public void translateAfter(ClientConnection connection, PacketLoginInEncryptionResponse packet) {
        OnlineServer onlineServer = connection.getServer().getOnlineServer();
        if(onlineServer == null) {
            connection.disconnect();
            return;
        }
        if(connection.getLoginUsername() == null) {
            connection.disconnect();
            return;
        }

        SecretKey secretkey = onlineServer.getSecretKey(onlineServer.getKey().getPrivate(), packet.getSecret());
        connection.setSecretKey(secretkey);

        final String serverId = (new BigInteger(onlineServer.digestData(SERVER_ID, onlineServer.getKey().getPublic(), secretkey))).toString(16);
        final String username = URLEncoder.encode(connection.getLoginUsername(), StandardCharsets.UTF_8);

        MojangAuth.getAuthData(serverId, username).thenAccept(json -> {
            if(json == null) {
                try {
                    connection.disconnect(Component.text("Invalid session (Try restarting your game)"));
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
                return;
            }
            UUID authUUID = UUID.fromString(
                    json.get("id").getAsString()
                            .replaceFirst(
                                    "(\\p{XDigit}{8})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}+)", "$1-$2-$3-$4-$5"
                            ));
            String authUsername = json.get("name").getAsString();
            final PlayerProfile profile = PlayerProfile.online(authUsername, authUUID);
            try {
                connection.sendPacket(new PacketLoginOutSuccess(authUUID, authUsername, profile.getTextures()));
            } catch (IOException ignored) {
                connection.disconnect();
                return;
            }
            connection.setClientState(ClientConnection.ClientState.PLAY);
            new Player(connection.getServer(), profile, connection);
        });
    }

    @Override
    public @NotNull Class<PacketLoginInEncryptionResponse> packetClass() {
        return PacketLoginInEncryptionResponse.class;
    }

}
