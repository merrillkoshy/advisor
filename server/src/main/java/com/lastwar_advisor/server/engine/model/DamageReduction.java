package com.lastwar_advisor.server.engine.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DamageReduction {
    private DamageType type;
    private Float percent;
    private ShieldTrigger trigger;
}
