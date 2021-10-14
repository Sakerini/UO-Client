package com.noetic.client.states;

import com.noetic.client.UODisplay;
import com.noetic.client.UOEngine;

import java.awt.*;

public abstract class State {

    protected int id;

    public State(int id) {
        this.id = id;
    }

    public abstract void init(UODisplay display);

    public abstract int getId();

    public abstract void tick(UOEngine engine, UODisplay display, double delta);

    public abstract void render(UOEngine engine, UODisplay display, Graphics2D graphics);

    public abstract void OnStateTransition(UODisplay display);
}
