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
public class UONotificationConfirmation implements UOWidget {

    private String message;
    private Rectangle2D.Double box;
    private UOButton button;

    private int x, y;
    private int width, height;


    public UONotificationConfirmation(String message) {
        this.message = message;
        this.box = new Rectangle2D.Double(0, 0, 450, 120);
        this.button = new UOButton("Okay");
        width = (int) box.getWidth();
        height = (int) box.getHeight();
    }

    @Override
    public void tick(UOEngine engine, UODisplay display, double delta) {
        button.tick(engine, display, delta);
    }

    @Override
    public void render(UOEngine engine, UODisplay display, Graphics2D graphics) {
        //todo add font
        graphics.setColor(new Color(0, 0, 0, 235));
        graphics.fill(box);
        graphics.setColor(Color.gray);
        graphics.draw(box);

        graphics.setColor(new Color(223, 195, 15));
        if (message.contains("\n")) {
            String[] strSplit = message.split("\n");
            for (int i = 0; i < strSplit.length; i++) {
                String s = strSplit[i];
                if (i == 0)
                    Drawer.drawCenteredString(s, x, y - graphics.getFontMetrics().getAscent(), width, height, graphics);
                else
                    Drawer.drawCenteredString(s, x, y + (i * graphics.getFontMetrics().getAscent()) - graphics.getFontMetrics().getAscent(), getWidth(), getHeight(), graphics);
            }
        } else
            Drawer.drawCenteredString(message, x, y - graphics.getFontMetrics().getAscent(), width, height, graphics);

        button.render(engine, display, graphics);
    }

    @Override
    public void setLocation(int x, int y) {
        this.x = x;
        this.y = y;
        box.x = x;
        box.y = y;
        button.setLocation((int)box.x + width / 2 - button.getWidth() / 2, (int)box.y + height - button.getHeight() - 12);
    }
}
