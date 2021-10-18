package com.noetic.client;

import com.noetic.client.handlers.InputHandler;
import com.noetic.client.states.*;
import com.noetic.client.utils.Configuration;
import lombok.Getter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;

@Getter
public class UODisplay {
    public final static String title = Configuration.appTitle;
    public final static String version = Configuration.appVersion;

    private final JFrame frame;
    private final Canvas canvas;
    private Font font;

    private int width = Configuration.windowWidth;
    private int height = Configuration.windowHeight;
    private Dimension dimension;

    private List<State> states = new ArrayList<>();
    private State activeState;
    private int intializedStates = 0;

    private InputHandler input;

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

        input = new InputHandler();
        canvas.addKeyListener(input);
        canvas.addMouseListener(input);
        canvas.addMouseMotionListener(input);
        canvas.setFocusTraversalKeysEnabled(false);
        canvas.requestFocus();

        addState(new LoginScreenState());
        addState(new CharacterSelectionState());
        addState(new CharacterCreationState());
        addState(new LoadingState());
        addState(new GameState());
        activeState = states.get(0);
        frame.setVisible(true);
    }

    public void addKeyListener(KeyListener listener) {
        canvas.addKeyListener(listener);
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

    public void enterState(int id) {
        for (State state : states) {
            if (state.getId() == id) {
                activeState = state;
                state.OnStateTransition(this);
                break;
            }
        }
        if (activeState == null || activeState.getId() != id)
            System.err.println("Unable to enter game-state with id: "+id+" - missing state.");
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

    public void exit() {
        System.exit(0);
    }
}
