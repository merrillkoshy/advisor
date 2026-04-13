package com.lastwar_advisor.server.seeder;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class DataSeeder implements ApplicationRunner {
    private final PlayerSeeder playerSeeder;
    private final GearSeeder gearSeeder;
    private final StatKeySeeder statKeySeeder;
    private final HeroSeeder heroSeeder;
    private final DroneOverlordSeeder droneOverlordSeeder;

    public DataSeeder(StatKeySeeder statKeySeeder, HeroSeeder heroSeeder, DroneOverlordSeeder droneOverlordSeeder,
            GearSeeder gearSeeder, PlayerSeeder playerSeeder) {
        this.statKeySeeder = statKeySeeder;
        this.heroSeeder = heroSeeder;
        this.droneOverlordSeeder = droneOverlordSeeder;
        this.gearSeeder = gearSeeder;
        this.playerSeeder = playerSeeder;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        statKeySeeder.seedStatKeys();
        heroSeeder.seedHeroes();
        droneOverlordSeeder.seedDroneComponents();
        droneOverlordSeeder.seedOverlordClasses();
        gearSeeder.seedGears();
        gearSeeder.seedGearStats();
        gearSeeder.seedGearLevels();
        playerSeeder.seedPlayers();
    }

}
