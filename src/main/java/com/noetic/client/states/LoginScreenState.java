package com.noetic.client.states;

import com.noetic.client.UODisplay;
import com.noetic.client.UOEngine;
import com.noetic.client.gui.UOButton;
import com.noetic.client.gui.UOTextField;
import com.noetic.client.handlers.InputHandler;
import com.noetic.client.utils.Drawer;
import com.noetic.client.utils.NetworkUtil;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class LoginScreenState extends State {

    public static final int ID = 0;

    private BufferedImage background;
    private UOTextField usernameTextField;
    private UOTextField passwordTextField;
    private UOButton loginButton;
    private UOButton quitButton;

    public LoginScreenState() {
        super(ID);
    }

    @Override
    public int getId() {
        return ID;
    }

    @Override
    public void init(UODisplay display) {
        try {
            background = ImageIO.read(getClass().getResourceAsStream("/ui/login_screen_bg.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        usernameTextField = new UOTextField(display, 150, 25, 6, 6);
        usernameTextField.setBackgroundColor(new Color(0, 0, 0, 225));
        usernameTextField.setBorderColor(Color.gray);
        usernameTextField.setLocation(display.getWidth() / 2 - usernameTextField.getWidth() / 2, 340);
        usernameTextField.setFocused(true);

        passwordTextField = new UOTextField(display, 150, 25, 6, 6);
        passwordTextField.setBackgroundColor(new Color(0, 0, 0, 225));
        passwordTextField.setBorderColor(Color.gray);
        passwordTextField.setLocation(display.getWidth() / 2 - passwordTextField.getWidth() / 2, 440);
        passwordTextField.setEchoChar('*');

        loginButton = new UOButton("Login");
        loginButton.setLocation(display.getWidth() / 2 - loginButton.getWidth() / 2, 500);
        loginButton.addActionListener(e -> {
            usernameTextField.setFocused(false);
            passwordTextField.setFocused(false);
            String username = usernameTextField.getText().trim();
            String password = passwordTextField.getText().trim();
            usernameTextField.setText("");
            passwordTextField.setText("");

            NetworkUtil.authorize(username, password);
        });

        quitButton = new UOButton("Quit");
        quitButton.setLocation(display.getWidth() - quitButton.getWidth() - 25, display.getHeight() - quitButton.getHeight() - 45);
        quitButton.addActionListener(e ->
                display.exit());

        NetworkUtil.initHandlers();
    }

    @Override
    public void tick(UOEngine engine, UODisplay display, double delta) {
        inputTick(display, delta);
        loginButton.tick(engine, display, delta);
        quitButton.tick(engine, display, delta);
        usernameTextField.tick(engine, display, delta);
        passwordTextField.tick(engine, display, delta);
    }

    private void inputTick(UODisplay display, double delta) {
        if (!usernameTextField.isFocused() && !passwordTextField.isFocused())
            usernameTextField.setFocused(true);

        InputHandler input = display.getInput();

        if (input.isKeyPressed(KeyEvent.VK_TAB)) {
            if (usernameTextField.isFocused()) {
                usernameTextField.setFocused(false);
                passwordTextField.setFocused(true);
            } else if (passwordTextField.isFocused()) {
                passwordTextField.setFocused(false);
                usernameTextField.setFocused(true);
            }
        }

        //todo Only make controls usable if we aren't on any other connection-state.
        setControlUsability(true);
    }

    private void setControlUsability(boolean isEnabled) {
        loginButton.setEnabled(isEnabled);
        quitButton.setEnabled(isEnabled);
        usernameTextField.setEnabled(isEnabled);
        passwordTextField.setEnabled(isEnabled);
    }

    @Override
    public void render(UOEngine engine, UODisplay display, Graphics2D graphics) {
        Drawer.drawImage(background, 0, 0, display.getWidth(), display.getHeight(), graphics);
        renderLoginUI(engine, display, graphics);
        graphics.setColor(new Color(223, 195, 15));
        Drawer.drawString("Version: " + UODisplay.version, 0, display.getHeight() - graphics.getFontMetrics().getHeight(), graphics);
        Drawer.drawString("2021@NOETIC", display.getWidth() / 2 - graphics.getFontMetrics().stringWidth("copyright2") / 2,
                display.getHeight() - graphics.getFontMetrics().getHeight(), graphics);
        quitButton.render(engine, display, graphics);
    }

    private void renderLoginUI(UOEngine engine, UODisplay display, Graphics2D graphics) {
        //todo set font
        graphics.setColor(Color.BLACK);
        int position = display.getWidth() / 2 - graphics.getFontMetrics().stringWidth("Account Name") / 2;
        Drawer.drawString("Account Name", position + 2, 302, graphics);
        graphics.setColor(new Color(223, 195, 15));
        Drawer.drawString("Account Name", position, 300, graphics);

        graphics.setColor(Color.BLACK);
        position = display.getWidth() / 2 - graphics.getFontMetrics().stringWidth("Account Password") / 2;
        Drawer.drawString("Account Password", position + 2, 402, graphics);
        graphics.setColor(new Color(223, 195, 15));
        Drawer.drawString("Account Password", position, 400, graphics);

        usernameTextField.render(engine, display, graphics);
        passwordTextField.render(engine, display, graphics);
        loginButton.render(engine, display, graphics);
    }

    @Override
    public void OnStateTransition(UODisplay display) {

    }
}
