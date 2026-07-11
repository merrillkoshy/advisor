package com.lastwar_advisor.server.engine.scenario.model;

import java.util.List;
import java.util.Map;

import com.lastwar_advisor.server.engine.model.DamageEvent;
import com.lastwar_advisor.server.engine.scenario.state.HeroState;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TickResult {
    private List<HeroState> killed;
    private List<SkillActivation> activations;
    private Map<HeroState, Double> damageDealt;
    private double elapsedTime;
    private boolean battleOver;
    private List<DamageEvent> damageEvents;
}
