package com.lastwar_advisor.server.engine.blueprint.profilebuilds;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.lastwar_advisor.server.engine.blueprint.DefenseProfile;
import com.lastwar_advisor.server.engine.blueprint.ShieldProfile;
import com.lastwar_advisor.server.engine.model.DamageReduction;
import com.lastwar_advisor.server.engine.model.DamageType;
import com.lastwar_advisor.server.engine.model.HeroType;
import com.lastwar_advisor.server.engine.model.ShieldTrigger;
import com.lastwar_advisor.server.entity.GameConstant;
import com.lastwar_advisor.server.entity.Hero;
import com.lastwar_advisor.server.entity.SkillEffect;
import com.lastwar_advisor.server.repository.GameConstantRepository;
import com.lastwar_advisor.server.util.StatKeyConstants;

@Component
public class DefenseProfileBuilder {
    public DefenseProfile build(Hero hero, List<SkillEffect> skillEffects,
            GameConstantRepository gameConstantRepository) {
        double defScalingConstant = gameConstantRepository
                .findByKey("DEF_SCALING_CONSTANT")
                .map(GameConstant::getValue)
                .orElse(5000.0); // safe fallback
        double def = hero.getDef();
        double hp = hero.getHp();
        double damageReduction = def / (def + defScalingConstant);
        double effectiveHP = hp / (1 - damageReduction);

        // Flatten all skill effects into a single stream for lookup
        List<SkillEffect> allEffects = skillEffects;
        // List<SkillEffect> allEffects = hero.getSkills().stream()
        // .flatMap(s -> s.getEffects().stream())
        // .filter(e -> e.getStatKey() != null && e.getValue() != null)
        // .toList();

        // Helper to sum values by stat key
        java.util.function.Function<String, Double> sumByKey = key -> allEffects.stream()
                .filter(e -> e.getStatKey().getKey().equals(key))
                .mapToDouble(SkillEffect::getValue)
                .sum();

        // DamageReduction list
        List<DamageReduction> reductions = new ArrayList<>();

        double physicalDR = sumByKey.apply(StatKeyConstants.PHYSICAL_DAMAGE_REDUCTION_PERCENT)
                + sumByKey.apply(StatKeyConstants.PHYSICAL_DAMAGE_RESISTANCE_PERCENT);
        if (physicalDR > 0) {
            DamageReduction dr = new DamageReduction();
            dr.setType(DamageType.PHYSICAL);
            dr.setPercent((float) physicalDR);
            dr.setTrigger(ShieldTrigger.PASSIVE); // TODO: Until we work with ON_HIT and ACTIVE
            reductions.add(dr);
        }

        double energyDR = sumByKey.apply(StatKeyConstants.ENERGY_DAMAGE_REDUCTION_PERCENT)
                + sumByKey.apply(StatKeyConstants.ENERGY_DAMAGE_RESISTANCE_PERCENT);
        if (energyDR > 0) {
            DamageReduction dr = new DamageReduction();
            dr.setType(DamageType.ENERGY);
            dr.setPercent((float) energyDR);
            dr.setTrigger(ShieldTrigger.PASSIVE); // TODO: Until we work with ON_HIT and ACTIVE
            reductions.add(dr);
        }

        // resistance_all_dmg_percent and hero_damage_reduction_percent → both types
        double allDR = sumByKey.apply(StatKeyConstants.RESISTANCE_ALL_DMG_PERCENT)
                + sumByKey.apply(StatKeyConstants.HERO_DAMAGE_REDUCTION_PERCENT);
        if (allDR > 0) {
            for (DamageType type : DamageType.values()) {
                DamageReduction dr = new DamageReduction();
                dr.setType(type);
                dr.setPercent((float) allDR);
                dr.setTrigger(ShieldTrigger.PASSIVE); // TODO: Until we work with ON_HIT and ACTIVE
                reductions.add(dr);
            }
        }

        // ShieldProfile
        ShieldProfile shieldProfile = hero.getSkills().stream()
                .filter(s -> s.getEffects().stream()
                        .anyMatch(e -> e.getStatKey() != null &&
                                e.getStatKey().getKey().equals(StatKeyConstants.SHIELD_HP_PERCENT)))
                .findFirst()
                .map(s -> {
                    double shieldValue = s.getEffects().stream()
                            .filter(e -> e.getStatKey().getKey().equals(StatKeyConstants.SHIELD_HP_PERCENT))
                            .mapToDouble(SkillEffect::getValue)
                            .findFirst()
                            .orElse(0.0);
                    ShieldProfile sp = new ShieldProfile();
                    sp.setHasShield(true);
                    sp.setAbsorbType(s.getDamageType() != null
                            ? DamageType.valueOf(s.getDamageType().toUpperCase())
                            : null);
                    sp.setShieldValuePercent(shieldValue);
                    sp.setTrigger(s.getType().equals("PASSIVE") ? ShieldTrigger.PASSIVE : ShieldTrigger.ACTIVE);
                    return sp;
                })
                .orElseGet(() -> {
                    ShieldProfile sp = new ShieldProfile();
                    sp.setHasShield(false);
                    return sp;
                });

        // Crit reductions
        double critChanceReduction = sumByKey.apply(StatKeyConstants.REDUCE_CRIT_CHANCE_PERCENT);
        double critDamageReduction = sumByKey.apply(StatKeyConstants.REDUCE_CRIT_DAMAGE_PERCENT);

        // vulnerability — enemy-dependent, scenario sets it
        DefenseProfile defenseProfile = new DefenseProfile();
        defenseProfile.setEffectiveHP(effectiveHP);
        defenseProfile.setReductions(reductions);
        ;
        defenseProfile.setShield(shieldProfile);
        defenseProfile.setType(HeroType.valueOf(hero.getType().toUpperCase()));
        defenseProfile.setTypeDefenseMultiplier(1.0); // neutral
                                                      // default,
                                                      // scenario
                                                      // overwrites
        defenseProfile.setVulnerability(null); // enemy-dependent, scenario sets it
        defenseProfile.setCritChanceReductionPercent(critChanceReduction);
        defenseProfile.setCritDamageReductionPercent(critDamageReduction);
        return defenseProfile;
    }
}
