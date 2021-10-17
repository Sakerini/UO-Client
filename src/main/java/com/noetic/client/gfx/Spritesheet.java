package com.noetic.client.gfx;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Spritesheet {

    private BufferedImage spritesheet;

    public Spritesheet(String src) {
        try {
            spritesheet = ImageIO.read(getClass().getResourceAsStream(src));
        } catch (FileNotFoundException ex) {
            System.err.println("Unable to find the spritesheet file.");
        } catch (IOException ex) {
            System.out.println("Unable to read from the given directory:");
            ex.printStackTrace();
        }
    }

    public BufferedImage getSubImage(int x, int y, int width, int height) {
        return spritesheet.getSubimage(x * width, y * height, width, height);
    }
}
