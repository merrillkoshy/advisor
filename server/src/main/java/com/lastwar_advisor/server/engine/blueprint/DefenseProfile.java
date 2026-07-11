package com.lastwar_advisor.server.engine.blueprint;

import java.util.List;

import com.lastwar_advisor.server.engine.model.DamageReduction;
import com.lastwar_advisor.server.engine.model.DamageType;
import com.lastwar_advisor.server.engine.model.HeroType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DefenseProfile {
    private double effectiveHP;
    private double critChanceReductionPercent;
    private double critDamageReductionPercent;
    private DamageType vulnerability;
    private List<DamageReduction> reductions;
    private ShieldProfile shield;
    private HeroType type;
    private double typeDefenseMultiplier; // are they being countered?
}
