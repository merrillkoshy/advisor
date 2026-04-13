// elements/SquadCard/SquadCard.tsx
import { cn } from "@/lib/utils";
import type { Squad } from "@/types";

interface SquadCardProps {
  squad: Squad;
  squadNumber: number;
  onClick: () => void;
}

export function SquadCard({ squad, squadNumber, onClick }: SquadCardProps) {
  // separate front and back from real data
  console.log({ squad });
  const frontSlots = squad.slots.filter((s) => s.position === "FRONT");
  const backSlots = squad.slots.filter((s) => s.position === "BACK");

  // fill missing slots up to 2 front, 3 back
  const frontDisplay = Array.from(
    { length: 2 },
    (_, i) => frontSlots.find((s) => s.slotIndex === i) ?? null,
  );
  const backDisplay = Array.from(
    { length: 3 },
    (_, i) => backSlots.find((s) => s.slotIndex === i) ?? null,
  );
  console.log({ frontDisplay }, { backDisplay });

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
          {/* {EMPTY_SLOTS.front.map((i) => (
            <div
              key={i}
              className="h-12 w-12 rounded-lg border border-dashed border-white/10 bg-white/5 group-hover:border-white/20 transition-colors"
            />
          ))} */}
          {frontDisplay.map((slot, i) => (
            <div
              key={i}
              className="h-12 w-12 rounded-lg border border-dashed border-white/10 bg-white/5 overflow-hidden"
            >
              {slot?.hero?.imageUrl && (
                <img
                  src={slot.hero.imageUrl}
                  alt={slot.hero.name}
                  className="w-full h-full object-cover"
                />
              )}
            </div>
          ))}
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
        {backDisplay.map((slot, i) => (
          <div
            key={i}
            className="h-12 w-12 rounded-lg border border-dashed border-white/10 bg-white/5 overflow-hidden"
          >
            {slot?.hero?.imageUrl && (
              <img
                src={slot.hero.imageUrl}
                alt={slot.hero.name}
                className="w-full h-full object-cover"
              />
            )}
          </div>
        ))}
      </div>

      {/* Edit hint */}
      <p className="mt-6 text-center text-xs text-white/20 group-hover:text-white/40 transition-colors">
        Click to configure
      </p>
    </button>
  );
}
