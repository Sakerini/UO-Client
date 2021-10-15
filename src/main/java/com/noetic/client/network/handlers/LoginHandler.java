package com.noetic.client.network.handlers;

import com.esotericsoftware.kryonet.Connection;
import com.noetic.client.enums.AuthStatus;
import com.noetic.client.network.packets.APacket;
import com.noetic.client.network.packets.LoginSCPacket;
import com.noetic.client.utils.NetworkUtil;

public class LoginHandler implements PacketHandler {
    private static final int UNKNOWN = 10;
    private static final int ONLINE = 2;
    private static final int INCORRECT = 1;
    private static final int OK = 0;

    @Override
    public void handlePacket(Connection connection, APacket packet) {
        LoginSCPacket loginPacket = (LoginSCPacket) packet;
        int code = loginPacket.code;

        switch (code) {
            case UNKNOWN:
                NetworkUtil.getAuthConnection().setStatus(AuthStatus.UNKNOWN);
                break;
            case ONLINE:
                NetworkUtil.getAuthConnection().setStatus(AuthStatus.ALREADY_ONLINE);
                break;
            case INCORRECT:
                NetworkUtil.getAuthConnection().setStatus(AuthStatus.INCORRECT);
                break;
            case OK:
                NetworkUtil.getAuthConnection().setStatus(AuthStatus.OK);
                break;
        }

    }
}
