// elements/SquadEditor/SquadEditor.tsx
import { HeroPicker } from "@/elements/HeroPicker";
import { cn } from "@/lib/utils";
import type { Hero } from "@/types";
import { rankColorMap, typeIconMap } from "@/utils/typeIconMap";
import { Plus } from "lucide-react";
import { useState } from "react";

import NoHero from "@/assets/icons/no-hero.svg";
import { HeroDetail } from "@/elements/HeroDetail";
import { useHeroes } from "@/hooks/useHeroes";

type SlotPosition = "front" | "back";

interface Slot {
  position: SlotPosition;
  index: number;
  hero: Hero | null;
  gear: {
    gun: number;
    armor: number;
    chip: number;
    radar: number;
  };
}

const INITIAL_SLOTS: Slot[] = [
  {
    position: "front",
    index: 0,
    hero: null,
    gear: { gun: 0, armor: 0, chip: 0, radar: 0 },
  },
  {
    position: "front",
    index: 1,
    hero: null,
    gear: { gun: 0, armor: 0, chip: 0, radar: 0 },
  },
  {
    position: "back",
    index: 0,
    hero: null,
    gear: { gun: 0, armor: 0, chip: 0, radar: 0 },
  },
  {
    position: "back",
    index: 1,
    hero: null,
    gear: { gun: 0, armor: 0, chip: 0, radar: 0 },
  },
  {
    position: "back",
    index: 2,
    hero: null,
    gear: { gun: 0, armor: 0, chip: 0, radar: 0 },
  },
];

interface SquadEditorProps {
  squadId: string;
}

export function SquadEditor({ squadId }: SquadEditorProps) {
  const [slots, setSlots] = useState<Slot[]>(INITIAL_SLOTS);
  const [selectedSlotKey, setSelectedSlotKey] = useState<string | null>(null);
  const [activeSlot, setActiveSlot] = useState<{
    position: SlotPosition;
    index: number;
  } | null>(null);
  const { data: heroes = [] } = useHeroes();

  const selectedSlot = slots.find(
    (s) => selectedSlotKey === `${s.position}-${s.index}`,
  );

  function handleSlotClick(position: SlotPosition, index: number) {
    const key = `${position}-${index}`;
    if (slots.find((s) => s.position === position && s.index === index)?.hero) {
      // filled slot — open detail panel
      setSelectedSlotKey((prev) => (prev === key ? null : key));
    } else {
      // empty slot — open hero picker
      setActiveSlot({ position, index });
    }
  }

  function handleGearStarsChange(
    position: SlotPosition,
    index: number,
    type: keyof Slot["gear"],
    stars: number,
  ) {
    setSlots((prev) =>
      prev.map((slot) =>
        slot.position === position && slot.index === index
          ? { ...slot, gear: { ...slot.gear, [type]: stars } }
          : slot,
      ),
    );
  }

  const pickedHeroIds = new Set(slots.map((s) => s.hero?.id).filter(Boolean));

  function handleHeroPick(hero: Hero) {
    if (!activeSlot) return;
    setSlots((prev) =>
      prev.map((slot) =>
        slot.position === activeSlot.position && slot.index === activeSlot.index
          ? { ...slot, hero }
          : slot,
      ),
    );
    setActiveSlot(null);
  }

  function handleDrawerClose() {
    setActiveSlot(null);
  }

  const frontSlots = slots.filter((s) => s.position === "front");
  const backSlots = slots.filter((s) => s.position === "back");

  return (
    <>
      <div className="flex flex-col items-center gap-6">
        {/* Front row */}
        <div className="flex gap-4">
          {frontSlots.map((slot) => (
            <HeroSlot
              key={`front-${slot.index}`}
              slot={slot}
              onClick={() => handleSlotClick("front", slot.index)}
            />
          ))}
        </div>

        {/* Divider */}
        <div className="flex w-full max-w-sm items-center gap-3">
          <div className="h-px flex-1 bg-white/10" />
          <span className="text-[10px] uppercase tracking-widest text-white/20">
            back
          </span>
          <div className="h-px flex-1 bg-white/10" />
        </div>

        {/* Back row */}
        <div className="flex gap-4">
          {backSlots.map((slot) => (
            <HeroSlot
              key={`back-${slot.index}`}
              slot={slot}
              onClick={() => handleSlotClick("back", slot.index)}
            />
          ))}
        </div>
        {/* Right: hero detail panel */}
        {selectedSlot?.hero && (
          <div className="flex-1">
            <HeroDetail
              hero={selectedSlot.hero}
              gearStars={selectedSlot.gear}
              onStarsChange={(type, stars) =>
                handleGearStarsChange(
                  selectedSlot.position,
                  selectedSlot.index,
                  type,
                  stars,
                )
              }
            />
          </div>
        )}
      </div>

      <HeroPicker
        open={activeSlot !== null}
        heroes={heroes}
        pickedHeroIds={pickedHeroIds as Set<number>}
        onPick={handleHeroPick}
        onClose={handleDrawerClose}
      />
    </>
  );
}

interface HeroSlotProps {
  slot: Slot;
  onClick: () => void;
}

function HeroSlot({ slot, onClick }: HeroSlotProps) {
  const { hero } = slot;
  const rankColor = hero ? rankColorMap[hero.rank] : null;

  return (
    <button
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
    </button>
  );
}
