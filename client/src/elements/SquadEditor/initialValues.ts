import { FORMATION_POSITIONS } from "@/constants";
import type { Slot } from "@/types";

export const INITIAL_SLOTS: Slot[] = [
  {
    position: FORMATION_POSITIONS.FRONT_1,
    index: 0,
    hero: null,
    gear: { gun: 0, armor: 0, chip: 0, radar: 0 },
  },
  {
    position: FORMATION_POSITIONS.FRONT_2,
    index: 1,
    hero: null,
    gear: { gun: 0, armor: 0, chip: 0, radar: 0 },
  },
  {
    position: FORMATION_POSITIONS.BACK_1,
    index: 0,
    hero: null,
    gear: { gun: 0, armor: 0, chip: 0, radar: 0 },
  },
  {
    position: FORMATION_POSITIONS.BACK_2,
    index: 1,
    hero: null,
    gear: { gun: 0, armor: 0, chip: 0, radar: 0 },
  },
  {
    position: FORMATION_POSITIONS.BACK_3,
    index: 2,
    hero: null,
    gear: { gun: 0, armor: 0, chip: 0, radar: 0 },
  },
];
