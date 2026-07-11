package com.lastwar_advisor.server.seeder;

import java.util.List;

import org.springframework.stereotype.Component;

import com.lastwar_advisor.server.entity.GameConstant;
import com.lastwar_advisor.server.repository.GameConstantRepository;

@Component
public class GameConstantSeeder {
    private final GameConstantRepository gameConstantRepository;

    public GameConstantSeeder(GameConstantRepository gameConstantRepository) {
        this.gameConstantRepository = gameConstantRepository;
    }

    public void seedGameConstants() {

        int seededCount = 0;
        List<Object[]> constants = List.of(
                new Object[] { "DEF_SCALING_CONSTANT", 5000.0 },
                new Object[] { "DRONE_ATTACK_INTERVAL_S", 10.0 },
                new Object[] { "SIMULATION_TIMEOUT_MS", 10000.0 },
                new Object[] { "DEFAULT_SKILL_LEVEL", 40.0 });
        for (Object[] k : constants) {
            String key = (String) k[0];
            boolean isNew = gameConstantRepository.findByKey(key).isEmpty();
            GameConstant gc = gameConstantRepository.findByKey(key).orElseGet(() -> {
                GameConstant newConstant = new GameConstant();
                newConstant.setKey(key);
                return newConstant;
            });
            gc.setValue((double) k[1]);
            gameConstantRepository.save(gc);
            if (isNew)
                seededCount++;
        }
        if (seededCount > 0) {
            System.out.println("Seeded " + seededCount + " Game Constants.");
        } else {
            System.out.println("Game Constants already seeded, skipping.");
        }
    }
}
