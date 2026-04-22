package com.lastwar_advisor.server.seeder;

import org.springframework.stereotype.Component;

import com.lastwar_advisor.server.entity.Opponent.Opponent;
import com.lastwar_advisor.server.repository.Opponent.OpponentRepository;

@Component
public class OpponentSeeder {
    private final OpponentRepository opponentRepository;

    public OpponentSeeder(OpponentRepository opponentRepository) {
        this.opponentRepository = opponentRepository;
    }

    public void seedOpponents() throws Exception {
        if (opponentRepository.count() > 0)
            return;
        Opponent p = new Opponent();
        p.setName("Advisor");
        opponentRepository.save(p);

        System.out.println("Opponent seeding complete.");
    }
}
