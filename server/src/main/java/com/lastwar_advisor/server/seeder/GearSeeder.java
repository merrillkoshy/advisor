package com.lastwar_advisor.server.seeder;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Component;

import com.lastwar_advisor.server.entity.Gear;
import com.lastwar_advisor.server.entity.GearLevel;
import com.lastwar_advisor.server.entity.GearStat;
import com.lastwar_advisor.server.entity.StatKey;
import com.lastwar_advisor.server.repository.GearLevelRepository;
import com.lastwar_advisor.server.repository.GearRepository;
import com.lastwar_advisor.server.repository.GearStatRepository;
import com.lastwar_advisor.server.repository.StatKeyRepository;
import com.lastwar_advisor.server.util.StatKeyConstants;

@Component
public class GearSeeder {

    private final GearRepository gearRepo;
    private final GearStatRepository gearStatRepo;
    private final GearLevelRepository gearLevelRepo;
    private final StatKeyRepository statKeyRepo;

    public GearSeeder(GearRepository gearRepo, GearStatRepository gearStatRepo, GearLevelRepository gearLevelRepo,
            StatKeyRepository statKeyRepo) {
        this.gearRepo = gearRepo;
        this.gearStatRepo = gearStatRepo;
        this.gearLevelRepo = gearLevelRepo;
        this.statKeyRepo = statKeyRepo;
    }

    public void seedGears() {
        Set<String> existingBaseNames = gearRepo.findAll().stream().map(Gear::getBaseName)
                .collect(java.util.stream.Collectors.toSet());
        int seededCount = 0;
        List<String[]> keys = List.of(
                // basePower, baseName, mythicName, type,
                new String[] { "100", "M5-A \"Thor\" Railgun", "M6-A \"Puma\" Cannon", "Gun" },
                new String[] { "100", "M5-A \"Hunter\" Chip", "M6-A \"Athena\\'s Crown\" Data Chip",
                        "Data Chip" },
                new String[] { "100", "M5-A \"Guard\" Reactive Armor", "M6-A \"Storm Fury\" Nano Armor", "Armor" },
                new String[] { "100", "M5-A \"Predator\" Radar", "M6-A \"Aegis\" Radar", "Radar" });

        for (String[] k : keys) {
            if (existingBaseNames.contains(k[1])) {
                continue;
            }
            seededCount++;
            Gear g = new Gear();
            g.setBasePower(Integer.parseInt(k[0]));
            g.setBaseName(k[1]);
            g.setMythicName(k[2]);
            g.setType(k[3]);
            gearRepo.save(g);
        }
        if (seededCount > 0) {
            System.out.println("Seeded " + seededCount + " gears.");
        } else {
            System.out.println("Gears already seeded, skipping.");
        }

    }

    public void seedGearStats() {

        Set<String> existingStats = gearStatRepo.findAllWithRelations().stream()
                .map(s -> s.getGear().getBaseName() + ":" + s.getStatKey().getKey())
                .collect(java.util.stream.Collectors.toSet());
        int seededCount = 0;
        List<Gear> gears = gearRepo.findAll();
        Map<String, StatKey> statKeyMap = statKeyRepo.findAll().stream()
                .collect(java.util.stream.Collectors.toMap(StatKey::getKey, sk -> sk));

        class GearStatInfo {
            String[] GearNames;
            String keyStr;
            double baseValue;
            double increment;

            public GearStatInfo(String[] GearNames, String keyStr, double baseValue, double increment) {

                this.GearNames = GearNames;
                this.keyStr = keyStr;
                this.baseValue = baseValue;
                this.increment = increment;
            }
        }
        for (Gear g : gears) {

            List<GearStatInfo> statInfos = List.of(
                    new GearStatInfo(
                            new String[] { "M5-A \"Thor\" Railgun", "M5-A \"Hunter\" Chip" },
                            StatKeyConstants.HERO_ATK_RAW,
                            895.00, 44.8),
                    new GearStatInfo(
                            new String[] { "M5-A \"Thor\" Railgun", "M5-A \"Guard\" Reactive Armor",
                                    "M5-A \"Predator\" Radar" },
                            StatKeyConstants.HERO_DEF_RAW, 119.00, 5.975),
                    new GearStatInfo(
                            new String[] { "M5-A \"Hunter\" Chip",
                                    "M5-A \"Guard\" Reactive Armor", "M5-A \"Predator\" Radar" },
                            StatKeyConstants.HERO_HP_RAW, 1790.00, 89.5),
                    new GearStatInfo(new String[] { "M5-A \"Thor\" Railgun" },
                            StatKeyConstants.CRIT_RATE_PERCENT, 5.00, 0.25),
                    new GearStatInfo(
                            new String[] { "M5-A \"Thor\" Railgun", "M5-A \"Hunter\" Chip" },
                            StatKeyConstants.HERO_ATK_BOOST_PERCENT, 2.5, 0.25),
                    new GearStatInfo(
                            new String[] { "M5-A \"Guard\" Reactive Armor", "M5-A \"Predator\" Radar" },
                            StatKeyConstants.HERO_DEF_BOOST_PERCENT, 2.5, 0.25),
                    new GearStatInfo(new String[] { "M5-A \"Hunter\" Chip" },
                            StatKeyConstants.RESISTANCE_ALL_DMG_PERCENT, 2.5, 0.25),
                    new GearStatInfo(
                            new String[] { "M5-A \"Guard\" Reactive Armor", "M5-A \"Predator\" Radar", },
                            StatKeyConstants.HERO_HP_BOOST_PERCENT, 2.5, 0.25));

            for (GearStatInfo info : statInfos) {
                if (!List.of(info.GearNames).contains(g.getBaseName()))
                    continue;
                if (existingStats.contains(g.getBaseName() + ":" + info.keyStr)) {
                    continue;
                }
                seededCount++;
                GearStat stat = new GearStat();
                stat.setGear(g);
                stat.setStatKey(statKeyMap.get(info.keyStr));
                stat.setBaseValue(info.baseValue);
                stat.setIncrement(info.increment);
                gearStatRepo.save(stat);
            }

        }
        if (seededCount > 0) {
            System.out.println("Seeded stats for " + seededCount + " gear stats.");
        } else {
            System.out.println("Gear stats already seeded, skipping.");
        }

    }

    public void seedGearLevels() {
        Set<String> existingLevels = gearLevelRepo.findAllWithRelations().stream()
                .map(l -> l.getGear().getBaseName() + ":" + l.getStatKey().getKey() + ":" + l.getLevel())
                .collect(java.util.stream.Collectors.toSet());
        int seededCount = 0;
        class LevelInfo {
            int level;
            String keyStr;
            Double value;

            public LevelInfo(int level, String keyStr, Double value) {
                this.level = level;
                this.keyStr = keyStr;
                this.value = value;

            }
        }
        Map<String, StatKey> statKeyMap = statKeyRepo.findAll().stream()
                .collect(java.util.stream.Collectors.toMap(StatKey::getKey, sk -> sk));

        Map<String, LevelInfo[]> levelMap = Map.of(
                "M5-A \"Thor\" Railgun", new LevelInfo[] {
                        new LevelInfo(50, StatKeyConstants.HERO_ATK_RAW, 750.00),
                        new LevelInfo(60, StatKeyConstants.CRIT_RATE_PERCENT, 5.00),
                        new LevelInfo(70, StatKeyConstants.HERO_ATK_RAW, 750.00),
                        new LevelInfo(80, StatKeyConstants.CRIT_RATE_PERCENT, 5.00),
                        new LevelInfo(90, StatKeyConstants.DMG_COUNTER_BOOST_PERCENT, 10.00) },
                "M5-A \"Hunter\" Chip", new LevelInfo[] {
                        new LevelInfo(50, StatKeyConstants.HERO_ATK_RAW, 750.00),
                        new LevelInfo(60, StatKeyConstants.RESISTANCE_ALL_DMG_PERCENT, 4.00),
                        new LevelInfo(70, StatKeyConstants.HERO_ATK_RAW, 750.00),
                        new LevelInfo(80, StatKeyConstants.RESISTANCE_ALL_DMG_PERCENT, 4.00),
                        new LevelInfo(90, StatKeyConstants.REDUCE_DAMAGE_TAKEN_PERCENT, 5.00) },
                "M5-A \"Guard\" Reactive Armor", new LevelInfo[] {
                        new LevelInfo(50, StatKeyConstants.HERO_HP_RAW, 63000.00),
                        new LevelInfo(60, StatKeyConstants.PHYSICAL_DAMAGE_RESISTANCE_PERCENT, 20.00),
                        new LevelInfo(70, StatKeyConstants.HERO_HP_RAW, 63000.00),
                        new LevelInfo(80, StatKeyConstants.PHYSICAL_DAMAGE_RESISTANCE_PERCENT, 20.00),
                        new LevelInfo(90, StatKeyConstants.REDUCE_CRIT_CHANCE_PERCENT, 15.00) },
                "M5-A \"Predator\" Radar", new LevelInfo[] {
                        new LevelInfo(50, StatKeyConstants.HERO_DEF_RAW, 300.00),
                        new LevelInfo(60, StatKeyConstants.ENERGY_DAMAGE_RESISTANCE_PERCENT, 20.00),
                        new LevelInfo(70, StatKeyConstants.HERO_DEF_RAW, 300.00),
                        new LevelInfo(80, StatKeyConstants.ENERGY_DAMAGE_RESISTANCE_PERCENT, 20.00),
                        new LevelInfo(90, StatKeyConstants.REDUCE_CRIT_DAMAGE_PERCENT, 30.00) });

        List<Gear> gears = gearRepo.findAll();

        for (Gear g : gears) {
            if (!levelMap.containsKey(g.getBaseName()))
                continue;
            for (int lvl = 50; lvl <= 90; lvl += 10) {

                GearLevel gl = new GearLevel();
                gl.setGear(g);
                LevelInfo[] infos = levelMap.get(g.getBaseName());
                if (infos == null)
                    continue;
                LevelInfo info = null;
                for (LevelInfo i : infos) {
                    if (i.level == lvl) {
                        info = i;
                        break;
                    }
                }
                if (info == null)
                    continue;
                if (existingLevels.contains(g.getBaseName() + ":" + info.keyStr + ":" + info.level)) {
                    continue;
                }
                seededCount++;
                gl.setLevel(info.level);
                gl.setValue(info.value);
                gl.setStatKey(statKeyMap.get(info.keyStr));

                gearLevelRepo.save(gl);
            }
        }
        if (seededCount > 0) {
            System.out.println("Seeded " + seededCount + " gear levels.");
        } else {
            System.out.println("Gear levels already seeded, skipping.");
        }
    }

}
