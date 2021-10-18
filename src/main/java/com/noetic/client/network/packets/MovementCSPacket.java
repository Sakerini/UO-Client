package com.noetic.client.network.packets;

public class MovementCSPacket {

    public int direction;
    public boolean isMoving;

    @Override
    public String toString() {
        return "cs_movement";
    }
}
