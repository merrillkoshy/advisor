package com.lastwar_advisor.server.engine.blueprint;

import com.lastwar_advisor.server.engine.model.DamageType;
import com.lastwar_advisor.server.engine.model.ShieldTrigger;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ShieldProfile {
    private boolean hasShield;
    private DamageType absorbType; // PHYSICAL (Murphy), ENERGY (Lucius), ALL (others)
    private double shieldValuePercent;
    private ShieldTrigger trigger; // ACTIVE, PASSIVE, ON_HIT (Adam's counter)
}
