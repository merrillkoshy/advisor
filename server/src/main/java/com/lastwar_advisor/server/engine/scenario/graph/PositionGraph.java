package com.lastwar_advisor.server.engine.scenario.graph;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.lastwar_advisor.server.engine.scenario.state.HeroState;
import com.lastwar_advisor.server.entity.SlotPosition;

@Component
public class PositionGraph {

    // Default auto-attack routes (attacker → targets)
    // Symmetric — applies to both sides
    private static final Map<SlotPosition, List<SlotPosition>> DEFAULT_ROUTES = Map.of(
            SlotPosition.FRONT_1, List.of(SlotPosition.FRONT_1, SlotPosition.BACK_2, SlotPosition.BACK_3),
            SlotPosition.FRONT_2, List.of(SlotPosition.FRONT_2, SlotPosition.BACK_1),
            SlotPosition.BACK_1, List.of(SlotPosition.FRONT_2),
            SlotPosition.BACK_2, List.of(SlotPosition.FRONT_1),
            SlotPosition.BACK_3, List.of(SlotPosition.FRONT_1));

    // Position fallback order when primary target dies
    private static final List<SlotPosition> FALLBACK_ORDER = List.of(
            SlotPosition.FRONT_1,
            SlotPosition.FRONT_2,
            SlotPosition.BACK_1,
            SlotPosition.BACK_2,
            SlotPosition.BACK_3);

    public List<AttackRoute> buildRoutes(
            List<HeroState> attackerStates,
            List<HeroState> defenderStates) {

        Set<SlotPosition> aliveDefenderPositions = defenderStates.stream()
                .filter(HeroState::isAlive)
                .map(HeroState::getPosition)
                .collect(Collectors.toSet());

        List<AttackRoute> routes = new ArrayList<>();

        for (HeroState attacker : attackerStates) {
            if (!attacker.isAlive())
                continue;

            List<SlotPosition> preferredTargets = DEFAULT_ROUTES
                    .getOrDefault(attacker.getPosition(), List.of());

            for (SlotPosition preferred : preferredTargets) {
                if (aliveDefenderPositions.contains(preferred)) {
                    // Primary target alive — route stands
                    routes.add(new AttackRoute(attacker.getPosition(), preferred));
                } else {
                    // Primary target dead — cascade to next alive position
                    SlotPosition fallback = resolveFallback(preferred, aliveDefenderPositions);
                    if (fallback != null) {
                        routes.add(new AttackRoute(attacker.getPosition(), fallback));
                    }
                }
                if (attacker.getHero().getName().equals("Stetmann")) {
                    System.out.println("STETMANN ROUTE: " + attacker.getPosition() + " → " + preferred + " | alive: "
                            + aliveDefenderPositions);
                }
            }

        }

        return routes;
    }

    private SlotPosition resolveFallback(
            SlotPosition deadPosition,
            Set<SlotPosition> alivePositions) {

        // Walk fallback order starting from dead position's index
        int startIndex = FALLBACK_ORDER.indexOf(deadPosition);
        for (int i = startIndex + 1; i < FALLBACK_ORDER.size(); i++) {
            if (alivePositions.contains(FALLBACK_ORDER.get(i))) {
                return FALLBACK_ORDER.get(i);
            }
        }
        // Also check positions before dead position
        for (int i = 0; i < startIndex; i++) {
            if (alivePositions.contains(FALLBACK_ORDER.get(i))) {
                return FALLBACK_ORDER.get(i);
            }
        }
        return null; // all defenders dead
    }
}
