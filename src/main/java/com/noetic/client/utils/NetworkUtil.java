package com.noetic.client.utils;

import com.esotericsoftware.kryonet.Connection;
import com.noetic.client.enums.PacketDirection;
import com.noetic.client.network.connections.AuthConnection;
import com.noetic.client.network.handlers.CharacterHandler;
import com.noetic.client.network.handlers.LoginHandler;
import com.noetic.client.network.handlers.PacketHandler;
import com.noetic.client.network.packets.APacket;
import com.noetic.client.network.packets.CharacterCreateCSPacket;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public class NetworkUtil {
    private static AuthConnection authConnection;
    private static Map<String, PacketHandler> handlers = new LinkedHashMap<>();

    public static void authorize(String username, String password) {
        authConnection = new AuthConnection(handlers);
        authConnection.authorize(username, password);
    }

    public static void sendCharacterCreationPacket(String name, int genderId) {
        CharacterCreateCSPacket packet = new CharacterCreateCSPacket();
        packet.name = name;
        packet.gender = genderId;

        authConnection.getClient().sendTCP(packet);
    }

    public static void sendSimplePacket(PacketDirection direction, APacket packet) {
        if (direction.equals(PacketDirection.Auth)) {
            AuthConnection connection = getAuthConnection();
            if (Objects.nonNull(connection.getClient())) {
                connection.getClient().sendTCP(packet);
            }
        } else {

        }
    }

    public static AuthConnection getAuthConnection() {
        return authConnection;
    }
    public static void setAuthConnection(AuthConnection connection) {
        authConnection = connection;
    }

    public static void initHandlers() {
        CharacterHandler characterHandler = new CharacterHandler();
        handlers.put("sc_login", new LoginHandler());
        handlers.put("sc_character_create", characterHandler);
        handlers.put("sc_character_list" , characterHandler);
    }
}
