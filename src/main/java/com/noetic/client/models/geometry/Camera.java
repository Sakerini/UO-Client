package com.noetic.client.models.geometry;

import java.awt.*;

public class Camera {
    private float subX, subY;

    public void translate(float x, float y, Graphics2D graphics) {
        subX = -x + (1280 / 2) - 16;
        subY = -y + (720 / 2) - 16;

        graphics.translate(subX, subY);
    }
}
