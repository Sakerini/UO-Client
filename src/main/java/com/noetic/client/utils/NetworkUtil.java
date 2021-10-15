package com.noetic.client.utils;

import com.noetic.client.network.connections.AuthConnection;
import com.noetic.client.network.handlers.PacketHandler;

import java.util.Map;

public class NetworkUtil {
    private static AuthConnection authConnection;
    private static Map<String, PacketHandler> handlers;

    public static void authorize(String username, String password) {
        authConnection = new AuthConnection(handlers);
        authConnection.authorize(username, password);
    }
}
