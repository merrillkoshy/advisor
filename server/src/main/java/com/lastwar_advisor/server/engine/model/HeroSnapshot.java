package com.lastwar_advisor.server.engine.model;

import java.util.Map;

import lombok.Data;

@Data
public class HeroSnapshot {
    private String heroName;
    private double currentHp;
    private double maxHp;
    private boolean alive;
    private Map<String, SkillSnapshot> skills; // skill name → snapshot
}
