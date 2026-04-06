type SkillEffect = {
  id: number;
  name: string;
  description: string;
};

type Skill = {
  id: number;
  name: string;
  description: string;
  cooldown: number;
  effects: SkillEffect[];
};

type Hero = {
  id: number;
  name: string;
  type: string;
  rank: string;
  tier: string;
  hp: number;
  atk: number;
  def: number;
  spd: number;
  power: number;
  skills: Skill[];
  createdAt: string;
  updatedAt: string;
};

type GearStat = {
  id: number;
  key: string;
  description: string;
  category: string;
  valueType: string;
};
type GearStats = {
  id: number;
  baseValue: number;
  increment: number;
  statKey: GearStat;
};

type GearLevel = {
  id: number;
  level: number;
  statKey: GearStat;
  value: number;
};

type Gear = {
  id: number;
  baseName: string;
  mythicName: string;
  basePower: number;
  type: string;
  stats: GearStats[];
  levels: GearLevel[];
};

export type { Gear, GearLevel, GearStats, Hero, Skill, SkillEffect };
