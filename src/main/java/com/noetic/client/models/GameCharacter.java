package com.noetic.client.models;

import com.noetic.client.enums.GenderType;
import com.noetic.client.enums.Zone;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GameCharacter {
    private String name;
    private Zone zone;
    private GenderType gender;
}
