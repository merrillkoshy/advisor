package com.lastwar_advisor.server.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lastwar_advisor.server.dto.DroneRequest;
import com.lastwar_advisor.server.dto.OpponentDroneProfileResponse;
import com.lastwar_advisor.server.dto.SquadSlotRequest;
import com.lastwar_advisor.server.entity.Opponent.OpponentDroneComponent;
import com.lastwar_advisor.server.entity.Opponent.OpponentSquad;
import com.lastwar_advisor.server.service.OpponentService;

@RestController
@RequestMapping("/opponents")
public class OpponentController {
    public final OpponentService opponentService;

    public OpponentController(OpponentService opponentService) {
        this.opponentService = opponentService;
    }

    @GetMapping("/{opponentId}/drone")
    public OpponentDroneProfileResponse getOpponentDrone(@PathVariable Long opponentId) {
        return opponentService.getOrCreateDrone(opponentId);
    }

    @PutMapping("/{opponentId}/drone")
    public OpponentDroneProfileResponse putOpponentDrone(@PathVariable Long opponentId, @RequestBody Integer level) {
        return opponentService.updateDroneLevel(opponentId, level);

    }

    @PutMapping("/{opponentId}/drone/components")
    public List<OpponentDroneComponent> putOpponentDroneComponents(@PathVariable Long opponentId,
            @RequestBody List<DroneRequest> droneRequest) {
        return opponentService.saveOpponentDroneComponents(opponentId, droneRequest);

    }

    @GetMapping("/{opponentId}/squads")
    public List<OpponentSquad> getSquads(@PathVariable Long opponentId) {
        return opponentService.getSquadsByOpponent(opponentId);
    }

    @GetMapping("/{opponentId}/squads/{squadNumber}")
    public OpponentSquad getSquadBySlot(@PathVariable Long opponentId, @PathVariable Integer squadNumber) {
        return opponentService.getSquad(opponentId, squadNumber);
    }

    @PutMapping("/{opponentId}/squads/{squadNumber}")
    public OpponentSquad putSquadBySlot(@PathVariable Long opponentId, @PathVariable Integer squadNumber,
            @RequestBody List<SquadSlotRequest> slots) {
        return opponentService.saveSquad(opponentId, squadNumber, slots);

    }

}
