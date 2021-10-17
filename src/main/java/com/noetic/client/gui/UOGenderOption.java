package com.noetic.client.gui;

import com.noetic.client.UODisplay;
import com.noetic.client.UOEngine;
import com.noetic.client.enums.GenderType;
import com.noetic.client.handlers.InputHandler;
import com.noetic.client.utils.Drawer;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;

@Getter
@Setter
public class UOGenderOption implements UOWidget {

    private final int WIDTH = 16;
    private final int HEIGHT = 32;

    private GenderType gender;
    private BufferedImage icon;
    private RoundRectangle2D.Double iconBorder;
    private RoundRectangle2D.Double toolTip;

    private int x, y;
    private boolean hovered = false;
    private boolean selected = false;

    @SneakyThrows
    public UOGenderOption(GenderType gender) {
        this.gender = gender;
        if (gender == GenderType.Male)
            icon = ImageIO.read(getClass().getResourceAsStream("/sprites/player/male_icon.png"));
        else if (gender == GenderType.Female)
            icon = ImageIO.read(getClass().getResourceAsStream("/sprites/player/female_icon.png"));
        iconBorder = new RoundRectangle2D.Double(0, 0, WIDTH + 3, HEIGHT + 3, 4, 10);
    }

    @Override
    public void tick(UOEngine engine, UODisplay display, double delta) {
        InputHandler input = display.getInput();

        Point mousePos = input.getMousePosition();
        if (iconBorder.contains(mousePos)) {
            hovered = true;
        } else {
            hovered = false;
        }
    }

    @Override
    public void render(UOEngine engine, UODisplay display, Graphics2D graphics) {
        Drawer.drawImage(icon, (int)(iconBorder.x + (iconBorder.width / 2 - WIDTH / 2)) + 1,
                (int)(iconBorder.y + (iconBorder.height / 2 - HEIGHT / 2)) + 1, 16, 32, graphics);

        InputHandler input = display.getInput();
        Point mousePos = input.getMousePosition();

        if (hovered) {
            graphics.setColor(Color.black);
        } else
            graphics.setColor(Color.gray);

        if (selected)
            graphics.setColor(Color.black);
        graphics.draw(iconBorder);

        if (hovered) {
            toolTip = new RoundRectangle2D.Double(0, 0,
                    graphics.getFontMetrics().stringWidth(gender.name()), graphics.getFontMetrics().getHeight(), 1, 1);
            toolTip.x = mousePos.x;
            toolTip.y = mousePos.y - toolTip.height;
            graphics.setColor(Color.darkGray);
            graphics.fill(toolTip);

            graphics.setColor(Color.white);
            Drawer.drawString(gender.name(), (float)toolTip.x, (float)toolTip.y, graphics);
        } else {
            toolTip = null;
        }
    }

    @Override
    public void setLocation(int x, int y) {
        this.x = x;
        this.y = y;
        iconBorder.x = x;
        iconBorder.y = y;
    }
    public RoundRectangle2D.Double getBorder() {
        return iconBorder;
    }
    public GenderType getGender() {
        return gender;
    }

}
