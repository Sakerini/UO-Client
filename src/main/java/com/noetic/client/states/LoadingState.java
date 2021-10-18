package com.noetic.client.states;

import com.noetic.client.UODisplay;
import com.noetic.client.UOEngine;
import com.noetic.client.enums.ParseState;
import com.noetic.client.enums.WorldStatus;
import com.noetic.client.handlers.GameDataHandler;
import com.noetic.client.network.connections.WorldConnection;
import com.noetic.client.tiled.TiledMap;

import java.awt.*;
import java.util.Objects;

public class LoadingState extends State{

    public static final int ID = 3;
    private Rectangle progressBar;

    public LoadingState() {
        super(ID);


    }

    @Override
    public void init(UODisplay display) {
        progressBar = new Rectangle(0, 0, 400, 25);
        progressBar.x = (int) (display.getWidth() / 2 - progressBar.getWidth() / 2);
        progressBar.y = (int) (display.getHeight() / 2 - progressBar.getHeight() / 2);
    }

    @Override
    public int getId() {
        return ID;
    }

    @Override
    public void tick(UOEngine engine, UODisplay display, double delta) {
        if (Objects.nonNull(GameDataHandler.getMap())) {
            GameDataHandler.getMap().start();
        }

        if (GameDataHandler.getMap().state.equals(ParseState.Finished)) {
            System.out.println(WorldConnection.getStatus());
            if (WorldConnection.getStatus().equals(WorldStatus.World)) {
                if (GameDataHandler.player.animationsInitialized()) {
                    display.enterState(GameState.ID);
                }

            }
        }
    }

    @Override
    public void render(UOEngine engine, UODisplay display, Graphics2D graphics) {
        graphics.setColor(Color.blue);
        graphics.fillRect(progressBar.x, progressBar.y,
                (int) (progressBar.width * GameDataHandler.getMap().state.getPercent()), progressBar.height);
    }

    @Override
    public void OnStateTransition(UODisplay display) {
        if (Objects.isNull(GameDataHandler.getMap())) {
            if (display.getActiveState() == this) {
                GameDataHandler.setMap(new TiledMap(GameDataHandler.characterInUse.getZone().getFile()));
            }
        }
    }
}
