package com.noetic.client.network.connections;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.noetic.client.enums.AuthStatus;
import com.noetic.client.network.Network;
import com.noetic.client.network.handlers.PacketHandler;
import com.noetic.client.network.packets.APacket;
import com.noetic.client.network.packets.LoginCSPacket;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;

import java.io.IOException;
import java.security.MessageDigest;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@Getter
@Setter
public class AuthConnection {
    private Client client = new Client();
    private AuthStatus status;

    public AuthConnection(Map<String, PacketHandler> handlers) {
        Network.registerLib(client.getKryo());

        client.addListener(new Listener() {
            public void received(Connection connection, Object object) {
                for (Map.Entry<String, PacketHandler> set : handlers.entrySet()) {
                    if (set.getKey().equalsIgnoreCase(object.toString())) {
                        set.getValue().handlePacket(client, (APacket) object);
                    }
                }
            }

            public void disconnected(Connection connection) {
                connection.close();
            }
        });
    }

    public void authorize(String username, String password) {
        new Thread("auth") {
            public void run() {
                status = AuthStatus.CONNECTING;
                client.start();
                try {
                    client.connect(5000,"127.0.0.1", 6770);
                    status = AuthStatus.AUTHENTICATING;
                    LoginCSPacket packet = new LoginCSPacket();
                    packet.username = username;
                    packet.password = hashPassword(password);
                    client.sendTCP(packet);
                } catch (IOException ex) {
                    status = AuthStatus.CONNECTION_FAILED;
                    Logger.getLogger("client").log(Level.WARNING, "{0}", ex.getMessage());
                }
            }
        }.start();
    }

    @SneakyThrows
    private String hashPassword(String password) {
        String hashedPassword = null;
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] hash = md.digest(password.getBytes());
        hashedPassword = bytesToHex(hash);
        return hashedPassword;
    }

    private String bytesToHex(byte[] ref) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : ref) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }
}
