package com.noetic.client.states;

import com.noetic.client.UODisplay;
import com.noetic.client.UOEngine;
import com.noetic.client.enums.AuthStatus;
import com.noetic.client.enums.GenderType;
import com.noetic.client.enums.PacketDirection;
import com.noetic.client.gui.UOButton;
import com.noetic.client.handlers.GameDataHandler;
import com.noetic.client.handlers.InputHandler;
import com.noetic.client.models.GameCharacter;
import com.noetic.client.network.Network;
import com.noetic.client.network.packets.CharacterListCSPacket;
import com.noetic.client.utils.Drawer;
import com.noetic.client.utils.NetworkUtil;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

public class CharacterSelectionState extends State {
    public static final int ID = 1;

    private BufferedImage background;

    private RoundRectangle2D.Double characterSelectionPanel;
    private RoundRectangle2D.Double[] characterPortraits;
    private RoundRectangle2D.Double characterSelector;

    private UOButton enterWorldButton;
    private UOButton createCharacterButton;
    private UOButton deleteCharacterButton;
    private UOButton backButton;

    private int selectedIndex = -1;

    public CharacterSelectionState() {
        super(ID);
    }

    @Override
    public void init(UODisplay display) {
        BufferedImage buttonImage = null;
        try {
            background = ImageIO.read(getClass().getResourceAsStream("/ui/character_selection.png"));
            buttonImage = ImageIO.read(getClass().getResourceAsStream("/ui/button.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        characterSelectionPanel = new RoundRectangle2D.Double(0, 0, 225, 565, 6, 6);
        characterSelectionPanel.x = display.getWidth() - characterSelectionPanel.getWidth() - 15;
        characterSelectionPanel.y = 15;

        enterWorldButton = new UOButton("Enter World");
        enterWorldButton.setEnabledButtonImage(buttonImage);
        enterWorldButton.setLocation(display.getWidth() / 2 - enterWorldButton.getWidth() / 2, display.getHeight() - enterWorldButton.getHeight() * 2);
        enterWorldButton.addActionListener(e -> {
            GameDataHandler.characterInUse = GameDataHandler.getCharacters().get(selectedIndex);
            NetworkUtil.connectToWorld();
        });

        createCharacterButton = new UOButton("Create Character");
        createCharacterButton.setEnabledButtonImage(buttonImage);
        createCharacterButton.setLocation((int) (characterSelectionPanel.x + (characterSelectionPanel.width / 2 - createCharacterButton.getWidth() / 2)), (int) (characterSelectionPanel.y + (characterSelectionPanel.height - createCharacterButton.getHeight() - 10)));
        createCharacterButton.addActionListener(e -> {
            display.enterState(CharacterCreationState.ID);
        });

        deleteCharacterButton = new UOButton("Delete Character");
        deleteCharacterButton.setEnabledButtonImage(buttonImage);
        deleteCharacterButton.setLocation(createCharacterButton.getX(), enterWorldButton.getY() - deleteCharacterButton.getHeight() * 2);
        deleteCharacterButton.addActionListener(e -> {
            //todo
        });

        backButton = new UOButton("Back");
        backButton.setEnabledButtonImage(buttonImage);
        backButton.setLocation(deleteCharacterButton.getX(), enterWorldButton.getY());
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                display.enterState(LoginScreenState.ID);
                NetworkUtil.getAuthConnection().getClient().close();
                NetworkUtil.setAuthConnection(null);
            }
        });

        characterPortraits = new RoundRectangle2D.Double[7];
        for (int i = 0; i < characterPortraits.length; i++) {
            characterPortraits[i] = new RoundRectangle2D.Double(0, 0, 195, 50, 6, 6);
            if (i == 0) {
                characterPortraits[i].x = characterSelectionPanel.x + characterSelectionPanel.width / 2 - characterPortraits[i].width / 2;
                characterPortraits[i].y = (int) characterSelectionPanel.y + 25;
            } else {
                characterPortraits[i].x = characterPortraits[0].x;
                characterPortraits[i].y = characterPortraits[i - 1].y + characterPortraits[i - 1].height + 10;
            }
        }

        characterSelector = new RoundRectangle2D.Double(0, 0, 195, 50, 6, 6);
    }

    @Override
    public int getId() {
        return ID;
    }

    @Override
    public void tick(UOEngine engine, UODisplay display, double delta) {
        if (Objects.requireNonNull(GameDataHandler.getCharacters()).size() < 1)
            selectedIndex = -1;

        if (GameDataHandler.getCharacters().size() > 0) {
            if (characterSelector.x == 0.0 && characterSelector.y == 0.0) {
                characterSelector.x = characterPortraits[0].x;
                characterSelector.y = characterPortraits[0].y;
                selectedIndex = 0;
            }
        }

        enterWorldButton.tick(engine, display, delta);
        createCharacterButton.tick(engine, display, delta);
        deleteCharacterButton.tick(engine, display, delta);
        backButton.tick(engine, display, delta);

        InputHandler input = display.getInput();

        if (input.isMouseButtonPressed(InputHandler.MOUSE_LEFT)) {
            for (int i = 0; i < characterPortraits.length; i++) {
                RoundRectangle2D.Double r = characterPortraits[i];
                if (r.contains(input.getMousePosition())) {
                    if (Objects.nonNull(GameDataHandler.getCharacters())) {
                        if (Objects.nonNull(GameDataHandler.getCharacters().get(i))) {
                            characterSelector.x = r.x;
                            characterSelector.y = r.y;
                            selectedIndex = i;
                        }
                    }
                }
            }
        }
    }

    @Override
    public void render(UOEngine engine, UODisplay display, Graphics2D graphics) {
        Drawer.drawImage(background, 0, 0, display.getWidth(), display.getHeight(), graphics);
        graphics.setColor(Color.gray);
        Stroke oldStroke = graphics.getStroke();
        graphics.setStroke(new BasicStroke(2));
        graphics.draw(characterSelectionPanel);
        graphics.setStroke(oldStroke);

        enterWorldButton.render(engine, display, graphics);
        createCharacterButton.render(engine, display, graphics);
        deleteCharacterButton.render(engine, display, graphics);
        backButton.render(engine, display, graphics);

        if (characterSelector.x != 0.0 && characterSelector.y != 0.0 && selectedIndex > -1) {
            graphics.setColor(new Color(238, 208, 18, 95));
            graphics.fill(characterSelector);
            graphics.setColor(Color.yellow);
            graphics.draw(characterSelector);
        }

        if (Objects.nonNull(GameDataHandler.getCharacters())) {
            int size = GameDataHandler.getCharacters().size();
            if (size > 0) {
                for (int i = 0; i < size; i++) {
                    GameCharacter character = GameDataHandler.getCharacters().get(i);

                    //todo setfont
                    graphics.setColor(new Color(223, 195, 15));
                    Drawer.drawString(character.getName(), (float) characterPortraits[i].x + 5, (float) characterPortraits[i].y, graphics);
                    //todo setfont 13f
                    graphics.setColor(Color.white);
                    Drawer.drawString("level 1", (float) characterPortraits[i].x + 5, (float) characterPortraits[i].y + graphics.getFontMetrics().getHeight() + 4, graphics);
                }
            }
        }

        if (selectedIndex > -1) {
            GenderType gender = GameDataHandler.getCharacters().get(selectedIndex).getGender();
            if (gender != null) {
                BufferedImage sprite = gender.getSpritesheet().getSubImage(1, 0, 77, 82);
                Drawer.drawImage(sprite, display.getWidth() / 2 - sprite.getWidth() / 2 - 105, display.getHeight() / 2 - sprite.getHeight() / 2 - 70, 330, 400, graphics);
            }
        }
    }

    @Override
    public void OnStateTransition(UODisplay display) {
        NetworkUtil.getAuthConnection().setStatus(AuthStatus.CharacterList);
        NetworkUtil.sendSimplePacket(PacketDirection.Auth, new CharacterListCSPacket());
    }
}
