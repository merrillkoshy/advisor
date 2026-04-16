package com.lastwar_advisor.server.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lastwar_advisor.server.dto.DroneProfileResponse;
import com.lastwar_advisor.server.dto.DroneRequest;
import com.lastwar_advisor.server.entity.PlayerDroneComponent;
import com.lastwar_advisor.server.service.DroneService;

@RestController
@RequestMapping("/players")
public class PlayerDroneController {

    public final DroneService service;

    public PlayerDroneController(DroneService service) {
        this.service = service;
    }

    @GetMapping("/{playerId}/drone")
    public DroneProfileResponse getPlayerDrone(@PathVariable Long playerId) {
        return service.getOrCreateDrone(playerId);
    }

    @PutMapping("/{playerId}/drone")
    public DroneProfileResponse putPlayerDrone(@PathVariable Long playerId, @RequestBody Integer level) {
        return service.updateDroneLevel(playerId, level);

    }

    @PutMapping("/{playerId}/drone/components")
    public List<PlayerDroneComponent> putPlayerDroneComponents(@PathVariable Long playerId,
            @RequestBody List<DroneRequest> droneRequest) {
        return service.savePlayerDroneComponents(playerId, droneRequest);

    }
}
