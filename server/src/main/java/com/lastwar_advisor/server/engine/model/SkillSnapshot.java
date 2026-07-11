package com.lastwar_advisor.server.engine.model;

import lombok.Data;

@Data
public class SkillSnapshot {
    private String skillName;
    private double cooldownRemaining;
    private boolean firedThisTick;
}
