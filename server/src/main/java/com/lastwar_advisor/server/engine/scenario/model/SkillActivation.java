package com.lastwar_advisor.server.engine.scenario.model;

import java.util.List;

import com.lastwar_advisor.server.engine.model.SkillType;
import com.lastwar_advisor.server.engine.scenario.state.HeroState;
import com.lastwar_advisor.server.entity.Skill;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SkillActivation {
    private Skill skill;
    private HeroState caster;
    private List<HeroState> targets; // already resolved, not positions
    private SkillType skillType;
    private boolean isIdeal; // did it fire without debuff interference?
}
