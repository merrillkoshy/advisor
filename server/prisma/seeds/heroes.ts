import { PrismaPg } from '@prisma/adapter-pg';
import { PrismaClient } from '@prisma/client';
import 'dotenv/config';
import * as fs from 'fs';
import * as path from 'path';
import { HeroWithSkills } from 'types';

const adapter = new PrismaPg({
  connectionString: process.env.DATABASE_URL!,
});

const prisma = new PrismaClient({ adapter });

const SKILL_LEVELS = ['lvl1', 'lvl20', 'lvl40'] as const;
const LEVEL_MAP: Record<string, number> = {
  lvl1: 1,
  lvl20: 20,
  lvl40: 40,
};

async function getStatKeyId(
  key: string,
  cache: Map<string, number>,
): Promise<number | null> {
  if (cache.has(key)) return cache.get(key)!;
  const statKey = await prisma.statKey.findUnique({ where: { key } });
  if (!statKey) {
    console.warn(`  ⚠ StatKey not found: "${key}" — skipping`);
    return null;
  }
  cache.set(key, statKey.id);
  return statKey.id;
}

export async function seedHeroes() {
  const dataPath = path.resolve(__dirname, '../../../data/heroes.json');
  const heroes: HeroWithSkills = JSON.parse(fs.readFileSync(dataPath, 'utf-8'));

  const statKeyCache = new Map<string, number>();

  if (!heroes || Array.isArray(heroes) === false || heroes.length === 0) {
    console.warn('No hero data found to seed.');
    return;
  }
  console.log(`Seeding ${heroes.length} heroes...`);

  for (const heroData of heroes) {
    console.log(`\n→ ${heroData.name}`);

    const hero = await prisma.hero.upsert({
      where: { name: heroData.name },
      update: {
        rank: heroData.rank,
        type: heroData.type,
        tier: heroData.tier,
        power: heroData.power,
        atk: heroData.atk,
        def: heroData.def,
        hp: heroData.hp,
        spd: heroData.spd,
      },
      create: {
        name: heroData.name,
        rank: heroData.rank,
        type: heroData.type,
        tier: heroData.tier,
        power: heroData.power,
        atk: heroData.atk,
        def: heroData.def,
        hp: heroData.hp,
        spd: heroData.spd,
      },
    });

    if (!heroData.skills || heroData.skills.length === 0) {
      console.log(`  ℹ No skills yet`);
      continue;
    }

    for (const skillData of heroData.skills) {
      console.log(`  ✦ Skill: ${skillData.name}`);

      const skill = await prisma.skill.upsert({
        where: {
          id:
            (
              await prisma.skill.findFirst({
                where: { heroId: hero.id, name: skillData.name },
              })
            )?.id ?? 0,
        },
        update: {
          type: skillData.type,
          name: skillData.name,
          priority: skillData.priority,
          description: skillData.description,
          cooldown: skillData.cooldown_s ?? null,
          target: skillData.target,
          damageType: skillData.damage_type ?? null,
          keyStats: skillData.key_stats ?? [],
        },
        create: {
          heroId: hero.id,
          type: skillData.type,
          name: skillData.name,
          priority: skillData.priority,
          description: skillData.description,
          cooldown: skillData.cooldown_s ?? null,
          target: skillData.target,
          damageType: skillData.damage_type ?? null,
          keyStats: skillData.key_stats ?? [],
        },
      });

      if (!skillData.scaling) continue;

      for (const levelKey of SKILL_LEVELS) {
        const levelData = skillData.scaling[levelKey];
        if (!levelData) continue;

        const level = LEVEL_MAP[levelKey];

        for (const [statKey, value] of Object.entries(levelData)) {
          const statKeyId = await getStatKeyId(statKey, statKeyCache);
          if (statKeyId === null) continue;

          const existingEffect = await prisma.skillEffect.findFirst({
            where: { skillId: skill.id, statKeyId, level },
          });

          const isBool = typeof value === 'boolean';
          const effectData = {
            skillId: skill.id,
            statKeyId,
            level,
            value: isBool || value === null ? null : (value as number),
            boolValue: isBool ? (value as boolean) : null,
          };

          if (existingEffect) {
            await prisma.skillEffect.update({
              where: { id: existingEffect.id },
              data: effectData,
            });
          } else {
            await prisma.skillEffect.create({ data: effectData });
          }
        }
      }
    }
  }

  console.log('\nHero seeding complete.');
}
