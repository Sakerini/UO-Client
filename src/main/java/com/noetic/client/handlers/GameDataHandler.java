package com.noetic.client.handlers;

import com.noetic.client.models.GameCharacter;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

public class GameDataHandler {
    private static List<GameCharacter> characters;

    public static List<GameCharacter> getCharacters() {
        return characters;
    }

    public static void setCharacters(List<GameCharacter> characters) {
        GameDataHandler.characters = characters;
    }
}
