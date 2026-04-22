export const BACKEND_URL: string = import.meta.env.VITE_BACKEND_URL;
export const APP_NAME: string = "LastWar Advisor";
export const FORMATION_POSITIONS = {
  FRONT: "FRONT",
  BACK: "BACK",
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
