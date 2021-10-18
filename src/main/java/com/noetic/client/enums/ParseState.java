package com.noetic.client.enums;

public enum ParseState {
    Waiting(0.0f),
    MapData(0.3f),
    Tilesets(0.6f),
    Layers(0.9f),
    Finished(1.0f);

    private float percent;

    ParseState(float percent) {
        this.percent = percent;
    }

    public float getPercent() {
        return percent;
    }
}
