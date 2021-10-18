package com.noetic.client.models;

import com.noetic.client.UODisplay;
import com.noetic.client.UOEngine;
import com.noetic.client.enums.GenderType;
import com.noetic.client.enums.Zone;
import com.noetic.client.gfx.Animation;

import java.awt.*;

public abstract class Player {

    public enum Direction {
        North(0),
        South(1),
        East(2),
        West(3),
        North_East(4),
        South_East(5),
        South_West(6),
        North_West(7);

        private int id;

        Direction(int id) {
            this.id = id;
        }

        public int getDirection() {
            return id;
        }
    }

    protected GenderType gender;
    private String name;

    protected Animation northAnimation;
    protected Animation eastAnimation;
    protected Animation southAnimation;
    protected Animation westAnimation;

    protected float x, y;

    protected boolean isMovingUp, isMovingDown, isMovingLeft, isMovingRight;
    protected Direction direction = Direction.North;

    protected Zone zone;

    public Player(GenderType gender, String name) {
        this.gender = gender;
        this.name = name;

        initAnimations();
    }

    public abstract void initAnimations();
    public abstract void tick(UOEngine engine, UODisplay display, double delta);
    public abstract void render(UOEngine engine, UODisplay display, Graphics2D graphics);

    /**
     * Called every tick.
     */
    public abstract void OnMove();

    public void spawn(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public void setDirection(int direction) {
        for (Direction d : Direction.values()) {
            if (d.id == direction) {
                this.direction = d;
            }
        }
    }

    public void setZone(int zone) {
        for (Zone z : Zone.values()) {
            if (z.getId() == zone) {
                this.zone = z;
            }
        }
    }

    /** Begin player-movement variables. **/
    public void setMovingUp(boolean isMovingUp) {
        if (!isMovingDown)
            this.isMovingUp = isMovingUp;
    }

    public void setMovingDown(boolean isMovingDown) {
        if (!isMovingUp)
            this.isMovingDown = isMovingDown;
    }

    public void setMovingLeft(boolean isMovingLeft) {
        if (!isMovingRight)
            this.isMovingLeft = isMovingLeft;
    }

    public void setMovingRight(boolean isMovingRight) {
        if (!isMovingLeft)
            this.isMovingRight = isMovingRight;
    }

    public void setX(float newX) {
        x = newX;
    }

    public void setY(float newY) {
        y = newY;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public GenderType getRaceType() {
        return gender;
    }

    public String getName() {
        return name;
    }

    public boolean animationsInitialized() {
        return (northAnimation != null && southAnimation != null && eastAnimation != null && westAnimation != null);
    }
}
