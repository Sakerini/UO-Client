package com.noetic.client.gui;

import com.noetic.client.UODisplay;
import com.noetic.client.UOEngine;
import com.noetic.client.handlers.InputHandler;
import com.noetic.client.utils.Drawer;
import lombok.Getter;
import lombok.Setter;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.IOException;

@Getter
@Setter
public class UOButton implements UOWidget {

    private BufferedImage enabledButtonImage;
    private BufferedImage disabledButtonImage;

    private String text;
    private int x, y;
    private int width, height;

    private Rectangle bounds;

    private boolean hovering = false;
    private boolean enabled = true;

    private ActionListener actionListener;

    public UOButton(String text) {
        try {
            enabledButtonImage = ImageIO.read(getClass().getResourceAsStream("/ui/button.png"));
            disabledButtonImage = ImageIO.read(getClass().getResourceAsStream("/ui/button_disabled.png"));
        } catch (FileNotFoundException ex) {
            System.err.println("Could not find the button image specified.");
        } catch (IOException ex) {
            System.err.println("Unable to read the button image path: "+ex.getMessage());
        }
        width = enabledButtonImage.getWidth();
        height = enabledButtonImage.getHeight();
        this.text = text;
    }

    @Override
    public void tick(UOEngine engine, UODisplay display, double delta) {
        InputHandler input = display.getInput();

        if (enabled) {
            if (bounds.contains(input.getMousePosition())) {
                hovering = true;
                if (input.isMouseButtonPressed(InputHandler.MOUSE_LEFT)) {
                    fireAction();
                }
            } else hovering = false;
        }
    }

    private void fireAction() {
        try {
            ActionEvent ae = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "");
            actionListener.actionPerformed(ae);
        } catch (NullPointerException ex) {
            System.err.println("This button does not have an action.");
        }
    }

    @Override
    public void render(UOEngine engine, UODisplay display, Graphics2D graphics) {
        //todo set fonts
        if (enabled)
            Drawer.drawImage(enabledButtonImage, x, y, width, height, graphics);
        else
            Drawer.drawImage(disabledButtonImage, x, y, width, height, graphics);

        if (enabled) {
            if (hovering)
                graphics.setColor(Color.WHITE);
            else
                graphics.setColor(new Color(223, 195, 15));
        } else
            graphics.setColor(Color.gray);
        Drawer.drawCenteredString(text, x, y, width, height, graphics);
    }

    @Override
    public void setLocation(int x, int y) {
        this.x = x;
        this.y = y;

        bounds = new Rectangle(x, y, width, height);
    }

    public void addActionListener(ActionListener actionListener) {
        this.actionListener = actionListener;
    }
}
