package com.noetic.client.gfx;

import com.noetic.client.models.geometry.Frame;
import com.noetic.client.utils.Drawer;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Animation {
    private ArrayList<Frame> frames;
    private Frame currentFrame;
    private int frameIndex = 0;

    private int tickCounter = 0;

    public Animation() {
        frames = new ArrayList<>();
    }

    public void addFrame(BufferedImage img, int duration) {
        Frame newFrame = new Frame(img, duration);
        frames.add(newFrame);
    }

    public void stop() {
        currentFrame = frames.get(0);
    }

    public void tick(double delta) {
        if (currentFrame == null)
            currentFrame = frames.get(0);
        tickCounter += 1 * delta; /* Update based on the delta to help avoid lag with a constant number. */
        if (tickCounter >= currentFrame.getFrameDuration()) {
            if (frameIndex >= frames.size()-1) {
                frameIndex = 0; /* Restart the animation if we've reached the end of the list of frames. */
            } else
                frameIndex++;
            tickCounter = 0;
            currentFrame = frames.get(frameIndex);
        }
    }

    public void render(Graphics2D graphics, float x, float y) {
        Drawer.drawImage(currentFrame.getFrameImage(), x, y, graphics);
    }
}
