package com.noetic.client.gfx;

import com.noetic.client.enums.GenderType;
import lombok.Getter;
import lombok.Setter;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.IOException;

@Getter
@Setter
public class Spritesheet {

    private BufferedImage spritesheetImage;

    public Spritesheet(String src) {
        try {
            spritesheetImage = ImageIO.read(getClass().getResourceAsStream(src));
        } catch (FileNotFoundException ex) {
            System.err.println("Unable to find the spritesheet file.");
        } catch (IOException ex) {
            System.out.println("Unable to read from the given directory:");
            ex.printStackTrace();
        }
    }

    public BufferedImage getSubImage(int x, int y, int width, int height) {
        return spritesheetImage.getSubimage(x * width, y * height, width, height);
    }

    public static BufferedImage getSpriteImage(GenderType gender, int x, int y) {
        Spritesheet allSprites = gender.getSpritesheet();
        int width = allSprites.getSpritesheetImage().getWidth() / 4;
        int height = allSprites.getSpritesheetImage().getHeight() / 2;
        return allSprites.getSubImage(x, y, width, height);
    }

    public static BufferedImage getSinglePositionSprite(BufferedImage image, int x, int y) {
        int width = image.getWidth() / 3;
        int height = image.getHeight() / 4;
        return image.getSubimage(x * width, y * height, width, height);
    }
}
