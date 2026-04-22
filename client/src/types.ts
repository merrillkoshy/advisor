import { APP_PATHS, FORMATION_POSITIONS } from "@/constants";

type FORMATION_POSITIONS =
  (typeof FORMATION_POSITIONS)[keyof typeof FORMATION_POSITIONS];

type APP_PATHS = (typeof APP_PATHS)[keyof typeof APP_PATHS];

type Stat = {
  id: number;
  key: string;
  description: string;
  category: string;
  valueType: string;
};

//Hero
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

//Gear
type GearStats = {
  id: number;
  baseValue: number;
  increment: number;
  statKey: Stat;
};

type GearLevel = {
  id: number;
  level: number;
  statKey: Stat;
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

//Player Squad
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
//Drone
type DroneBase = { id: number; level: number; starTier: number; title: string };
type DroneComponentStats = {
  baseValue: number;
  id: number;
  increment: number;
  statKey: Stat;
  unlockLevel: number;
};
type DroneComponentDetail = {
  description: string;
  id: number;
  maxLevel: number;
  name: string;
  imageUrl: string;
  stats: DroneComponentStats[];
};
type DroneComponent = {
  droneComponent: DroneComponentDetail;
  id: number;
  level: number;
};
interface Drone {
  drone: DroneBase;
  components: DroneComponent[];
}

export type {
  APP_PATHS,
  Drone,
  DroneComponent,
  FORMATION_POSITIONS,
  Gear,
  GearLevel,
  GearStats,
  Hero,
  Skill,
  SkillEffect,
  Slot,
  Squad,
  SquadSlot,
};
