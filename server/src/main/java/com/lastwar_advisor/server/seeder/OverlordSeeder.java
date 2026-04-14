package com.lastwar_advisor.server.seeder;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.lastwar_advisor.server.entity.OverlordClass;
import com.lastwar_advisor.server.repository.DroneComponentRepository;
import com.lastwar_advisor.server.repository.OverlordClassRepository;
import com.lastwar_advisor.server.repository.StatKeyRepository;

@Component
public class OverlordSeeder {

    private final OverlordClassRepository overlordClassRepo;

    public OverlordSeeder(DroneComponentRepository droneComponentRepo, OverlordClassRepository overlordClassRepo,
            StatKeyRepository statKeyRepo) {

        this.overlordClassRepo = overlordClassRepo;

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
