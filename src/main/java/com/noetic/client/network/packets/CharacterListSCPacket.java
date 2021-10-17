package com.noetic.client.network.packets;

import java.util.ArrayList;

public class CharacterListSCPacket extends APacket {
    public ArrayList<CharacterSCPacket> characterList;
    @Override
    public String toString() {
        return "sc_character_list";
    }
}
