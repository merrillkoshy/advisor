package com.lastwar_advisor.server.engine.scenario.graph;

import com.lastwar_advisor.server.entity.SlotPosition;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AttackRoute {
    private SlotPosition attacker;
    private SlotPosition target;

    public AttackRoute(SlotPosition attacker, SlotPosition target) {
        this.attacker = attacker;
        this.target = target;
    }
}
