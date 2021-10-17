package com.noetic.client.models;

import com.noetic.client.enums.GenderType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GameCharacter {
    private String name;
    private int zone;
    private GenderType gender;
}
