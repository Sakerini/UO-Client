package com.noetic.client;

import lombok.Getter;

import javax.swing.*;
import java.awt.*;

@Getter
public class UODisplay {
    private final String title = "UO";
    private final String version = "v0.0.1 (development)";

    private JFrame frame;
    private Canvas canvas;
    private Font font;

    private int width = 1280;
    private int height = 720;
    private Dimension dimension;

    public UODisplay(Canvas canvas) {
        this.canvas = canvas;
        dimension = new Dimension(width, height);
        canvas.setMaximumSize(dimension);
        canvas.setMinimumSize(dimension);
        canvas.setPreferredSize(dimension);
        frame = new JFrame(title);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new CardLayout());
        frame.add(canvas);
        frame.pack();

        //todo set icon
        //todo set font

        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        //todo set input listeners to canvas
        canvas.requestFocus();
        frame.setVisible(true);
    }


}
