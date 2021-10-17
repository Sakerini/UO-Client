package com.noetic.client.gui;

import com.noetic.client.UODisplay;
import com.noetic.client.UOEngine;
import com.noetic.client.enums.GenderType;
import com.noetic.client.handlers.InputHandler;
import com.noetic.client.utils.Drawer;
import lombok.Getter;
import lombok.Setter;

import java.awt.*;
import java.awt.geom.RoundRectangle2D;

@Getter
@Setter
public class UOCharacterOptionPanel implements UOWidget{

    private RoundRectangle2D.Double genderSelectionPanel;

    private UOGenderOption maleOption;
    private UOGenderOption femaleOption;

    private GenderType selectedGender;

    public UOCharacterOptionPanel(UODisplay display) {
        genderSelectionPanel = new RoundRectangle2D.Double(25, 0, 225, 325, 6, 6);
        genderSelectionPanel.y = display.getHeight() / 2 - genderSelectionPanel.height / 2;

        maleOption = new UOGenderOption(GenderType.Male);
        maleOption.setLocation((int)(genderSelectionPanel.x + (genderSelectionPanel.width - 75)), (int)genderSelectionPanel.y + 45);
        maleOption.setSelected(true);

        femaleOption = new UOGenderOption(GenderType.Female);
        femaleOption.setLocation((int)(genderSelectionPanel.x + (genderSelectionPanel.width / 4 - femaleOption.getWIDTH() / 2)), (int)genderSelectionPanel.y + 45);

    }

    @Override
    public void tick(UOEngine engine, UODisplay display, double delta) {
        maleOption.tick(engine, display, delta);
        femaleOption.tick(engine, display, delta);

        tickSelected(engine, display, delta);
    }

    @Override
    public void render(UOEngine engine, UODisplay display, Graphics2D graphics) {
        graphics.setColor(Color.gray);
        Stroke oldStroke = graphics.getStroke();
        graphics.setStroke(new BasicStroke(2));
        graphics.draw(genderSelectionPanel);
        graphics.setStroke(oldStroke);

        Drawer.drawString("Gender:", maleOption.getX() - 60 , maleOption.getY() - 30, graphics);

        maleOption.render(engine, display, graphics);
        femaleOption.render(engine, display, graphics);
    }

    @Override
    public void setLocation(int x, int y) {

    }

    private void tickSelected(UOEngine engine, UODisplay display, double delta) {
        InputHandler input = display.getInput();
        Point mousePos = input.getMousePosition();

        if (input.isMouseButtonPressed(InputHandler.MOUSE_LEFT)) {
            if (maleOption.getBorder().contains(mousePos)) {
                femaleOption.setSelected(false);
                maleOption.setSelected(true);
            } else if (femaleOption.getBorder().contains(mousePos)) {
                maleOption.setSelected(false);
                femaleOption.setSelected(true);
            }
        }

        if (maleOption.isSelected())
            selectedGender = maleOption.getGender();
        else if (femaleOption.isSelected())
            selectedGender = femaleOption.getGender();
    }
}
