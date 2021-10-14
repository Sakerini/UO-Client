package com.noetic.client;

import com.noetic.client.states.LoginScreenState;
import com.noetic.client.states.State;
import com.noetic.client.utils.Configuration;
import lombok.Getter;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

@Getter
public class UODisplay {
    private final String title = Configuration.appTitle;
    private final String version = Configuration.appVersion;

    private final JFrame frame;
    private final Canvas canvas;
    private Font font;

    private int width = Configuration.windowWidth;
    private int height = Configuration.windowHeight;
    private Dimension dimension;

    private List<State> states = new ArrayList<>();
    private State activeState;
    private int intializedStates = 0;

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

        addState(new LoginScreenState());
        activeState = states.get(0);
        frame.setVisible(true);
    }

    private void addState(State state) {
        for (State s : states) {
            if (s.getId() == state.getId()) {
                System.err.println("Unable to add game-state: already exists.");
                return;
            }
        }
        states.add(state);
    }

    public void initStatesOnNewThread() {
        do {
            for (State state : states) {
                state.init(this);
                intializedStates++;
            }
        } while (intializedStates != states.size());
    }

    public boolean haveStatesInitialized() {
        return intializedStates == states.size();
    }
}
