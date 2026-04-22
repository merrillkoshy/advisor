package com.lastwar_advisor.server.seeder;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class DataSeeder implements ApplicationRunner {
    private final PlayerSeeder playerSeeder;
    private final OpponentSeeder opponentSeeder;
    private final GearSeeder gearSeeder;
    private final StatKeySeeder statKeySeeder;
    private final HeroSeeder heroSeeder;
    private final DroneSeeder droneSeeder;
    private final OverlordSeeder overlordSeeder;

    public DataSeeder(StatKeySeeder statKeySeeder, HeroSeeder heroSeeder, DroneSeeder droneSeeder,
            OverlordSeeder overlordSeeder,
            GearSeeder gearSeeder, PlayerSeeder playerSeeder, OpponentSeeder opponentSeeder) {
        this.statKeySeeder = statKeySeeder;
        this.heroSeeder = heroSeeder;
        this.droneSeeder = droneSeeder;
        this.overlordSeeder = overlordSeeder;
        this.gearSeeder = gearSeeder;
        this.playerSeeder = playerSeeder;
        this.opponentSeeder = opponentSeeder;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        statKeySeeder.seedStatKeys();
        heroSeeder.seedHeroes();
        // Drone
        droneSeeder.seedDroneComponents();
        droneSeeder.seedDrone();

        overlordSeeder.seedOverlordClasses();

        // Gear
        gearSeeder.seedGears();
        gearSeeder.seedGearStats();
        gearSeeder.seedGearLevels();

        // Personas
        playerSeeder.seedPlayers();
        opponentSeeder.seedOpponents();
    }

}
