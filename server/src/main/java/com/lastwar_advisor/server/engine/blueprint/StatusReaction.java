package com.lastwar_advisor.server.engine.blueprint;

import com.lastwar_advisor.server.engine.model.ReactionType;
import com.lastwar_advisor.server.engine.model.StatusEffect;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StatusReaction {
    private StatusEffect effect; // STUN, TAUNT, DAMAGE_DEBUFF etc
    private ReactionType reaction; // FREEZE_SKILLS, OVERRIDE_TARGET etc
    private double duration;
}
