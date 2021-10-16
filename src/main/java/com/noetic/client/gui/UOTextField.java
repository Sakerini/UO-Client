package com.noetic.client.gui;

import com.noetic.client.UODisplay;
import com.noetic.client.UOEngine;
import com.noetic.client.utils.Drawer;
import lombok.Data;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.RoundRectangle2D;
import java.util.Objects;

@Data
public class UOTextField implements UOWidget, KeyListener {

    private Color borderColor;
    private Color backgroundColor;
    private Color foregroundColor;

    private int x, y;
    private int width, height;
    private RoundRectangle2D.Double bounds;

    private boolean focused = false;
    private boolean enabled = true;

    private char echoChar;
    private String text = "", passwordText = "";
    private int cursorPosition = 0;

    public UOTextField(UODisplay display, int width, int height, int arcW, int arcH) {
        display.addKeyListener(this);
        this.width = width;
        this.height = height;
        bounds = new RoundRectangle2D.Double(0, 0, width, height, arcW, arcH);
    }

    @Override
    public void tick(UOEngine engine, UODisplay display, double delta) {

    }

    @Override
    public void render(UOEngine engine, UODisplay display, Graphics2D graphics) {
        //todo set font
        if (Objects.nonNull(backgroundColor))
            graphics.setColor(backgroundColor);
        else
            graphics.setColor(new Color(255, 255, 255, 0));
        graphics.fill(bounds);

        int localCursorPosition = graphics.getFontMetrics().stringWidth(text.substring(0, cursorPosition));
        int translateX = 0;

        if (localCursorPosition > width - 1) {
            translateX = width - localCursorPosition - graphics.getFontMetrics().stringWidth("|");
        }

        Rectangle oldClip = (Rectangle) graphics.getClip();
        graphics.setClip(bounds);
        graphics.translate(translateX + 2, 0);
        if (Objects.nonNull(foregroundColor))
            graphics.setColor(foregroundColor);
        else
            graphics.setColor(Color.BLACK);
        Drawer.drawString(text, x, y + 4, graphics);
        if (focused)
            Drawer.drawString("|", x+localCursorPosition, y + 4, graphics);

        graphics.translate(-translateX - 2, 0);
        graphics.setClip(oldClip);

        /** Set the border color of the textfield if it is not null. **/
        if (borderColor != null)
            graphics.setColor(borderColor);
        else
            graphics.setColor(new Color(255, 255, 255, 0));
        graphics.draw(bounds);
    }

    @Override
    public void setLocation(int x, int y) {
        this.x = x;
        this.y = y;
        bounds.x = x;
        bounds.y = y;
    }

    public void setText(String text) {
        this.text = text;
        if (cursorPosition > text.length())
            cursorPosition = text.length();
        if (echoChar != 0)
            this.passwordText = text;
    }

    public String getText() {
        if (echoChar != 0) {
            return passwordText.trim();
        } else
            return text.trim();
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent event) {
        if (focused && enabled) {
            int keyCode = event.getKeyCode();

            /** These display a "?" symbol. **/
            if (keyCode == KeyEvent.VK_TAB || keyCode == KeyEvent.VK_CAPS_LOCK
                    || keyCode == KeyEvent.VK_SHIFT || keyCode == KeyEvent.VK_ENTER
                    || keyCode == KeyEvent.VK_CONTROL || keyCode == KeyEvent.VK_ALT)
                return;

            if (keyCode == KeyEvent.VK_BACK_SPACE) {
                if (cursorPosition > 0 && text.length() > 0) {
                    /** Remove characters from the main text. **/
                    text = text.substring(0, cursorPosition - 1) + text.substring(cursorPosition);

                    /** Remove characters from the passwordText as well if there is some to remove. **/
                    if (passwordText.length() > 0) {
                        passwordText = passwordText.substring(0, cursorPosition - 1) + passwordText.substring(cursorPosition);
                    }
                    cursorPosition--;
                }
                return;
            }

            /** If we are using an echo character, append the text with that character but store the real text in a field. **/
            if (echoChar != 0) {
                text += echoChar;
                passwordText += event.getKeyChar();
            } else
                text += event.getKeyChar();
            cursorPosition++;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
