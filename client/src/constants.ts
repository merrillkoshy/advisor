export const BACKEND_URL: string = import.meta.env.VITE_BACKEND_URL;
export const STORAGE_BASE_URL: string = import.meta.env.VITE_STORAGE_BASE_URL;
export const APP_NAME: string = "LastWar Advisor";
export const FORMATION_POSITIONS = {
  FRONT_1: "FRONT_1",
  FRONT_2: "FRONT_2",
  BACK_1: "BACK_1",
  BACK_2: "BACK_2",
  BACK_3: "BACK_3",
} as const;

export const PLAYER_ID = 1;
export const OPPONENT_ID = 1;

export const APP_PATHS = {
  advisor: "/",
  squads: "/squads",
  squad: "/squads/$squadId",
  opponents: "/opponents",
  opponent: "/opponents/$opponentSquadId",
  heroes: "/heroes",
  hero: "/heroes/$heroId",
  gears: "/gears",
  drone: "/drone",
  overlord: "/army/overlord",
  tech: "/army/tech",
  unit: "/army/unit",
  wall_of_honor: "/army/wall-of-honor",
  cosmetics: "/army/cosmetics",
  tactics_cards: "/army/tactics-cards",
  decorations: "/army/decorations",
};
export const DRONE_STAR_TITLES = [
  'TD-1 "Pathfinder"',
  'TD-2 "Blaster"',
  'TD-3 "Silver Knight"',
  'TD-4 "Phantom"',
  'TD-5 "Destroyer"',
  'TD-6 "Colossus"',
];

export const SQUAD_TYPE = {
  TANK: "TANK",
  AIRCRAFT: "AIRCRAFT",
  MISSILE_VEHICLE: "MISSILE_VEHICLE",
};
