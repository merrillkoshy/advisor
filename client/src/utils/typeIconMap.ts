import Aircraft from "@/assets/icons/Aircraft.svg";

import MissileVehicle from "@/assets/icons/MissileVehicle.svg";
import Tank from "@/assets/icons/Tank.svg";

export const typeIconMap: Record<string, string> = {
  Tank,
  Aircraft,
  MissileVehicle,
};

export const rankColorMap: Record<string, string> = {
  UR: "text-yellow-400",
  SSR: "text-purple-400",
  SR: "text-cyan-400",
};
