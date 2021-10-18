package com.noetic.client.handlers;

import com.noetic.client.models.GameCharacter;
import com.noetic.client.models.WorldCharacter;
import com.noetic.client.models.WorldCharacterMP;
import com.noetic.client.tiled.TiledMap;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

public class GameDataHandler {

    private static TiledMap map;
    private static List<GameCharacter> characters;
    public static GameCharacter characterInUse;
    public static WorldCharacter player;
    public static List<WorldCharacterMP> players = new ArrayList<>();;
    public static int worldPort = 6771;
    public static String accountName;

    public static List<GameCharacter> getCharacters() {
        return characters;
    }

    public static void setCharacters(List<GameCharacter> characters) {
        GameDataHandler.characters = characters;
    }

    public static TiledMap getMap() {
        return map;
    }

    public static void setMap(TiledMap map) {
        GameDataHandler.map = map;
    }
}
