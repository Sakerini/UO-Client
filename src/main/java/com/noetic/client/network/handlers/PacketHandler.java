package com.noetic.client.network.handlers;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Server;
import com.noetic.client.network.packets.APacket;

public interface PacketHandler {
    void handlePacket(Connection connection, APacket packet);

}
