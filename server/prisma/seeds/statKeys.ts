import { PrismaPg } from '@prisma/adapter-pg';
import { PrismaClient } from '@prisma/client';
import 'dotenv/config';

const adapter = new PrismaPg({
  connectionString: process.env.DATABASE_URL,
});

const prisma = new PrismaClient({ adapter });

export async function seedStatKeys() {
  console.log('Seeding StatKeys...');

  const statKeys = [
    // ─── OFFENSE ────────────────────────────────────────────────
    {
      key: 'atk_percent',
      description: 'Attack percentage boost',
      category: 'Offense',
      valueType: 'float',
    },
    {
      key: 'energy_damage_percent',
      description: 'Energy damage boost percentage',
      category: 'Offense',
      valueType: 'float',
    },
    {
      key: 'physical_damage_percent',
      description: 'Physical damage boost percentage',
      category: 'Offense',
      valueType: 'float',
    },
    {
      key: 'bonus_damage_percent',
      description: 'Bonus damage percentage',
      category: 'Offense',
      valueType: 'float',
    },
    {
      key: 'hit_count',
      description: 'Number of hits per skill',
      category: 'Offense',
      valueType: 'int',
    },
    {
      key: 'crit_rate_percent',
      description: 'Critical hit rate percentage',
      category: 'Offense',
      valueType: 'float',
    },
    {
      key: 'crit_damage_percent',
      description: 'Critical hit damage percentage',
      category: 'Offense',
      valueType: 'float',
    },
    {
      key: 'team_atk_buff_percent',
      description: 'Team-wide attack buff percentage',
      category: 'Offense',
      valueType: 'float',
    },
    {
      key: 'hero_atk_boost_percent',
      description: 'Hero attack boost percentage',
      category: 'Offense',
      valueType: 'float',
    },
    {
      key: 'hero_skill_damage_percent',
      description: 'Hero skill damage boost percentage',
      category: 'Offense',
      valueType: 'float',
    },

    // ─── DEFENSE ────────────────────────────────────────────────
    {
      key: 'def_percent',
      description: 'Defense percentage boost',
      category: 'Defense',
      valueType: 'float',
    },
    {
      key: 'hp_percent',
      description: 'HP percentage boost',
      category: 'Defense',
      valueType: 'float',
    },
    {
      key: 'shield_hp_percent',
      description: 'Shield value as percentage of HP',
      category: 'Defense',
      valueType: 'float',
    },
    {
      key: 'physical_damage_reduction_percent',
      description: 'Physical damage reduction percentage',
      category: 'Defense',
      valueType: 'float',
    },
    {
      key: 'energy_damage_reduction_percent',
      description: 'Energy damage reduction percentage',
      category: 'Defense',
      valueType: 'float',
    },
    {
      key: 'team_damage_reduction_percent',
      description: 'Team-wide damage reduction percentage',
      category: 'Defense',
      valueType: 'float',
    },
    {
      key: 'front_row_damage_reduction_percent',
      description: 'Front row damage reduction percentage',
      category: 'Defense',
      valueType: 'float',
    },
    {
      key: 'crit_reduction_percent',
      description: 'Reduced chance of taking critical hits',
      category: 'Defense',
      valueType: 'float',
    },
    {
      key: 'hero_def_boost_percent',
      description: 'Hero defense boost percentage',
      category: 'Defense',
      valueType: 'float',
    },
    {
      key: 'hero_hp_boost_percent',
      description: 'Hero HP boost percentage',
      category: 'Defense',
      valueType: 'float',
    },
    {
      key: 'team_def_buff_percent',
      description: 'Team-wide defense buff percentage',
      category: 'Defense',
      valueType: 'float',
    },

    // ─── UTILITY ────────────────────────────────────────────────
    {
      key: 'cooldown_reduction_percent',
      description: 'Skill cooldown reduction percentage',
      category: 'Utility',
      valueType: 'float',
    },
    {
      key: 'attack_speed_percent',
      description: 'Attack speed boost percentage',
      category: 'Utility',
      valueType: 'float',
    },
    {
      key: 'move_speed_percent',
      description: 'Movement speed boost percentage',
      category: 'Utility',
      valueType: 'float',
    },
    {
      key: 'duration_s',
      description: 'Effect duration in seconds',
      category: 'Utility',
      valueType: 'float',
    },
    {
      key: 'target_upgrade',
      description: 'Skill target upgrades to full team at higher levels',
      category: 'Utility',
      valueType: 'bool',
    },

    // ─── CROWD CONTROL ──────────────────────────────────────────
    {
      key: 'stun',
      description: 'Stuns the target',
      category: 'CrowdControl',
      valueType: 'bool',
    },
    {
      key: 'taunt',
      description: 'Forces enemies to target this hero',
      category: 'CrowdControl',
      valueType: 'bool',
    },
    {
      key: 'enhanced_taunt',
      description: 'Enhanced taunt with additional effects',
      category: 'CrowdControl',
      valueType: 'bool',
    },

    // ─── SUPPORT ────────────────────────────────────────────────
    {
      key: 'team_heal_hp_percent',
      description: 'Team-wide HP heal percentage',
      category: 'Support',
      valueType: 'float',
    },

    // ─── TECH ───────────────────────────────────────────────────
    {
      key: 'boost_physical_damage_percent',
      description: 'Tech boost to physical damage',
      category: 'Tech',
      valueType: 'float',
    },
    {
      key: 'boost_energy_damage_percent',
      description: 'Tech boost to energy damage',
      category: 'Tech',
      valueType: 'float',
    },
    {
      key: 'reduce_damage_taken_percent',
      description: 'Tech reduction to damage taken',
      category: 'Tech',
      valueType: 'float',
    },
    {
      key: 'tank_atk_boost_percent',
      description: 'Tank hero attack boost from tech',
      category: 'Tech',
      valueType: 'float',
    },
    {
      key: 'tank_def_boost_percent',
      description: 'Tank hero defense boost from tech',
      category: 'Tech',
      valueType: 'float',
    },
    {
      key: 'tank_hp_boost_percent',
      description: 'Tank hero HP boost from tech',
      category: 'Tech',
      valueType: 'float',
    },
    {
      key: 'tank_damage_boost_percent',
      description: 'Tank hero damage boost from tech',
      category: 'Tech',
      valueType: 'float',
    },
    {
      key: 'tank_damage_reduction_percent',
      description: 'Tank hero damage reduction from tech',
      category: 'Tech',
      valueType: 'float',
    },
    {
      key: 'aircraft_atk_boost_percent',
      description: 'Aircraft hero attack boost from tech',
      category: 'Tech',
      valueType: 'float',
    },
    {
      key: 'aircraft_def_boost_percent',
      description: 'Aircraft hero defense boost from tech',
      category: 'Tech',
      valueType: 'float',
    },
    {
      key: 'aircraft_hp_boost_percent',
      description: 'Aircraft hero HP boost from tech',
      category: 'Tech',
      valueType: 'float',
    },
    {
      key: 'aircraft_damage_boost_percent',
      description: 'Aircraft hero damage boost from tech',
      category: 'Tech',
      valueType: 'float',
    },
    {
      key: 'aircraft_damage_reduction_percent',
      description: 'Aircraft hero damage reduction from tech',
      category: 'Tech',
      valueType: 'float',
    },
    {
      key: 'missile_atk_boost_percent',
      description: 'Missile hero attack boost from tech',
      category: 'Tech',
      valueType: 'float',
    },
    {
      key: 'missile_def_boost_percent',
      description: 'Missile hero defense boost from tech',
      category: 'Tech',
      valueType: 'float',
    },
    {
      key: 'missile_hp_boost_percent',
      description: 'Missile hero HP boost from tech',
      category: 'Tech',
      valueType: 'float',
    },
    {
      key: 'missile_damage_boost_percent',
      description: 'Missile hero damage boost from tech',
      category: 'Tech',
      valueType: 'float',
    },
    {
      key: 'missile_damage_reduction_percent',
      description: 'Missile hero damage reduction from tech',
      category: 'Tech',
      valueType: 'float',
    },
    {
      key: 'unit_morale_boost_percent',
      description: 'Unit morale boost from tech',
      category: 'Tech',
      valueType: 'float',
    },
    {
      key: 'unit_atk_boost_percent',
      description: 'Unit attack boost from tech',
      category: 'Tech',
      valueType: 'float',
    },
    {
      key: 'unit_def_boost_percent',
      description: 'Unit defense boost from tech',
      category: 'Tech',
      valueType: 'float',
    },
    {
      key: 'unit_hp_boost_percent',
      description: 'Unit HP boost from tech',
      category: 'Tech',
      valueType: 'float',
    },

    // ─── DRONE ──────────────────────────────────────────────────
    {
      key: 'drone_level',
      description: 'Drone level',
      category: 'Drone',
      valueType: 'int',
    },
    {
      key: 'drone_combat_boost_level',
      description: 'Drone combat boost level',
      category: 'Drone',
      valueType: 'int',
    },
    {
      key: 'drone_chip_stars',
      description: 'Drone chip star rating',
      category: 'Drone',
      valueType: 'int',
    },
    {
      key: 'drone_hero_hp_boost_percent',
      description: 'Drone boost to hero HP percentage',
      category: 'Drone',
      valueType: 'float',
    },
    {
      key: 'drone_hero_def_boost_percent',
      description: 'Drone boost to hero defense percentage',
      category: 'Drone',
      valueType: 'float',
    },
    {
      key: 'drone_hero_atk_boost_percent',
      description: 'Drone boost to hero attack percentage',
      category: 'Drone',
      valueType: 'float',
    },
    {
      key: 'drone_radar_level',
      description: 'Drone radar research component level',
      category: 'Drone',
      valueType: 'int',
    },
    {
      key: 'drone_turbo_level',
      description: 'Drone turbo engine research component level',
      category: 'Drone',
      valueType: 'int',
    },
    {
      key: 'drone_armor_level',
      description: 'Drone external armor research component level',
      category: 'Drone',
      valueType: 'int',
    },
    {
      key: 'drone_thermal_level',
      description: 'Drone thermal imager research component level',
      category: 'Drone',
      valueType: 'int',
    },
    {
      key: 'drone_fuel_level',
      description: 'Drone fuel cell research component level',
      category: 'Drone',
      valueType: 'int',
    },
    {
      key: 'drone_missile_level',
      description: 'Drone airborne missile research component level',
      category: 'Drone',
      valueType: 'int',
    },
    {
      key: 'drone_hp',
      description: 'Drone HP integer value',
      category: 'Drone',
      valueType: 'int',
    },
    {
      key: 'drone_def',
      description: 'Drone defense integer value',
      category: 'Drone',
      valueType: 'int',
    },
    {
      key: 'drone_atk',
      description: 'Drone attack integer value',
      category: 'Drone',
      valueType: 'int',
    },

    // ─── OVERLORD ───────────────────────────────────────────────
    {
      key: 'overlord_atk',
      description: 'Overlord attack integer value',
      category: 'Overlord',
      valueType: 'int',
    },
    {
      key: 'overlord_def',
      description: 'Overlord defense integer value',
      category: 'Overlord',
      valueType: 'int',
    },
    {
      key: 'overlord_hp',
      description: 'Overlord HP integer value',
      category: 'Overlord',
      valueType: 'int',
    },
    {
      key: 'overlord_atk_boost_percent',
      description: 'Overlord attack boost percentage',
      category: 'Overlord',
      valueType: 'float',
    },
    {
      key: 'overlord_def_boost_percent',
      description: 'Overlord defense boost percentage',
      category: 'Overlord',
      valueType: 'float',
    },
    {
      key: 'overlord_hp_boost_percent',
      description: 'Overlord HP boost percentage',
      category: 'Overlord',
      valueType: 'float',
    },
    {
      key: 'overlord_expert_crit_rate_percent',
      description: 'Overlord Expert Overlord skill crit rate boost',
      category: 'Overlord',
      valueType: 'float',
    },
    {
      key: 'hero_overlord_hp',
      description: 'Hero and overlord HP integer boost',
      category: 'Overlord',
      valueType: 'int',
    },
    {
      key: 'hero_overlord_def',
      description: 'Hero and overlord defense integer boost',
      category: 'Overlord',
      valueType: 'int',
    },
    {
      key: 'hero_overlord_atk',
      description: 'Hero and overlord attack integer boost',
      category: 'Overlord',
      valueType: 'int',
    },

    // ─── DECORATIONS ────────────────────────────────────────────
    {
      key: 'decor_hero_atk_boost_percent',
      description: 'Decoration hero attack boost percentage',
      category: 'Decoration',
      valueType: 'float',
    },
    {
      key: 'decor_hero_def_boost_percent',
      description: 'Decoration hero defense boost percentage',
      category: 'Decoration',
      valueType: 'float',
    },
    {
      key: 'decor_hero_hp_boost_percent',
      description: 'Decoration hero HP boost percentage',
      category: 'Decoration',
      valueType: 'float',
    },
    {
      key: 'decor_overlord_atk_boost_percent',
      description: 'Decoration overlord attack boost percentage',
      category: 'Decoration',
      valueType: 'float',
    },
    {
      key: 'decor_overlord_def_boost_percent',
      description: 'Decoration overlord defense boost percentage',
      category: 'Decoration',
      valueType: 'float',
    },
    {
      key: 'decor_overlord_hp_boost_percent',
      description: 'Decoration overlord HP boost percentage',
      category: 'Decoration',
      valueType: 'float',
    },
    {
      key: 'decor_damage_reduction_percent',
      description: 'Decoration damage reduction percentage',
      category: 'Decoration',
      valueType: 'float',
    },
    {
      key: 'decor_crit_rate_percent',
      description: 'Decoration crit rate percentage',
      category: 'Decoration',
      valueType: 'float',
    },

    // ─── COSMETICS ──────────────────────────────────────────────
    {
      key: 'cosmetic_hero_atk_boost_percent',
      description: 'Cosmetic hero attack boost percentage',
      category: 'Cosmetic',
      valueType: 'float',
    },
    {
      key: 'cosmetic_hero_def_boost_percent',
      description: 'Cosmetic hero defense boost percentage',
      category: 'Cosmetic',
      valueType: 'float',
    },
    {
      key: 'cosmetic_hero_hp_boost_percent',
      description: 'Cosmetic hero HP boost percentage',
      category: 'Cosmetic',
      valueType: 'float',
    },
    {
      key: 'cosmetic_damage_reduction_percent',
      description: 'Cosmetic damage reduction percentage',
      category: 'Cosmetic',
      valueType: 'float',
    },

    // ─── WALL OF HONOR ──────────────────────────────────────────
    {
      key: 'woh_air_count',
      description: 'Wall of Honor air hero count',
      category: 'WallOfHonor',
      valueType: 'int',
    },
    {
      key: 'woh_air_level',
      description: 'Wall of Honor air hero total level',
      category: 'WallOfHonor',
      valueType: 'int',
    },
    {
      key: 'woh_tank_count',
      description: 'Wall of Honor tank hero count',
      category: 'WallOfHonor',
      valueType: 'int',
    },
    {
      key: 'woh_tank_level',
      description: 'Wall of Honor tank hero total level',
      category: 'WallOfHonor',
      valueType: 'int',
    },
    {
      key: 'woh_missile_count',
      description: 'Wall of Honor missile hero count',
      category: 'WallOfHonor',
      valueType: 'int',
    },
    {
      key: 'woh_missile_level',
      description: 'Wall of Honor missile hero total level',
      category: 'WallOfHonor',
      valueType: 'int',
    },

    // ─── UNITS ──────────────────────────────────────────────────
    {
      key: 'unit_troop_level',
      description: 'Troop level',
      category: 'Units',
      valueType: 'int',
    },
    {
      key: 'unit_quantity',
      description: 'Quantity of troops per march',
      category: 'Units',
      valueType: 'int',
    },
    {
      key: 'unit_power',
      description: 'Calculated unit power',
      category: 'Units',
      valueType: 'int',
    },

    // ─── TACTICS CARDS ──────────────────────────────────────────
    {
      key: 'tactics_card_power',
      description: 'Aggregate tactics card power',
      category: 'TacticsCards',
      valueType: 'float',
    },
  ];

  for (const statKey of statKeys) {
    await prisma.statKey.upsert({
      where: { key: statKey.key },
      update: statKey,
      create: statKey,
    });
  }

  console.log(`Seeded ${statKeys.length} StatKeys`);
  console.log('Done.');
}
