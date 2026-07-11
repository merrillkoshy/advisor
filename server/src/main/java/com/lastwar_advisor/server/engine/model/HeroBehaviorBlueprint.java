package com.lastwar_advisor.server.engine.model;

import java.util.List;

import com.lastwar_advisor.server.engine.blueprint.DefenseProfile;
import com.lastwar_advisor.server.engine.blueprint.OffenseProfile;
import com.lastwar_advisor.server.engine.blueprint.SkillTargetProfile;
import com.lastwar_advisor.server.engine.blueprint.StatusReaction;
import com.lastwar_advisor.server.engine.blueprint.SupportProfile;
import com.lastwar_advisor.server.engine.blueprint.SynergyProfile;
import com.lastwar_advisor.server.entity.SlotPosition;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HeroBehaviorBlueprint {
    private String heroName;
    private HeroType type;
    private SlotPosition position;

    private DefenseProfile defenseProfile;
    private OffenseProfile offenseProfile;
    private SupportProfile supportProfile;
    private List<SkillTargetProfile> skillTargets;
    private List<StatusReaction> statusReactions;
    private SynergyProfile synergyProfile;
}
