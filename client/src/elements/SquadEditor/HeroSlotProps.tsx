import NoHero from "@/assets/icons/no-hero.svg";
import { cn } from "@/lib/utils";
import type { Slot } from "@/types";
import { typeIconMap } from "@/utils/typeIconMap";
import { PenIcon, Plus } from "lucide-react";

interface HeroSlotProps {
  slot: Slot | null;
  onClick: () => void;
  onEdit: () => void;
}

export function HeroSlot({ slot, onEdit, onClick }: HeroSlotProps) {
  const hero = slot?.hero ?? null;

  return (
    <div
      onClick={onClick}
      className={cn(
        "relative w-20 h-24 rounded-xl border transition-all duration-200",
        "focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-white/30",
        hero
          ? "border-white/20 bg-white/5 hover:border-white/40"
          : "border-dashed border-white/10 bg-white/5 hover:border-white/30 hover:bg-white/10",
      )}
    >
      {hero ? (
        <>
          {/* Rank accent bar */}
          <div
            className={cn(
              "absolute top-0 left-0 right-0 h-0.5 rounded-t-xl",
              hero.rank === "UR" && "bg-yellow-400",
              hero.rank === "SSR" && "bg-purple-400",
              hero.rank === "SR" && "bg-cyan-400",
            )}
          />
          {/*Edit button */}
          <button
            className="absolute z-10 bg-black/80 top-0 right-0 p-1 rounded-l-sm"
            onClick={(e) => {
              e.stopPropagation();
              onEdit();
            }}
          >
            <PenIcon />
          </button>
          {/* Hero image */}
          <img
            src={hero.imageUrl ?? NoHero}
            alt={hero.name}
            className="w-full h-full object-cover rounded-xl"
          />
          {/* Type icon */}
          {typeIconMap[hero.type] && (
            <img
              src={typeIconMap[hero.type]}
              alt={hero.type}
              className="absolute bottom-1 right-1 w-4 h-4"
            />
          )}
          {/* Name */}
          <div className="absolute bottom-0 left-0 right-0 rounded-b-xl bg-black/60 px-1 py-0.5">
            <p className="text-[9px] text-white/80 text-center truncate">
              {hero.name}
            </p>
          </div>
        </>
      ) : (
        <Plus className="w-5 h-5 text-white/20 absolute top-1/2 left-1/2 -translate-x-1/2 -translate-y-1/2" />
      )}
    </div>
  );
}
