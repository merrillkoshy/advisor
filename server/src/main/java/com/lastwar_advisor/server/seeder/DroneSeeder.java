package com.lastwar_advisor.server.seeder;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.lastwar_advisor.server.entity.DroneComponent;
import com.lastwar_advisor.server.entity.DroneComponentStat;
import com.lastwar_advisor.server.entity.DroneMilestone;
import com.lastwar_advisor.server.entity.DroneSkillTier;
import com.lastwar_advisor.server.repository.DroneComponentRepository;
import com.lastwar_advisor.server.repository.DroneMilestoneRepository;
import com.lastwar_advisor.server.repository.DroneSkillTierRepository;
import com.lastwar_advisor.server.repository.StatKeyRepository;

@Component
public class DroneSeeder {

        private final DroneComponentRepository droneComponentRepo;
        private final DroneMilestoneRepository droneMilestoneRepository;
        private final DroneSkillTierRepository droneSkillTierRepository;

        private final StatKeyRepository statKeyRepo;

        public DroneSeeder(DroneComponentRepository droneComponentRepo,
                        DroneMilestoneRepository droneMilestoneRepository,
                        DroneSkillTierRepository droneSkillTierRepository,
                        StatKeyRepository statKeyRepo) {
                this.droneComponentRepo = droneComponentRepo;
                this.droneMilestoneRepository = droneMilestoneRepository;
                this.droneSkillTierRepository = droneSkillTierRepository;
                this.statKeyRepo = statKeyRepo;
        }

        public void seedDroneComponents() {
                int seededCount = 0;

                // { name, description, maxLevel, stats: { statKey, unlockLevel, baseValue,
                // increment }[] }
                List<Object[]> components = List.of(
                                new Object[] { "Radar Research",
                                                "Affects drone HP and reduces chances of taking critical hits", 12,
                                                List.of(
                                                                new Object[] { "drone_hp", 1, 2700.0, 197942.0 },
                                                                new Object[] { "crit_reduction_percent", 8, 1.0, 1.0 }),
                                                "https://fzyzmcjvvkmmdeuiuoan.supabase.co/storage/v1/object/public/lastwar-assets/drone/components/radar_research.png"
                                },
                                new Object[] { "Turbo Engine Research",
                                                "Affects Hero & Overlord HP and boosts hero HP percentage", 12,
                                                List.of(
                                                                new Object[] { "hero_overlord_hp", 1, 1350.0,
                                                                                144601.0 },
                                                                new Object[] { "hero_hp_boost_percent", 8, 5.0, 5.0 }),
                                                "https://fzyzmcjvvkmmdeuiuoan.supabase.co/storage/v1/object/public/lastwar-assets/drone/components/turbo_engine_research.png"
                                },
                                new Object[] { "External Armor Research",
                                                "Affects Hero & Overlord DEF and boosts hero defense percentage", 12,
                                                List.of(
                                                                new Object[] { "hero_overlord_def", 1, 6.0, 894.0 },
                                                                new Object[] { "hero_def_boost_percent", 8, 5.0, 5.0 }),
                                                "https://fzyzmcjvvkmmdeuiuoan.supabase.co/storage/v1/object/public/lastwar-assets/drone/components/external_armor_research.png"
                                },
                                new Object[] { "Thermal Imager Research",
                                                "Affects drone DEF and boosts crit rate percentage", 12,
                                                List.of(
                                                                new Object[] { "drone_def", 1, 13.0, 1873.0 },
                                                                new Object[] { "crit_rate_percent", 8, 1.0, 2.5 }),
                                                "https://fzyzmcjvvkmmdeuiuoan.supabase.co/storage/v1/object/public/lastwar-assets/drone/components/thermal_imager_research.png"
                                },
                                new Object[] { "Fuel Cell Research",
                                                "Affects drone ATK and boosts crit damage percentage", 12,
                                                List.of(
                                                                new Object[] { "drone_atk", 1, 64.0, 10637.0 },
                                                                new Object[] { "crit_damage_percent", 8, 3.0, 4.0 }),
                                                "https://fzyzmcjvvkmmdeuiuoan.supabase.co/storage/v1/object/public/lastwar-assets/drone/components/fuel_cell_research.png"
                                },
                                new Object[] { "Airborne Missile Research",
                                                "Affects Hero & Overlord ATK, boosts hero skill damage and hero ATK percentage",
                                                12,
                                                List.of(
                                                                new Object[] { "hero_overlord_atk", 1, 32.0, 3004.0 },
                                                                new Object[] { "hero_skill_damage_percent", 1, 0.25,
                                                                                0.75 },
                                                                new Object[] { "hero_atk_boost_percent", 8, 5.0, 5.0 }),
                                                "https://fzyzmcjvvkmmdeuiuoan.supabase.co/storage/v1/object/public/lastwar-assets/drone/components/airborne_missile_research.png"
                                });

                for (Object[] c : components) {
                        String name = (String) c[0];

                        DroneComponent dc = droneComponentRepo.findByName(name).orElseGet(() -> {
                                DroneComponent newDc = new DroneComponent();
                                newDc.setName(name);
                                return newDc;
                        });

                        dc.setDescription((String) c[1]);
                        dc.setMaxLevel((Integer) c[2]);
                        dc.setImageUrl((String) c[4]);

                        if (dc.getStats() == null || dc.getStats().isEmpty()) {
                                List<?> statsData = (List<?>) c[3];
                                List<DroneComponentStat> stats = new ArrayList<>();
                                for (Object statEntry : statsData) {
                                        Object[] s = (Object[]) statEntry;
                                        DroneComponentStat stat = new DroneComponentStat();
                                        stat.setDroneComponent(dc);
                                        stat.setUnlockLevel((Integer) s[1]);
                                        stat.setBaseValue((Double) s[2]);
                                        stat.setIncrement((Double) s[3]);
                                        statKeyRepo.findByKey((String) s[0]).ifPresent(stat::setStatKey);
                                        stats.add(stat);
                                }
                                dc.setStats(stats);
                        }

                        droneComponentRepo.save(dc);
                        seededCount++;
                }

                if (seededCount > 0) {
                        System.out.println("Seeded " + seededCount + " Drone Components.");
                } else {
                        System.out.println("Drone Components already seeded, skipping.");
                }
        }

        public void seedDrone() {
                if (droneMilestoneRepository.count() > 0) {
                        System.out.println("Drone already seeded, skipping.");
                        return;
                }

                // Seed milestones
                List<Object[]> milestones = List.of(
                                new Object[] { 50, "drone_atk", 5000.0 },
                                new Object[] { 100, "hero_atk_boost_percent", 5.0 },
                                new Object[] { 150, "hero_skill_damage_percent", 2.0 },
                                new Object[] { 200, "hero_damage_reduction_percent", 5.0 },
                                new Object[] { 250, "enemy_unit_death_rate_percent", 5.0 });

                for (Object[] m : milestones) {
                        DroneMilestone milestone = new DroneMilestone();
                        milestone.setUnlockLevel((Integer) m[0]);
                        milestone.setValue((Double) m[2]);
                        statKeyRepo.findByKey((String) m[1]).ifPresent(milestone::setStatKey);
                        droneMilestoneRepository.save(milestone);
                }

                // Seed skill tiers
                List<Object[]> skillTiers = List.of(
                                new Object[] { 0, 355.20,
                                                "Targeting 2 front-row units, dealing Physical Damage equal to 355.20% of the Attack. (Cooldown: 9s)" },
                                new Object[] { 1, 357.00, "The affected target takes 20% more damage for 3 seconds." },
                                new Object[] { 2, 428.40, "Deal 20% Extra Damage." },
                                new Object[] { 3, 479.52, "The affected target takes 25% more damage for 3 seconds." },
                                new Object[] { 4, 559.44, "Increase Additional Damage to 40%." },
                                new Object[] { 5, 568.40,
                                                "Affected targets take 25% increased damage, lasting for 4 seconds." });

                for (Object[] t : skillTiers) {
                        DroneSkillTier tier = new DroneSkillTier();
                        tier.setStarTier((Integer) t[0]);
                        tier.setDamageVar((Double) t[1]);
                        tier.setEffectDescription((String) t[2]);
                        droneSkillTierRepository.save(tier);
                }

                System.out.println("Seeded Drone milestones and skill tiers.");
        }
}
