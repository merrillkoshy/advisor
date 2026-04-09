// elements/SquadCard/SquadCard.tsx
import { cn } from "@/lib/utils";

interface SquadCardProps {
  squadNumber: 1 | 2 | 3;
  onClick: () => void;
}

const EMPTY_SLOTS = {
  front: [1, 2],
  back: [1, 2, 3],
};

export function SquadCard({ squadNumber, onClick }: SquadCardProps) {
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
          {squadNumber}
        </span>
      </div>

      {/* Formation preview */}
      <div className="space-y-3">
        {/* Front row - 2 slots */}
        <div className="flex justify-center gap-2">
          {EMPTY_SLOTS.front.map((i) => (
            <div
              key={i}
              className="h-12 w-12 rounded-lg border border-dashed border-white/10 bg-white/5 group-hover:border-white/20 transition-colors"
            />
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
        {EMPTY_SLOTS.back.map((i) => (
          <div
            key={i}
            className="h-12 w-12 rounded-lg border border-dashed border-white/10 bg-white/5 group-hover:border-white/20 transition-colors"
          />
        ))}
      </div>

      {/* Edit hint */}
      <p className="mt-6 text-center text-xs text-white/20 group-hover:text-white/40 transition-colors">
        Click to configure
      </p>
    </button>
  );
}
