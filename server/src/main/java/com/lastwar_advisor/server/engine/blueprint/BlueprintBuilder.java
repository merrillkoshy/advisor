package com.lastwar_advisor.server.engine.blueprint;

import java.util.List;

import org.springframework.stereotype.Service;

import com.lastwar_advisor.server.engine.blueprint.profilebuilds.DefenseProfileBuilder;
import com.lastwar_advisor.server.engine.blueprint.profilebuilds.OffenseProfileBuilder;
import com.lastwar_advisor.server.engine.blueprint.profilebuilds.SkillTargetProfileMapper;
import com.lastwar_advisor.server.engine.blueprint.profilebuilds.SupportProfileBuilder;
import com.lastwar_advisor.server.engine.blueprint.profilebuilds.SynergyProfileBuilder;
import com.lastwar_advisor.server.engine.model.CombatantStats;
import com.lastwar_advisor.server.engine.model.HeroBehaviorBlueprint;
import com.lastwar_advisor.server.engine.model.HeroType;
import com.lastwar_advisor.server.entity.Hero;
import com.lastwar_advisor.server.entity.SkillEffect;
import com.lastwar_advisor.server.entity.SlotPosition;
import com.lastwar_advisor.server.repository.GameConstantRepository;

@Service
public class BlueprintBuilder {
    private final SkillTargetProfileMapper skillTargetProfileMapper;
    private final GameConstantRepository gameConstantRepository;
    private final DefenseProfileBuilder defenseProfileBuilder;
    private final OffenseProfileBuilder offenseProfileBuilder;
    private final SupportProfileBuilder supportProfileBuilder;
    private final SynergyProfileBuilder synergyProfileBuilder;

    public BlueprintBuilder(SkillTargetProfileMapper skillTargetProfileMapper,
            GameConstantRepository gameConstantRepository,
            DefenseProfileBuilder defenseProfileBuilder,
            OffenseProfileBuilder offenseProfileBuilder,
            SupportProfileBuilder supportProfileBuilder,
            SynergyProfileBuilder synergyProfileBuilder) {
        this.skillTargetProfileMapper = skillTargetProfileMapper;
        this.gameConstantRepository = gameConstantRepository;
        this.defenseProfileBuilder = defenseProfileBuilder;
        this.offenseProfileBuilder = offenseProfileBuilder;
        this.supportProfileBuilder = supportProfileBuilder;
        this.synergyProfileBuilder = synergyProfileBuilder;
    }

    public HeroBehaviorBlueprint build(
            Hero hero,
            SlotPosition position,
            List<Hero> squadContext, // for synergy calculation
            List<SkillEffect> skillEffects,
            CombatantStats stats // drone, tech, overlord modifiers
    ) {

        // SynergyProfile
        SynergyProfile synergyProfile = synergyProfileBuilder.build(hero, squadContext);

        List<SkillTargetProfile> allSkillProfiles = skillTargetProfileMapper.mapHeroToSkillTargetProfiles(hero);
        // DefenseProfile
        DefenseProfile defenseProfile = defenseProfileBuilder.build(hero, skillEffects, gameConstantRepository);
        // OffenseProfile
        OffenseProfile offenseProfile = offenseProfileBuilder.build(hero, allSkillProfiles);
        // SupportProfile
        SupportProfile supportProfile = supportProfileBuilder.build(hero, allSkillProfiles);

        // Final
        HeroBehaviorBlueprint blueprint = new HeroBehaviorBlueprint();
        blueprint.setHeroName(hero.getName());
        blueprint.setType(HeroType.valueOf(hero.getType().toUpperCase()));
        blueprint.setPosition(position);
        blueprint.setSkillTargets(allSkillProfiles);
        blueprint.setSynergyProfile(synergyProfile);
        blueprint.setDefenseProfile(defenseProfile);
        blueprint.setOffenseProfile(offenseProfile);
        blueprint.setSupportProfile(supportProfile);
        blueprint.setStatusReactions(null); // scenario feeds this
        return blueprint;

    }
}
