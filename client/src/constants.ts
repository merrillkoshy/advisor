export const BACKEND_URL: string = import.meta.env.VITE_BACKEND_URL;
export const APP_NAME: string = "LastWar Advisor";
export const FORMATION_POSITIONS = {
  FRONT: "FRONT",
  BACK: "BACK",
} as const;

export const PLAYER_ID = 1;

export const APP_PATHS = {
  advisor: "/",
  squads: "/squads",
  squad: "/squads/$squadId",
  heroes: "/heroes",
  gears: "/gears",
  drone: "/army/drone",
  overlord: "/army/overlord",
  tech: "/army/tech",
  unit: "/army/unit",
  wall_of_honor: "/army/wall-of-honor",
  cosmetics: "/army/cosmetics",
  tactics_cards: "/army/tactics-cards",
  decorations: "/army/decorations",
};
