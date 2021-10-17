package com.noetic.client;

import com.noetic.client.handlers.NotificationHandler;
import com.noetic.client.utils.NetworkUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UOEngine implements Runnable {

    public static final int FPS = 60;
    private boolean isRunning = false;
    private double delta;
    private int renderedFps;

    private Canvas canvas;
    private UODisplay display;
    private Thread thread;

    public UOEngine() {
        canvas = new Canvas();
        display = new UODisplay(canvas);

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                display.initStatesOnNewThread();
            }
        });

    }

    @Override
    public synchronized void run() {
        isRunning = true;
        thread = new Thread(this, UODisplay.title + "_thread");
        thread.start();
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
        display.getInput().poll();

        if (display.haveStatesInitialized()) {
            display.getActiveState().tick(this, display,delta);
        }
    }

    private void render() {
        BufferStrategy bs = canvas.getBufferStrategy();
        if (Objects.isNull(bs)) {
            canvas.createBufferStrategy(3);
            return;
        }

        Graphics2D graphics = (Graphics2D) bs.getDrawGraphics();
        RenderingHints rh =
                new RenderingHints(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);

        rh.put(RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_QUALITY);
        graphics.setRenderingHints(rh);

        graphics.setColor(Color.WHITE);
        graphics.fillRect(0, 0, display.getWidth(), display.getHeight());

        /** Make sure we've initialized states before trying to render. **/
        if (display.haveStatesInitialized())
            display.getActiveState().render(this, display, graphics);

        NotificationHandler.handle(display.getActiveState().getId(), this, display, graphics);

        graphics.dispose();
        bs.show();
    }
}
