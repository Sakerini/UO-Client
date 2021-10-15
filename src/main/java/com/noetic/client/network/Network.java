package com.noetic.client.network;

import com.esotericsoftware.kryo.Kryo;
import com.noetic.client.network.packets.LoginCSPacket;
import com.noetic.client.network.packets.LoginSCPacket;

import java.util.ArrayList;

public class Network {

	public static void registerLib(Kryo kryo) {
		kryo.register(LoginSCPacket.class);
		kryo.register(LoginCSPacket.class);
		kryo.register(ArrayList.class);
	}
}
