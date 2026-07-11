package com.lastwar_advisor.server.engine.blueprint.profilebuilds;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.lastwar_advisor.server.engine.blueprint.OffenseProfile;
import com.lastwar_advisor.server.engine.blueprint.SkillTargetProfile;
import com.lastwar_advisor.server.engine.model.DamageType;
import com.lastwar_advisor.server.engine.model.HeroType;
import com.lastwar_advisor.server.engine.model.TargetType;
import com.lastwar_advisor.server.entity.Hero;

@Component
public class OffenseProfileBuilder {
    public OffenseProfile build(
            Hero hero,
            List<SkillTargetProfile> allSkillProfiles) {

        Set<TargetType> offensiveTargets = Set.of(
                TargetType.RANDOM, TargetType.SINGLE, TargetType.FRONT_ROW,
                TargetType.BACK_ROW, TargetType.ALL_ENEMIES, TargetType.HIGHEST_HP, TargetType.LOWEST_HP);

        List<SkillTargetProfile> offensiveSkills = allSkillProfiles.stream()
                .filter(stp -> offensiveTargets.contains(stp.getTargetType()))
                .toList();

        double baseAttackPower = hero.getAtk();

        // Primary damage type — whichever type appears most across offensive skills
        DamageType primaryDamageType = offensiveSkills.stream()
                .filter(stp -> stp.getDamageType() != null)
                .collect(Collectors.groupingBy(SkillTargetProfile::getDamageType, Collectors.counting()))
                .entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(DamageType.PHYSICAL);

        OffenseProfile offenseProfile = new OffenseProfile();
        offenseProfile.setType(HeroType.valueOf(hero.getType().toUpperCase()));
        offenseProfile.setTypeAttackMultiplier(1.0); // scenario overwrites
        offenseProfile.setPrimaryDamageType(primaryDamageType);
        offenseProfile.setBaseAttackPower(baseAttackPower);
        offenseProfile.setSkills(offensiveSkills);
        return offenseProfile;
    }
}
