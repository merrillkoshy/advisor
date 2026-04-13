import { FORMATION_POSITIONS } from "@/constants";
import type { Slot } from "@/types";

export const INITIAL_SLOTS: Slot[] = [
  {
    position: FORMATION_POSITIONS.FRONT,
    index: 0,
    hero: null,
    gear: { gun: 0, armor: 0, chip: 0, radar: 0 },
  },
  {
    position: FORMATION_POSITIONS.FRONT,
    index: 1,
    hero: null,
    gear: { gun: 0, armor: 0, chip: 0, radar: 0 },
  },
  {
    position: FORMATION_POSITIONS.BACK,
    index: 0,
    hero: null,
    gear: { gun: 0, armor: 0, chip: 0, radar: 0 },
  },
  {
    position: FORMATION_POSITIONS.BACK,
    index: 1,
    hero: null,
    gear: { gun: 0, armor: 0, chip: 0, radar: 0 },
  },
  {
    position: FORMATION_POSITIONS.BACK,
    index: 2,
    hero: null,
    gear: { gun: 0, armor: 0, chip: 0, radar: 0 },
  },
];
