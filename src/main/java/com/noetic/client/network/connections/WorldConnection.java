package com.noetic.client.network.connections;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.noetic.client.enums.WorldStatus;
import com.noetic.client.handlers.GameDataHandler;
import com.noetic.client.network.Network;
import com.noetic.client.network.handlers.PacketHandler;
import com.noetic.client.network.packets.APacket;
import com.noetic.client.network.packets.WorldConnectionCSPacket;
import com.noetic.client.utils.NetworkUtil;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@Getter
@Setter
public class WorldConnection {

    private Client client;
    private static WorldStatus status = WorldStatus.Waiting;

    public WorldConnection() {
        if (client != null)
            return;
        client = new Client();
        Network.registerLib(client.getKryo());
        new Thread("world") {
            public void run() {
                status = WorldStatus.Connecting;
                client.start();
                try {
                    client.connect(5000, "127.0.0.1", GameDataHandler.worldPort, GameDataHandler.worldPort + 1);
                    WorldConnectionCSPacket packet = new WorldConnectionCSPacket();
                    packet.accountName = GameDataHandler.accountName;
                    packet.characterName = GameDataHandler.characterInUse.getName();
                    client.sendTCP(packet);
                } catch (IOException ex) {
                    status = WorldStatus.ConnectingFailed;
                    Logger.getLogger("client").log(Level.WARNING, "{0}", ex.getMessage());
                }
            }
        }.start();

        client.addListener(new Listener() {
            public void received(Connection connection, Object object) {
                for (Map.Entry<String, PacketHandler> set : NetworkUtil.getHandlers().entrySet()) {
                    if (set.getKey().equalsIgnoreCase(object.toString())) {
                        set.getValue().handlePacket(client, (APacket)object);
                    }
                }
            }
        });
    }

    public static WorldStatus getStatus() {
        return status;
    }

    public static void setStatus(WorldStatus connectionStatus) {
        status = connectionStatus;
    }
}
