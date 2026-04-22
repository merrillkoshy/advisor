package com.lastwar_advisor.server.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.lastwar_advisor.server.dto.DroneRequest;
import com.lastwar_advisor.server.dto.OpponentDroneProfileResponse;
import com.lastwar_advisor.server.dto.SquadSlotRequest;
import com.lastwar_advisor.server.entity.DroneComponent;
import com.lastwar_advisor.server.entity.Hero;
import com.lastwar_advisor.server.entity.Opponent.Opponent;
import com.lastwar_advisor.server.entity.Opponent.OpponentDrone;
import com.lastwar_advisor.server.entity.Opponent.OpponentDroneComponent;
import com.lastwar_advisor.server.entity.Opponent.OpponentSquad;
import com.lastwar_advisor.server.entity.Opponent.OpponentSquadSlot;
import com.lastwar_advisor.server.repository.DroneComponentRepository;
import com.lastwar_advisor.server.repository.DroneMilestoneRepository;
import com.lastwar_advisor.server.repository.DroneSkillTierRepository;
import com.lastwar_advisor.server.repository.HeroRepository;
import com.lastwar_advisor.server.repository.Opponent.OpponentDroneComponentRepository;
import com.lastwar_advisor.server.repository.Opponent.OpponentDroneRepository;
import com.lastwar_advisor.server.repository.Opponent.OpponentRepository;
import com.lastwar_advisor.server.repository.Opponent.OpponentSquadRepository;
import com.lastwar_advisor.server.repository.Opponent.OpponentSquadSlotRepository;

import jakarta.transaction.Transactional;

@Service
public class OpponentService {
        private final OpponentRepository repo_opponent;
        private final OpponentSquadRepository repo;
        private final OpponentSquadSlotRepository repo_ss;
        private final HeroRepository repo_h;

        private final DroneComponentRepository droneComponentRepo;
        private final OpponentDroneComponentRepository opponentDroneComponentRepo;
        private final OpponentDroneRepository droneRepo;

        public OpponentService(OpponentRepository repo_opponent, OpponentSquadRepository repo,
                        OpponentSquadSlotRepository repo_ss,
                        HeroRepository repo_h,
                        DroneComponentRepository droneComponentRepo,
                        OpponentDroneComponentRepository opponentDroneComponentRepo,
                        DroneMilestoneRepository droneMilestoneRepo,
                        DroneSkillTierRepository droneSkillTierRepo,
                        OpponentDroneRepository droneRepo) {
                this.repo_opponent = repo_opponent;
                this.repo = repo;
                this.repo_ss = repo_ss;
                this.repo_h = repo_h;
                this.droneComponentRepo = droneComponentRepo;
                this.opponentDroneComponentRepo = opponentDroneComponentRepo;

                this.droneRepo = droneRepo;
        }

        @Transactional
        public List<OpponentSquad> getSquadsByOpponent(Long opponentId) {
                Opponent opponent = repo_opponent.findById(opponentId)
                                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                                "Opponent not found"));
                return repo.findByOpponent(opponent);
        }

        public OpponentSquad getSquad(Long opponentId, Integer squadNumber) {
                Opponent opponent = repo_opponent.findById(opponentId)
                                .orElseThrow(() -> new RuntimeException("Opponent not found"));
                return repo.findByOpponentAndSquadNumber(opponent, squadNumber)
                                .orElseGet(() -> {
                                        OpponentSquad ns = new OpponentSquad();
                                        ns.setOpponent(opponent);
                                        ns.setSquadNumber(squadNumber);
                                        return repo.save(ns);
                                });
        };

        public OpponentSquad saveSquad(Long opponentId, Integer squadNumber, List<SquadSlotRequest> squad) {
                Opponent opponent = repo_opponent.findById(opponentId)
                                .orElseThrow(() -> new RuntimeException("Opponent not found"));
                OpponentSquad finalSquad = repo.findByOpponentAndSquadNumber(opponent, squadNumber)
                                .orElseGet(() -> {
                                        OpponentSquad ns = new OpponentSquad();
                                        ns.setOpponent(opponent);
                                        ns.setSquadNumber(squadNumber);
                                        return repo.save(ns);
                                });

                for (SquadSlotRequest slotReq : squad) {
                        System.out.println("Processing slot: " + slotReq.getPosition() + " " + slotReq.getSlotIndex()
                                        + " heroId: "
                                        + slotReq.getHeroId());
                        OpponentSquadSlot slot = repo_ss
                                        .findBySquadAndPositionAndSlotIndex(finalSquad, slotReq.getPosition(),
                                                        slotReq.getSlotIndex())
                                        .orElse(new OpponentSquadSlot());

                        Hero hero = repo_h.findById(slotReq.getHeroId())
                                        .orElseThrow(() -> new RuntimeException("Hero not found"));
                        System.out.println("Found hero: " + hero.getName());

                        slot.setSquad(finalSquad);
                        slot.setPosition(slotReq.getPosition());
                        slot.setSlotIndex(slotReq.getSlotIndex());
                        slot.setHero(hero);
                        slot.setGunStars(slotReq.getGunStars());
                        slot.setArmorStars(slotReq.getArmorStars());
                        slot.setChipStars(slotReq.getChipStars());
                        slot.setRadarStars(slotReq.getRadarStars());

                        repo_ss.save(slot);
                }

                return finalSquad;

        }

        // Opponent Drone
        public List<DroneComponent> getAllComponents() {
                return droneComponentRepo.findAll();
        }

        public OpponentDroneProfileResponse getOrCreateDrone(Long opponentId) {
                Opponent opponent = repo_opponent.findById(opponentId)
                                .orElseThrow(() -> new RuntimeException("Opponent not found"));

                OpponentDrone drone = droneRepo.findByOpponentId(opponentId).orElseGet(() -> {
                        OpponentDrone newDrone = new OpponentDrone();
                        newDrone.setOpponent(opponent);
                        newDrone.setLevel(1);
                        return droneRepo.save(newDrone);
                });

                List<OpponentDroneComponent> components = opponentDroneComponentRepo
                                .findByOpponentWithComponents(opponent);
                if (components.isEmpty()) {
                        components = droneComponentRepo.findAll().stream().map(dc -> {
                                OpponentDroneComponent odc = new OpponentDroneComponent();
                                odc.setOpponent(opponent);
                                odc.setDroneComponent(dc);
                                odc.setLevel(0);
                                return opponentDroneComponentRepo.save(odc);
                        }).toList();
                }

                return new OpponentDroneProfileResponse(drone, components);
        }

        public OpponentDroneProfileResponse updateDroneLevel(Long opponentId, Integer level) {
                Opponent opponent = repo_opponent.findById(opponentId)
                                .orElseThrow(() -> new RuntimeException("Opponent not found"));
                OpponentDrone drone = droneRepo.findByOpponentId(opponentId)
                                .orElseThrow(() -> new RuntimeException("Drone not found"));
                drone.setLevel(level);
                droneRepo.save(drone);
                List<OpponentDroneComponent> components = opponentDroneComponentRepo
                                .findByOpponentWithComponents(opponent);
                return new OpponentDroneProfileResponse(drone, components);
        }

        public List<OpponentDroneComponent> saveOpponentDroneComponents(Long opponentId,
                        List<DroneRequest> droneRequests) {
                Opponent opponent = repo_opponent.findById(opponentId)
                                .orElseThrow(() -> new RuntimeException("Player not found"));

                for (DroneRequest req : droneRequests) {
                        DroneComponent dc = droneComponentRepo.findById(req.getDroneComponentId())
                                        .orElseThrow(
                                                        () -> new RuntimeException("Invalid Drone Component Id: "
                                                                        + req.getDroneComponentId()));

                        OpponentDroneComponent odc = opponentDroneComponentRepo
                                        .findByOpponentAndDroneComponent(opponent, dc)
                                        .orElseGet(() -> {
                                                OpponentDroneComponent newPdc = new OpponentDroneComponent();
                                                newPdc.setOpponent(opponent);
                                                newPdc.setDroneComponent(dc);
                                                return newPdc;
                                        });

                        odc.setLevel(req.getDroneComponentLevel());
                        opponentDroneComponentRepo.save(odc);
                }

                return opponentDroneComponentRepo.findByOpponentWithComponents(opponent);

        }

}
