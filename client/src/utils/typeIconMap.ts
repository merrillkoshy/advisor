import Aircraft from "@/assets/icons/Aircraft.svg";

import MissileVehicle from "@/assets/icons/MissileVehicle.svg";
import Tank from "@/assets/icons/Tank.svg";
import { SQUAD_TYPE } from "@/constants";

export const typeIconMap: Record<string, string> = {
  [SQUAD_TYPE.TANK]: Tank,
  [SQUAD_TYPE.AIRCRAFT]: Aircraft,
  [SQUAD_TYPE.MISSILE_VEHICLE]: MissileVehicle,
};

export const rankColorMap: Record<string, string> = {
  UR: "text-yellow-400",
  SSR: "text-purple-400",
  SR: "text-cyan-400",
};
