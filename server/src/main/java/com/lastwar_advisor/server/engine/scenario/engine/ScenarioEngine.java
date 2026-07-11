package com.lastwar_advisor.server.engine.scenario.engine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.lastwar_advisor.server.engine.blueprint.BlueprintBuilder;
import com.lastwar_advisor.server.engine.model.CombatantStats;
import com.lastwar_advisor.server.engine.model.HeroBehaviorBlueprint;
import com.lastwar_advisor.server.engine.model.HeroSnapshot;
import com.lastwar_advisor.server.engine.model.SkillSnapshot;
import com.lastwar_advisor.server.engine.model.SkillType;
import com.lastwar_advisor.server.engine.model.TickSnapshot;
import com.lastwar_advisor.server.engine.scenario.model.ScenarioRequest;
import com.lastwar_advisor.server.engine.scenario.model.ScenarioResult;
import com.lastwar_advisor.server.engine.scenario.model.TickResult;
import com.lastwar_advisor.server.engine.scenario.state.BattleState;
import com.lastwar_advisor.server.engine.scenario.state.HeroState;
import com.lastwar_advisor.server.entity.Drone;
import com.lastwar_advisor.server.entity.DroneComponent;
import com.lastwar_advisor.server.entity.DroneComponentStat;
import com.lastwar_advisor.server.entity.DroneMilestone;
import com.lastwar_advisor.server.entity.GameConstant;
import com.lastwar_advisor.server.entity.Hero;
import com.lastwar_advisor.server.entity.SkillEffect;
import com.lastwar_advisor.server.entity.Squad;
import com.lastwar_advisor.server.entity.SquadSlot;
import com.lastwar_advisor.server.entity.Opponent.Opponent;
import com.lastwar_advisor.server.entity.Opponent.OpponentDrone;
import com.lastwar_advisor.server.entity.Opponent.OpponentDroneComponent;
import com.lastwar_advisor.server.entity.Opponent.OpponentSquad;
import com.lastwar_advisor.server.entity.Opponent.OpponentSquadSlot;
import com.lastwar_advisor.server.entity.Player.Player;
import com.lastwar_advisor.server.entity.Player.PlayerDroneComponent;
import com.lastwar_advisor.server.repository.DroneMilestoneRepository;
import com.lastwar_advisor.server.repository.DroneSkillTierRepository;
import com.lastwar_advisor.server.repository.GameConstantRepository;
import com.lastwar_advisor.server.repository.SquadRepository;
import com.lastwar_advisor.server.repository.Opponent.OpponentSquadRepository;
import com.lastwar_advisor.server.util.StatKeyConstants;

@Service
public class ScenarioEngine {

    private final BlueprintBuilder blueprintBuilder;
    private final TickResolver tickResolver;
    private final SquadRepository squadRepository;
    private final OpponentSquadRepository opponentSquadRepository;
    private final DroneSkillTierRepository droneSkillTierRepository;
    private final DroneMilestoneRepository droneMilestoneRepository;
    private final GameConstantRepository gameConstantRepository;
    private final SimpMessagingTemplate messagingTemplate;

    private static final double TICK_DURATION = 0.5;
    private static final double BATTLE_DURATION = 200.0;
    private static final int TOTAL_TICKS = (int) (BATTLE_DURATION / TICK_DURATION);

    public ScenarioEngine(BlueprintBuilder blueprintBuilder,
            TickResolver tickResolver,
            SquadRepository squadRepository,
            OpponentSquadRepository opponentSquadRepository,
            DroneSkillTierRepository droneSkillTierRepository,
            DroneMilestoneRepository droneMilestoneRepository,
            GameConstantRepository gameConstantRepository,
            SimpMessagingTemplate messagingTemplate) {
        this.blueprintBuilder = blueprintBuilder;
        this.tickResolver = tickResolver;
        this.squadRepository = squadRepository;
        this.opponentSquadRepository = opponentSquadRepository;
        this.droneSkillTierRepository = droneSkillTierRepository;
        this.droneMilestoneRepository = droneMilestoneRepository;
        this.gameConstantRepository = gameConstantRepository;
        this.messagingTemplate = messagingTemplate;
    }

    private void broadcastTick(String battleId, TickSnapshot snapshot) {
        messagingTemplate.convertAndSend("/topic/battle/" + battleId, snapshot);
    }

    public ScenarioResult run(ScenarioRequest request) {

        // 1. Load squads
        Squad playerSquad = squadRepository.findById(request.getPlayerSquadId())
                .orElseThrow(() -> new RuntimeException("Player squad not found"));
        OpponentSquad enemySquad = opponentSquadRepository.findById(request.getEnemySquadId())
                .orElseThrow(() -> new RuntimeException("Enemy squad not found"));

        // 2. Build CombatantStats for both sides
        CombatantStats playerStats = buildPlayerStats(playerSquad.getPlayer());
        CombatantStats enemyStats = buildOpponentStats(enemySquad.getOpponent());

        // 3. Build HeroStates for both sides
        List<HeroState> playerStates = buildPlayerHeroStates(playerSquad, playerStats);
        List<HeroState> enemyStates = buildOpponentHeroStates(enemySquad, enemyStats);

        // 4. Initialize BattleState
        BattleState battleState = new BattleState();
        battleState.setPlayerStates(playerStates);
        battleState.setEnemyStates(enemyStates);
        battleState.setElapsedTime(0.0);
        battleState.setBattleOver(false);
        battleState.setWinningSide(null);

        // 5. Apply ultimate stat boosts at tick 0 (skill 4 equivalent)
        applyUltimateStatBoosts(playerStates);
        applyUltimateStatBoosts(enemyStates);

        // Initial state snapshot before any damage
        TickSnapshot initialSnapshot = buildTickSnapshot(null, playerStates, enemyStates, -1);
        broadcastTick(request.getBattleId(), initialSnapshot);

        // 6. Run tick loop
        List<TickResult> tickResults = new ArrayList<>();
        for (int tick = 0; tick < TOTAL_TICKS; tick++) {
            double elapsedTime = tick * TICK_DURATION;
            TickResult tickResult = tickResolver.resolve(
                    battleState, playerStats, enemyStats, TICK_DURATION, elapsedTime);
            tickResults.add(tickResult);

            TickSnapshot snapshot = buildTickSnapshot(tickResult, playerStates, enemyStates, elapsedTime);
            broadcastTick(request.getBattleId(), snapshot);

            // Simulate Battle - should be removed later, just for testing purposes
            try {
                Thread.sleep(2);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
            // simulation ends

            if (tickResult.isBattleOver())
                break;
        }

        // 7. Resolve winner if battle goes full duration
        if (!battleState.isBattleOver()) {
            double playerHP = playerStates.stream()
                    .filter(HeroState::isAlive)
                    .mapToDouble(HeroState::getCurrentHP)
                    .sum();
            double enemyHP = enemyStates.stream()
                    .filter(HeroState::isAlive)
                    .mapToDouble(HeroState::getCurrentHP)
                    .sum();
            battleState.setWinningSide(playerHP >= enemyHP ? "player" : "enemy");
            battleState.setBattleOver(true);
        }

        // 8. Compute result
        return buildScenarioResult(battleState, playerStates, tickResults);
    }

    private List<HeroState> buildPlayerHeroStates(Squad squad, CombatantStats stats) {
        List<HeroState> states = new ArrayList<>();

        for (SquadSlot slot : squad.getSlots()) {
            Hero hero = slot.getHero();
            if (hero == null)
                continue;

            // Build blueprint
            List<SkillEffect> skillEffects = hero.getSkills().stream()
                    .flatMap(s -> s.getEffects().stream())
                    .toList();

            List<Hero> squadHeroes = squad.getSlots().stream()
                    .map(SquadSlot::getHero)
                    .filter(h -> h != null)
                    .toList();

            HeroBehaviorBlueprint blueprint = blueprintBuilder.build(
                    hero, slot.getPosition(), squadHeroes, skillEffects, stats);

            // Initialize HeroState
            HeroState heroState = new HeroState();
            heroState.setHero(hero);
            heroState.setBlueprint(blueprint);
            heroState.setPosition(slot.getPosition());
            heroState.setCurrentHP(hero.getHp());
            heroState.setMaxHp(hero.getHp());
            heroState.setAlive(true);
            heroState.setActiveDebuffs(new HashMap<>());
            heroState.setShieldActive(false);
            heroState.setShieldHP(0.0);
            heroState.setTauntSource(null);

            // Initialize skill cooldowns — all start at 0 (ready to fire)
            Map<String, Double> cooldowns = new HashMap<>();
            hero.getSkills().forEach(s -> {
                if (s.getCooldown() == null) {
                    // PASSIVE/ULTIMATE — never enters cooldown loop
                    cooldowns.put(s.getName(), Double.MAX_VALUE);
                } else {
                    // Start all active skills at full cooldown except the shortest one
                    // (auto-attack)
                    cooldowns.put(s.getName(), s.getCooldown());
                }
            });

            // Auto-attack — shortest cooldown skill starts ready
            // hero.getSkills().stream()
            // .filter(s -> s.getCooldown() != null)
            // .min(Comparator.comparingDouble(Skill::getCooldown))
            // .ifPresent(autoAttack -> cooldowns.put(autoAttack.getName(), 0.0));
            // removed this block in favor of the new system of 'NORMAL_ATTACK'
            double spd = hero.getSpd();
            double interval = 1.0 - (spd - 60) / 1000.0;
            heroState.setNormalAttackCooldown(interval);
            heroState.setSkillCooldowns(cooldowns);

            states.add(heroState);
        }

        return states;
    }

    private List<HeroState> buildOpponentHeroStates(OpponentSquad squad, CombatantStats stats) {
        List<HeroState> states = new ArrayList<>();

        for (OpponentSquadSlot slot : squad.getSlots()) {
            Hero hero = slot.getHero();
            if (hero == null)
                continue;

            // Build blueprint
            List<SkillEffect> skillEffects = hero.getSkills().stream()
                    .flatMap(s -> s.getEffects().stream())
                    .toList();

            List<Hero> squadHeroes = squad.getSlots().stream()
                    .map(OpponentSquadSlot::getHero)
                    .filter(h -> h != null)
                    .toList();

            HeroBehaviorBlueprint blueprint = blueprintBuilder.build(
                    hero, slot.getPosition(), squadHeroes, skillEffects, stats);

            // Initialize HeroState
            HeroState heroState = new HeroState();
            heroState.setHero(hero);
            heroState.setBlueprint(blueprint);
            heroState.setPosition(slot.getPosition());
            // Magic number from Reddit
            double rawHp = hero.getHp();
            double def = hero.getDef();
            double ehp = rawHp * (def + 4000) / 4000;
            heroState.setCurrentHP(ehp);
            heroState.setMaxHp(ehp);
            //
            heroState.setAlive(true);
            heroState.setActiveDebuffs(new HashMap<>());
            heroState.setShieldActive(false);
            heroState.setShieldHP(0.0);
            heroState.setTauntSource(null);

            // Initialize skill cooldowns — all start at 0 (ready to fire)
            Map<String, Double> cooldowns = new HashMap<>();
            hero.getSkills().forEach(s -> {
                if (s.getCooldown() == null) {
                    // PASSIVE/ULTIMATE — never enters cooldown loop
                    cooldowns.put(s.getName(), Double.MAX_VALUE);
                } else {
                    // Start all active skills at full cooldown except the shortest one
                    // (auto-attack)
                    cooldowns.put(s.getName(), s.getCooldown());
                }
            });

            // Auto-attack — shortest cooldown skill starts ready
            // hero.getSkills().stream()
            // .filter(s -> s.getCooldown() != null)
            // .min(Comparator.comparingDouble(Skill::getCooldown))
            // .ifPresent(autoAttack -> cooldowns.put(autoAttack.getName(), 0.0));
            // removed this block in favor of the new system of 'NORMAL_ATTACK'
            double spd = hero.getSpd();
            double interval = 1.0 - (spd - 60) / 1000.0;
            heroState.setNormalAttackCooldown(interval);
            heroState.setSkillCooldowns(cooldowns);

            states.add(heroState);
        }

        return states;
    }

    private void applyUltimateStatBoosts(List<HeroState> heroStates) {
        double defaultSkillLevel = gameConstantRepository
                .findByKey("DEFAULT_SKILL_LEVEL")
                .map(GameConstant::getValue)
                .orElse(40.0); // safe fallback
        for (HeroState state : heroStates) {
            state.getHero().getSkills().stream()
                    .filter(s -> s.getKeyStats() != null
                            && s.getKeyStats().contains("Stat Boost")
                            && SkillType.valueOf(s.getType().toUpperCase()) == SkillType.ULTIMATE)
                    .flatMap(s -> s.getEffects().stream())
                    .filter(e -> e.getStatKey() != null && e.getValue() != null && e.getLevel() == defaultSkillLevel)
                    .forEach(e -> {
                        System.out
                                .println("BEFORE BOOST: " + state.getHero().getName() + " HP: " + state.getCurrentHP());
                        String key = e.getStatKey().getKey();
                        double value = e.getValue();
                        if (key.equals(StatKeyConstants.HP_PERCENT)) {
                            state.setCurrentHP(state.getCurrentHP() * (1 + value / 100));
                            state.setMaxHp(state.getCurrentHP());
                        } else if (key.equals(StatKeyConstants.ATK_PERCENT)) {
                            int boostedAtk = (int) (state.getHero().getAtk() * (1 + value / 100));
                            state.getHero().setAtk(boostedAtk);
                        } else if (key.equals(StatKeyConstants.DEF_PERCENT)) {
                            int boostedDef = (int) (state.getHero().getDef() * (1 + value / 100));
                            state.getHero().setDef(boostedDef);
                        }
                    });
        }
    }

    private TickSnapshot buildTickSnapshot(
            TickResult tickResult,
            List<HeroState> playerStates,
            List<HeroState> enemyStates,
            double elapsedTime) {

        TickSnapshot snapshot = new TickSnapshot();
        snapshot.setElapsedTime(elapsedTime);
        Set<String> firedSkills = tickResult != null
                ? tickResult.getActivations().stream()
                        .map(a -> a.getCaster().getHero().getName() + ":" +
                                (a.getSkill() != null ? a.getSkill().getName() : "Normal Attack"))
                        .collect(Collectors.toSet())
                : Collections.emptySet();
        snapshot.setPlayerStates(playerStates.stream().map(h -> buildHeroSnapshot(h, firedSkills)).toList());
        snapshot.setEnemyStates(enemyStates.stream().map(h -> buildHeroSnapshot(h, firedSkills)).toList());
        snapshot.setDamageEvents(tickResult != null ? tickResult.getDamageEvents() : Collections.emptyList());
        return snapshot;
    }

    private CombatantStats buildPlayerStats(Player player) {
        CombatantStats stats = new CombatantStats();

        Drone drone = player.getDrone();
        if (drone == null)
            return stats;

        // Drone level and star tier
        stats.setDroneLevel((double) drone.getLevel());
        stats.setDroneStarTier(drone.getStarTier());

        // DroneSkillTier — damage var for this star tier
        droneSkillTierRepository.findByStarTier(drone.getStarTier())
                .ifPresent(tier -> stats.setDroneDamageVar(tier.getDamageVar()));

        // Accumulators
        double droneAtk = 0, droneHp = 0, droneDef = 0;
        double droneHeroAtkBoost = 0, droneHeroHpBoost = 0, droneHeroDefBoost = 0;

        // Component stats — baseValue + (level * increment)
        for (PlayerDroneComponent pdc : player.getDroneComponents()) {
            DroneComponent component = pdc.getDroneComponent();
            int level = pdc.getLevel();

            for (DroneComponentStat stat : component.getStats()) {
                if (stat.getStatKey() == null)
                    continue;
                if (stat.getUnlockLevel() != null && level < stat.getUnlockLevel())
                    continue;

                double effectiveValue = stat.getBaseValue() + (level * stat.getIncrement());
                String key = stat.getStatKey().getKey();
                // String valueType = stat.getStatKey().getValueType();

                if (key.equals(StatKeyConstants.DRONE_ATK)) {
                    droneAtk += effectiveValue;
                } else if (key.equals(StatKeyConstants.DRONE_HP)) {
                    droneHp += effectiveValue;
                } else if (key.equals(StatKeyConstants.DRONE_DEF)) {
                    droneDef += effectiveValue;
                } else if (key.equals(StatKeyConstants.DRONE_HERO_ATK_BOOST_PERCENT)) {
                    droneHeroAtkBoost += effectiveValue;
                } else if (key.equals(StatKeyConstants.DRONE_HERO_HP_BOOST_PERCENT)) {
                    droneHeroHpBoost += effectiveValue;
                } else if (key.equals(StatKeyConstants.DRONE_HERO_DEF_BOOST_PERCENT)) {
                    droneHeroDefBoost += effectiveValue;
                }
            }
        }

        // Milestone boosts — sum all unlocked milestones
        List<DroneMilestone> unlockedMilestones = droneMilestoneRepository
                .findByUnlockLevelLessThanEqual(drone.getLevel());

        for (DroneMilestone milestone : unlockedMilestones) {
            if (milestone.getStatKey() == null)
                continue;
            String key = milestone.getStatKey().getKey();

            if (key.equals(StatKeyConstants.DRONE_ATK)) {
                droneAtk += milestone.getValue();
            } else if (key.equals(StatKeyConstants.DRONE_HP)) {
                droneHp += milestone.getValue();
            } else if (key.equals(StatKeyConstants.DRONE_DEF)) {
                droneDef += milestone.getValue();
            } else if (key.equals(StatKeyConstants.DRONE_HERO_ATK_BOOST_PERCENT)) {
                droneHeroAtkBoost += milestone.getValue();
            } else if (key.equals(StatKeyConstants.DRONE_HERO_HP_BOOST_PERCENT)) {
                droneHeroHpBoost += milestone.getValue();
            } else if (key.equals(StatKeyConstants.DRONE_HERO_DEF_BOOST_PERCENT)) {
                droneHeroDefBoost += milestone.getValue();
            }
        }

        stats.setDroneAtk(droneAtk);
        stats.setDroneHp(droneHp);
        stats.setDroneDef(droneDef);
        stats.setDroneHeroAtkBoostPercent(droneHeroAtkBoost);
        stats.setDroneHeroHpBoostPercent(droneHeroHpBoost);
        stats.setDroneHeroDefBoostPercent(droneHeroDefBoost);

        return stats;
    }

    private CombatantStats buildOpponentStats(Opponent opponent) {
        CombatantStats stats = new CombatantStats();

        OpponentDrone drone = opponent.getDrone();
        if (drone == null)
            return stats;

        // Drone level and star tier
        stats.setDroneLevel((double) drone.getLevel());
        stats.setDroneStarTier(drone.getStarTier());

        // DroneSkillTier — damage var for this star tier
        droneSkillTierRepository.findByStarTier(drone.getStarTier())
                .ifPresent(tier -> stats.setDroneDamageVar(tier.getDamageVar()));

        // Accumulators
        double droneAtk = 0, droneHp = 0, droneDef = 0;
        double droneHeroAtkBoost = 0, droneHeroHpBoost = 0, droneHeroDefBoost = 0;

        // Component stats — baseValue + (level * increment)
        for (OpponentDroneComponent pdc : opponent.getDroneComponents()) {
            DroneComponent component = pdc.getDroneComponent();
            int level = pdc.getLevel();

            for (DroneComponentStat stat : component.getStats()) {
                if (stat.getStatKey() == null)
                    continue;
                if (stat.getUnlockLevel() != null && level < stat.getUnlockLevel())
                    continue;

                double effectiveValue = stat.getBaseValue() + (level * stat.getIncrement());
                String key = stat.getStatKey().getKey();
                // String valueType = stat.getStatKey().getValueType();

                if (key.equals(StatKeyConstants.DRONE_ATK)) {
                    droneAtk += effectiveValue;
                } else if (key.equals(StatKeyConstants.DRONE_HP)) {
                    droneHp += effectiveValue;
                } else if (key.equals(StatKeyConstants.DRONE_DEF)) {
                    droneDef += effectiveValue;
                } else if (key.equals(StatKeyConstants.DRONE_HERO_ATK_BOOST_PERCENT)) {
                    droneHeroAtkBoost += effectiveValue;
                } else if (key.equals(StatKeyConstants.DRONE_HERO_HP_BOOST_PERCENT)) {
                    droneHeroHpBoost += effectiveValue;
                } else if (key.equals(StatKeyConstants.DRONE_HERO_DEF_BOOST_PERCENT)) {
                    droneHeroDefBoost += effectiveValue;
                }
            }
        }

        // Milestone boosts — sum all unlocked milestones
        List<DroneMilestone> unlockedMilestones = droneMilestoneRepository
                .findByUnlockLevelLessThanEqual(drone.getLevel());

        for (DroneMilestone milestone : unlockedMilestones) {
            if (milestone.getStatKey() == null)
                continue;
            String key = milestone.getStatKey().getKey();

            if (key.equals(StatKeyConstants.DRONE_ATK)) {
                droneAtk += milestone.getValue();
            } else if (key.equals(StatKeyConstants.DRONE_HP)) {
                droneHp += milestone.getValue();
            } else if (key.equals(StatKeyConstants.DRONE_DEF)) {
                droneDef += milestone.getValue();
            } else if (key.equals(StatKeyConstants.DRONE_HERO_ATK_BOOST_PERCENT)) {
                droneHeroAtkBoost += milestone.getValue();
            } else if (key.equals(StatKeyConstants.DRONE_HERO_HP_BOOST_PERCENT)) {
                droneHeroHpBoost += milestone.getValue();
            } else if (key.equals(StatKeyConstants.DRONE_HERO_DEF_BOOST_PERCENT)) {
                droneHeroDefBoost += milestone.getValue();
            }
        }

        stats.setDroneAtk(droneAtk);
        stats.setDroneHp(droneHp);
        stats.setDroneDef(droneDef);
        stats.setDroneHeroAtkBoostPercent(droneHeroAtkBoost);
        stats.setDroneHeroHpBoostPercent(droneHeroHpBoost);
        stats.setDroneHeroDefBoostPercent(droneHeroDefBoost);

        return stats;
    }

    private ScenarioResult buildScenarioResult(
            BattleState battleState,
            List<HeroState> playerStates,
            List<TickResult> tickResults) {

        boolean playerWon = "player".equals(battleState.getWinningSide());

        double winRate = playerWon ? 1.0 : 0.0;

        long survived = playerStates.stream().filter(HeroState::isAlive).count();

        String tier = resolveTier(winRate, survived);
        String explanation = buildExplanation(battleState, playerStates, tickResults, tier);

        ScenarioResult result = new ScenarioResult();
        result.setWinRate(winRate);
        result.setAvgHerosSurvived(survived);
        result.setTier(tier);
        result.setExplanation(explanation);
        return result;
    }

    private String resolveTier(double winRate, long survived) {
        if (winRate == 0.0)
            return "COOKED";
        if (survived >= 3)
            return "SOLID";
        if (survived >= 1)
            return "COIN_FLIP";
        return "COOKED";
    }

    private String buildExplanation(
            BattleState battleState,
            List<HeroState> playerStates,
            List<TickResult> tickResults,
            String tier) {

        StringBuilder sb = new StringBuilder();
        // String winningSide = battleState.getWinningSide();

        if ("COOKED".equals(tier)) {
            sb.append("Your squad got wiped. ");
        } else if ("SOLID".equals(tier)) {
            sb.append("Clean win — your squad dominated. ");
        } else {
            sb.append("Could go either way. ");
        }

        // First kill event
        tickResults.stream()
                .filter(t -> !t.getKilled().isEmpty())
                .findFirst()
                .ifPresent(t -> {
                    HeroState firstKill = t.getKilled().get(0);
                    sb.append(String.format("%s went down first at %.1fs. ",
                            firstKill.getHero().getName(),
                            t.getElapsedTime() + TICK_DURATION)); // add TICK_DURATION here
                });

        // Survivors
        long survived = playerStates.stream().filter(HeroState::isAlive).count();
        sb.append(String.format("%d hero%s standing at the end.",
                survived, survived == 1 ? "" : "es"));

        return sb.toString();
    }

    private HeroSnapshot buildHeroSnapshot(HeroState herostate, Set<String> firedSkills) {
        HeroSnapshot snapshot = new HeroSnapshot();
        snapshot.setHeroName(herostate.getHero().getName());
        snapshot.setCurrentHp(Math.round(herostate.getCurrentHP() * 10.0) / 10.0);
        snapshot.setMaxHp(Math.round(herostate.getMaxHp() * 10.0) / 10.0);
        snapshot.setAlive(herostate.isAlive());
        // Map skills
        snapshot.setSkills(herostate.getSkillCooldowns().entrySet().stream()
                .collect(HashMap::new, (map, entry) -> {
                    String skillName = entry.getKey();
                    Double cooldownRemaining = entry.getValue();
                    boolean firedThisTick = firedSkills.contains(herostate.getHero().getName() + ":" + skillName);
                    SkillSnapshot skillSnapshot = new SkillSnapshot();
                    skillSnapshot.setSkillName(skillName);
                    double cd = cooldownRemaining >= Double.MAX_VALUE / 2 ? -1 : cooldownRemaining;
                    skillSnapshot.setCooldownRemaining(Math.round(cd * 10.0) / 10.0);
                    skillSnapshot.setFiredThisTick(firedThisTick);
                    map.put(skillName, skillSnapshot);
                }, HashMap::putAll));
        return snapshot;
    }
}
