import DamageEventLog from "@/elements/Advisor/DamageEventLog";
import type { DamageEvent, SquadSlot, TickSnapshot } from "@/types";
import { SquadSide } from "./SquadSide";

type BattlefieldLayoutProps = {
  player: SquadSlot[] | null;
  opponent: SquadSlot[] | null;
  snapshot: TickSnapshot | null;
  allDamageEvents: DamageEvent[];
};

export function BattlefieldLayout({
  player,
  opponent,
  snapshot,
  allDamageEvents,
}: BattlefieldLayoutProps) {
  console.log("BattlefieldLayout snapshot:", snapshot);
  const [damageEvents, elapsedTime, playerStates, enemyStates] = snapshot
    ? [
        snapshot.damageEvents,
        snapshot.elapsedTime,
        snapshot.playerStates,
        snapshot.enemyStates,
      ]
    : [null, 0, null, null];
  return (
    <div className="flex flex-col items-center gap-4">
      <div className="flex flex-col items-center gap-1">
        <span className="text-[10px] text-white/35 tracking-widest uppercase font-bold">
          Elapsed Time
        </span>
        <span className="text-xs text-white font-bold rounded-sm border border-white/10 bg-white/5 py-1 px-2 w-25 text-center">
          {elapsedTime.toFixed(2)}s
        </span>
      </div>
      <div className="flex items-center justify-center gap-4">
        <SquadSide squad={player} heroStates={playerStates} />
        <div className="flex flex-col items-center gap-1">
          <span className="text-xs text-white/20 tracking-widest uppercase">
            vs
          </span>
        </div>
        <SquadSide squad={opponent} flip heroStates={enemyStates} />
      </div>
      <DamageEventLog damageEvents={allDamageEvents} />
    </div>
  );
}
