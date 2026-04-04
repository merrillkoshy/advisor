package com.lastwar_advisor.server.seeder;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.lastwar_advisor.server.entity.DroneComponent;
import com.lastwar_advisor.server.entity.OverlordClass;
import com.lastwar_advisor.server.entity.StatKey;
import com.lastwar_advisor.server.repository.DroneComponentRepository;
import com.lastwar_advisor.server.repository.OverlordClassRepository;
import com.lastwar_advisor.server.repository.StatKeyRepository;

@Component
public class DroneOverlordSeeder {

    private final DroneComponentRepository droneComponentRepo;
    private final OverlordClassRepository overlordClassRepo;
    private final StatKeyRepository statKeyRepo;

    public DroneOverlordSeeder(DroneComponentRepository droneComponentRepo, OverlordClassRepository overlordClassRepo,
            StatKeyRepository statKeyRepo) {
        this.droneComponentRepo = droneComponentRepo;
        this.overlordClassRepo = overlordClassRepo;
        this.statKeyRepo = statKeyRepo;
    }

    public void seedDroneComponents() {
        Set<String> existingComponents = droneComponentRepo.findAll()
                .stream().map(DroneComponent::getName)
                .collect(Collectors.toSet());
        int seededCount = 0;

        List<Object[]> components = List.of(
                new Object[] { "Radar Research", "Affects drone HP and reduces chances of taking critical hits",
                        new String[] { "drone_hp", "crit_reduction_percent" } },
                new Object[] { "Turbo Engine Research", "Affects Hero & Overlord HP and boosts hero HP percentage",
                        new String[] { "hero_overlord_hp", "hero_hp_boost_percent" } },
                new Object[] { "External Armor Research",
                        "Affects Hero & Overlord DEF and boosts hero defense percentage",
                        new String[] { "hero_overlord_def", "hero_def_boost_percent" } },
                new Object[] { "Thermal Imager Research", "Affects drone DEF and boosts crit rate percentage",
                        new String[] { "drone_def", "crit_rate_percent" } },
                new Object[] { "Fuel Cell Research", "Affects drone ATK and boosts crit damage percentage",
                        new String[] { "drone_atk", "crit_damage_percent" } },
                new Object[] { "Airborne Missile Research",
                        "Affects Hero & Overlord ATK, boosts hero skill damage and hero ATK percentage",
                        new String[] { "hero_overlord_atk", "hero_skill_damage_percent" } });

        for (Object[] c : components) {
            if (existingComponents.contains((String) c[0])) {
                continue;
            }
            seededCount++;
            DroneComponent dc = new DroneComponent();
            dc.setName((String) c[0]);
            dc.setDescription((String) c[1]);
            dc.setMaxLevel(12);

            List<StatKey> statKeys = new ArrayList<>();
            for (String keyStr : (String[]) c[2]) {
                statKeyRepo.findByKey(keyStr).ifPresent(statKeys::add);
            }
            dc.setStatKeys(statKeys);

            droneComponentRepo.save(dc);
        }

        if (seededCount > 0) {
            System.out.println("Seeded " + seededCount + " Drone Components.");
        } else {
            System.out.println("Drone Components already seeded, skipping.");
        }
    }

    public void seedOverlordClasses() {

        Set<Integer> existingClasses = overlordClassRepo.findAll()
                .stream().map(OverlordClass::getLevel)
                .collect(Collectors.toSet());

        int seededCount = 0;

        List<Object[]> classes = List.of(
                new Object[] { 0, "Recruit" },
                new Object[] { 10, "Private" },
                new Object[] { 20, "Fighter" },
                new Object[] { 30, "Warrior" },
                new Object[] { 40, "Warlord" },
                new Object[] { 50, "Champion" },
                new Object[] { 60, "Overlord" });

        for (Object[] c : classes) {
            if (existingClasses.contains((Integer) c[0])) {
                continue;
            }
            seededCount++;
            OverlordClass oc = new OverlordClass();
            oc.setLevel((Integer) c[0]);
            oc.setClassName((String) c[1]);
            oc.setOverlordAtk(0);
            oc.setOverlordDef(0);
            oc.setOverlordHp(0);
            oc.setOverlordHpBoost(null);
            oc.setOverlordAtkBoost(null);
            oc.setOverlordDefBoost(null);
            overlordClassRepo.save(oc);
        }
        if (seededCount > 0) {
            System.out.println("Seeded " + seededCount + " Overlord Classes.");
        } else {
            System.out.println("Overlord Classes already seeded, skipping.");
        }

    }

}
