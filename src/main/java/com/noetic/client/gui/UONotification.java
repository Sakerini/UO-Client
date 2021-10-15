package com.noetic.client.gui;

import com.noetic.client.UODisplay;
import com.noetic.client.UOEngine;
import com.noetic.client.utils.Drawer;
import lombok.Getter;
import lombok.Setter;

import java.awt.*;
import java.awt.geom.Rectangle2D;

@Getter
@Setter
public class UONotification implements UOWidget {

    private String message;
    private Rectangle2D.Double box;

    private int x, y;
    private int width, height;

    public UONotification(String message) {
        this.message = message;
        this.box = new Rectangle2D.Double(0, 0, 450, 120);
        width = (int) box.getWidth();
        height = (int) box.getHeight();
    }

    @Override
    public void tick(UOEngine engine, UODisplay display, double delta) {

    }

    @Override
    public void render(UOEngine engine, UODisplay display, Graphics2D graphics) {
        //todo set font

        graphics.setColor(new Color(0, 0, 0, 235));
        graphics.fill(box);
        graphics.setColor(Color.gray);
        graphics.draw(box);

        graphics.setColor(new Color(223, 195, 15));
        Drawer.drawCenteredString(message, x, y, width, height, graphics);
    }

    @Override
    public void setLocation(int x, int y) {
        this.x = x;
        this.y = y;
        box.x = x;
        box.y = y;
    }
}
