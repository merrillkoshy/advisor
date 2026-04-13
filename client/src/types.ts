import { APP_PATHS, FORMATION_POSITIONS } from "@/constants";

type FORMATION_POSITIONS =
  (typeof FORMATION_POSITIONS)[keyof typeof FORMATION_POSITIONS];

type APP_PATHS = (typeof APP_PATHS)[keyof typeof APP_PATHS];

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
  imageUrl: string | null;
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

type SquadSlot = {
  id: number;
  position: FORMATION_POSITIONS;
  slotIndex: number;
  hero: Hero | null;
  gunStars: number;
  armorStars: number;
  chipStars: number;
  radarStars: number;
};

type Squad = {
  id: number;
  squadNumber: number;
  slots: SquadSlot[];
};

type SquadSlotRequest = {
  position: FORMATION_POSITIONS;
  slotIndex: number;
  heroId: number;
  gunStars: number;
  armorStars: number;
  chipStars: number;
  radarStars: number;
};

interface Slot {
  position: FORMATION_POSITIONS;
  index: number;
  hero: Hero | null;
  gear: {
    gun: number;
    armor: number;
    chip: number;
    radar: number;
  };
}

export type {
  APP_PATHS,
  FORMATION_POSITIONS,
  Gear,
  GearLevel,
  GearStats,
  Hero,
  Skill,
  SkillEffect,
  Slot,
  Squad,
  SquadSlotRequest,
};
