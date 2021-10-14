package com.noetic.client.utils;

import java.awt.*;
import java.awt.geom.AffineTransform;

public class Drawer {

    public static void drawString(String str, float x, float y, Graphics2D graphics) {
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics.drawString(str, x, graphics.getFontMetrics().getAscent() + y);
    }

    public static void drawCenteredString(String str, int x, int y, int boundsX, int boundsY, Graphics2D graphics) {
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics.drawString(str, x + (boundsX / 2 - graphics.getFontMetrics().stringWidth(str) / 2), y + graphics.getFontMetrics().getAscent() + (boundsY / 2 - graphics.getFontMetrics().getHeight() / 2));
    }

    public static void drawImage(Image img, int x, int y, int width, int height, Graphics2D graphics) {
        graphics.drawImage(img, x, y, width, height, null);
    }

    public static void drawImage(Image img, float x, float y, Graphics2D graphics) {
        AffineTransform xform = new AffineTransform();
        xform.translate(x, y);
        graphics.drawImage(img, xform, null);
    }

    public static String BytesToHex(byte[] ref) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : ref) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }
}
