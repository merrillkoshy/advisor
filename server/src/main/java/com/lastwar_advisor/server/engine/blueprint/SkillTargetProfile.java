package com.lastwar_advisor.server.engine.blueprint;

import java.util.Map;

import com.lastwar_advisor.server.engine.model.BuffType;
import com.lastwar_advisor.server.engine.model.DamageType;
import com.lastwar_advisor.server.engine.model.SkillType;
import com.lastwar_advisor.server.engine.model.TargetType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SkillTargetProfile {
    private String skillName;
    private SkillType skillType; // ACTIVE, PASSIVE, ULTIMATE
    private TargetType targetType;
    private double cooldown;
    private DamageType damageType;
    private Map<BuffType, Double> magnitudes;
    // damage % or buff % depending on skill
}
