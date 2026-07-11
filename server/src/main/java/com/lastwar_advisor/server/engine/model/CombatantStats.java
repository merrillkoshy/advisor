package com.lastwar_advisor.server.engine.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CombatantStats {
    // Drone
    private Double droneLevel;
    private Integer droneStarTier;
    private Double droneDamageVar; // from DroneSkillTier
    private Double droneAtk; // from components + milestones
    private Double droneHp; // from components + milestones
    private Double droneDef; // from components + milestones
    private Double droneHeroAtkBoostPercent; // from components + milestones
    private Double droneHeroHpBoostPercent; // from components + milestones
    private Double droneHeroDefBoostPercent; // from components + milestones
    private Double critRatePercent;
    private Double critDamagePercent;
    // tech and overlord fields coming later
}
