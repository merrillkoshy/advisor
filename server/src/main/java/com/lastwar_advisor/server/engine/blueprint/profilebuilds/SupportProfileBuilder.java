package com.lastwar_advisor.server.engine.blueprint.profilebuilds;

import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Component;

import com.lastwar_advisor.server.engine.blueprint.SkillTargetProfile;
import com.lastwar_advisor.server.engine.blueprint.SupportProfile;
import com.lastwar_advisor.server.engine.model.BuffType;
import com.lastwar_advisor.server.engine.model.TargetType;
import com.lastwar_advisor.server.entity.Hero;
import com.lastwar_advisor.server.entity.SkillEffect;
import com.lastwar_advisor.server.util.StatKeyConstants;

@Component
public class SupportProfileBuilder {
    public SupportProfile build(
            Hero hero,
            List<SkillTargetProfile> allSkillProfiles) {
        Set<TargetType> supportTargets = Set.of(
                TargetType.SELF, TargetType.ALL_ALLIES, TargetType.SELF_AND_ALLIES);

        List<SkillTargetProfile> supportSkills = allSkillProfiles.stream()
                .filter(stp -> supportTargets.contains(stp.getTargetType()))
                .toList();

        double teamHealPercent = hero.getSkills().stream()
                .flatMap(s -> s.getEffects().stream())
                .filter(e -> e.getStatKey() != null &&
                        e.getStatKey().getKey().equals(StatKeyConstants.TEAM_HEAL_HP_PERCENT))
                .mapToDouble(SkillEffect::getValue)
                .sum();

        List<BuffType> buffTypes = supportSkills.stream()
                .flatMap(stp -> stp.getMagnitudes().keySet().stream())
                .distinct()
                .toList();

        List<TargetType> scopes = supportSkills.stream()
                .map(SkillTargetProfile::getTargetType)
                .distinct()
                .toList();

        SupportProfile supportProfile = new SupportProfile();
        supportProfile.setSkills(supportSkills);
        supportProfile.setTeamHealPercent(teamHealPercent);
        supportProfile.setBuffTypes(buffTypes);
        supportProfile.setScopes(scopes);

        return supportProfile;
    }
}
