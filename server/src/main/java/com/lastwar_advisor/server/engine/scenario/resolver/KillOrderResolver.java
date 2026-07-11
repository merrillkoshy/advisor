package com.lastwar_advisor.server.engine.scenario.resolver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.lastwar_advisor.server.engine.model.CombatantStats;
import com.lastwar_advisor.server.engine.model.DamageEvent;
import com.lastwar_advisor.server.engine.model.SkillType;
import com.lastwar_advisor.server.engine.scenario.model.SkillActivation;
import com.lastwar_advisor.server.engine.scenario.state.BattleState;
import com.lastwar_advisor.server.engine.scenario.state.HeroState;
import com.lastwar_advisor.server.entity.Skill;

@Component
public class KillOrderResolver {

    public List<HeroState> resolve(
            List<HeroState> playerStates,
            List<HeroState> enemyStates,
            Map<HeroState, Double> damageMap) {

        List<HeroState> killed = new ArrayList<>();

        // Apply damage to all heroes simultaneously
        for (Map.Entry<HeroState, Double> entry : damageMap.entrySet()) {
            HeroState target = entry.getKey();
            double incomingDamage = entry.getValue();

            if (!target.isAlive())
                continue;

            target.setCurrentHP(target.getCurrentHP() - incomingDamage);

            if (target.getCurrentHP() <= 0) {
                target.setCurrentHP(0);
                target.setAlive(false);
                killed.add(target);
            }
            if (target.getHero().getName().equals("Lucius")) {
                System.out.println("LUCIUS HP: " + target.getCurrentHP() + " after taking " + incomingDamage);
            }
        }

        return killed;
    }

    // Aggregate all damage targeting the same hero across all activations
    public Map<HeroState, Double> buildDamageMap(
            List<SkillActivation> activations,
            BattleState battleState,
            DamageCalculator damageCalculator,
            CombatantStats attackerStats,
            CombatantStats defenderStats,
            List<DamageEvent> damageEvents,
            double elapsedTime) {

        Map<HeroState, Double> damageMap = new HashMap<>();

        for (SkillActivation activation : activations) {
            Skill skill = activation.getSkill();
            HeroState caster = activation.getCaster();

            boolean casterIsPlayer = battleState.getPlayerStates().contains(caster);
            CombatantStats stats = casterIsPlayer ? attackerStats : defenderStats;

            boolean isNormalAttack = activation.getSkillType() == SkillType.NORMAL_ATTACK;

            // Only damage-dealing skills (normal attack always deals damage)
            if (!isNormalAttack && skill.getDamageType() == null)
                continue;

            for (HeroState target : activation.getTargets()) {
                if (target == caster)
                    continue;
                if (!target.isAlive())
                    continue;

                double damage = isNormalAttack
                        ? damageCalculator.calculateNormalAttack(caster, target, stats)
                        : damageCalculator.calculate(caster, target, skill, stats);

                damageMap.merge(target, damage, Double::sum);
                DamageEvent event = new DamageEvent();
                event.setAttackerName(caster.getHero().getName());
                event.setTargetName(target.getHero().getName());
                event.setSkillName(isNormalAttack ? "Normal Attack" : skill.getName());
                event.setRawDamage(damage);
                event.setTimestamp(elapsedTime);
                damageEvents.add(event);
            }
        }

        return damageMap;
    }
}
