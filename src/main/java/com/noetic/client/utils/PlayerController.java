package com.noetic.client.utils;

import com.noetic.client.enums.Key;
import com.noetic.client.handlers.InputHandler;
import com.noetic.client.models.WorldCharacter;

import java.awt.event.KeyEvent;

public class PlayerController {

    private WorldCharacter player;

    public PlayerController(WorldCharacter player) {
        this.player = player;
    }

    public void tick(InputHandler input) {
        for (Key key : Key.values()) {
            if (input.isKeyDown(key.keyCode)) {
                key.isDown = true;
            } else {
                key.isDown = false;
            }
        }

        for (Key key : Key.values()) {
            if (key.isDown) {
                switch (key.key) {
                    case 'w':
                        player.setMovingUp(true);
                        break;
                    case 's':
                        player.setMovingDown(true);
                        break;
                    case 'a':
                        player.setMovingLeft(true);
                        break;
                    case 'd':
                        player.setMovingRight(true);
                        break;
                }
            } else {
                switch (key.key) {
                    case 'w':
                        player.setMovingUp(false);
                        break;
                    case 's':
                        player.setMovingDown(false);
                        break;
                    case 'a':
                        player.setMovingLeft(false);
                        break;
                    case 'd':
                        player.setMovingRight(false);
                        break;
                }
            }
        }
    }
}
