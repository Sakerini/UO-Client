package com.noetic.client.network.handlers;

import com.esotericsoftware.kryonet.Connection;
import com.noetic.client.enums.AuthStatus;
import com.noetic.client.enums.GenderType;
import com.noetic.client.enums.WorldStatus;
import com.noetic.client.handlers.GameDataHandler;
import com.noetic.client.models.WorldCharacter;
import com.noetic.client.models.WorldCharacterMP;
import com.noetic.client.network.connections.WorldConnection;
import com.noetic.client.network.packets.*;
import com.noetic.client.utils.NetworkUtil;

import java.util.logging.Level;
import java.util.logging.Logger;

public class WorldHandler implements PacketHandler {

    @Override
    public void handlePacket(Connection connection, APacket packet) {
        if (packet instanceof WorldConnectionSCPacket) {
            NetworkUtil.getAuthConnection().setStatus(AuthStatus.World);
        } else if (packet instanceof WorldPositionSCPacket) {
            handleSpawn(((WorldPositionSCPacket) packet));
        } else if (packet instanceof PlayerSCPacket) {
            handlePlayerList(((PlayerSCPacket) packet));
        } else if (packet instanceof WorldSCPacket) {
            WorldConnection.setStatus(WorldStatus.World);
        } else if (packet instanceof PlayerConnectSCPacket) {
            handleNewConnection((PlayerConnectSCPacket) packet);
        }
    }

    private void handleNewConnection(PlayerConnectSCPacket packet) {
        GenderType genderType = null;

        for (GenderType gender : GenderType.values()) {
            if (gender.getId() == packet.genderId) {
                genderType = gender;
            }
        }

        Logger.getLogger("client").log(Level.INFO, "Adding new player to world: {0}", packet.name);
        WorldCharacterMP newPlayer = new WorldCharacterMP(genderType, packet.name);
        newPlayer.setX(packet.x);
        newPlayer.setY(packet.y);
        GameDataHandler.players.add(newPlayer);
    }

    private void handleSpawn(WorldPositionSCPacket packet) {
        GameDataHandler.player = new WorldCharacter(
                GameDataHandler.characterInUse.getGender(),
                GameDataHandler.characterInUse.getName()
        );

        GameDataHandler.player.spawn(packet.X, packet.Y);
    }

    private void handlePlayerList(PlayerSCPacket packet) {
        String name = packet.name;
        int genderId = packet.genderId;
        float x = packet.x;
        float y = packet.y;

        GenderType genderType = null;

        for (GenderType gender : GenderType.values()) {
            if (gender.getId() == genderId) {
                genderType = gender;
            }
        }

        Logger.getLogger("client").log(Level.INFO, "Adding new player: {0}", name);
        WorldCharacterMP newPlayer = new WorldCharacterMP(genderType, name);
        newPlayer.setX(x);
        newPlayer.setY(y);
        GameDataHandler.players.add(newPlayer);
    }
}
