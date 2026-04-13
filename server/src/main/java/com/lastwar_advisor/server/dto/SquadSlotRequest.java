package com.lastwar_advisor.server.dto;

import com.lastwar_advisor.server.entity.SlotPosition;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SquadSlotRequest {
    private SlotPosition position;
    private Integer slotIndex;
    private Long heroId;
    private Integer gunStars;
    private Integer armorStars;
    private Integer chipStars;
    private Integer radarStars;
}
