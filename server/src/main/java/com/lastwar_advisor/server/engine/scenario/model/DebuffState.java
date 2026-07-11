package com.lastwar_advisor.server.engine.scenario.model;

import com.lastwar_advisor.server.engine.model.StatusEffect;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DebuffState {
    private StatusEffect effect;
    private double remainingDuration;
    private double magnitude; // reduction percent from the skill that applied it
}
