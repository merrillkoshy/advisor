package com.lastwar_advisor.server.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.lastwar_advisor.server.dto.DroneProfileResponse;
import com.lastwar_advisor.server.dto.DroneRequest;
import com.lastwar_advisor.server.entity.Drone;
import com.lastwar_advisor.server.entity.DroneComponent;
import com.lastwar_advisor.server.entity.DroneMilestone;
import com.lastwar_advisor.server.entity.DroneSkillTier;
import com.lastwar_advisor.server.entity.Player;
import com.lastwar_advisor.server.entity.PlayerDroneComponent;
import com.lastwar_advisor.server.repository.DroneComponentRepository;
import com.lastwar_advisor.server.repository.DroneMilestoneRepository;
import com.lastwar_advisor.server.repository.DroneRepository;
import com.lastwar_advisor.server.repository.DroneSkillTierRepository;
import com.lastwar_advisor.server.repository.PlayerDroneComponentRepository;
import com.lastwar_advisor.server.repository.PlayerRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DroneService {

    private final DroneComponentRepository droneComponentRepo;
    private final DroneMilestoneRepository droneMilestoneRepo;
    private final DroneSkillTierRepository droneSkillTierRepo;
    private final DroneRepository droneRepo;
    private final PlayerRepository playerRepo;
    private final PlayerDroneComponentRepository playerDroneRepo;

    public List<DroneComponent> getAllComponents() {
        return droneComponentRepo.findAll();
    }

    public List<DroneMilestone> getAllMilestones() {
        return droneMilestoneRepo.findAll();
    }

    public List<DroneSkillTier> getAllSkillTiers() {
        return droneSkillTierRepo.findAll();
    }

    public DroneProfileResponse getOrCreateDrone(Long playerId) {
        Player player = playerRepo.findById(playerId)
                .orElseThrow(() -> new RuntimeException("Player not found"));

        Drone drone = droneRepo.findByPlayerId(playerId).orElseGet(() -> {
            Drone newDrone = new Drone();
            newDrone.setPlayer(player);
            newDrone.setLevel(1);
            return droneRepo.save(newDrone);
        });

        List<PlayerDroneComponent> components = playerDroneRepo.findByPlayerWithComponents(player);
        if (components.isEmpty()) {
            components = droneComponentRepo.findAll().stream().map(dc -> {
                PlayerDroneComponent pdc = new PlayerDroneComponent();
                pdc.setPlayer(player);
                pdc.setDroneComponent(dc);
                pdc.setLevel(0);
                return playerDroneRepo.save(pdc);
            }).toList();
        }

        return new DroneProfileResponse(drone, components);
    }

    public DroneProfileResponse updateDroneLevel(Long playerId, Integer level) {
        Player player = playerRepo.findById(playerId)
                .orElseThrow(() -> new RuntimeException("Player not found"));
        Drone drone = droneRepo.findByPlayerId(playerId)
                .orElseThrow(() -> new RuntimeException("Drone not found"));
        drone.setLevel(level);
        droneRepo.save(drone);
        List<PlayerDroneComponent> components = playerDroneRepo.findByPlayerWithComponents(player);
        return new DroneProfileResponse(drone, components);
    }

    public List<PlayerDroneComponent> savePlayerDroneComponents(Long playerId, List<DroneRequest> droneRequests) {
        Player player = playerRepo.findById(playerId)
                .orElseThrow(() -> new RuntimeException("Player not found"));

        for (DroneRequest req : droneRequests) {
            DroneComponent dc = droneComponentRepo.findById(req.getDroneComponentId())
                    .orElseThrow(
                            () -> new RuntimeException("Invalid Drone Component Id: " + req.getDroneComponentId()));

            PlayerDroneComponent pdc = playerDroneRepo.findByPlayerAndDroneComponent(player, dc)
                    .orElseGet(() -> {
                        PlayerDroneComponent newPdc = new PlayerDroneComponent();
                        newPdc.setPlayer(player);
                        newPdc.setDroneComponent(dc);
                        return newPdc;
                    });

            pdc.setLevel(req.getDroneComponentLevel());
            playerDroneRepo.save(pdc);
        }

        return playerDroneRepo.findByPlayerWithComponents(player);

    }

}
