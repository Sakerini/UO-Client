package com.noetic.client.states;

import com.noetic.client.UODisplay;
import com.noetic.client.UOEngine;
import com.noetic.client.utils.Drawer;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class LoginScreenState extends State {

    public static final int ID = 0;

    private BufferedImage background;

    public LoginScreenState() {
        super(ID);
    }

    @Override
    public int getId() {
        return ID;
    }

    @Override
    public void init(UODisplay display) {
        try {
            background = ImageIO.read(getClass().getResourceAsStream("/ui/login_screen_bg.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void tick(UOEngine engine, UODisplay display, double delta) {

    }

    @Override
    public void render(UOEngine engine, UODisplay display, Graphics2D graphics) {
        Drawer.drawImage(background, 0, 0, display.getWidth(), display.getHeight(), graphics);
    }

    @Override
    public void OnStateTransition(UODisplay display) {

    }
}
