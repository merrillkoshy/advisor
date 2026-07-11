import { FORMATION_POSITIONS, PLAYER_ID } from "@/constants";
import { DroneSlotDisplay } from "@/elements/Advisor/DroneSlotDisplay";
import type { HeroSnapshot, SquadSlot } from "@/types";
import { getSquadByPosition } from "@/utils/getSquadByPosition";
import { HeroSlotDisplay } from "./HeroSlotDisplay";

type SquadSideProps = {
  squad: SquadSlot[] | null;
  flip?: boolean;
  heroStates?: HeroSnapshot[] | null;
};

const Placeholder = ({ label }: { label: string }) => (
  <div className="w-10 h-10 rounded-lg border border-white/10 bg-white/4 flex items-center justify-center">
    <span className="text-[8px] text-white/25">{label}</span>
  </div>
);

export function SquadSide({ squad, flip = false, heroStates }: SquadSideProps) {
  console.log(
    "heroStates -" + (flip === true ? "player" : "enemy") + " : ",
    heroStates,
  );

  const [FRONT_1, FRONT_2, BACK_1, BACK_2, BACK_3] = [
    getSquadByPosition(squad, FORMATION_POSITIONS.FRONT_1) || null,
    getSquadByPosition(squad, FORMATION_POSITIONS.FRONT_2) || null,
    getSquadByPosition(squad, FORMATION_POSITIONS.BACK_1) || null,
    getSquadByPosition(squad, FORMATION_POSITIONS.BACK_2) || null,
    getSquadByPosition(squad, FORMATION_POSITIONS.BACK_3) || null,
  ];

  const getHeroState = (squadSlot: SquadSlot | null) => {
    if (!squadSlot || !heroStates) return null;
    return (
      heroStates.find((state) => state.heroName === squadSlot.hero?.name) ||
      null
    );
  };
  const formation = (
    <div
      className={`flex flex-col ${flip ? "items-start" : "items-end"} gap-2`}
    >
      {/* Back row */}
      <div className={`flex items-center gap-2 ${flip ? "ml-10" : "mr-10"}`}>
        {flip && <Placeholder label="OVL" />}
        <HeroSlotDisplay squad={BACK_3} heroState={getHeroState(BACK_3)} />
        {/* <HeroSlotDisplay squad={BACK_3.length > 0 ? BACK_3[0] : null} /> */}
        {!flip && <Placeholder label="OVL" />}
      </div>

      {/* Front 1 */}
      <HeroSlotDisplay squad={FRONT_1} heroState={getHeroState(FRONT_1)} />

      {/* Mid row */}
      <div className={`flex items-center gap-2 ${flip ? "ml-10" : "mr-10"}`}>
        {!flip && <DroneSlotDisplay playerId={PLAYER_ID} />}
        <HeroSlotDisplay squad={BACK_2} heroState={getHeroState(BACK_2)} />
        {flip && <DroneSlotDisplay playerId={0} />}
      </div>

      {/* Front 2 */}
      <HeroSlotDisplay squad={FRONT_2} heroState={getHeroState(FRONT_2)} />

      {/* Bottom back */}
      <div className={`flex items-center gap-2 ${flip ? "ml-10" : "mr-10"}`}>
        <HeroSlotDisplay squad={BACK_1} heroState={getHeroState(BACK_1)} />
      </div>
    </div>
  );

  return formation;
}
