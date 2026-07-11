package com.lastwar_advisor.server.engine.model;

import java.util.List;

import lombok.Data;

@Data
public class TickSnapshot {
    private double elapsedTime;
    private List<HeroSnapshot> playerStates;
    private List<HeroSnapshot> enemyStates;
    private List<DamageEvent> damageEvents;
}
