package com.lastwar_advisor.server.engine.scenario.resolver;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.lastwar_advisor.server.engine.model.StatusEffect;
import com.lastwar_advisor.server.engine.scenario.model.DebuffState;
import com.lastwar_advisor.server.engine.scenario.model.SkillActivation;
import com.lastwar_advisor.server.engine.scenario.state.BattleState;
import com.lastwar_advisor.server.engine.scenario.state.HeroState;
import com.lastwar_advisor.server.entity.Skill;
import com.lastwar_advisor.server.entity.SkillEffect;
import com.lastwar_advisor.server.util.StatKeyConstants;

@Component
public class DebuffResolver {

    public void resolve(
            List<SkillActivation> activations,
            BattleState battleState,
            double tickDuration) {

        for (SkillActivation activation : activations) {
            Skill skill = activation.getSkill();
            if (skill == null) // normal attacks have no skill, no debuff effects to process
                continue;

            List<HeroState> targets = activation.getTargets();

            if (skill.getEffects() == null || skill.getEffects().isEmpty())
                continue;

            for (SkillEffect effect : skill.getEffects()) {
                if (effect.getStatKey() == null || effect.getValue() == null)
                    continue;

                String key = effect.getStatKey().getKey();
                StatusEffect statusEffect = mapToStatusEffect(key);
                if (statusEffect == null)
                    continue;

                double magnitude = effect.getValue();
                double duration = skill.getEffects().stream()
                        .filter(e -> e.getStatKey() != null &&
                                e.getStatKey().getKey().equals(StatKeyConstants.DURATION_S))
                        .mapToDouble(SkillEffect::getValue)
                        .findFirst()
                        .orElse(tickDuration); // fallback — lasts one tick if no duration specified

                for (HeroState target : targets) {
                    if (!target.isAlive())
                        continue;

                    DebuffState debuffState = new DebuffState();
                    debuffState.setEffect(statusEffect);
                    debuffState.setRemainingDuration(duration);
                    debuffState.setMagnitude(magnitude);

                    // TAUNT — also set tauntSource on target
                    if (statusEffect == StatusEffect.TAUNT) {
                        target.setTauntSource(activation.getCaster());
                    }

                    // Overwrite existing debuff only if new magnitude is higher
                    DebuffState existing = target.getActiveDebuffs().get(statusEffect);
                    if (existing == null || magnitude > existing.getMagnitude()) {
                        target.getActiveDebuffs().put(statusEffect, debuffState);
                    }
                }
            }
        }
    }

    // Tick down all active debuffs, remove expired ones
    public void tickDebuffs(List<HeroState> heroStates, double tickDuration) {
        for (HeroState state : heroStates) {
            if (!state.isAlive())
                continue;

            Iterator<Map.Entry<StatusEffect, DebuffState>> it = state.getActiveDebuffs().entrySet().iterator();

            while (it.hasNext()) {
                Map.Entry<StatusEffect, DebuffState> entry = it.next();
                DebuffState debuff = entry.getValue();
                debuff.setRemainingDuration(debuff.getRemainingDuration() - tickDuration);

                if (debuff.getRemainingDuration() <= 0) {
                    // Clean up taunt source if taunt expires
                    if (entry.getKey() == StatusEffect.TAUNT) {
                        state.setTauntSource(null);
                    }
                    it.remove();
                }
            }
        }
    }

    private StatusEffect mapToStatusEffect(String statKey) {
        return switch (statKey) {
            case StatKeyConstants.STUN -> StatusEffect.STUN;
            case StatKeyConstants.TAUNT, StatKeyConstants.ENHANCED_TAUNT -> StatusEffect.TAUNT;
            case StatKeyConstants.REDUCE_DAMAGE_TAKEN_PERCENT,
                    StatKeyConstants.RESISTANCE_ALL_DMG_PERCENT ->
                StatusEffect.DAMAGE_DEBUFF;
            case StatKeyConstants.ENERGY_DAMAGE_PERCENT -> StatusEffect.ENERGY_DEBUFF;
            case StatKeyConstants.PHYSICAL_DAMAGE_PERCENT -> StatusEffect.PHYSICAL_DEBUFF;
            default -> null;
        };
    }
}
