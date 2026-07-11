package com.lastwar_advisor.server.engine.scenario.resolver;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Component;

import com.lastwar_advisor.server.engine.model.SkillType;
import com.lastwar_advisor.server.engine.model.StatusEffect;
import com.lastwar_advisor.server.engine.model.TargetType;
import com.lastwar_advisor.server.engine.scenario.graph.AttackRoute;
import com.lastwar_advisor.server.engine.scenario.model.SkillActivation;
import com.lastwar_advisor.server.engine.scenario.state.BattleState;
import com.lastwar_advisor.server.engine.scenario.state.HeroState;
import com.lastwar_advisor.server.entity.Skill;
import com.lastwar_advisor.server.entity.SlotPosition;
import com.lastwar_advisor.server.util.TargetTypeMapper;

@Component
public class SkillActivationResolver {

        private record TargetResolution(List<HeroState> targets, TargetType targetType) {
        }

        public List<SkillActivation> resolve(
                        HeroState caster,
                        BattleState battleState,
                        List<AttackRoute> currentRoutes,
                        double tickDuration) {

                List<SkillActivation> activations = new ArrayList<>();

                if (!caster.isAlive())
                        return activations;

                boolean isStunned = caster.getActiveDebuffs().containsKey(StatusEffect.STUN);

                // --- Attack skill resolution (existing loop, decides if any attack skill
                // fires) ---
                boolean attackSkillFired = false;

                for (Skill skill : caster.getHero().getSkills()) {
                        String skillName = skill.getName();
                        SkillType skillType = SkillType.valueOf(skill.getType().toUpperCase());

                        // Stat boost ultimates — applied at battle start, never fire mid-battle
                        if (skill.getKeyStats() != null && skill.getKeyStats().contains("Stat Boost")
                                        && skillType == SkillType.ULTIMATE)
                                continue;

                        // Stun blocks ACTIVE and ULTIMATE
                        if (isStunned && (skillType == SkillType.ACTIVE || skillType == SkillType.ULTIMATE))
                                continue;

                        // Tick down cooldown unconditionally
                        double remainingCooldown = caster.getSkillCooldowns().getOrDefault(skillName, 0.0);
                        double newCooldown = remainingCooldown - tickDuration;
                        caster.getSkillCooldowns().put(skillName, newCooldown);
                        if (newCooldown > 0)
                                continue; // not ready yet

                        // Resolve targets
                        TargetResolution resolution = resolveTargets(skill, caster, battleState, currentRoutes);
                        List<HeroState> targets = resolution.targets();
                        if (targets.isEmpty() && skillType != SkillType.PASSIVE)
                                continue;

                        // Build activation
                        SkillActivation activation = new SkillActivation();
                        activation.setSkill(skill);
                        activation.setCaster(caster);
                        activation.setTargets(targets);
                        activation.setSkillType(skillType);
                        activation.setIdeal(caster.getActiveDebuffs().isEmpty());
                        activations.add(activation);

                        TargetType resolvedTargetType = skill.getTarget() != null
                                        ? TargetTypeMapper.map(skill.getTarget())
                                        : TargetType.SELF;

                        if (isAttackTargetType(resolvedTargetType)) {
                                attackSkillFired = true;
                        }

                        // Reset cooldown after successful activation
                        if (skill.getCooldown() != null && skill.getCooldown() > 0) {
                                caster.getSkillCooldowns().put(skillName, skill.getCooldown());
                        }
                }
                // --- Normal attack (only fires if no attack skill claimed this action) ---
                if (!attackSkillFired && !isStunned) {
                        double remaining = caster.getNormalAttackCooldown() - tickDuration;
                        caster.setNormalAttackCooldown(remaining);

                        if (remaining <= 0) {
                                List<HeroState> targets = resolveNormalAttackTarget(caster, battleState,
                                                currentRoutes);
                                if (!targets.isEmpty()) {
                                        SkillActivation activation = new SkillActivation();
                                        activation.setSkill(null);
                                        activation.setCaster(caster);
                                        activation.setTargets(targets);
                                        activation.setSkillType(SkillType.NORMAL_ATTACK);
                                        activation.setIdeal(caster.getActiveDebuffs().isEmpty());
                                        activations.add(activation);

                                        double spd = caster.getHero().getSpd();
                                        double interval = 1.0 - (spd - 60) / 1000.0;
                                        caster.setNormalAttackCooldown(interval);
                                }
                        }
                }

                return activations;
        }

        private TargetResolution resolveTargets(
                        Skill skill,
                        HeroState caster,
                        BattleState battleState,
                        List<AttackRoute> currentRoutes) {

                // Determine which side is enemy
                boolean casterIsPlayer = battleState.getPlayerStates().contains(caster);
                List<HeroState> enemyStates = casterIsPlayer
                                ? battleState.getEnemyStates()
                                : battleState.getPlayerStates();
                List<HeroState> allySates = casterIsPlayer
                                ? battleState.getPlayerStates()
                                : battleState.getEnemyStates();

                List<HeroState> aliveEnemies = enemyStates.stream()
                                .filter(HeroState::isAlive).toList();
                List<HeroState> aliveAllies = allySates.stream()
                                .filter(HeroState::isAlive).toList();

                // Taunt overrides all targeting
                boolean isTaunted = caster.getActiveDebuffs().containsKey(StatusEffect.TAUNT);
                TargetType targetType = skill.getTarget() != null
                                ? TargetTypeMapper.map(skill.getTarget())
                                : TargetType.SELF;

                // Taunt overrides all targeting — resolve to actual taunting hero
                if (isTaunted && caster.getTauntSource() != null && caster.getTauntSource().isAlive()) {
                        return new TargetResolution(List.of(caster.getTauntSource()), targetType);
                }
                // Taunter is dead — taunt breaks, normal targeting resumes
                if (isTaunted && (caster.getTauntSource() == null || !caster.getTauntSource().isAlive())) {
                        caster.getActiveDebuffs().remove(StatusEffect.TAUNT);
                        caster.setTauntSource(null);
                }

                List<HeroState> targets = switch (targetType) {
                        case RANDOM -> aliveEnemies.isEmpty() ? List.of()
                                        : List.of(aliveEnemies
                                                        .get(new java.util.Random().nextInt(aliveEnemies.size())));

                        case SINGLE -> {
                                // Find the hero at the routed target position
                                SlotPosition routedPosition = currentRoutes.stream()
                                                .filter(r -> r.getAttacker() == caster.getPosition())
                                                .map(AttackRoute::getTarget)
                                                .findFirst()
                                                .orElse(null);

                                yield routedPosition == null
                                                ? (aliveEnemies.isEmpty() ? List.of() : List.of(aliveEnemies.get(0)))
                                                : enemyStates.stream()
                                                                .filter(e -> e.isAlive()
                                                                                && e.getPosition() == routedPosition)
                                                                .findFirst()
                                                                .map(List::of)
                                                                .orElse(aliveEnemies.isEmpty() ? List.of()
                                                                                : List.of(aliveEnemies.get(0)));
                        }

                        case ALL_ENEMIES -> aliveEnemies;

                        case FRONT_ROW_ENEMIES -> aliveEnemies.stream()
                                        .filter(e -> e.getPosition().name().startsWith("FRONT"))
                                        .toList();

                        case BACK_ROW_ENEMIES -> aliveEnemies.stream()
                                        .filter(e -> e.getPosition().name().startsWith("BACK"))
                                        .toList();

                        case BACK_ROW_ENEMY_SINGLE -> aliveEnemies.stream()
                                        .filter(e -> e.getPosition().name().startsWith("BACK"))
                                        .findFirst()
                                        .map(List::of).orElse(List.of());

                        case FRONT_ROW_ALLIES -> aliveAllies.stream()
                                        .filter(e -> e.getPosition().name().startsWith("FRONT"))
                                        .toList();

                        case FRONT_ROW -> aliveEnemies.stream()
                                        .filter(e -> e.getPosition().name().startsWith("FRONT"))
                                        .toList();

                        case BACK_ROW -> aliveEnemies.stream()
                                        .filter(e -> e.getPosition().name().startsWith("BACK"))
                                        .toList();

                        case HIGHEST_HP -> aliveEnemies.stream()
                                        .max(Comparator.comparingDouble(HeroState::getCurrentHP))
                                        .map(List::of).orElse(List.of());

                        case LOWEST_HP -> aliveEnemies.stream()
                                        .min(Comparator.comparingDouble(HeroState::getCurrentHP))
                                        .map(List::of).orElse(List.of());

                        case SELF -> List.of(caster);

                        case ALL_ALLIES -> aliveAllies;

                        case SELF_AND_ALLIES -> {
                                List<HeroState> combined = new ArrayList<>(aliveAllies);
                                if (!combined.contains(caster))
                                        combined.add(caster);
                                yield combined;
                        }
                };
                return new TargetResolution(targets, targetType);
        }

        private List<HeroState> resolveNormalAttackTarget(HeroState caster, BattleState battleState,
                        List<AttackRoute> currentRoutes) {

                boolean casterIsPlayer = battleState.getPlayerStates().contains(caster);
                List<HeroState> enemyStates = casterIsPlayer
                                ? battleState.getEnemyStates()
                                : battleState.getPlayerStates();

                List<HeroState> aliveEnemies = enemyStates.stream()
                                .filter(HeroState::isAlive).toList();

                SlotPosition routedPosition = currentRoutes.stream()
                                .filter(r -> r.getAttacker() == caster.getPosition())
                                .map(AttackRoute::getTarget)
                                .findFirst()
                                .orElse(null);

                return routedPosition == null
                                ? (aliveEnemies.isEmpty() ? List.of() : List.of(aliveEnemies.get(0)))
                                : enemyStates.stream()
                                                .filter(e -> e.isAlive() && e.getPosition() == routedPosition)
                                                .findFirst()
                                                .map(List::of)
                                                .orElse(aliveEnemies.isEmpty() ? List.of()
                                                                : List.of(aliveEnemies.get(0)));
        }

        private boolean isAttackTargetType(TargetType targetType) {
                return switch (targetType) {
                        case SELF, ALL_ALLIES, FRONT_ROW_ALLIES, SELF_AND_ALLIES -> false;
                        default -> true;
                };
        }

}