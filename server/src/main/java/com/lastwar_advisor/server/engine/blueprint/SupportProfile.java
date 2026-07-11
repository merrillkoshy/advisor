package com.lastwar_advisor.server.engine.blueprint;

import java.util.List;

import com.lastwar_advisor.server.engine.model.BuffType;
import com.lastwar_advisor.server.engine.model.TargetType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SupportProfile {
    private List<SkillTargetProfile> skills;
    private double teamHealPercent;
    private List<BuffType> buffTypes;
    private List<TargetType> scopes; // SELF, FRONT_ROW, ALL_ALLIES, SELF_AND_ALLIES
}
