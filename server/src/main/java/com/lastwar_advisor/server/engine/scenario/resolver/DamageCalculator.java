package com.lastwar_advisor.server.engine.scenario.resolver;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.lastwar_advisor.server.engine.model.CombatantStats;
import com.lastwar_advisor.server.engine.model.DamageReduction;
import com.lastwar_advisor.server.engine.model.DamageType;
import com.lastwar_advisor.server.engine.model.HeroType;
import com.lastwar_advisor.server.engine.model.StatusEffect;
import com.lastwar_advisor.server.engine.scenario.state.HeroState;
import com.lastwar_advisor.server.entity.Skill;
import com.lastwar_advisor.server.entity.SkillEffect;
import com.lastwar_advisor.server.util.StatKeyConstants;

@Component
public class DamageCalculator {

        private static final double MAGIC_DEFENSE_CONSTANT = 4000; // Constant used in the damage mitigation formula

        private static final Map<HeroType, HeroType> TYPE_COUNTER = Map.of(
                        HeroType.AIRCRAFT, HeroType.TANK, // AIR counters TANK
                        HeroType.TANK, HeroType.MISSILE_VEHICLE, // TANK counters MISSILE
                        HeroType.MISSILE_VEHICLE, HeroType.AIRCRAFT // MISSILE counters AIR
        );

        public double calculate(
                        HeroState attacker,
                        HeroState defender,
                        Skill skill,
                        CombatantStats attackerStats) {

                // 1. Base damage
                double atk = attacker.getHero().getAtk();

                // 2. Drone ATK boost
                if (attackerStats.getDroneAtk() != null) {
                        atk += attackerStats.getDroneAtk();
                }
                if (attackerStats.getDroneHeroAtkBoostPercent() != null) {
                        atk *= (1 + attackerStats.getDroneHeroAtkBoostPercent() / 100);
                }

                // 3. Skill magnitude — atk_percent at level 40
                double atkPercent = skill.getEffects().stream()
                                .filter(e -> e.getStatKey() != null &&
                                                e.getStatKey().getKey().equals(StatKeyConstants.ATK_PERCENT) &&
                                                e.getValue() != null)
                                .mapToDouble(SkillEffect::getValue)
                                .findFirst()
                                .orElse(100.0); // fallback to 100% if no scaling found
                double rawDamage = atk * (atkPercent / 100);

                // 4. Synergy multiplier
                double synergyMultiplier = attacker.getBlueprint()
                                .getSynergyProfile().getSynergyMultiplier();
                rawDamage *= (1 + synergyMultiplier);

                // 5. Type advantage
                HeroType attackerType = attacker.getBlueprint().getOffenseProfile().getType();
                HeroType defenderType = defender.getBlueprint().getDefenseProfile().getType();
                if (TYPE_COUNTER.get(attackerType) == defenderType) {
                        rawDamage *= 1.20; // attacker counters defender
                } else if (TYPE_COUNTER.get(defenderType) == attackerType) {
                        rawDamage *= 0.80; // defender counters attacker
                }

                // 6. Attacker debuffs — reduce outgoing damage
                DamageType skillDamageType = skill.getDamageType() != null
                                ? DamageType.valueOf(skill.getDamageType().toUpperCase())
                                : DamageType.PHYSICAL;

                if (attacker.getActiveDebuffs().containsKey(StatusEffect.DAMAGE_DEBUFF)) {
                        double reduction = attacker.getActiveDebuffs()
                                        .get(StatusEffect.DAMAGE_DEBUFF).getMagnitude() / 100;
                        rawDamage *= (1 - reduction);
                }
                if (skillDamageType == DamageType.ENERGY
                                && attacker.getActiveDebuffs().containsKey(StatusEffect.ENERGY_DEBUFF)) {
                        double reduction = attacker.getActiveDebuffs()
                                        .get(StatusEffect.ENERGY_DEBUFF).getMagnitude() / 100;
                        rawDamage *= (1 - reduction);
                }
                if (skillDamageType == DamageType.PHYSICAL
                                && attacker.getActiveDebuffs().containsKey(StatusEffect.PHYSICAL_DEBUFF)) {
                        double reduction = attacker.getActiveDebuffs()
                                        .get(StatusEffect.PHYSICAL_DEBUFF).getMagnitude() / 100;
                        rawDamage *= (1 - reduction);
                }

                // 7. Crit
                double critRate = attackerStats.getCritRatePercent() != null
                                ? attackerStats.getCritRatePercent() / 100
                                : 0.0;
                double critDamage = attackerStats.getCritDamagePercent() != null
                                ? attackerStats.getCritDamagePercent() / 100
                                : 0.0;
                double defenderCritRateReduction = defender.getBlueprint()
                                .getDefenseProfile().getCritChanceReductionPercent() / 100;
                double defenderCritDamageReduction = defender.getBlueprint()
                                .getDefenseProfile().getCritDamageReductionPercent() / 100;

                double effectiveCritRate = Math.max(0, critRate - defenderCritRateReduction);
                double effectiveCritDamage = Math.max(0, critDamage - defenderCritDamageReduction);
                // Use average crit contribution — deterministic, no RNG
                rawDamage *= (1 + effectiveCritRate * effectiveCritDamage);

                // 8. Defender damage reduction
                List<DamageReduction> reductions = defender.getBlueprint()
                                .getDefenseProfile().getReductions();
                if (reductions != null) {
                        for (DamageReduction dr : reductions) {
                                if (dr.getType() == skillDamageType) {
                                        rawDamage *= (1 - dr.getPercent() / 100);
                                }
                        }
                }
                // 8.5 DEF mitigation — Damage Taken = rawDamage × 4000 / (DEF + 4000)
                double defenderDef = defender.getHero().getDef();
                rawDamage = rawDamage * MAGIC_DEFENSE_CONSTANT / (defenderDef + MAGIC_DEFENSE_CONSTANT);

                // 9. Shield absorbs first
                if (defender.isShieldActive() && defender.getShieldHP() > 0) {
                        double absorbed = Math.min(defender.getShieldHP(), rawDamage);
                        defender.setShieldHP(defender.getShieldHP() - absorbed);
                        rawDamage -= absorbed;
                        if (defender.getShieldHP() <= 0) {
                                defender.setShieldActive(false);
                                defender.setShieldHP(0);
                        }
                }

                return Math.round(Math.max(0, rawDamage) * 10.0) / 10.0;
        }

        public double calculateNormalAttack(
                        HeroState attacker,
                        HeroState defender,
                        CombatantStats attackerStats) {

                double atk = attacker.getHero().getAtk();

                if (attackerStats.getDroneAtk() != null) {
                        atk += attackerStats.getDroneAtk();
                }
                if (attackerStats.getDroneHeroAtkBoostPercent() != null) {
                        atk *= (1 + attackerStats.getDroneHeroAtkBoostPercent() / 100);
                }

                double rawDamage = atk; // no skill scaling — normal attack is 100% atk

                double synergyMultiplier = attacker.getBlueprint()
                                .getSynergyProfile().getSynergyMultiplier();
                rawDamage *= (1 + synergyMultiplier);

                HeroType attackerType = attacker.getBlueprint().getOffenseProfile().getType();
                HeroType defenderType = defender.getBlueprint().getDefenseProfile().getType();
                if (TYPE_COUNTER.get(attackerType) == defenderType) {
                        rawDamage *= 1.20;
                } else if (TYPE_COUNTER.get(defenderType) == attackerType) {
                        rawDamage *= 0.80;
                }

                DamageType damageType = DamageType.PHYSICAL;

                if (attacker.getActiveDebuffs().containsKey(StatusEffect.DAMAGE_DEBUFF)) {
                        double reduction = attacker.getActiveDebuffs()
                                        .get(StatusEffect.DAMAGE_DEBUFF).getMagnitude() / 100;
                        rawDamage *= (1 - reduction);
                }
                if (attacker.getActiveDebuffs().containsKey(StatusEffect.PHYSICAL_DEBUFF)) {
                        double reduction = attacker.getActiveDebuffs()
                                        .get(StatusEffect.PHYSICAL_DEBUFF).getMagnitude() / 100;
                        rawDamage *= (1 - reduction);
                }

                double critRate = attackerStats.getCritRatePercent() != null
                                ? attackerStats.getCritRatePercent() / 100
                                : 0.0;
                double critDamage = attackerStats.getCritDamagePercent() != null
                                ? attackerStats.getCritDamagePercent() / 100
                                : 0.0;
                double defenderCritRateReduction = defender.getBlueprint()
                                .getDefenseProfile().getCritChanceReductionPercent() / 100;
                double defenderCritDamageReduction = defender.getBlueprint()
                                .getDefenseProfile().getCritDamageReductionPercent() / 100;

                double effectiveCritRate = Math.max(0, critRate - defenderCritRateReduction);
                double effectiveCritDamage = Math.max(0, critDamage - defenderCritDamageReduction);
                rawDamage *= (1 + effectiveCritRate * effectiveCritDamage);

                List<DamageReduction> reductions = defender.getBlueprint()
                                .getDefenseProfile().getReductions();
                if (reductions != null) {
                        for (DamageReduction dr : reductions) {
                                if (dr.getType() == damageType) {
                                        rawDamage *= (1 - dr.getPercent() / 100);
                                }
                        }
                }

                double defenderDef = defender.getHero().getDef();
                rawDamage = rawDamage * MAGIC_DEFENSE_CONSTANT / (defenderDef + MAGIC_DEFENSE_CONSTANT);

                if (defender.isShieldActive() && defender.getShieldHP() > 0) {
                        double absorbed = Math.min(defender.getShieldHP(), rawDamage);
                        defender.setShieldHP(defender.getShieldHP() - absorbed);
                        rawDamage -= absorbed;
                        if (defender.getShieldHP() <= 0) {
                                defender.setShieldActive(false);
                                defender.setShieldHP(0);
                        }
                }

                return Math.round(Math.max(0, rawDamage) * 10.0) / 10.0;
        }
}
