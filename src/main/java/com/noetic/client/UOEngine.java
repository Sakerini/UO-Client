package com.noetic.client;

import java.util.logging.Level;
import java.util.logging.Logger;

public class UOEngine implements Runnable{

    public static final int FPS = 60;
    private boolean isRunning = false;
    private double delta;
    private int renderedFps;

    @Override
    public void run() {
        isRunning = true;

        Logger.getLogger("server").log(Level.INFO, "Engine is started");
        long lastTime = System.nanoTime();
        double nsPerTick = 1000000000D / FPS;

        int ticks = 0;
        int frames = 0;

        long lastTimer = System.currentTimeMillis();

        while (isRunning) {
            long now = System.nanoTime();
            delta += (now - lastTime) / nsPerTick;
            lastTime = now;
            boolean shouldRender = false;

            while (delta >= 1) {
                ticks++;
                tick();
                delta -= 1;
                shouldRender = true;
            }

            /*
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
             */

            if (shouldRender) {
                frames++;
                render();
            }

            if (System.currentTimeMillis() - lastTimer >= 1000) {
                lastTimer += 1000;
                renderedFps = frames;
                frames = 0;
                ticks = 0;

                Logger.getLogger("server").log(Level.INFO, "Rendered frames {0}", renderedFps);
            }
        }
    }

    /**
     * Run game ticks.
     */
    private void tick() {

    }

    private void render() {
    }
}
