package com.noetic.client.enums;

import com.noetic.client.gfx.Spritesheet;

public enum GenderType {
    Male(1, new Spritesheet("/sprites/player/male.png")),
    Female(2, new Spritesheet("/sprites/player/female.png"));

    private int id;
    private Spritesheet spritesheet;

    GenderType(int id, Spritesheet spritesheet) {
        this.id = id;
        this.spritesheet = spritesheet;
    }

    public int getId() {
        return id;
    }

    public Spritesheet getSpritesheet() {
        return spritesheet;
    }
}
