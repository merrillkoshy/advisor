import { PLAYER_ID } from "@/constants";
import { DroneSlotDisplay } from "@/elements/Advisor/DroneSlotDisplay";
import type { SquadSlot } from "@/types";
import { HeroSlotDisplay } from "./HeroSlotDisplay";

type Hero = {
  name: string;
  imageUrl?: string;
  rank: string;
  type: string;
} | null;

type SquadSideProps = {
  front: SquadSlot[] | null;
  back: SquadSlot[] | null;
  flip?: boolean;
};

const Placeholder = ({ label }: { label: string }) => (
  <div className="w-10 h-10 rounded-lg border border-white/10 bg-white/4 flex items-center justify-center">
    <span className="text-[8px] text-white/25">{label}</span>
  </div>
);

export function SquadSide({ front, back, flip = false }: SquadSideProps) {
  const formation = (
    <div
      className={`flex flex-col ${flip ? "items-start" : "items-end"} gap-2`}
    >
      {/* Back row */}
      <div className={`flex items-center gap-2 ${flip ? "ml-10" : "mr-10"}`}>
        {flip && <Placeholder label="OVL" />}
        <HeroSlotDisplay squad={back ? back[2] : null} />
        {!flip && <Placeholder label="OVL" />}
      </div>

      {/* Front 1 */}
      <HeroSlotDisplay squad={front ? front[0] : null} />

      {/* Mid row */}
      <div className={`flex items-center gap-2 ${flip ? "ml-10" : "mr-10"}`}>
        {!flip && <DroneSlotDisplay playerId={PLAYER_ID} />}
        <HeroSlotDisplay squad={back ? back[1] : null} />
        {flip && <DroneSlotDisplay playerId={0} />}
      </div>

      {/* Front 2 */}
      <HeroSlotDisplay squad={front ? front[1] : null} />

      {/* Bottom back */}
      <div className={`flex items-center gap-2 ${flip ? "ml-10" : "mr-10"}`}>
        <HeroSlotDisplay squad={back ? back[0] : null} />
      </div>
    </div>
  );

  return formation;
}
