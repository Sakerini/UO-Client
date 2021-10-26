package com.noetic.client.states;

import com.noetic.client.UODisplay;
import com.noetic.client.UOEngine;
import com.noetic.client.enums.GenderType;
import com.noetic.client.gfx.Spritesheet;
import com.noetic.client.gui.UOButton;
import com.noetic.client.gui.UOCharacterOptionPanel;
import com.noetic.client.gui.UOTextField;
import com.noetic.client.utils.Drawer;
import com.noetic.client.utils.NetworkUtil;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class CharacterCreationState extends State {

    public static final int ID = 2;

    private BufferedImage background;

    private UOCharacterOptionPanel optionPanel;
    private UOTextField nameTextField;
    private UOButton acceptCharacterButton;
    private UOButton backButton;

    public CharacterCreationState() {
        super(ID);
    }

    public void init(UODisplay display) {

        BufferedImage buttonImage = null;
        try {
            background = ImageIO.read(getClass().getResourceAsStream("/ui/character_creation_bg.jpg"));
            buttonImage = ImageIO.read(getClass().getResourceAsStream("/ui/button.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        optionPanel = new UOCharacterOptionPanel(display);

        nameTextField = new UOTextField(display, 175, 25, 6, 6);
        nameTextField.setBackgroundColor(new Color(0, 0, 0, 225));
        nameTextField.setForegroundColor(Color.WHITE);
        nameTextField.setLocation(display.getWidth() / 2 - nameTextField.getWidth() / 2, display.getHeight() - nameTextField.getHeight() * 2 - 25);

        acceptCharacterButton = new UOButton("Accept");
        acceptCharacterButton.setEnabledButtonImage(buttonImage);
        acceptCharacterButton.setLocation(display.getWidth() - acceptCharacterButton.getWidth() - 15, nameTextField.getY());
        acceptCharacterButton.addActionListener(event -> {
            String name = nameTextField.getText().trim();
            int genderId = optionPanel.getSelectedGender().getId();

            NetworkUtil.sendCharacterCreationPacket(name, genderId);
        });

        backButton = new UOButton("Back");
        backButton.setEnabledButtonImage(buttonImage);
        backButton.setLocation(acceptCharacterButton.getX(), acceptCharacterButton.getY() + backButton.getHeight() + 5);
        backButton.addActionListener(event -> {
           display.enterState(CharacterSelectionState.ID);
        });
    }

    @Override
    public int getId() {
        return ID;
    }

    @Override
    public void tick(UOEngine engine, UODisplay display, double delta) {
        if (!nameTextField.isFocused())
            nameTextField.setFocused(true);

        optionPanel.tick(engine, display, delta);
        acceptCharacterButton.tick(engine, display, delta);
        backButton.tick(engine, display, delta);
    }

    @Override
    public void render(UOEngine engine, UODisplay display, Graphics2D graphics) {
        Drawer.drawImage(background, 0, 0, display.getWidth(), display.getHeight(), graphics);
        optionPanel.render(engine, display, graphics);

        /** Get the current selected race and render it's first "idle" image. **/
        GenderType gender = optionPanel.getSelectedGender();
        if (gender != null) {
            BufferedImage genderSprite = Spritesheet.getSpriteImage(gender, 1, 0);

            BufferedImage sprite = Spritesheet.getSinglePositionSprite(genderSprite, 1, 0);
            Drawer.drawImage(sprite,
                    display.getWidth() / 2 - sprite.getWidth() / 2 - 100,
                    display.getHeight() / 2 - sprite.getHeight() / 2 - 150, 330, 400, graphics);

            /*todo delete
            Drawer.drawImage(genderSprite,
                    display.getWidth() / 2 - genderSprite.getWidth() / 2 - 100,
                    display.getHeight() / 2 - genderSprite.getHeight() / 2 - 150, 330, 400, graphics);

             */
        }
        graphics.setColor(Color.GRAY);
        Drawer.drawString("Name", nameTextField.getX() + 65, nameTextField.getY() -25, graphics);
        nameTextField.render(engine, display, graphics);
        acceptCharacterButton.render(engine, display, graphics);
        backButton.render(engine, display, graphics);
    }

    @Override
    public void OnStateTransition(UODisplay display) {
        nameTextField.setText("");
    }
}
