// elements/SquadCard/SquadCard.tsx
import { FORMATION_POSITIONS } from "@/constants";
import { cn } from "@/lib/utils";
import type { Squad } from "@/types";
import { getSquadByPosition } from "@/utils/getSquadByPosition";

interface SquadCardProps {
  squad: Squad;
  squadNumber: number;
  onClick: () => void;
}

export function SquadCard({ squad, squadNumber, onClick }: SquadCardProps) {
  // separate front and back from real data

  const [front1, front2, back1, back2, back3] = [
    getSquadByPosition(squad.slots, FORMATION_POSITIONS.FRONT_1),
    getSquadByPosition(squad.slots, FORMATION_POSITIONS.FRONT_2),
    getSquadByPosition(squad.slots, FORMATION_POSITIONS.BACK_1),
    getSquadByPosition(squad.slots, FORMATION_POSITIONS.BACK_2),
    getSquadByPosition(squad.slots, FORMATION_POSITIONS.BACK_3),
  ];

  return (
    <button
      onClick={onClick}
      className={cn(
        "group relative w-full rounded-xl border border-white/10 bg-white/5",
        "p-6 text-left transition-all duration-200",
        "hover:border-white/20 hover:bg-white/10 hover:shadow-lg hover:shadow-black/20",
        "focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-white/30",
      )}
    >
      {/* Header */}
      <div className="mb-6 flex items-center justify-between">
        <span className="text-xs font-semibold uppercase tracking-widest text-white/40">
          Squad
        </span>
        <span className="text-2xl font-bold text-white/20 group-hover:text-white/40 transition-colors">
          {`${squadNumber}`}
        </span>
      </div>

      {/* Formation preview */}
      <div className="space-y-3">
        {/* Front row - 2 slots */}
        <div className="flex justify-center gap-2">
          <div
            key={"front-1"}
            className="h-12 w-12 rounded-lg border border-dashed border-white/10 bg-white/5 overflow-hidden"
          >
            {front1?.hero?.imageUrl && (
              <img
                src={front1.hero.imageUrl}
                alt={front1.hero.name}
                className="w-full h-full object-cover"
              />
            )}
          </div>
          <div
            key={"front-2"}
            className="h-12 w-12 rounded-lg border border-dashed border-white/10 bg-white/5 overflow-hidden"
          >
            {front2?.hero?.imageUrl && (
              <img
                src={front2.hero.imageUrl}
                alt={front2.hero.name}
                className="w-full h-full object-cover"
              />
            )}
          </div>
        </div>
      </div>

      {/* Divider */}
      <div className="flex items-center my-2">
        <div className="h-px flex-1 bg-white/5" />
        <span className="text-[10px] uppercase tracking-widest text-white/20">
          Back row
        </span>
        <div className="h-px flex-1 bg-white/5" />
      </div>

      {/* Back row - 3 slots */}
      <div className="flex justify-center gap-2">
        <div
          key={"back-1"}
          className="h-12 w-12 rounded-lg border border-dashed border-white/10 bg-white/5 overflow-hidden"
        >
          {back1?.hero?.imageUrl && (
            <img
              src={back1.hero.imageUrl}
              alt={back1.hero.name}
              className="w-full h-full object-cover"
            />
          )}
        </div>
        <div
          key={"back-2"}
          className="h-12 w-12 rounded-lg border border-dashed border-white/10 bg-white/5 overflow-hidden"
        >
          {back2?.hero?.imageUrl && (
            <img
              src={back2.hero.imageUrl}
              alt={back2.hero.name}
              className="w-full h-full object-cover"
            />
          )}
        </div>
        <div
          key={"back-3"}
          className="h-12 w-12 rounded-lg border border-dashed border-white/10 bg-white/5 overflow-hidden"
        >
          {back3?.hero?.imageUrl && (
            <img
              src={back3.hero.imageUrl}
              alt={back3.hero.name}
              className="w-full h-full object-cover"
            />
          )}
        </div>
      </div>

      {/* Edit hint */}
      <p className="mt-6 text-center text-xs text-white/20 group-hover:text-white/40 transition-colors">
        Click to configure
      </p>
    </button>
  );
}
