package com.noetic.client.handlers;

import com.noetic.client.enums.KeyState;

import java.awt.*;
import java.awt.event.*;

public class InputHandler implements KeyListener, MouseListener, MouseMotionListener {
    private final int KEY_COUNT = 256;
    private boolean[] currentKeys;
    private KeyState[] keys;

    private final int BUTTON_COUNT = 3;
    public static final int MOUSE_LEFT = 1;
    public static final int MOUSE_MIDDLE = 2;
    public static final int MOUSE_RIGHT = 3;


    private Point polledMousePosition;
    private Point currentMousePosition;

    private boolean[] state = null;
    private KeyState[] poll = null;

    public InputHandler() {
        currentKeys = new boolean[KEY_COUNT];
        keys = new KeyState[KEY_COUNT];
        for(int i = 0; i < KEY_COUNT; i++) {
            keys[i] = KeyState.RELEASED;
        }

        polledMousePosition = new Point(0, 0);
        currentMousePosition = new Point(0, 0);

        state = new boolean[BUTTON_COUNT];
        poll = new KeyState[BUTTON_COUNT];
        for (int i = 0; i < BUTTON_COUNT; i++) {
            poll[i] = KeyState.RELEASED;
        }
    }

    /**
     * Poll the keys every frame or "tick".
     */
    public synchronized void poll() {
        for (int i = 0; i < KEY_COUNT; i++) {
            if (currentKeys[i]) {
                if (keys[i] == KeyState.RELEASED)
                    keys[i] = KeyState.ONCE;
                else
                    keys[i] = KeyState.PRESSED;
            } else {
                keys[i] = KeyState.RELEASED;
            }
        }

        polledMousePosition = new Point(currentMousePosition);
        for (int i = 0; i < BUTTON_COUNT; i++) {
            if (state[i]) {
                if (poll[i] == KeyState.RELEASED)
                    poll[i] = KeyState.ONCE;
                else
                    poll[i] = KeyState.PRESSED;
            } else {
                poll[i] = KeyState.RELEASED;
            }
        }
    }


    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public synchronized void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if (keyCode >= 0 && keyCode < KEY_COUNT) {
            currentKeys[keyCode] = true;
        }
    }

    @Override
    public synchronized void keyReleased(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if (keyCode >= 0 && keyCode < KEY_COUNT) {
            currentKeys[keyCode] = false;
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public synchronized void mousePressed(MouseEvent e) {
        state[e.getButton()-1] = true;
    }

    @Override
    public synchronized void mouseReleased(MouseEvent e) {
        state[e.getButton()-1] = false;
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public synchronized void mouseMoved(MouseEvent e) {
        currentMousePosition = e.getPoint();
    }

    public boolean isKeyDown(int keyCode) {
        return keys[keyCode] == KeyState.ONCE || keys[keyCode] == KeyState.PRESSED;
    }

    public boolean isKeyPressed(int keyCode) {
        return keys[keyCode] == KeyState.ONCE;
    }

    public boolean isMouseButtonPressed(int button) {
        return poll[button-1] == KeyState.ONCE;
    }

    public boolean isMouseButtonDown(int button) {
        return poll[button-1] == KeyState.ONCE || poll[button-1] == KeyState.PRESSED;
    }

    public Point getMousePosition() {
        return polledMousePosition;
    }
}
