package com.noetic.client.models;

import com.noetic.client.UODisplay;
import com.noetic.client.UOEngine;
import com.noetic.client.enums.GenderType;
import com.noetic.client.gfx.Animation;
import com.noetic.client.utils.NetworkUtil;
import com.noetic.client.utils.PlayerController;

import java.awt.*;

public class WorldCharacter extends Player {
    private PlayerController controller;
    private boolean hasSentIdle = false;

    public WorldCharacter(GenderType gender, String name) {
        super(gender, name);
        controller = new PlayerController(this);
    }

    @Override
    public void initAnimations() {
        northAnimation = new Animation();
        northAnimation.addFrame(gender.getSpritesheet().getSubImage(1, 3, 32, 32), 12);
        northAnimation.addFrame(gender.getSpritesheet().getSubImage(2, 3, 32, 32), 12);
        northAnimation.addFrame(gender.getSpritesheet().getSubImage(1, 3, 32, 32), 12);
        northAnimation.addFrame(gender.getSpritesheet().getSubImage(0, 3, 32, 32), 12);

        eastAnimation = new Animation();
        eastAnimation.addFrame(gender.getSpritesheet().getSubImage(1, 2, 32, 32), 12);
        eastAnimation.addFrame(gender.getSpritesheet().getSubImage(2, 2, 32, 32), 12);
        eastAnimation.addFrame(gender.getSpritesheet().getSubImage(1, 2, 32, 32), 12);
        eastAnimation.addFrame(gender.getSpritesheet().getSubImage(0, 2, 32, 32), 12);

        southAnimation = new Animation();
        southAnimation.addFrame(gender.getSpritesheet().getSubImage(1, 0, 32, 32), 12);
        southAnimation.addFrame(gender.getSpritesheet().getSubImage(2, 0, 32, 32), 12);
        southAnimation.addFrame(gender.getSpritesheet().getSubImage(1, 0, 32, 32), 12);
        southAnimation.addFrame(gender.getSpritesheet().getSubImage(0, 0, 32, 32), 12);

        westAnimation = new Animation();
        westAnimation.addFrame(gender.getSpritesheet().getSubImage(1, 1, 32, 32), 12);
        westAnimation.addFrame(gender.getSpritesheet().getSubImage(2, 1, 32, 32), 12);
        westAnimation.addFrame(gender.getSpritesheet().getSubImage(1, 1, 32, 32), 12);
        westAnimation.addFrame(gender.getSpritesheet().getSubImage(0, 1, 32, 32), 12);
    }

    @Override
    public void tick(UOEngine engine, UODisplay display, double delta) {
        controller.tick(display.getInput());

        if (isMovingUp)
            direction = Direction.North;

        if (isMovingRight) {
            direction = Direction.East;
            eastAnimation.tick(delta);
        }

        if (isMovingUp && isMovingRight)
            direction = Direction.North_East;

        if (isMovingUp || isMovingUp && isMovingRight || isMovingUp && isMovingLeft)
            northAnimation.tick(delta);

        if (isMovingDown)
            direction = Direction.South;

        if (isMovingDown && isMovingRight)
            direction = Direction.South_East;

        if (isMovingLeft) {
            direction = Direction.West;
            westAnimation.tick(delta);
        }

        if (isMovingUp && isMovingLeft)
            direction = Direction.North_West;

        if (isMovingDown && isMovingLeft)
            direction = Direction.South_West;

        if (isMovingDown || isMovingDown && isMovingRight || isMovingDown && isMovingLeft)
            southAnimation.tick(delta);

        OnMove();
    }

    @Override
    public void render(UOEngine engine, UODisplay display, Graphics2D graphics) {
        if (isMovingUp) {
            northAnimation.render(graphics, x, y);
        } else if (isMovingDown) {
            southAnimation.render(graphics, x, y);
        } else if (isMovingLeft) {
            westAnimation.render(graphics, x, y);
        } else if (isMovingRight) {
            eastAnimation.render(graphics, x, y);
        } else {
            switch (direction) {
                case North:
                case North_East:
                case North_West:
                    northAnimation.stop();
                    northAnimation.render(graphics, x, y);
                    break;
                case South:
                case South_East:
                case South_West:
                    southAnimation.stop();
                    southAnimation.render(graphics, x, y);
                    break;
                case East:
                    eastAnimation.stop();
                    eastAnimation.render(graphics, x, y);
                    break;
                case West:
                    westAnimation.stop();
                    westAnimation.render(graphics, x, y);
                    break;
            }
        }
    }

    @Override
    public void OnMove() {
        if (isMovingUp || isMovingDown || isMovingLeft || isMovingRight) {
            hasSentIdle = false;
            NetworkUtil.sendMovement(direction.getDirection(), true);
        } else {
            if (!hasSentIdle) {
                NetworkUtil.sendMovement(direction.getDirection(), false);
                hasSentIdle = true;
            }
        }
    }

}
