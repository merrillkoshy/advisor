import { PrismaPg } from '@prisma/adapter-pg';
import { PrismaClient } from '@prisma/client';
import 'dotenv/config';

const adapter = new PrismaPg({
  connectionString: process.env.DATABASE_URL!,
});

const prisma = new PrismaClient({ adapter });

export async function seedDroneComponents() {
  console.log('Seeding Drone Components...');

  const components = [
    {
      name: 'Radar Research',
      description:
        'Affects drone HP and reduces chances of taking critical hits',
      maxLevel: 12,
      statKeys: ['drone_hp', 'crit_reduction_percent'],
    },
    {
      name: 'Turbo Engine Research',
      description: 'Affects Hero & Overlord HP and boosts hero HP percentage',
      maxLevel: 12,
      statKeys: ['hero_overlord_hp', 'hero_hp_boost_percent'],
    },
    {
      name: 'External Armor Research',
      description:
        'Affects Hero & Overlord DEF and boosts hero defense percentage',
      maxLevel: 12,
      statKeys: ['hero_overlord_def', 'hero_def_boost_percent'],
    },
    {
      name: 'Thermal Imager Research',
      description: 'Affects drone DEF and boosts crit rate percentage',
      maxLevel: 12,
      statKeys: ['drone_def', 'crit_rate_percent'],
    },
    {
      name: 'Fuel Cell Research',
      description: 'Affects drone ATK and boosts crit damage percentage',
      maxLevel: 12,
      statKeys: ['drone_atk', 'crit_damage_percent'],
    },
    {
      name: 'Airborne Missile Research',
      description:
        'Affects Hero & Overlord ATK, boosts hero skill damage and hero ATK percentage',
      maxLevel: 12,
      statKeys: ['hero_overlord_atk', 'hero_skill_damage_percent'],
    },
  ];

  for (const component of components) {
    const created = await prisma.droneComponent.upsert({
      where: { name: component.name },
      update: {
        description: component.description,
        maxLevel: component.maxLevel,
      },
      create: {
        name: component.name,
        description: component.description,
        maxLevel: component.maxLevel,
      },
    });

    for (const statKeyStr of component.statKeys) {
      const statKey = await prisma.statKey.findUnique({
        where: { key: statKeyStr },
      });

      if (!statKey) {
        console.warn(`  ⚠ StatKey not found: "${statKeyStr}" — skipping`);
        continue;
      }

      const existing = await prisma.droneComponentStatKey.findFirst({
        where: { droneComponentId: created.id, statKeyId: statKey.id },
      });

      if (!existing) {
        await prisma.droneComponentStatKey.create({
          data: {
            droneComponentId: created.id,
            statKeyId: statKey.id,
          },
        });
      }
    }
  }

  console.log(`Seeded ${components.length} Drone Components`);
}

export async function seedOverlordClasses() {
  console.log('Seeding Overlord Classes...');

  const classes = [
    {
      level: 0,
      className: 'Recruit',
      overlordAtk: 0,
      overlordDef: 0,
      overlordHp: 0,
      overlordHpBoost: null,
      overlordAtkBoost: null,
      overlordDefBoost: null,
    },
    {
      level: 10,
      className: 'Private',
      overlordAtk: 0,
      overlordDef: 0,
      overlordHp: 0,
      overlordHpBoost: null,
      overlordAtkBoost: null,
      overlordDefBoost: null,
    },
    {
      level: 20,
      className: 'Fighter',
      overlordAtk: 0,
      overlordDef: 0,
      overlordHp: 0,
      overlordHpBoost: null,
      overlordAtkBoost: null,
      overlordDefBoost: null,
    },
    {
      level: 30,
      className: 'Warrior',
      overlordAtk: 0,
      overlordDef: 0,
      overlordHp: 0,
      overlordHpBoost: null,
      overlordAtkBoost: null,
      overlordDefBoost: null,
    },
    {
      level: 40,
      className: 'Warlord',
      overlordAtk: 0,
      overlordDef: 0,
      overlordHp: 0,
      overlordHpBoost: null,
      overlordAtkBoost: null,
      overlordDefBoost: null,
    },
    {
      level: 50,
      className: 'Champion',
      overlordAtk: 0,
      overlordDef: 0,
      overlordHp: 0,
      overlordHpBoost: null,
      overlordAtkBoost: null,
      overlordDefBoost: null,
    },
    {
      level: 60,
      className: 'Overlord',
      overlordAtk: 0,
      overlordDef: 0,
      overlordHp: 0,
      overlordHpBoost: null,
      overlordAtkBoost: null,
      overlordDefBoost: null,
    },
  ];

  for (const overlordClass of classes) {
    await prisma.overlordClass.upsert({
      where: { level: overlordClass.level },
      update: overlordClass,
      create: overlordClass,
    });
  }

  console.log(`Seeded ${classes.length} Overlord Classes`);
}
