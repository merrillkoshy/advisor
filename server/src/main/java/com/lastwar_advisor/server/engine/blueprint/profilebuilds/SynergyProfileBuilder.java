package com.lastwar_advisor.server.engine.blueprint.profilebuilds;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.lastwar_advisor.server.engine.blueprint.SynergyProfile;
import com.lastwar_advisor.server.engine.model.HeroType;
import com.lastwar_advisor.server.entity.Hero;

@Component
public class SynergyProfileBuilder {
    private double calculateSynergyMultiplier(long sameTypeCount, long secondaryTypeCount) {
        if (sameTypeCount == 5)
            return 0.2;
        if (sameTypeCount == 4)
            return 0.15;
        if (sameTypeCount == 3 && secondaryTypeCount == 2)
            return 0.1;
        return 0;
    }

    public SynergyProfile build(Hero hero, List<Hero> squadContext) {
        Map<String, Long> typeCounts = squadContext.stream()
                .collect(Collectors.groupingBy(Hero::getType, Collectors.counting()));
        long sameTypeCount = typeCounts.getOrDefault(hero.getType(), 0L);
        long secondaryTypeCount = typeCounts.entrySet().stream()
                .filter(entry -> !entry.getKey().equals(hero.getType()))
                .mapToLong(Map.Entry::getValue)
                .max()
                .orElse(0L);
        long maxCount = typeCounts.values().stream()
                .mapToLong(Long::longValue)
                .max()
                .orElse(0L);

        boolean isAnchor = sameTypeCount == maxCount;
        double multiplier = calculateSynergyMultiplier(sameTypeCount, secondaryTypeCount);
        SynergyProfile synergyProfile = new SynergyProfile();
        synergyProfile.setContributesTo(HeroType.valueOf(hero.getType().toUpperCase()));
        synergyProfile.setSynergyMultiplier(multiplier);
        synergyProfile.setSameTypeCount((int) sameTypeCount);
        synergyProfile.setAnchor(isAnchor);
        return synergyProfile;
    }

}
