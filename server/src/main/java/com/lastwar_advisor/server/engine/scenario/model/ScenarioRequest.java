package com.lastwar_advisor.server.engine.scenario.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ScenarioRequest {
    private Long playerSquadId;
    private Long enemySquadId;
    private String battleId;
}
