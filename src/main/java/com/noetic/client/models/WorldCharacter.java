package com.noetic.client.models;

import com.noetic.client.UODisplay;
import com.noetic.client.UOEngine;
import com.noetic.client.enums.GenderType;
import com.noetic.client.gfx.Animation;
import com.noetic.client.gfx.Spritesheet;
import com.noetic.client.utils.NetworkUtil;
import com.noetic.client.utils.PlayerController;

import java.awt.*;
import java.awt.image.BufferedImage;

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
        BufferedImage genderSprite = Spritesheet.getSpriteImage(gender, 0, 0);
        northAnimation.addFrame(Spritesheet.getSinglePositionSprite(genderSprite, 1, 3), 12);
        northAnimation.addFrame(Spritesheet.getSinglePositionSprite(genderSprite, 2, 3), 12);
        northAnimation.addFrame(Spritesheet.getSinglePositionSprite(genderSprite, 1, 3), 12);
        northAnimation.addFrame(Spritesheet.getSinglePositionSprite(genderSprite, 0, 3), 12);

        eastAnimation = new Animation();
        eastAnimation.addFrame(Spritesheet.getSinglePositionSprite(genderSprite, 1, 2), 12);
        eastAnimation.addFrame(Spritesheet.getSinglePositionSprite(genderSprite, 2, 2), 12);
        eastAnimation.addFrame(Spritesheet.getSinglePositionSprite(genderSprite, 1, 2), 12);
        eastAnimation.addFrame(Spritesheet.getSinglePositionSprite(genderSprite, 0, 2), 12);

        southAnimation = new Animation();
        southAnimation.addFrame(Spritesheet.getSinglePositionSprite(genderSprite, 1, 0), 12);
        southAnimation.addFrame(Spritesheet.getSinglePositionSprite(genderSprite, 2, 0), 12);
        southAnimation.addFrame(Spritesheet.getSinglePositionSprite(genderSprite, 1, 0), 12);
        southAnimation.addFrame(Spritesheet.getSinglePositionSprite(genderSprite, 0, 0), 12);

        westAnimation = new Animation();
        westAnimation.addFrame(Spritesheet.getSinglePositionSprite(genderSprite, 1, 1), 12);
        westAnimation.addFrame(Spritesheet.getSinglePositionSprite(genderSprite, 2, 1), 12);
        westAnimation.addFrame(Spritesheet.getSinglePositionSprite(genderSprite, 1, 1), 12);
        westAnimation.addFrame(Spritesheet.getSinglePositionSprite(genderSprite, 0, 1), 12);
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
