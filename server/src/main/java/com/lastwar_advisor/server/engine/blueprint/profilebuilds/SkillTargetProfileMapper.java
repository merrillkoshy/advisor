package com.lastwar_advisor.server.engine.blueprint.profilebuilds;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.lastwar_advisor.server.engine.blueprint.SkillTargetProfile;
import com.lastwar_advisor.server.engine.model.BuffType;
import com.lastwar_advisor.server.engine.model.DamageType;
import com.lastwar_advisor.server.engine.model.SkillType;
import com.lastwar_advisor.server.entity.Hero;
import com.lastwar_advisor.server.util.StatKeyConstants;
import com.lastwar_advisor.server.util.TargetTypeMapper;

@Component
public class SkillTargetProfileMapper {

    private final Map<String, BuffType> fullBuffTypeMap;

    public SkillTargetProfileMapper() {
        fullBuffTypeMap = new HashMap<>();
        fullBuffTypeMap.put(StatKeyConstants.ATK_PERCENT, BuffType.ATK);
        fullBuffTypeMap.put(StatKeyConstants.HERO_ATK_BOOST_PERCENT, BuffType.ATK);
        fullBuffTypeMap.put(StatKeyConstants.TEAM_ATK_BUFF_PERCENT, BuffType.ATK);
        fullBuffTypeMap.put(StatKeyConstants.DEF_PERCENT, BuffType.DEF);
        fullBuffTypeMap.put(StatKeyConstants.HERO_DEF_BOOST_PERCENT, BuffType.DEF);
        fullBuffTypeMap.put(StatKeyConstants.TEAM_DEF_BUFF_PERCENT, BuffType.DEF);
        fullBuffTypeMap.put(StatKeyConstants.HP_PERCENT, BuffType.HP);
        fullBuffTypeMap.put(StatKeyConstants.HERO_HP_BOOST_PERCENT, BuffType.HP);
        fullBuffTypeMap.put(StatKeyConstants.COOLDOWN_REDUCTION_PERCENT, BuffType.COOLDOWN);
        fullBuffTypeMap.put(StatKeyConstants.ATTACK_SPEED_PERCENT, BuffType.ATTACK_SPEED);
        fullBuffTypeMap.put(StatKeyConstants.MOVE_SPEED_PERCENT, BuffType.MOVE_SPEED);

    }

    public List<SkillTargetProfile> mapHeroToSkillTargetProfiles(Hero hero) {
        return hero.getSkills().stream()
                .map(s -> {
                    Map<BuffType, Double> magnitudes = new HashMap<>();
                    s.getEffects().stream()
                            .filter(e -> e.getStatKey() != null && e.getValue() != null)
                            .forEach(e -> {
                                BuffType bt = fullBuffTypeMap.get(e.getStatKey().getKey());
                                if (bt != null) {
                                    magnitudes.merge(bt, e.getValue(), Double::sum);
                                }
                            });

                    SkillTargetProfile stp = new SkillTargetProfile();
                    stp.setSkillName(s.getName());
                    stp.setSkillType(SkillType.valueOf(s.getType().toUpperCase()));
                    stp.setTargetType(TargetTypeMapper.map(s.getTarget()));
                    stp.setCooldown(s.getCooldown() != null ? s.getCooldown() : 0.0);
                    stp.setDamageType(s.getDamageType() != null
                            ? DamageType.valueOf(s.getDamageType().toUpperCase())
                            : null);
                    stp.setMagnitudes(magnitudes);
                    return stp;
                })
                .toList();
    }
}