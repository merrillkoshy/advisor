package com.lastwar_advisor.server.dto;

import java.util.List;

import com.lastwar_advisor.server.entity.Drone;
import com.lastwar_advisor.server.entity.PlayerDroneComponent;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DroneProfileResponse {

    private Drone drone;
    private List<PlayerDroneComponent> components;

    public DroneProfileResponse(Drone drone, List<PlayerDroneComponent> components) {
        this.drone = drone;
        this.components = components;
    }
}
