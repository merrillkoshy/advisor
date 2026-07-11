package com.lastwar_advisor.server.engine.scenario.state;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BattleState {
    private List<HeroState> playerStates;
    private List<HeroState> enemyStates;
    private double elapsedTime;
    private boolean battleOver;
    private String winningSide; // "player" or "enemy"
}
