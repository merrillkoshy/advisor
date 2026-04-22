package com.lastwar_advisor.server.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.lastwar_advisor.server.dto.SquadSlotRequest;
import com.lastwar_advisor.server.entity.Hero;
import com.lastwar_advisor.server.entity.Squad;
import com.lastwar_advisor.server.entity.SquadSlot;
import com.lastwar_advisor.server.entity.Player.Player;
import com.lastwar_advisor.server.repository.HeroRepository;
import com.lastwar_advisor.server.repository.PlayerRepository;
import com.lastwar_advisor.server.repository.SquadRepository;
import com.lastwar_advisor.server.repository.SquadSlotRepository;

import jakarta.transaction.Transactional;

@Service
public class SquadService {
        private final PlayerRepository repo_player;
        private final SquadRepository repo;
        private final SquadSlotRepository repo_ss;
        private final HeroRepository repo_h;

        public SquadService(PlayerRepository repo_player, SquadRepository repo, SquadSlotRepository repo_ss,
                        HeroRepository repo_h) {
                this.repo_player = repo_player;
                this.repo = repo;
                this.repo_ss = repo_ss;
                this.repo_h = repo_h;
        }

        @Transactional
        public List<Squad> getSquadsByPlayer(Long playerId) {
                Player player = repo_player.findById(playerId)
                                .orElseThrow(() -> new RuntimeException("Player not found"));
                return repo.findByPlayer(player);
        }

        public Squad getSquad(Long playerId, Integer squadNumber) {
                Player player = repo_player.findById(playerId)
                                .orElseThrow(() -> new RuntimeException("Player not found"));
                return repo.findByPlayerAndSquadNumberWithHeroes(player, squadNumber)
                                .orElseGet(() -> {
                                        Squad ns = new Squad();
                                        ns.setPlayer(player);
                                        ns.setSquadNumber(squadNumber);
                                        return repo.save(ns);
                                });
        };

        public Squad saveSquad(Long playerId, Integer squadNumber, List<SquadSlotRequest> squad) {
                Player player = repo_player.findById(playerId)
                                .orElseThrow(() -> new RuntimeException("Player not found"));
                Squad finalSquad = repo.findByPlayerAndSquadNumber(player, squadNumber)
                                .orElseGet(() -> {
                                        Squad ns = new Squad();
                                        ns.setPlayer(player);
                                        ns.setSquadNumber(squadNumber);
                                        return repo.save(ns);
                                });

                for (SquadSlotRequest slotReq : squad) {
                        System.out.println("Processing slot: " + slotReq.getPosition() + " " + slotReq.getSlotIndex()
                                        + " heroId: "
                                        + slotReq.getHeroId());
                        SquadSlot slot = repo_ss
                                        .findBySquadAndPositionAndSlotIndex(finalSquad, slotReq.getPosition(),
                                                        slotReq.getSlotIndex())
                                        .orElse(new SquadSlot());

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
}
