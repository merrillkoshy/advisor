package com.lastwar_advisor.server.dto;

import java.util.List;

import com.lastwar_advisor.server.entity.Opponent.OpponentDrone;
import com.lastwar_advisor.server.entity.Opponent.OpponentDroneComponent;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OpponentDroneProfileResponse {

    private OpponentDrone drone;
    private List<OpponentDroneComponent> components;

    public OpponentDroneProfileResponse(OpponentDrone drone, List<OpponentDroneComponent> components) {
        this.drone = drone;
        this.components = components;
    }
}
