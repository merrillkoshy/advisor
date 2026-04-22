package com.lastwar_advisor.server.seeder;

import org.springframework.stereotype.Component;

import com.lastwar_advisor.server.entity.Player.Player;
import com.lastwar_advisor.server.repository.PlayerRepository;

@Component
public class PlayerSeeder {
    private final PlayerRepository playerRepository;

    public PlayerSeeder(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    public void seedPlayers() throws Exception {
        if (playerRepository.count() > 0)
            return;
        Player p = new Player();
        p.setName("Azrael");
        playerRepository.save(p);

        System.out.println("Player seeding complete.");
    }
}
