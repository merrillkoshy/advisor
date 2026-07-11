package com.lastwar_advisor.server.engine.blueprint;

import java.util.List;

import com.lastwar_advisor.server.engine.model.DamageType;
import com.lastwar_advisor.server.engine.model.HeroType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OffenseProfile {
    private HeroType type;
    private double typeAttackMultiplier; // are they countering?
    private DamageType primaryDamageType;
    private double baseAttackPower;
    private List<SkillTargetProfile> skills;
}
