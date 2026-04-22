import { FORMATION_POSITIONS } from "@/constants";
import type { SquadSlot } from "@/types";
import { SquadSide } from "./SquadSide";

type BattlefieldLayoutProps = {
  player: SquadSlot[] | null;
  opponent: SquadSlot[] | null;
};

export function BattlefieldLayout({
  player,
  opponent,
}: BattlefieldLayoutProps) {
  return (
    <div className="flex items-center justify-center gap-4">
      <SquadSide
        front={
          player?.filter(
            (squad) => squad.position === FORMATION_POSITIONS.FRONT,
          ) ?? null
        }
        back={
          player?.filter(
            (squad) => squad.position === FORMATION_POSITIONS.BACK,
          ) ?? null
        }
      />
      <div className="flex flex-col items-center gap-1">
        <span className="text-xs text-white/20 tracking-widest uppercase">
          vs
        </span>
      </div>
      <SquadSide
        front={
          opponent?.filter(
            (squad) => squad.position === FORMATION_POSITIONS.FRONT,
          ) ?? null
        }
        back={
          opponent?.filter(
            (squad) => squad.position === FORMATION_POSITIONS.BACK,
          ) ?? null
        }
        flip
      />
    </div>
  );
}
