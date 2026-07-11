package com.lastwar_advisor.server.engine.scenario.engine;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.lastwar_advisor.server.engine.model.CombatantStats;
import com.lastwar_advisor.server.engine.model.DamageEvent;
import com.lastwar_advisor.server.engine.scenario.graph.AttackRoute;
import com.lastwar_advisor.server.engine.scenario.graph.PositionGraph;
import com.lastwar_advisor.server.engine.scenario.model.SkillActivation;
import com.lastwar_advisor.server.engine.scenario.model.TickResult;
import com.lastwar_advisor.server.engine.scenario.resolver.DamageCalculator;
import com.lastwar_advisor.server.engine.scenario.resolver.DebuffResolver;
import com.lastwar_advisor.server.engine.scenario.resolver.KillOrderResolver;
import com.lastwar_advisor.server.engine.scenario.resolver.SkillActivationResolver;
import com.lastwar_advisor.server.engine.scenario.state.BattleState;
import com.lastwar_advisor.server.engine.scenario.state.HeroState;

@Component
public class TickResolver {

        private final PositionGraph positionGraph;
        private final SkillActivationResolver skillActivationResolver;
        private final DebuffResolver debuffResolver;
        private final KillOrderResolver killOrderResolver;
        private final DamageCalculator damageCalculator;

        public TickResolver(PositionGraph positionGraph,
                        SkillActivationResolver skillActivationResolver,
                        DebuffResolver debuffResolver,
                        KillOrderResolver killOrderResolver,
                        DamageCalculator damageCalculator) {
                this.positionGraph = positionGraph;
                this.skillActivationResolver = skillActivationResolver;
                this.debuffResolver = debuffResolver;
                this.killOrderResolver = killOrderResolver;
                this.damageCalculator = damageCalculator;
        }

        public TickResult resolve(
                        BattleState battleState,
                        CombatantStats playerStats,
                        CombatantStats enemyStats,
                        double tickDuration,
                        double elapsedTime) {

                List<HeroState> playerStates = battleState.getPlayerStates();
                List<HeroState> enemyStates = battleState.getEnemyStates();

                // 1. Build live routing for both sides
                List<AttackRoute> playerRoutes = positionGraph.buildRoutes(playerStates, enemyStates);
                List<AttackRoute> enemyRoutes = positionGraph.buildRoutes(enemyStates, playerStates);

                // 2. Resolve skill activations for both sides
                List<SkillActivation> allActivations = new ArrayList<>();
                for (HeroState hero : playerStates) {
                        allActivations.addAll(skillActivationResolver.resolve(
                                        hero, battleState, playerRoutes, tickDuration));
                }
                for (HeroState hero : enemyStates) {
                        allActivations.addAll(skillActivationResolver.resolve(
                                        hero, battleState, enemyRoutes, tickDuration));
                }

                // 3. Apply debuffs from activations
                debuffResolver.resolve(allActivations, battleState, tickDuration);

                // 4. Tick down debuff durations
                debuffResolver.tickDebuffs(playerStates, tickDuration);
                debuffResolver.tickDebuffs(enemyStates, tickDuration);

                // 5. Build damage map
                List<DamageEvent> damageEvents = new ArrayList<>();
                Map<HeroState, Double> damageMap = killOrderResolver.buildDamageMap(
                                allActivations, battleState, damageCalculator, playerStats, enemyStats, damageEvents,
                                elapsedTime);

                // 6. Resolve damage and kill order
                List<HeroState> killed = killOrderResolver.resolve(
                                playerStates, enemyStates, damageMap);

                // 7. Check battle over
                boolean playerWiped = playerStates.stream().noneMatch(HeroState::isAlive);
                boolean enemyWiped = enemyStates.stream().noneMatch(HeroState::isAlive);
                boolean battleOver = playerWiped || enemyWiped;

                if (battleOver) {
                        battleState.setBattleOver(true);
                        battleState.setWinningSide(enemyWiped ? "player" : "enemy");
                }

                // 8. Build TickResult
                TickResult result = new TickResult();
                result.setKilled(killed);
                result.setActivations(allActivations);
                result.setDamageDealt(damageMap);
                result.setDamageEvents(damageEvents);
                result.setElapsedTime(elapsedTime);
                result.setBattleOver(battleOver);

                return result;
        }
}
