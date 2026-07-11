import type { FORMATION_POSITIONS } from "@/types";

type SquadSlotRequest = {
  position: FORMATION_POSITIONS;
  slotIndex: number;
  heroId: number;
  gunStars: number;
  armorStars: number;
  chipStars: number;
  radarStars: number;
};

type DroneComponentRequest = {
  droneComponentId: number;
  droneComponentLevel: number;
};
type DroneRequest = {
  droneLevel: number;
};

type AdvisorRequest = {
  playerSquadId: number;
  enemySquadId: number;
  battleId: string;
};

export type {
  AdvisorRequest,
  DroneComponentRequest,
  DroneRequest,
  SquadSlotRequest,
};
