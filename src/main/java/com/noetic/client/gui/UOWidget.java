package com.noetic.client.gui;

import com.noetic.client.UODisplay;
import com.noetic.client.UOEngine;

import java.awt.*;

public interface UOWidget {
    void tick(UOEngine engine, UODisplay display, double delta);
    void render(UOEngine engine, UODisplay display, Graphics2D graphics);
    void setLocation(int x, int y);
}
