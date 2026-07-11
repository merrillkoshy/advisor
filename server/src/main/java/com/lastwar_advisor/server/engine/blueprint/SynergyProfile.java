package com.lastwar_advisor.server.engine.blueprint;

import com.lastwar_advisor.server.engine.model.HeroType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SynergyProfile {
    private HeroType contributesTo;
    private boolean isAnchor; // is this the dominant type in the squad?
    private double synergyMultiplier; // 0, 0.10, 0.15, 0.20
    private int sameTypeCount; // how many of this type in the squad

}
