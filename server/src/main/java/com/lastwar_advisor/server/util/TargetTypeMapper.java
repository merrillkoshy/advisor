package com.lastwar_advisor.server.util;

import com.lastwar_advisor.server.engine.model.TargetType;

public class TargetTypeMapper {
    public static TargetType map(String target) {
        if (target == null)
            return TargetType.SELF;
        return switch (target.toLowerCase()) {
            case "random", "random_multi", "enemy", "random_enemy" -> TargetType.RANDOM;
            case "single" -> TargetType.SINGLE;
            case "front_row", "random_front_row_enemy" -> TargetType.FRONT_ROW;
            case "back_row" -> TargetType.BACK_ROW;
            case "all_enemies", "aoe" -> TargetType.ALL_ENEMIES;
            case "front_row_enemies" -> TargetType.FRONT_ROW_ENEMIES;
            case "back_row_enemies" -> TargetType.BACK_ROW_ENEMIES;
            case "back_row_enemy" -> TargetType.BACK_ROW_ENEMY_SINGLE;
            case "lowest_hp_percent_enemy" -> TargetType.LOWEST_HP;
            case "self" -> TargetType.SELF;
            case "all_allies" -> TargetType.ALL_ALLIES;
            case "front_row_allies" -> TargetType.FRONT_ROW_ALLIES;
            case "self_and_allies" -> TargetType.SELF_AND_ALLIES;
            default -> TargetType.SELF;
        };
    }
}
