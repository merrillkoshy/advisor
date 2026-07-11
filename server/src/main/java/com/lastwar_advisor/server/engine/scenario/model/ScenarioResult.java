package com.lastwar_advisor.server.engine.scenario.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ScenarioResult {
    private double winRate;
    private double avgHerosSurvived;
    private String tier;
    private String explanation;
}
