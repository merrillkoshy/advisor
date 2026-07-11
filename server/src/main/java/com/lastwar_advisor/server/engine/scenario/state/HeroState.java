package com.lastwar_advisor.server.engine.scenario.state;

import java.util.Map;

import com.lastwar_advisor.server.engine.model.HeroBehaviorBlueprint;
import com.lastwar_advisor.server.engine.model.StatusEffect;
import com.lastwar_advisor.server.engine.scenario.model.DebuffState;
import com.lastwar_advisor.server.entity.Hero;
import com.lastwar_advisor.server.entity.SlotPosition;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HeroState {
    private Hero hero;
    private HeroBehaviorBlueprint blueprint;
    private SlotPosition position;
    private double currentHP;
    private double maxHp;
    private boolean alive;
    Map<StatusEffect, DebuffState> activeDebuffs; // StatusEffect → full debuff info
    private Map<String, Double> skillCooldowns; // skill name → remaining cooldown
    private double normalAttackCooldown;
    private boolean shieldActive;
    private double shieldHP;
    private HeroState tauntSource; // null if no taunt active
}