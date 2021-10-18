package com.noetic.client.models.geometry;

import lombok.Getter;
import lombok.Setter;

import java.awt.image.BufferedImage;

@Getter
@Setter
public class Frame {
    private BufferedImage frameImage;
    private int frameDuration;

    public Frame(BufferedImage frameImage, int frameDuration) {
        this.frameImage = frameImage;
        this.frameDuration = frameDuration;
    }
}
