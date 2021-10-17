package com.noetic.client.network.handlers;

import com.esotericsoftware.kryonet.Connection;
import com.noetic.client.enums.AuthStatus;
import com.noetic.client.enums.GenderType;
import com.noetic.client.handlers.GameDataHandler;
import com.noetic.client.models.GameCharacter;
import com.noetic.client.network.packets.*;
import com.noetic.client.utils.NetworkUtil;

import java.util.ArrayList;

public class CharacterHandler implements PacketHandler {

    private final int SERVER_ERROR = -1;
    private final int EXISTS = 1;
    private final int OK = 0;

    @Override
    public void handlePacket(Connection connection, APacket packet) {
        if (packet instanceof CharacterCreateSCPacket)
            handleCharacterCreation((CharacterCreateSCPacket) packet);
        else if (packet instanceof CharacterListSCPacket) {
            handleCharacterList(((CharacterListSCPacket) packet));
        }
    }

    private void handleCharacterList(CharacterListSCPacket packet) {
        ArrayList<CharacterSCPacket> characters = packet.characterList;
        GameDataHandler.setCharacters(new ArrayList<>());
        if (characters.size() > 0) {
            for (CharacterSCPacket c : characters) {
                GameCharacter gameCharacter = new GameCharacter();
                gameCharacter.setName(c.name);
                gameCharacter.setZone(c.zone);

                for (GenderType g : GenderType.values()) {
                    if (c.gender == g.getId()) {
                        gameCharacter.setGender(g);
                    }
                }
                GameDataHandler.getCharacters().add(gameCharacter);
            }
        }
        NetworkUtil.getAuthConnection().setStatus(AuthStatus.Waiting);
    }

    private void handleCharacterCreation(CharacterCreateSCPacket packet) {
        int code = packet.code;

        if (code == SERVER_ERROR) {
            NetworkUtil.getAuthConnection().setStatus(AuthStatus.CharacterCreateServerError);
        } else if (code == EXISTS) {
            NetworkUtil.getAuthConnection().setStatus(AuthStatus.CharacterCreateExists);
        } else if (code == OK) {
            NetworkUtil.getAuthConnection().setStatus(AuthStatus.CharacterCreateOk);
        }
    }
}
