package com.lastwar_advisor.server.engine.model;

import lombok.Data;

@Data
public class DamageEvent {
    private String attackerName;
    private String targetName;
    private String skillName;
    private double rawDamage;
    private double timestamp;
}
