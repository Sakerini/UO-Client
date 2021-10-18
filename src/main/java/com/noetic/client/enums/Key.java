package com.noetic.client.enums;

import java.awt.event.KeyEvent;

public enum Key {
    Move_Up(KeyEvent.VK_W, 'w'),
    Move_Down(KeyEvent.VK_S, 's'),
    Move_Left(KeyEvent.VK_A, 'a'),
    Move_Right(KeyEvent.VK_D, 'd');

    public int keyCode;
    public char key;
    public boolean isDown;

    Key(int keyCode, char key) {
        this.keyCode = keyCode;
        this.key = key;
    }
}
