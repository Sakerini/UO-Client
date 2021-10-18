package com.noetic.client.states;

import com.noetic.client.UODisplay;
import com.noetic.client.UOEngine;
import com.noetic.client.handlers.GameDataHandler;
import com.noetic.client.models.WorldCharacterMP;
import com.noetic.client.models.geometry.Camera;

import java.awt.*;

public class GameState extends State{
    public static final int ID = 4;

    private Camera camera;

    public GameState() {
        super(ID);
    }

    @Override
    public void init(UODisplay display) {
        camera = new Camera();
    }

    @Override
    public int getId() {
        return ID;
    }

    @Override
    public void tick(UOEngine engine, UODisplay display, double delta) {
        GameDataHandler.player.tick(engine, display, delta);
        for (WorldCharacterMP player : GameDataHandler.players) {
            player.tick(engine, display, delta);
        }
    }

    @Override
    public void render(UOEngine engine, UODisplay display, Graphics2D graphics) {
        camera.translate(GameDataHandler.player.getX(), GameDataHandler.player.getY(), graphics);
        GameDataHandler.getMap().level.render(graphics);
        GameDataHandler.player.render(engine, display, graphics);
        for (WorldCharacterMP player : GameDataHandler.players) {
            if (player.animationsInitialized())
                player.render(engine, display, graphics);
        }
    }

    @Override
    public void OnStateTransition(UODisplay display) {

    }
}
