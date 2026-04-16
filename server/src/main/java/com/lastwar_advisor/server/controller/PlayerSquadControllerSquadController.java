package com.lastwar_advisor.server.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lastwar_advisor.server.dto.SquadSlotRequest;
import com.lastwar_advisor.server.entity.Squad;
import com.lastwar_advisor.server.service.SquadService;

@RestController
@RequestMapping("/players")
public class PlayerSquadControllerSquadController {

    private final SquadService service;

    public PlayerSquadControllerSquadController(SquadService service) {
        this.service = service;
    }

    @GetMapping("/{playerId}/squads")
    public List<Squad> getSquads(@PathVariable Long playerId) {
        return service.getSquadsByPlayer(playerId);
    }

    @GetMapping("/{playerId}/squads/{squadNumber}")
    public Squad getSquadBySlot(@PathVariable Long playerId, @PathVariable Integer squadNumber) {
        return service.getSquad(playerId, squadNumber);
    }

    @PutMapping("/{playerId}/squads/{squadNumber}")
    public Squad putSquadBySlot(@PathVariable Long playerId, @PathVariable Integer squadNumber,
            @RequestBody List<SquadSlotRequest> slots) {
        return service.saveSquad(playerId, squadNumber, slots);

    }
}
